package pink.zak.api.wavybot.repositories.task;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;
import pink.zak.api.wavybot.models.task.RedisTask;

import java.util.UUID;

@Repository
public interface RedisTaskRepository extends KeyValueRepository<RedisTask, UUID> {
}
