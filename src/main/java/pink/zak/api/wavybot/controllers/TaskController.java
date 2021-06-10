package pink.zak.api.wavybot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pink.zak.api.wavybot.models.task.RedisTask;
import pink.zak.api.wavybot.services.TaskService;

import java.util.UUID;

@RestController
@RequestMapping("/task/{uuid}")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("get")
    public RedisTask getTask(@PathVariable UUID uuid) {
        return this.taskService.getTaskById(uuid);
    }
}
