package com.recenter.model.enums;

public enum ServiceType {
    DAILY,
    HOURLY,
    ONCE;

    public static ServiceType fromDb(String db) {
        if (db == null) return null;
        switch(db.toLowerCase()) {
            case "daily": return DAILY;
            case "hourly": return HOURLY;
            case "once": return ONCE;
            default: throw new IllegalArgumentException("Unknown serviceType: " + db);
        }
    }

    public String toDb() {
        switch(this) {
            case DAILY: return "daily";
            case HOURLY: return "hourly";
            case ONCE: return "once";
            default: return this.name().toLowerCase();
        }
    }
}
