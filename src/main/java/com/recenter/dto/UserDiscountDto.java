package com.recenter.dto;

import java.time.LocalDateTime;

public class UserDiscountDto {
    private Long discountId;
    private Long userId;
    private boolean isUsed;
    private LocalDateTime assignedAt;
    private DiscountDto discount;

    public Long getDiscountId() { return discountId; }
    public void setDiscountId(Long discountId) { this.discountId = discountId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { isUsed = used; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public DiscountDto getDiscount() { return discount; }
    public void setDiscount(DiscountDto discount) { this.discount = discount; }
}
