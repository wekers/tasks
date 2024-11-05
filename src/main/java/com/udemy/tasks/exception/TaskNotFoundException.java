package com.udemy.tasks.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException() {
        super("Task not found");
    }
}
