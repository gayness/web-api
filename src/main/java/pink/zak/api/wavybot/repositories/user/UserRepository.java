package pink.zak.api.wavybot.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pink.zak.api.wavybot.models.user.User;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByWavyUserWavyUuidIs(UUID uuid);
}
