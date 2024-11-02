package com.udemy.tasks.utils;

import com.udemy.tasks.controller.dto.TaskDTO;
import com.udemy.tasks.model.Task;
import com.udemy.tasks.model.TaskState;

public class TestUtils {

    public static Task buildValidTask() {
        return Task.builder()
                .withId("123")
                .withTitle("title")
                .withDescription("Description")
                .withPriority(1)
                .withState(TaskState.INSERT)
                .build();
    }

    public static TaskDTO buildValidTaskDTO() {
        TaskDTO dto = new TaskDTO();
        dto.setId("123");
        dto.setTitle("title");
        dto.setDescription("Description");
        dto.setPriority(1);
        dto.setState(TaskState.INSERT);
        return dto;
    }
}
