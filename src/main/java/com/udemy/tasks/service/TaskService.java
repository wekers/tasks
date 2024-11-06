package com.udemy.tasks.service;

import com.udemy.tasks.exception.TaskNotFoundException;
import com.udemy.tasks.model.Address;
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

    private final TaskRepository repository;

    private final TaskCustomRepository taskCustomRepository;

    private final AddressService addressService;

    public TaskService(TaskRepository taskRepository,
                       TaskCustomRepository taskCustomRepository, AddressService addressService) {
        this.repository = taskRepository;
        this.taskCustomRepository = taskCustomRepository;
        this.addressService = addressService;
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
                .flatMap(repository::save);
    }

    public Mono<Task> update(Task task) {
        return repository.findById(task.getId())
                .map(task::update)
                .flatMap(repository::save)
                .switchIfEmpty(Mono.error(TaskNotFoundException::new))
                .doOnError(error -> LOGGER.error("Error during update task with id: {}. Message: {}", task.getId(), error.getMessage()));
    }

    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);

    }

    public Mono<Task> updateAddress(Task task, Address address) {
        return Mono.just(task)
                .map(it -> task.updateAddress(address));
    }

    public Mono<Task> start(String id, String zipcode) {
        return repository.findById(id)
                .zipWhen(it -> addressService.getAddress(zipcode))
                .flatMap(it -> updateAddress(it.getT1(), it.getT2()))
                .map(Task::start)
                .flatMap(repository::save)
                .switchIfEmpty(Mono.error(TaskNotFoundException::new))
                .doOnError(error -> LOGGER.error("Error on start task. ID: {}", id, error));
    }
}

