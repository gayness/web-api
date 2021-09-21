package pink.zak.api.riptide.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pink.zak.api.riptide.helpers.redis.Leaderboard;
import pink.zak.api.riptide.services.LeaderboardService;

import java.util.Map;

@RestController
@RequestMapping("/leaderboard/{discordId}")
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("getPartial")
    public Map<Long, Pair<Object, Long>> getPartial(
            @PathVariable long discordId,
            @RequestParam Leaderboard leaderboard,
            @RequestParam(required = false, defaultValue = "1") long start,
            @RequestParam(required = false, defaultValue = "15") long end) {
        return this.leaderboardService.getPartialLeaderboard(leaderboard, discordId, start - 1, end - 1);
    }
}
