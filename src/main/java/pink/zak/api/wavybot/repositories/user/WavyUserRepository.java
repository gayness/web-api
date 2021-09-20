package pink.zak.api.wavybot.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pink.zak.api.wavybot.models.user.WavyUser;

import java.util.UUID;

@Repository
public interface WavyUserRepository extends JpaRepository<WavyUser, UUID> {

    WavyUser findByUserDiscordId(long userId);

    WavyUser findByWavyUsernameIgnoreCase(String username);

}
