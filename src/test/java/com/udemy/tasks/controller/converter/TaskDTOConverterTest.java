package com.udemy.tasks.controller.converter;

import com.udemy.tasks.controller.dto.TaskDTO;
import com.udemy.tasks.model.Task;
import com.udemy.tasks.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TaskDTOConverterTest {

    @InjectMocks
    private TaskDTOConverter converter;

    @Test
    void converter_mustReturnTaskDTO_whenInputTask() {

        Task task = TestUtils.buildValidTask();

        TaskDTO dto = converter.convert(task);

        assertEquals(dto.getId(), task.getId());
        assertEquals(dto.getTitle(), task.getTitle());
        assertEquals(dto.getDescription(), task.getDescription());
        assertEquals(dto.getPriority(), task.getPriority());
        assertEquals(dto.getState(), task.getState());

    }

    @Test
    void coverter_mustReturnTask_whenInputTaskDTO(){

        TaskDTO dto = TestUtils.buildValidTaskDTO();

        Task task = converter.convert(dto);

        assertEquals(task.getId(), dto.getId());
        assertEquals(task.getTitle(), dto.getTitle());
        assertEquals(task.getDescription(), dto.getDescription());
        assertEquals(task.getPriority(), dto.getPriority());
        assertEquals(task.getState(), dto.getState());


    }


}