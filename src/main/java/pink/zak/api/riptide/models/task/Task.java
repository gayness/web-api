package pink.zak.api.riptide.models.task;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

@Data
@NoArgsConstructor
public class Task<T> {
    @Id
    private UUID taskId;
    private long creationTime;
    private int requiredProgress;
    private AtomicInteger progress = new AtomicInteger();

    @Transient
    private CompletableFuture<T> future;

    public static <T> Task<T> create() {
        Task<T> task = new Task<>();
        task.setTaskId(UUID.randomUUID());
        task.setCreationTime(System.currentTimeMillis());
        return task;
    }

    public void updateProgress(IntUnaryOperator updateFunction) {
        this.progress.updateAndGet(updateFunction);
    }

    @Transient
    public boolean isCompleted() {
        return this.progress.get() >= this.requiredProgress;
    }

    @Transient
    public NewTaskResponse toResponse() {
        return new NewTaskResponse(this.taskId);
    }

    @Transient
    public RedisTask toRedisTask() {
        return new RedisTask(this.taskId, this.creationTime, this.requiredProgress, this.progress.get());
    }
}
