package pink.zak.api.wavybot.services;

import com.google.common.collect.Maps;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import pink.zak.api.wavybot.helpers.redis.Leaderboard;

import java.util.Map;
import java.util.Set;

@Service
public class LeaderboardService {
    private final RedisTemplate<String, Object> redis;

    public LeaderboardService(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }

    public Map<Long, Pair<Object, Long>> getPartialLeaderboard(Leaderboard leaderboard, long discordId, long start, long end) {
        Set<ZSetOperations.TypedTuple<Object>> idk = this.redis.opsForZSet().reverseRangeWithScores(leaderboard.getLeaderboardId(discordId), start, end);
        System.out.println(idk);
        Map<Long, Pair<Object, Long>> positionMap = Maps.newHashMap();
        long i = start - 1;
        for (ZSetOperations.TypedTuple<Object> tuple : idk) {
            i++;
            positionMap.put(i + 1, Pair.of(tuple.getValue(), Math.round(tuple.getScore())));
        }
        return positionMap;
    }
}
