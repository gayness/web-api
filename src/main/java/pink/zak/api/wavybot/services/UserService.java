package pink.zak.api.wavybot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.wavybot.exceptions.RiptideStatusCode;
import pink.zak.api.wavybot.exceptions.RiptideStatusException;
import pink.zak.api.wavybot.helpers.ListenHelper;
import pink.zak.api.wavybot.helpers.redis.RedisHelper;
import pink.zak.api.wavybot.models.dto.wavy.music.WavyTrackDto;
import pink.zak.api.wavybot.models.dto.wavy.music.album.WavyAlbumDto;
import pink.zak.api.wavybot.models.dto.wavy.music.listens.WavyListenDto;
import pink.zak.api.wavybot.models.task.Task;
import pink.zak.api.wavybot.models.user.User;
import pink.zak.api.wavybot.models.user.WavyUser;
import pink.zak.api.wavybot.models.user.music.MusicData;
import pink.zak.api.wavybot.models.user.music.TrackListen;
import pink.zak.api.wavybot.repositories.user.UserRepository;
import pink.zak.api.wavybot.repositories.user.WavyUserRepository;
import pink.zak.api.wavybot.requesters.WavyRequester;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final WavyUserRepository wavyUserRepository;
    private final WavyRequester requester;
    private final RedisHelper redisHelper;
    private final ListenHelper listenHelper;

    @Autowired
    public UserService(UserRepository userRepository, WavyUserRepository wavyUserRepository, WavyRequester requester, RedisHelper redisHelper, ListenHelper listenHelper) {
        this.userRepository = userRepository;
        this.wavyUserRepository = wavyUserRepository;
        this.requester = requester;
        this.redisHelper = redisHelper;
        this.listenHelper = listenHelper;
    }

    @Cacheable(value = "user")
    public User getUserById(long discordId, boolean createIfAbsent) {
        Optional<User> optionalUser = this.userRepository.findById(discordId);
        if (optionalUser.isPresent())
            return optionalUser.get();
        if (createIfAbsent)
            return this.createUser(discordId);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    @CachePut("user")
    public User createUser(long discordId) {
        return this.userRepository.insert(new User(discordId));
    }

    @CachePut("user")
    public void save(User user) {
        this.userRepository.save(user);
    }

    public Task<Set<WavyListenDto>> linkUser(String wavyUsername, long discordId) throws RiptideStatusException {
        final WavyUser[] testWavyUser = {this.wavyUserRepository.findByUsernameIsIgnoreCase(wavyUsername)};
        if (testWavyUser[0] != null && testWavyUser[0].getUserId() > 1)
            throw RiptideStatusCode.WAVY_ALREADY_LINKED.getException();
        User user = this.getUserById(discordId, true);
        if (user.getWavyUser() != null)
            throw RiptideStatusCode.DISCORD_ALREADY_LINKED.getException();
        return this.requester.retrieveWavyUser(wavyUsername).completable().thenApply(wavyUserDto -> {
            long wavyDiscordId = wavyUserDto.getDiscordId();
            if (wavyDiscordId < 1) {
                throw RiptideStatusCode.WAVY_PROFILE_HAS_NO_DISCORD.getException();
            } else if (wavyDiscordId != discordId) {
                throw RiptideStatusCode.WAVY_DISCORD_DOES_NOT_MATCH.getException();
            } else {
                WavyUser wavyUser = testWavyUser[0] == null ? wavyUserDto.toUser(discordId) : testWavyUser[0];
                wavyUser.setUserId(discordId);
                this.wavyUserRepository.save(wavyUser);
                user.setWavyUser(wavyUser);

                return this.addAllListensForUser(user);
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        }).join();
    }

    public Task<Set<WavyListenDto>> updateUserListens(User user) {
        List<TrackListen> listens = user.getMusicData().getListens();
        if (listens.isEmpty())
            return this.addAllListensForUser(user);
        Collections.sort(listens);
        long mostRecentListenTimestamp = listens.get(0).getListenTime() + 1000; // Add 1000 as otherwise it'll include the most recent
        Task<Set<WavyListenDto>> taskStatus = this.requester.retrieveListensSince(user.getWavyUser().getWavyUuid(), mostRecentListenTimestamp);
        this.addListens(taskStatus, user, mostRecentListenTimestamp);
        return taskStatus;
    }

    public Task<Set<WavyListenDto>> addAllListensForUser(User user) {
        Task<Set<WavyListenDto>> taskStatus = this.requester.retrieveAllListens(user.getWavyUser().getWavyUuid());
        this.addListens(taskStatus, user, 0);
        return taskStatus;
    }

    private void addListens(Task<Set<WavyListenDto>> taskStatus, User user, long previousFetchTime) {
        taskStatus.getFuture().thenAccept(listenDtos -> {
            Set<TrackListen> listens = listenDtos.stream().map(WavyListenDto::toTrackListen).collect(Collectors.toSet());
            MusicData musicData = user.getMusicData();
            musicData.getListens().addAll(listens);
            listenDtos.stream().map(WavyListenDto::getTrack).map(WavyTrackDto::getId).forEach(id -> {
                if (musicData.getTrackPlays().containsKey(id))
                    musicData.getTrackPlays().get(id).incrementAndGet();
                else
                    musicData.getTrackPlays().put(id, new AtomicInteger(1));
            });
            listenDtos.stream().map(WavyListenDto::getTrack).map(WavyTrackDto::getAlbum).map(WavyAlbumDto::getId).forEach(id -> {
                if (musicData.getAlbumPlays().containsKey(id))
                    musicData.getAlbumPlays().get(id).incrementAndGet();
                else
                    musicData.getAlbumPlays().put(id, new AtomicInteger(1));
            });
            listenDtos.stream().map(WavyListenDto::getArtistIds).forEach(idSet -> {
                for (String id : idSet) {
                    if (musicData.getArtistPlays().containsKey(id))
                        musicData.getArtistPlays().get(id).incrementAndGet();
                    else
                        musicData.getArtistPlays().put(id, new AtomicInteger(1));
                }
            });
            this.save(user);
            listenDtos.forEach(this.listenHelper::processListen);
            this.redisHelper.updateLeaderboards(user);
        });
    }
}
