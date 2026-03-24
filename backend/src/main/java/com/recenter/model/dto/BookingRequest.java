package com.recenter.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingRequest {
    @NotNull
    private Long serviceId;

    @NotNull
    @Future(message = "Дата заезда должна быть в будущем")
    private LocalDateTime startDate;

    @NotNull
    @Future(message = "Дата выезда должна быть в будущем")
    private LocalDateTime endDate;
}