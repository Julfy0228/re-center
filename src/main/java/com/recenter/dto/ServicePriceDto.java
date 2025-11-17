package com.recenter.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ServicePriceDto {
    private Long id;
    private Long serviceId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal priceValue;
    private String priceDescription;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BigDecimal getPriceValue() { return priceValue; }
    public void setPriceValue(BigDecimal priceValue) { this.priceValue = priceValue; }

    public String getPriceDescription() { return priceDescription; }
    public void setPriceDescription(String priceDescription) { this.priceDescription = priceDescription; }
}
