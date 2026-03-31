package com.recenter.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.recenter.model.enums.NewsStatus;

@Data
public class NewsRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;

    private NewsStatus status;
}
