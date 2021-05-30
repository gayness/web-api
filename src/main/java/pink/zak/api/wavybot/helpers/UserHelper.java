package pink.zak.api.wavybot.helpers;

import com.mongodb.lang.NonNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import pink.zak.api.wavybot.helpers.redis.RedisHelper;
import pink.zak.api.wavybot.models.dto.wavy.music.listens.WavyListenDto;
import pink.zak.api.wavybot.models.user.User;
import pink.zak.api.wavybot.models.user.music.TrackListen;
import pink.zak.discord.wavybot.models.dto.wavy.music.WavyTrackDto;
import pink.zak.discord.wavybot.models.dto.wavy.music.album.WavyAlbumDto;
import pink.zak.discord.wavybot.models.user.music.MusicData;
import pink.zak.discord.wavybot.requesters.WavyRequester;
import pink.zak.discord.wavybot.storage.UserStorage;
import pink.zak.discord.wavybot.storage.management.DatabaseStorageManager;
import pink.zak.discord.wavybot.utils.futures.TaskStatus;
import pink.zak.discord.wavybot.utils.threads.ThreadManager;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class UserHelper {
    private final WavyRequester requester;
    private final ListenHelper listenHelper;
    private final RedisHelper redisHelper;
    private final UserStorage userStorage;

    public UserHelper(WavyRequester requester, ListenHelper listenHelper, RedisHelper redisHelper, DatabaseStorageManager storageManager) {
        this.requester = requester;
        this.listenHelper = listenHelper;
        this.redisHelper = redisHelper;
        this.userStorage = storageManager.getUserStorage();
    }

    public LinkResponse linkUser(String wavyUsername, long discordId) {
        User wavyTestUser = this.userStorage.findByWavyUsername(wavyUsername);
        System.out.println("Wavy test user " + wavyTestUser);
        if (wavyTestUser != null)
            return new LinkResponse(LinkResponse.ResponseStatus.WAVY_ALREADY_LINKED, null);
        User user = this.getOrCreateUser(discordId);
        if (user.getWavyUser() != null)
            return new LinkResponse(LinkResponse.ResponseStatus.DISCORD_ALREADY_LINKED, null);

        return this.requester.retrieveWavyUser(wavyUsername).thenApply(wavyUserDto -> {
            long wavyDiscordId = wavyUserDto.getDiscordId();
            if (wavyDiscordId < 1) {
                return new LinkResponse(LinkResponse.ResponseStatus.WAVY_PROFILE_HAS_NO_DISCORD, null);
            } else if (wavyDiscordId != discordId) {
                return new LinkResponse(LinkResponse.ResponseStatus.WAVY_DISCORD_DOES_NOT_MATCH, null);
            } else {
                user.setWavyUser(wavyUserDto.toUser());
                TaskStatus<?> taskStatus = this.addAllListensForUser(user);
                return new LinkResponse(LinkResponse.ResponseStatus.SUCCESSFUL, taskStatus);
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        }).join();
    }

    public TaskStatus<?> updateUserListens(User user) {
        List<TrackListen> listens = user.getMusicData().getListens();
        if (listens.isEmpty())
            return this.addAllListensForUser(user);
        Collections.sort(listens);
        long mostRecentListenTimestamp = listens.get(0).getListenTime() + 1000; // Add 1000 as otherwise it'll include the most recent
        TaskStatus<Set<WavyListenDto>> taskStatus = this.requester.retrieveListensSince(user.getWavyUser().getUuid(), mostRecentListenTimestamp);
        this.addListens(taskStatus, user, mostRecentListenTimestamp);
        return taskStatus;
    }

    public TaskStatus<Set<WavyListenDto>> addAllListensForUser(User user) {
        TaskStatus<Set<WavyListenDto>> taskStatus = this.requester.retrieveAllListens(user.getWavyUser().getUuid());
        this.addListens(taskStatus, user, 0);
        return taskStatus;
    }

    private void addListens(TaskStatus<Set<WavyListenDto>> taskStatus, User user, long previousFetchTime) {
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
            this.userStorage.saveUser(user);
            listenDtos.forEach(this.listenHelper::processListen);
            this.redisHelper.updateLeaderboards(user);
        });
    }

    public static class LinkResponse {
        @NonNull
        private final ResponseStatus responseStatus;
        @Nullable
        private final TaskStatus<?> taskStatus;

        private LinkResponse(@NonNull ResponseStatus responseStatus, @Nullable TaskStatus<?> taskStatus) {
            this.responseStatus = responseStatus;
            this.taskStatus = taskStatus;
        }

        @NonNull
        public ResponseStatus getResponseStatus() {
            return this.responseStatus;
        }

        @Nullable
        public TaskStatus<?> getTaskStatus() {
            return this.taskStatus;
        }

        public enum ResponseStatus {
            WAVY_ALREADY_LINKED,
            DISCORD_ALREADY_LINKED,
            WAVY_PROFILE_HAS_NO_DISCORD,
            WAVY_DISCORD_DOES_NOT_MATCH,
            SUCCESSFUL
        }
    }
}
