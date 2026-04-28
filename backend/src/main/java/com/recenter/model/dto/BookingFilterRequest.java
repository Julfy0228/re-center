package com.recenter.model.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingFilterRequest {
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Boolean paid;
}
