package pink.zak.api.wavybot.repositories.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pink.zak.api.wavybot.models.user.User;
import pink.zak.api.wavybot.models.user.WavyUser;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {

    User findByWavyUserIs(WavyUser user);
}
