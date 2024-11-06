package com.udemy.tasks.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskUpdateDTO {

    @NotBlank(message = "Valor inválido para campo ID")
    private String id;

    @NotBlank(message = "Valor não pode ser em branco para o campo Title")
    @Size(min = 3, max = 20, message = "O titulo deve ter entre 3 a 20 caracteres")
    private String title;

    @NotBlank(message = "Valor não pode ser em branco para o campo Description")
    @Size(min = 10, max = 50, message = "O titulo deve ter entre 10 a 50 caracteres")
    private String description;

    @Min(value = 1, message = "A prioridade deve ser maior que zero")
    private int priority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

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
