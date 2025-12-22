package com.recenter.model.enums;

public enum PaymentStatus {
    PENDING("pending"),
    PAID("paid"),
    CANCELLED("cancelled");

    private final String db;
    PaymentStatus(String db) { this.db = db; }
    public String toDb() { return db; }
    public static PaymentStatus fromDb(String v) {
        if (v == null) return null;
        return switch (v.toLowerCase()) {
            case "pending" -> PENDING;
            case "paid" -> PAID;
            case "cancelled" -> CANCELLED;
            default -> throw new IllegalArgumentException("Unknown PaymentStatus: " + v);
        };
    }
}
