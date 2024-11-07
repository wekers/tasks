package com.udemy.tasks.controller;


import com.udemy.tasks.controller.converter.TaskDTOConverter;
import com.udemy.tasks.controller.converter.TaskInsertDTOConverter;
import com.udemy.tasks.controller.converter.TaskUpdateDTOConverter;
import com.udemy.tasks.controller.dto.TaskDTO;
import com.udemy.tasks.controller.dto.TaskInsertDTO;
import com.udemy.tasks.controller.dto.TaskUpdateDTO;
import com.udemy.tasks.model.TaskState;
import com.udemy.tasks.service.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private final TaskService service;
    private final TaskDTOConverter converter;
    private final TaskInsertDTOConverter insertDTOConverter;
    private final TaskUpdateDTOConverter updateDTOConverter;

    public TaskController(TaskService service, TaskDTOConverter converter, TaskInsertDTOConverter insertDTOConverter, TaskUpdateDTOConverter updateDTOConverter) {
        this.service = service;
        this.converter = converter;
        this.insertDTOConverter = insertDTOConverter;
        this.updateDTOConverter = updateDTOConverter;
    }

    @GetMapping
    public Mono<Page<TaskDTO>> getTasks(@RequestParam(required = false) String id,
                                   @RequestParam(required = false) String title,
                                   @RequestParam(required = false) String description,
                                   @RequestParam(required = false, defaultValue = "0") int priority,
                                   @RequestParam(required = false) TaskState taskState,
                                   @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return service.findPaginated(converter.converter(id, title, description, priority, taskState), pageNumber, pageSize)
                .map(it -> it.map(converter::convert));
    }

    @PutMapping
    public Mono<TaskDTO> updateTask(@RequestBody @Valid TaskUpdateDTO taskUpdateDTO) {
        return service.update(updateDTOConverter.convert(taskUpdateDTO))
                .doOnNext(it -> LOGGER.info("Update task with id {}", it.getId()))
                .map(converter::convert);


    }

    @PostMapping
    public Mono<TaskDTO> createTask(@RequestBody @Valid TaskInsertDTO taskInsertDTO){
        return service.insert(insertDTOConverter.convert(taskInsertDTO))
                .doOnNext(task -> LOGGER.info("Saved task with id {}", task.getId()))
                .map(converter::convert);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id){
        return Mono.just(id)
                .doOnNext(it -> LOGGER.info("Deleted task with id {}", id))
                .flatMap(service::deleteById);

    }

    @PostMapping("/start")
    public Mono<TaskDTO> start(@RequestParam String id, @RequestParam String zipcode){
        return service.start(id, zipcode)
                .map(converter::convert);
    }
}
