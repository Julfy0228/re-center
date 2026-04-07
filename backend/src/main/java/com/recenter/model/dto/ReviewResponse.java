package com.recenter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long id;
    private Long bookingId;
    private Long serviceId;
    private String serviceTitle;
    private String content;
    private Integer rating;
    private String status;
    private LocalDateTime createdAt;
}
