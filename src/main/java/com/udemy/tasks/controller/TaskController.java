package com.udemy.tasks.controller;


import com.udemy.tasks.controller.converter.TaskDTOConverter;
import com.udemy.tasks.controller.dto.TaskDTO;
import com.udemy.tasks.model.Task;
import com.udemy.tasks.service.TaskService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Mono<List<TaskDTO>> getTasks(){
        return service.list().map(converter::convertList);
    }

    @PostMapping
    public Mono<TaskDTO> createTask(@RequestBody Task task){
        return service.insert(task).map(converter::convert);
    }
}
