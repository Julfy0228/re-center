package com.recenter.model;

import com.recenter.model.enums.ServiceType;
import java.time.LocalDateTime;

public class Service {
    private int id;
    private String title;
    private String description;
    private ServiceType serviceType;
    private String duration; // e.g. '2d 1h 5m' or '8h'
    private double price;
    private Integer minPeople;
    private Integer maxPeople;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime expireDate;
    private Integer categoryId;

    public Service() {}

    public Service(int id, String title, String description, ServiceType serviceType, String duration, double price, Integer minPeople, Integer maxPeople, boolean isActive, LocalDateTime createdAt, LocalDateTime expireDate, Integer categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.serviceType = serviceType;
        this.duration = duration;
        this.price = price;
        this.minPeople = minPeople;
        this.maxPeople = maxPeople;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.expireDate = expireDate;
        this.categoryId = categoryId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Integer getMinPeople() { return minPeople; }
    public void setMinPeople(Integer minPeople) { this.minPeople = minPeople; }

    public Integer getMaxPeople() { return maxPeople; }
    public void setMaxPeople(Integer maxPeople) { this.maxPeople = maxPeople; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getExpireDate() { return expireDate; }
    public void setExpireDate(LocalDateTime expireDate) { this.expireDate = expireDate; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
}
