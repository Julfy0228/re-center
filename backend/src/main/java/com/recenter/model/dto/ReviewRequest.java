package com.recenter.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.recenter.model.enums.ReviewStatus;

@Data
public class ReviewRequest {
    @NotNull
    private Long bookingId;

    @NotBlank
    private String content;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    private ReviewStatus status;
}
