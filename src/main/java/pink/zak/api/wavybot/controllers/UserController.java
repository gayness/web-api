package pink.zak.api.wavybot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pink.zak.api.wavybot.models.task.NewTaskResponse;
import pink.zak.api.wavybot.models.user.User;
import pink.zak.api.wavybot.models.user.WavyUser;
import pink.zak.api.wavybot.models.user.music.MusicData;
import pink.zak.api.wavybot.services.MusicDataService;
import pink.zak.api.wavybot.services.UserService;
import pink.zak.api.wavybot.services.WavyUserService;

@RestController
@RequestMapping("/user/{discordId}")
public class UserController {
    private final UserService userService;
    private final WavyUserService wavyUserService;
    private final MusicDataService musicDataService;

    @Autowired
    public UserController(UserService userService, WavyUserService wavyUserService, MusicDataService musicDataService) {
        this.userService = userService;
        this.wavyUserService = wavyUserService;
        this.musicDataService = musicDataService;
    }

    @GetMapping("get")
    public User getUserById(@PathVariable long discordId, boolean createIfAbsent) {
        return this.userService.getUserById(discordId, createIfAbsent);
    }

    @GetMapping("getWavy")
    public WavyUser getWavyUser(@PathVariable long discordId) {
        return this.wavyUserService.getById(discordId);
    }

    @GetMapping("getMusicData")
    public MusicData getMusicData(@PathVariable long discordId) {
        return this.musicDataService.getByDiscordId(discordId);
    }

    @GetMapping("updateListens")
    public NewTaskResponse updateUserListens(@PathVariable long discordId) {
        WavyUser user = this.wavyUserService.getById(discordId);
        return this.wavyUserService.updateUserListens(user).toResponse();
    }

    @GetMapping("linkWavy")
    public NewTaskResponse linkWavy(@PathVariable long discordId, @RequestParam String wavyUsername) {
        return this.userService.linkUser(wavyUsername, discordId).toResponse();
    }
}
