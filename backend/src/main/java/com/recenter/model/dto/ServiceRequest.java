package com.recenter.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ServiceRequest {
    @NotBlank
    private String title;

    private String description;

    @Positive
    private BigDecimal price;

    private Long categoryId;
}