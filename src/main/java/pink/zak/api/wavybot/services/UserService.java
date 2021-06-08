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
import pink.zak.api.wavybot.models.dto.wavy.music.listens.WavyListenDto;
import pink.zak.api.wavybot.models.task.Task;
import pink.zak.api.wavybot.models.user.User;
import pink.zak.api.wavybot.models.user.WavyUser;
import pink.zak.api.wavybot.repositories.user.UserRepository;
import pink.zak.api.wavybot.repositories.user.WavyUserRepository;
import pink.zak.api.wavybot.requesters.WavyRequester;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final WavyUserRepository wavyUserRepository;
    private final WavyUserService wavyUserService;
    private final WavyRequester requester;

    @Autowired
    public UserService(UserRepository userRepository, WavyUserRepository wavyUserRepository, WavyUserService wavyUserService, MusicDataService musicDataService, WavyRequester requester, RedisHelper redisHelper, ListenHelper listenHelper) {
        this.userRepository = userRepository;
        this.wavyUserRepository = wavyUserRepository;
        this.wavyUserService = wavyUserService;
        this.requester = requester;
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
    public User save(User user) {
        return this.userRepository.save(user);
    }

    public Task<Set<WavyListenDto>> linkUser(String wavyUsername, long discordId) throws RiptideStatusException {
        WavyUser testWavyUserX = this.wavyUserRepository.findByUsernameIsIgnoreCase(wavyUsername);
        if (testWavyUserX != null && testWavyUserX.getDiscordId() > 1)
            throw RiptideStatusCode.WAVY_ALREADY_LINKED.getException();
        User user = this.getUserById(discordId, true);
        if (user.getWavyUuid() != null)
            throw RiptideStatusCode.DISCORD_ALREADY_LINKED.getException();
        return this.requester.retrieveWavyUser(wavyUsername).completable().thenApply(wavyUserDto -> {
            long wavyDiscordId = wavyUserDto.getDiscordId();
            if (wavyDiscordId < 1) {
                throw RiptideStatusCode.WAVY_PROFILE_HAS_NO_DISCORD.getException();
            } else if (wavyDiscordId != discordId) {
                throw RiptideStatusCode.WAVY_DISCORD_DOES_NOT_MATCH.getException();
            } else {
                WavyUser wavyUser = testWavyUserX == null ? wavyUserDto.toUser(discordId) : testWavyUserX;
                wavyUser.setDiscordId(discordId);
                user.setWavyUuid(wavyUser.getWavyUuid());
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
