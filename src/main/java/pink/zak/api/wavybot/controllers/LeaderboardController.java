package pink.zak.api.wavybot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pink.zak.api.wavybot.helpers.redis.Leaderboard;
import pink.zak.api.wavybot.services.LeaderboardService;
import reactor.util.function.Tuple2;

import java.util.Map;

@RestController
@RequestMapping("/leaderboard/{leaderboard}/{discordId}")
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("getPartial")
    public Map<Long, Tuple2<Object, Long>> getPartial(
            @PathVariable Leaderboard leaderboard,
            @PathVariable long discordId,
            @RequestParam(name = "start", required = false, defaultValue = "1") long start,
            @RequestParam(name = "end", required = false, defaultValue = "15") long end) {
        return this.leaderboardService.getPartialLeaderboard(leaderboard, discordId, start - 1, end - 1);
    }
}
