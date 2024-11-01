package com.udemy.tasks.service;

import com.udemy.tasks.model.Task;
import com.udemy.tasks.repository.TaskCustomRepository;
import com.udemy.tasks.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TaskService {



    private final TaskRepository taskRepository;

    private final TaskCustomRepository taskCustomRepository;

    public TaskService(TaskRepository taskRepository,
                       TaskCustomRepository taskCustomRepository) {
        this.taskRepository = taskRepository;
        this.taskCustomRepository = taskCustomRepository;
    }


    public Mono<Task> insert(Task task) {
        return Mono.just(task)
                .map(Task::insert)
                .flatMap(this::save);
    }

    public Page<Task> findPaginated(Task task, Integer pageNumber, Integer Pagesize) {
        return taskCustomRepository.findPaginated(task, pageNumber, Pagesize);
    }

    private Mono<Task> save(Task task){
        return Mono.just(task)
                .map(taskRepository::save);
    }
}
