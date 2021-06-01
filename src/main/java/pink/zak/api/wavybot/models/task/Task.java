package pink.zak.api.wavybot.models.task;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

@RedisHash(value = "task", timeToLive = 86400) // 1 day
@Data
@NoArgsConstructor
public class Task<T> {
    @Id
    private UUID taskId;
    private int requiredProgress;
    private AtomicInteger progress = new AtomicInteger();

    @Transient
    private CompletableFuture<T> future;

    public void updateProgress(IntUnaryOperator updateFunction) {
        this.progress.updateAndGet(updateFunction);
    }

    @Transient
    public boolean isCompleted() {
        return this.progress.get() >= this.requiredProgress;
    }

    @Transient
    public TaskResponse toResponse() {
        return new TaskResponse(this.taskId, this.requiredProgress);
    }
}
