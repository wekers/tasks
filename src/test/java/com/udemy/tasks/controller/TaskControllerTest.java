package com.udemy.tasks.controller;

import com.udemy.tasks.controller.converter.TaskDTOConverter;
import com.udemy.tasks.controller.converter.TaskInsertDTOConverter;
import com.udemy.tasks.controller.dto.TaskDTO;
import com.udemy.tasks.controller.dto.TaskInsertDTO;
import com.udemy.tasks.model.Task;
import com.udemy.tasks.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@SpringBootTest
class TaskControllerTest {

    @InjectMocks
    private TaskController controller;

    @Mock
    private TaskService service;

    @Mock
    private TaskDTOConverter converter;

    @Mock
    private TaskInsertDTOConverter insertDTOConverter;

    @Test
    void controller_mustReturnOk_whenSaveSuccessfully() {


        when(converter.convert(any(Task.class))).thenReturn(new TaskDTO());
        when(service.insert(any())).thenReturn(Mono.just(new Task()));

        WebTestClient client = WebTestClient.bindToController(controller).build();

        client.post().uri("/tasks")
                .bodyValue(new TaskInsertDTO())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskDTO.class);
    }

   /* @Test
    void controller_mustReturnOk_whenGetPaginatedSuccessfully() {

        when(service.findPaginated(any(), anyInt(), anyInt())).thenReturn(Mono.just(Page.empty()));

        WebTestClient client = WebTestClient.bindToController(controller).build();

        client.get()
                .uri("/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskDTO.class);

    }*/

    @Test
    void controller_mustReturnNoContent_whenDeleteSuccessfully() {

        String taskId = "any-id";

        when(service.deleteById(any())).thenReturn(Mono.empty());

        WebTestClient client = WebTestClient.bindToController(controller).build();

        client.delete()
                .uri("/tasks/" + taskId)
                .exchange()
                .expectStatus().isNoContent();

    }



}