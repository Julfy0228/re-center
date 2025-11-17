package com.recenter.model;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private int serviceId;
    private int rating; // 1..5
    private int userId;
    private String comment;
    private LocalDateTime date;

    public Review() {}

    public Review(int id, int serviceId, int rating, int userId, String comment, LocalDateTime date) {
        this.id = id;
        this.serviceId = serviceId;
        this.rating = rating;
        this.userId = userId;
        this.comment = comment;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
