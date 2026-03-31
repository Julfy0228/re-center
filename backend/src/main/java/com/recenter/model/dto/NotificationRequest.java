package com.recenter.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.recenter.model.enums.NotificationType;

@Data
public class NotificationRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String message;

    private NotificationType type;
}
