package pink.zak.api.wavybot.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pink.zak.api.wavybot.models.server.Server;

import java.util.List;

@Repository
public interface ServerRepository extends MongoRepository<Server, Long> {

    List<Server> findByLinkedUsersContains(long userId);
}
