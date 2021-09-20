package pink.zak.api.wavybot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pink.zak.api.wavybot.models.server.Server;
import pink.zak.api.wavybot.models.user.User;

import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

    List<Server> findByLinkedUsersContains(User user);
}
