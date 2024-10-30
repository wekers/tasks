package com.udemy.tasks.controller;


import com.udemy.tasks.model.Task;
import com.udemy.tasks.service.TaskService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {


    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public Mono<List<Task>> getTasks(){
        return service.list();
    }

    @PostMapping
    public Mono<Task> createTask(@RequestBody Task task){
        return service.insert(task);
    }
}
