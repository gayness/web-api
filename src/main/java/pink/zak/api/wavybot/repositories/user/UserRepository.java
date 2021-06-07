package pink.zak.api.wavybot.repositories.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pink.zak.api.wavybot.models.user.User;

import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {

    User findByWavyUuidIs(UUID uuid);
}
