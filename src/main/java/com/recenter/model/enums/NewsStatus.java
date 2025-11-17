package com.recenter.model.enums;

public enum NewsStatus {
    DRAFT("draft"),
    PUBLISHED("published");

    private final String db;
    NewsStatus(String db) { this.db = db; }
    public String toDb() { return db; }
    public static NewsStatus fromDb(String v) {
        if (v == null) return null;
        switch(v.toLowerCase()) {
            case "draft": return DRAFT;
            case "published": return PUBLISHED;
            default: throw new IllegalArgumentException("Unknown NewsStatus: " + v);
        }
    }
}
