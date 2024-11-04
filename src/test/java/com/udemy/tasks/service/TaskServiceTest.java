package com.udemy.tasks.service;

import com.udemy.tasks.model.Task;
import com.udemy.tasks.repository.TaskCustomRepository;
import com.udemy.tasks.repository.TaskRepository;
import com.udemy.tasks.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceTest {

    @InjectMocks
    private TaskService service;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskCustomRepository taskCustomRepository;

    @Test
    void service_mustReturnTask_whenInsertSuccessfully() {

        Task task = TestUtils.buildValidTask();

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        StepVerifier.create(service.insert(task))
                .then(() -> verify(taskRepository, times(1)).save(any()))
                .expectNext(task)
                .verifyComplete();
    }

    @Test
    void service_mustReturnVoid_whenDeleteSuccessfully() {
        StepVerifier.create(service.deleteById("someId"))
                .then(() -> verify(taskRepository, times(1)).deleteById(any()))
                .verifyComplete();
    }

    @Test
    void service_mustReturnTaskPage_whenFindPaginated(){

        Task task = TestUtils.buildValidTask();

        when(taskCustomRepository.findPaginated(any(), anyInt(), anyInt())).thenReturn(Page.empty());

        Page<Task> result = service.findPaginated(task, 0, 10);

        Assertions.assertNotNull(result);

        verify(taskCustomRepository, times(1)).findPaginated(any(), anyInt(), anyInt());
    }

}