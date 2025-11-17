package com.recenter.model;

import java.time.LocalDateTime;

public class Activity {
    private int id;
    private int userId;
    private String type;
    private String details;
    private LocalDateTime date;

    public Activity() {}

    public Activity(int id, int userId, String type, String details, LocalDateTime date) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.details = details;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
