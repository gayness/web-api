package pink.zak.api.riptide.repositories.task;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;
import pink.zak.api.riptide.models.task.RedisTask;

import java.util.UUID;

@Repository
public interface RedisTaskRepository extends KeyValueRepository<RedisTask, UUID> {
}
