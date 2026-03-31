package com.recenter.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.recenter.model.enums.ActivityType;

@Data
public class ActivityRequest {
    @NotNull
    private Long userId;

    @NotNull
    private ActivityType type;

    private String details;
}
