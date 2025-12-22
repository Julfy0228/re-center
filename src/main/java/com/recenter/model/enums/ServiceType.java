package com.recenter.model.enums;

public enum ServiceType {
    DAILY,
    HOURLY,
    ONCE;

    public static ServiceType fromDb(String db) {
        if (db == null) return null;
        return switch (db.toLowerCase()) {
            case "daily" -> DAILY;
            case "hourly" -> HOURLY;
            case "once" -> ONCE;
            default -> throw new IllegalArgumentException("Unknown serviceType: " + db);
        };
    }

    public String toDb() {
        return switch (this) {
            case DAILY -> "daily";
            case HOURLY -> "hourly";
            case ONCE -> "once";
            default -> this.name().toLowerCase();
        };
    }
}
