package pink.zak.api.riptide.models.task;

import lombok.Data;

import java.util.UUID;

@Data
public class NewTaskResponse {
    private final UUID taskId;

    public NewTaskResponse(UUID taskId) {
        this.taskId = taskId;
    }
}
