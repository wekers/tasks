package com.udemy.tasks.controller;


import com.udemy.tasks.controller.converter.TaskDTOConverter;
import com.udemy.tasks.controller.dto.TaskDTO;
import com.udemy.tasks.model.Task;
import com.udemy.tasks.model.TaskState;
import com.udemy.tasks.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tasks")
public class TaskController {


    private final TaskService service;
    private final TaskDTOConverter converter;

    public TaskController(TaskService service, TaskDTOConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    public Page<TaskDTO> getTasks(@RequestParam(required = false) String id,
                                   @RequestParam(required = false) String title,
                                   @RequestParam(required = false) String description,
                                   @RequestParam(required = false, defaultValue = "0") int priority,
                                   @RequestParam(required = false) TaskState taskState,
                                   @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        //return service.list().map(converter::convertList);
        return service.findPaginated(converter.converter(id, title, description, priority, taskState), pageNumber, pageSize).map(converter::convert);
    }

    @PostMapping
    public Mono<TaskDTO> createTask(@RequestBody Task task){
        return service.insert(task).map(converter::convert);
    }
}
