package pink.zak.api.wavybot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pink.zak.api.wavybot.models.dto.riptide.UserLinkDto;
import pink.zak.api.wavybot.models.task.TaskResponse;
import pink.zak.api.wavybot.models.user.User;
import pink.zak.api.wavybot.services.UserService;

@RestController
@RequestMapping("/user/{discordId}")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public User getUserById(@PathVariable long discordId, boolean createIfAbsent) {
        return this.userService.getUserById(discordId, createIfAbsent);
    }

    @PostMapping("/updateListens")
    public TaskResponse updateUserListens(@PathVariable long discordId) {
        System.out.println("BRRRRRRR");
        User user = this.userService.getUserById(discordId, false);
        return this.userService.updateUserListens(user).toResponse();
    }

    @PostMapping("/linkWavy")
    public TaskResponse linkWavy(@PathVariable long discordId, @RequestBody UserLinkDto userLinkDto) {
        System.out.println("BRRRRRRR");
        return this.userService.linkUser(userLinkDto.getUsername(), discordId).toResponse();
    }
}
