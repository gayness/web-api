package pink.zak.api.wavybot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.wavybot.models.user.WavyUser;
import pink.zak.api.wavybot.repositories.user.WavyUserRepository;

@Service
public class WavyUserService {
    private final WavyUserRepository wavyUserRepository;

    @Autowired
    public WavyUserService(WavyUserRepository wavyUserRepository) {
        this.wavyUserRepository = wavyUserRepository;
    }

    @Cacheable("wavyUser")
    public WavyUser getById(long discordId) {
        WavyUser wavyUser = this.wavyUserRepository.findByUserIdIs(discordId);
        if (wavyUser == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Music data not found");
        return wavyUser;
    }

    @CachePut("wavyUser")
    public WavyUser insert(WavyUser wavyUser) {
        return this.wavyUserRepository.insert(wavyUser);
    }

    @CachePut("wavyUser")
    public WavyUser save(WavyUser wavyUser) {
        return this.wavyUserRepository.save(wavyUser);
    }
}
