package pink.zak.api.wavybot.repositories.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pink.zak.api.wavybot.models.user.User;
import pink.zak.api.wavybot.models.user.WavyUser;

import java.util.UUID;

@Repository
public interface WavyUserRepository extends MongoRepository<WavyUser, UUID> {

    WavyUser findByUserIdIs(User user);
}
