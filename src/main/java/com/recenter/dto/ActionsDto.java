package com.recenter.dto;

import java.time.LocalDateTime;

public class ActionsDto {
    private Long id;
    private Long userId;
    private String actionType;
    private LocalDateTime actionTime;
    private String details;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public LocalDateTime getActionTime() { return actionTime; }
    public void setActionTime(LocalDateTime actionTime) { this.actionTime = actionTime; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
