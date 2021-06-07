package pink.zak.api.wavybot.helpers.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import pink.zak.api.wavybot.models.server.Server;
import pink.zak.api.wavybot.models.user.User;
import pink.zak.api.wavybot.models.user.music.MusicData;
import pink.zak.api.wavybot.repositories.ServerRepository;
import pink.zak.api.wavybot.services.MusicDataService;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class RedisHelper {
    private final ServerRepository serverRepository;
    private final MusicDataService musicDataService;
    private final RedisTemplate<String, Object> redis;

    @Autowired
    public RedisHelper(ServerRepository serverRepository, MusicDataService musicDataService, RedisTemplate<String, Object> redisConnection) {
        this.serverRepository = serverRepository;
        this.musicDataService = musicDataService;
        this.redis = redisConnection;
    }

    @Async
    public ListenableFuture<Void> updateLeaderboards(User user) {
        Set<Long> serverIds = this.getServerLeaderboardsForUser(user);
        MusicData musicData = this.musicDataService.getByDiscordId(user.getDiscordId());
        double listens = musicData.getListens().size();
        double uniqueAlbums = musicData.getAlbumPlays().size();
        double uniqueArtists = musicData.getArtistPlays().size();
        String userId = String.valueOf(user.getDiscordId());
        for (long serverId : serverIds) {
            this.redis.opsForZSet().add(Leaderboard.SERVER_TRACKS.getLeaderboardId(serverId), userId, listens);
            this.redis.opsForZSet().add(Leaderboard.SERVER_ALBUMS.getLeaderboardId(serverId), userId, uniqueAlbums);
            this.redis.opsForZSet().add(Leaderboard.SERVER_ARTISTS.getLeaderboardId(serverId), userId, uniqueArtists);
        }
        String trackLeaderboardId = Leaderboard.PERSONAL_TRACKS.getLeaderboardId(user.getDiscordId());
        String albumLeaderboardId = Leaderboard.PERSONAL_ALBUMS.getLeaderboardId(user.getDiscordId());
        String artistLeaderboardId = Leaderboard.PERSONAL_ARTISTS.getLeaderboardId(user.getDiscordId());
        for (Map.Entry<String, AtomicInteger> entry : musicData.getTrackPlays().entrySet()) {
            this.redis.opsForZSet().add(trackLeaderboardId, entry.getKey(), entry.getValue().get());
        }
        for (Map.Entry<String, AtomicInteger> entry : musicData.getAlbumPlays().entrySet()) {
            this.redis.opsForZSet().add(albumLeaderboardId, entry.getKey(), entry.getValue().get());
        }
        for (Map.Entry<String, AtomicInteger> entry : musicData.getArtistPlays().entrySet()) {
            this.redis.opsForZSet().add(artistLeaderboardId, entry.getKey(), entry.getValue().get());
        }
        return new AsyncResult<>(null);
    }

    private Set<Long> getServerLeaderboardsForUser(User user) {
        return this.serverRepository.findByLinkedUsersContains(user.getDiscordId()).stream()
                .map(Server::getServerId)
                .collect(Collectors.toSet());
    }
}
