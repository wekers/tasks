package com.udemy.tasks.service;

import com.udemy.tasks.exception.TaskNotFoundException;
import com.udemy.tasks.messaging.TaskNotificationProducer;
import com.udemy.tasks.model.Address;
import com.udemy.tasks.model.Task;
import com.udemy.tasks.repository.TaskCustomRepository;
import com.udemy.tasks.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {


    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository repository;

    private final TaskCustomRepository taskCustomRepository;

    private final AddressService addressService;

    private final TaskNotificationProducer producer;

    public TaskService(TaskRepository taskRepository,
                       TaskCustomRepository taskCustomRepository, AddressService addressService, TaskNotificationProducer producer) {
        this.repository = taskRepository;
        this.taskCustomRepository = taskCustomRepository;
        this.addressService = addressService;
        this.producer = producer;
    }


    public Mono<Task> insert(Task task) {
        return Mono.just(task)
                .map(Task::insert)
                .flatMap(this::save)
                .doOnError(error -> LOGGER.error("Error during save task. Title: {}", task.getTitle(), error));
    }

    public Flux<Task> list() {
        return repository.findAll();
    }




    public Page<Task> findPaginated(Task task, Integer pageNumber, Integer pageSize) {
        return taskCustomRepository.findPaginated(task, pageNumber, pageSize)
                .block();
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

    @PostConstruct
    private void scheduleDoneOlderTasks(){
        Mono.delay(Duration.ofSeconds(5))
                .doOnNext(it -> LOGGER.info("Starting task monitoring"))
                .subscribe();

        //Flux.interval(Duration.ofSeconds(10))
        Flux.interval(Duration.ofDays(1))
                .flatMap(it -> doneOlderTasks())
                .filter(tasks -> tasks > 0)
                .doOnNext(tasks -> LOGGER.info("{} task(s) completed after being active for over 7 days.", tasks))
                .subscribe();
    }

    private Mono<Long> doneOlderTasks(){
        return taskCustomRepository.updateStateToDoneForOlderTasks(LocalDate.now().minusDays(7));

    }

    public Mono<Task> start(String id, String zipcode) {
        return repository.findById(id)
                .zipWhen(it -> addressService.getAddress(zipcode))
                .flatMap(it -> updateAddress(it.getT1(), it.getT2()))
                .map(Task::start)
                .flatMap(repository::save)
                .flatMap(producer::sendNotification)
                .switchIfEmpty(Mono.error(TaskNotFoundException::new))
                .doOnError(error -> LOGGER.error("Error on start task. ID: {}", id, error))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(10))
                        .jitter(0.5)
                        .filter(throwable -> throwable instanceof TaskNotFoundException));
    }

    public Mono<Task> done(Task task) {
        return Mono.just(task)
                .doOnNext(it -> LOGGER.info("Finishing task. ID: {}", task.getId()))
                .map(Task::done)
                .flatMap(repository::save);
    }

    public Mono<List<Task>> doneMany(List<String> ids){
        return Flux.fromIterable(ids)
                .flatMap(id -> repository.findById(id)
                        .map(Task::done)
                        .flatMap(repository::save)
                        .doOnNext(it -> LOGGER.info("Done task. ID: {}", it.getId())))
                .collectList();
    }

    public Flux<Task> refreshCreated(){
        return repository.findAll()
                .filter(Task::createdIsEmpty)
                .map(Task::createdNow)
                .flatMap(repository::save);
    }
}

