package com.recenter.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.recenter.model.enums.BookingStatus;
import java.time.LocalDateTime;

@Data
public class BookingRequest {

    private Long serviceId;

    @Future(message = "Дата заезда должна быть в будущем")
    private LocalDateTime startDate;

    @Future(message = "Дата выезда должна быть в будущем")
    private LocalDateTime endDate;

    private Integer peopleCount;

    private BookingStatus status;
}
