package pink.zak.api.wavybot.repositories.task;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import pink.zak.api.wavybot.models.task.Task;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
    protected void updateDatabase() {
        this.redisTaskRepository.saveAll(this.activeTasks.values().stream().map(Task::toRedisTask).collect(Collectors.toSet()));
        for (Task<?> task : this.activeTasks.values())
            if (task.isCompleted() && (System.currentTimeMillis() - task.getCreationTime() > 10000 || task.getRequiredProgress() > 0)) {
                this.activeTasks.remove(task.getTaskId());
                System.out.println("Removing task ID from local repo");
            }
    }

}
