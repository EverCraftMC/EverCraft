package io.github.evercraftmc.core.impl;

public enum ECEnvironment {
    BUNGEE(ECEnvironmentType.PROXY),
    SPIGOT(ECEnvironmentType.BACKEND);

    private final ECEnvironmentType type;

    ECEnvironment(ECEnvironmentType type) {
        this.type = type;
    }

    public ECEnvironmentType getType() {
        return this.type;
    }
}