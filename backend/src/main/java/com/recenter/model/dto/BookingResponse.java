package com.recenter.model.dto;

import com.recenter.model.enums.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long id;
    private Long serviceId;
    private String serviceTitle;
    private Long userId;
    private String userEmail;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer peopleCount;
    private BigDecimal initialPrice;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private boolean paid;
}
