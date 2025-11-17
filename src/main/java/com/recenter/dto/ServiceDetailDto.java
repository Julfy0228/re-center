package com.recenter.dto;

import java.math.BigDecimal;
import java.util.List;

public class ServiceDetailDto {
    private Long id;
    private String title;
    private String description;
    private String serviceType;
    private int minCapacity;
    private int maxCapacity;
    private BigDecimal basePrice;
    private boolean isDynamicPricing;
    private boolean isActive;
    private boolean isDeleted;
    private Integer durationMinutes;
    private List<ServicePhotoDto> photos;
    private List<ServicePriceDto> priceList;
    private List<ReviewDto> reviews;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public int getMinCapacity() { return minCapacity; }
    public void setMinCapacity(int minCapacity) { this.minCapacity = minCapacity; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public boolean isDynamicPricing() { return isDynamicPricing; }
    public void setDynamicPricing(boolean dynamicPricing) { isDynamicPricing = dynamicPricing; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public List<ServicePhotoDto> getPhotos() { return photos; }
    public void setPhotos(List<ServicePhotoDto> photos) { this.photos = photos; }

    public List<ServicePriceDto> getPriceList() { return priceList; }
    public void setPriceList(List<ServicePriceDto> priceList) { this.priceList = priceList; }

    public List<ReviewDto> getReviews() { return reviews; }
    public void setReviews(List<ReviewDto> reviews) { this.reviews = reviews; }
}
