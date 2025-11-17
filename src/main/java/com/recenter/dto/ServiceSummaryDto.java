package com.recenter.dto;

import java.math.BigDecimal;

public class ServiceSummaryDto {
    private Long id;
    private String title;
    private String description;
    private String serviceType;
    private BigDecimal basePrice;
    private boolean isDynamicPricing;
    private boolean isActive;
    private boolean isDeleted;
    private ServiceCategoryDto category;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public boolean isDynamicPricing() { return isDynamicPricing; }
    public void setDynamicPricing(boolean dynamicPricing) { isDynamicPricing = dynamicPricing; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }

    public ServiceCategoryDto getCategory() { return category; }
    public void setCategory(ServiceCategoryDto category) { this.category = category; }
}
