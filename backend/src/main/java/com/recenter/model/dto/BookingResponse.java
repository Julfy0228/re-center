package com.recenter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long id;
    
    // Данные об услуге
    private Long serviceId;
    private String serviceTitle; // Берем из Service.title [cite: 134]
    
    // Данные о пользователе
    private Long userId;
    private String userEmail; // Берем из User.email [cite: 140]
    
    // Данные бронирования
    private LocalDateTime startDate; // Из Booking.startDate [cite: 87]
    private LocalDateTime endDate;   // Твое новое поле
    private Integer peopleCount;     // Из Booking.peopleCount [cite: 87]
    private BigDecimal totalPrice;   // Из Booking.initialPrice [cite: 87]
    private String status;           // Из Booking.status [cite: 88]
    private LocalDateTime createdAt; // Из Booking.createdAt [cite: 88]
}