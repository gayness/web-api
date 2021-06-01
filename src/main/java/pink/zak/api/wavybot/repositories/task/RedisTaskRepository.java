package pink.zak.api.wavybot.repositories.task;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pink.zak.api.wavybot.models.task.RedisTask;

import java.util.UUID;

@Repository
public interface RedisTaskRepository extends CrudRepository<RedisTask, UUID> {
}
