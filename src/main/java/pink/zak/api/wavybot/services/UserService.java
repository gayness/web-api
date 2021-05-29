package pink.zak.api.wavybot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pink.zak.api.wavybot.models.user.User;
import pink.zak.api.wavybot.repositories.user.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "user")
    public User getUserById(long discordId) {
        return this.userRepository.findById(discordId).orElse(null);
    }
}
