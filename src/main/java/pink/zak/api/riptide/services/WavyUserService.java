package pink.zak.api.riptide.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.riptide.helpers.ListenHelper;
import pink.zak.api.riptide.helpers.redis.RedisHelper;
import pink.zak.api.riptide.models.dto.wavy.music.WavyTrackDto;
import pink.zak.api.riptide.models.dto.wavy.music.album.WavyAlbumDto;
import pink.zak.api.riptide.models.dto.wavy.music.listens.WavyListenDto;
import pink.zak.api.riptide.models.task.Task;
import pink.zak.api.riptide.models.user.WavyUser;
import pink.zak.api.riptide.models.user.music.MusicData;
import pink.zak.api.riptide.models.user.music.TrackListen;
import pink.zak.api.riptide.repositories.user.WavyUserRepository;
import pink.zak.api.riptide.requesters.WavyRequester;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class WavyUserService {
    private final WavyUserRepository wavyUserRepository;
    private final RedisHelper redisHelper;
    private final ListenHelper listenHelper;
    private final WavyRequester requester;

    @Autowired
    public WavyUserService(WavyUserRepository wavyUserRepository, RedisHelper redisHelper, ListenHelper listenHelper, WavyRequester requester) {
        this.wavyUserRepository = wavyUserRepository;
        this.redisHelper = redisHelper;
        this.listenHelper = listenHelper;
        this.requester = requester;
    }

    //@Cacheable("wavyUser")
    public WavyUser getById(long discordId) {
        WavyUser wavyUser = this.wavyUserRepository.findByUserDiscordId(discordId);
        if (wavyUser == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Music data not found");
        return wavyUser;
    }

    //@CachePut("wavyUser")
    public WavyUser save(WavyUser wavyUser) {
        return this.wavyUserRepository.save(wavyUser);
    }

    public Task<Set<WavyListenDto>> updateUserListens(WavyUser wavyUser) {
        MusicData musicData = wavyUser.getMusicData();
        List<TrackListen> listens = musicData.getListens();
        if (listens.isEmpty())
            return this.addAllListensForUser(wavyUser);
        Collections.sort(listens);
        long mostRecentListenTimestamp = listens.get(0).getListenTime() + 1000; // Add 1000 as otherwise it'll include the most recent
        Task<Set<WavyListenDto>> taskStatus = this.requester.retrieveListensSince(wavyUser.getWavyUuid(), mostRecentListenTimestamp);
        this.addListens(taskStatus, wavyUser);
        return taskStatus;
    }

    public Task<Set<WavyListenDto>> addAllListensForUser(WavyUser user) {
        Task<Set<WavyListenDto>> taskStatus = this.requester.retrieveAllListens(user.getWavyUuid());
        this.addListens(taskStatus, user);
        return taskStatus;
    }

    private void addListens(Task<Set<WavyListenDto>> taskStatus, WavyUser user) {
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
            this.redisHelper.updateLeaderboards(user, musicData);
            listenDtos.forEach(this.listenHelper::processListen);
        });
    }
}
