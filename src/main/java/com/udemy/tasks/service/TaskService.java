package com.udemy.tasks.service;

import com.udemy.tasks.model.Task;
import com.udemy.tasks.repository.TaskRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TaskService {



    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public Mono<Task> insert(Task task) {
        return Mono.just(task)
                .map(Task::insert)
                .flatMap(this::save);
    }

    public Mono<List<Task>> list() {
        return Mono.just(taskRepository.findAll());
    }

    private Mono<Task> save(Task task){
        return Mono.just(task)
                .map(taskRepository::save);
    }
}
