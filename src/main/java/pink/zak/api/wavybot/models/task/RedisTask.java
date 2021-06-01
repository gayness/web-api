package pink.zak.api.wavybot.models.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash(value = "task", timeToLive = 86400) // 1 day
@AllArgsConstructor
@Getter
public class RedisTask {
    @Id
    private final UUID taskId;
    private final long creationTime;
    private final int requiredProgress;
    private final int progress;
}
