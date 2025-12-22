package com.recenter.model.enums;

public enum Role {
    CLIENT("client"),
    MANAGER("manager"),
    ADMIN("admin");

    private final String dbValue;

    Role(String dbValue) { this.dbValue = dbValue; }

    public String toDb() { return dbValue; }

    public static Role fromDb(String db) {
        if (db == null) return null;
        return switch (db.toLowerCase()) {
            case "client" -> CLIENT;
            case "manager" -> MANAGER;
            case "admin" -> ADMIN;
            default -> throw new IllegalArgumentException("Unknown role: " + db);
        };
    }
}
