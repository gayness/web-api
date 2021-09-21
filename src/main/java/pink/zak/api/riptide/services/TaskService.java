package pink.zak.api.riptide.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.riptide.models.task.RedisTask;
import pink.zak.api.riptide.models.task.Task;
import pink.zak.api.riptide.repositories.task.LocalTaskRepository;
import pink.zak.api.riptide.repositories.task.RedisTaskRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    private final LocalTaskRepository localTaskRepository;
    private final RedisTaskRepository redisTaskRepository;

    @Autowired
    public TaskService(LocalTaskRepository localTaskRepository, RedisTaskRepository redisTaskRepository) {
        this.localTaskRepository = localTaskRepository;
        this.redisTaskRepository = redisTaskRepository;
    }

    public RedisTask getTaskById(UUID uuid) {
        Optional<RedisTask> optionalTask = this.redisTaskRepository.findById(uuid);
        if (optionalTask.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        return optionalTask.get();
    }

    public void addTask(Task<?> task) {
        this.localTaskRepository.addTask(task);
        this.redisTaskRepository.save(task.toRedisTask());
    }
}
