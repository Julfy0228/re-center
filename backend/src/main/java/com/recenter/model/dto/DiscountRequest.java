package com.recenter.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.recenter.model.enums.DiscountType;

@Data
public class DiscountRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @NotNull
    private DiscountType type;

    @NotNull
    private BigDecimal amount;
}
