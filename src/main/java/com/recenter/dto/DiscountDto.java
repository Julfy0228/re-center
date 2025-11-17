package com.recenter.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DiscountDto {
    private Long id;
    private String title;
    private String type; // PERCENT, FIXED
    private BigDecimal value;
    private LocalDate validUntil;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }

    public LocalDate getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }
}
