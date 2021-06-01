package pink.zak.api.wavybot.repositories.task;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import pink.zak.api.wavybot.models.task.Task;

import java.util.Map;
import java.util.UUID;

@Repository
public class LocalTaskRepository {
    private final Map<UUID, Task<?>> activeTasks = Maps.newConcurrentMap();

    private final RedisTaskRepository redisTaskRepository;

    @Autowired
    public LocalTaskRepository(RedisTaskRepository redisTaskRepository) {
        this.redisTaskRepository = redisTaskRepository;
    }

    public void addTask(Task<?> task) {
        this.activeTasks.put(task.getTaskId(), task);
    }

    @Scheduled(fixedRate = 500)
    private void updateDatabase() {
        this.redisTaskRepository.saveAll(this.activeTasks.values());
        for (Task<?> task : this.activeTasks.values())
            if (task.isCompleted())
                this.activeTasks.remove(task.getTaskId());
    }

}
