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
        switch(db.toLowerCase()) {
            case "client": return CLIENT;
            case "manager": return MANAGER;
            case "admin": return ADMIN;
            default: throw new IllegalArgumentException("Unknown role: " + db);
        }
    }
}
