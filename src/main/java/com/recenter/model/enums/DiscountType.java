package com.recenter.model.enums;

public enum DiscountType {
    PERCENT("percent"),
    FIXED("fixed");

    private final String db;
    DiscountType(String db) { this.db = db; }
    public String toDb() { return db; }
    public static DiscountType fromDb(String v) {
        if (v == null) return null;
        return switch (v.toLowerCase()) {
            case "percent" -> PERCENT;
            case "fixed" -> FIXED;
            default -> throw new IllegalArgumentException("Unknown DiscountType: " + v);
        };
    }
}
