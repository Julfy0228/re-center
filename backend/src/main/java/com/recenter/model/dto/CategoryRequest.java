package com.recenter.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "Название категории не может быть пустым")
    private String name;
    private String description;
}