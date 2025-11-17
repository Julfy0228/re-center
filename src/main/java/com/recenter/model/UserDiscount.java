package com.recenter.model;

import java.time.LocalDateTime;

public class UserDiscount {
    private int id;
    private int discountId;
    private int userId;
    private boolean isUsed;
    private LocalDateTime assignedAt;

    public UserDiscount() {}

    public UserDiscount(int id, int discountId, int userId, boolean isUsed, LocalDateTime assignedAt) {
        this.id = id;
        this.discountId = discountId;
        this.userId = userId;
        this.isUsed = isUsed;
        this.assignedAt = assignedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDiscountId() { return discountId; }
    public void setDiscountId(int discountId) { this.discountId = discountId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { isUsed = used; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
}
