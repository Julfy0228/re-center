package com.recenter.model.enums;

public enum NewsStatus {
    DRAFT("draft"),
    PUBLISHED("published");

    private final String db;
    NewsStatus(String db) { this.db = db; }
    public String toDb() { return db; }
    public static NewsStatus fromDb(String v) {
        if (v == null) return null;
        return switch (v.toLowerCase()) {
            case "draft" -> DRAFT;
            case "published" -> PUBLISHED;
            default -> throw new IllegalArgumentException("Unknown NewsStatus: " + v);
        };
    }
}
