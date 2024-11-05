package com.udemy.tasks.service;

import com.udemy.tasks.exception.TaskNotFoundException;
import com.udemy.tasks.model.Task;
import com.udemy.tasks.repository.TaskCustomRepository;
import com.udemy.tasks.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TaskService {


    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

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
                .flatMap(this::save)
                .doOnError(error -> LOGGER.error("Error during save task. Title: {}", task.getTitle(), error));
    }



    public Mono<Page<Task>> findPaginated(Task task, Integer pageNumber, Integer Pagesize) {
        return taskCustomRepository.findPaginated(task, pageNumber, Pagesize);
    }

    private Mono<Task> save(Task task){
        return Mono.just(task)
                .doOnNext(t -> LOGGER.info("Saving task with title {}", t.getTitle()))
                .flatMap(taskRepository::save);
    }

    public Mono<Task> update(Task task) {
        return taskRepository.findById(task.getId())
                .map(task::update)
                .flatMap(taskRepository::save)
                .switchIfEmpty(Mono.error(TaskNotFoundException::new))
                .doOnError(error -> LOGGER.error("Error during update task with id: {}. Message: {}", task.getId(), error.getMessage()));
    }

    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);

    }
}
