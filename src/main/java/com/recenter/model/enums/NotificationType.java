package com.recenter.model.enums;

public enum NotificationType {
    BOOKING("booking"),
    PAYMENT("payment"),
    PROMOTION("promotion"),
    SYSTEM("system");

    private final String db;
    NotificationType(String db) { this.db = db; }
    public String toDb() { return db; }
    public static NotificationType fromDb(String v) {
        if (v == null) return null;
        return switch (v.toLowerCase()) {
            case "booking" -> BOOKING;
            case "payment" -> PAYMENT;
            case "promotion" -> PROMOTION;
            case "system" -> SYSTEM;
            default -> throw new IllegalArgumentException("Unknown NotificationType: " + v);
        };
    }
}
