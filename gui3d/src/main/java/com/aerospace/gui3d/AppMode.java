package com.aerospace.gui3d;

public enum AppMode {
    DEMO("Modo demonstração"),
    DATABASE("Modo PostgreSQL");

    private final String displayName;

    AppMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
