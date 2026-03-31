package com.recenter.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotNull
    private Long bookingId;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String paymentMethod;
}
