package com.recenter.model;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private int userId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int serviceId;
    private int peopleCount;
    private double initialPrice;
    private double totalPrice;
    private LocalDateTime bookingDate;
    private LocalDateTime createdAt;

    public Booking() {}

    public Booking(int id, int userId, LocalDateTime startDate, LocalDateTime endDate, int serviceId, int peopleCount, double initialPrice, double totalPrice, LocalDateTime bookingDate, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.serviceId = serviceId;
        this.peopleCount = peopleCount;
        this.initialPrice = initialPrice;
        this.totalPrice = totalPrice;
        this.bookingDate = bookingDate;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int getPeopleCount() { return peopleCount; }
    public void setPeopleCount(int peopleCount) { this.peopleCount = peopleCount; }

    public double getInitialPrice() { return initialPrice; }
    public void setInitialPrice(double initialPrice) { this.initialPrice = initialPrice; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
