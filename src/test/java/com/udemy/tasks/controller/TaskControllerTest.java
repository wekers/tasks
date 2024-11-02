package com.udemy.tasks.controller;

import com.udemy.tasks.controller.converter.TaskDTOConverter;
import com.udemy.tasks.controller.dto.TaskDTO;
import com.udemy.tasks.model.Task;
import com.udemy.tasks.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
class TaskControllerTest {

    @InjectMocks
    private TaskController controller;

    @Mock
    private TaskService service;

    @Mock
    private TaskDTOConverter converter;

    @Test
    void controller_mustReturnOk_whenSaveSuccessfully() {


        when(converter.convert(any(Task.class))).thenReturn(new TaskDTO());
        when(service.insert(any())).thenReturn(Mono.just(new Task()));

        WebTestClient client = WebTestClient.bindToController(controller).build();

        client.post().uri("/tasks")
                .bodyValue(new TaskDTO())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskDTO.class);
    }

}