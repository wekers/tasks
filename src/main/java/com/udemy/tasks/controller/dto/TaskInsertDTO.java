package com.udemy.tasks.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskInsertDTO {

    private String title;
    private String description;
    private int priority;

    public String getTitle() {
        return title;
    }

    @NotBlank(message = "Valor não pode ser em branco para o campo Title")
    @Size(min = 3, max = 20, message = "O titulo deve ter entre 3 a 20 caracteres")
    public void setTitle(String title) {
        this.title = title;
    }

    @NotBlank(message = "Valor não pode ser em branco para o campo Description")
    @Size(min = 10, max = 50, message = "O titulo deve ter entre 10 a 50 caracteres")
    public String getDescription() {
        return description;
    }

    @Min(value = 1, message = "A prioridade deve ser maior que zero")
    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
