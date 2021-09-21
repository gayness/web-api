package pink.zak.api.riptide.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.riptide.exceptions.RiptideStatusCode;
import pink.zak.api.riptide.exceptions.RiptideStatusException;
import pink.zak.api.riptide.models.dto.wavy.music.listens.WavyListenDto;
import pink.zak.api.riptide.models.task.Task;
import pink.zak.api.riptide.models.user.User;
import pink.zak.api.riptide.models.user.WavyUser;
import pink.zak.api.riptide.repositories.user.UserRepository;
import pink.zak.api.riptide.repositories.user.WavyUserRepository;
import pink.zak.api.riptide.requesters.WavyRequester;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final WavyUserRepository wavyUserRepository;
    private final WavyUserService wavyUserService;
    private final WavyRequester requester;

    @Autowired
    public UserService(UserRepository userRepository, WavyUserRepository wavyUserRepository, WavyUserService wavyUserService, WavyRequester requester) {
        this.userRepository = userRepository;
        this.wavyUserRepository = wavyUserRepository;
        this.wavyUserService = wavyUserService;
        this.requester = requester;
    }

    //@Cacheable(value = "user")
    public User getUserById(long discordId, boolean createIfAbsent) {
        Optional<User> optionalUser = this.userRepository.findById(discordId);
        if (optionalUser.isPresent())
            return optionalUser.get();
        if (createIfAbsent)
            return this.createUser(discordId);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    //@CachePut("user")
    public User createUser(long discordId) {
        return this.save(new User(discordId));
    }

    //@CachePut("user")
    public User save(User user) {
        return this.userRepository.save(user);
    }

    public Task<Set<WavyListenDto>> linkUser(String wavyUsername, long discordId) throws RiptideStatusException {
        User user = this.getUserById(discordId, true);
        if (user.getWavyUser() != null)
            throw RiptideStatusCode.DISCORD_ALREADY_LINKED.getException();

        WavyUser currentlyLinkedWavy = this.wavyUserRepository.findByWavyUsernameIgnoreCase(wavyUsername);
        if (currentlyLinkedWavy != null && currentlyLinkedWavy.getUser() != null)
            throw RiptideStatusCode.WAVY_ALREADY_LINKED.getException();

        return this.requester.retrieveWavyUser(wavyUsername).thenApply(wavyUserDto -> {
            long wavyDiscordId = wavyUserDto.getDiscordId();
            if (wavyDiscordId < 1) {
                throw RiptideStatusCode.WAVY_PROFILE_HAS_NO_DISCORD.getException();
            } else if (wavyDiscordId != discordId) {
                throw RiptideStatusCode.WAVY_DISCORD_DOES_NOT_MATCH.getException();
            } else {
                WavyUser wavyUser = currentlyLinkedWavy == null ? wavyUserDto.toUser(user) : currentlyLinkedWavy;
                user.setWavyUser(wavyUser);

                this.wavyUserService.save(wavyUser);
                this.save(user);

                return this.wavyUserService.updateUserListens(wavyUser);
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        }).join();
    }
}
