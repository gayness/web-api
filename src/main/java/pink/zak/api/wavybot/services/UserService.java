package pink.zak.api.wavybot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.wavybot.models.user.User;
import pink.zak.api.wavybot.repositories.user.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "user")
    public User getUserById(long discordId, boolean createIfAbsent) {
        Optional<User> optionalUser = this.userRepository.findById(discordId);
        if (optionalUser.isPresent())
            return optionalUser.get();
        if (createIfAbsent)
            return this.userRepository.insert(new User(discordId));
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    @CachePut("user")
    public void save(User album) {
        this.userRepository.save(album);
    }
}
