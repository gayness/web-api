package pink.zak.api.wavybot.models.task;

import lombok.Data;

import java.util.UUID;

@Data
public class TaskResponse {
    private final UUID taskId;
    private final int requiredProgress;

    public TaskResponse(UUID taskId, int requiredProgress) {
        this.taskId = taskId;
        this.requiredProgress = requiredProgress;
    }
}
