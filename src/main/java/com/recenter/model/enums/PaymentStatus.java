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
        switch(v.toLowerCase()) {
            case "pending": return PENDING;
            case "paid": return PAID;
            case "cancelled": return CANCELLED;
            default: throw new IllegalArgumentException("Unknown PaymentStatus: " + v);
        }
    }
}
