package io.github.evercraftmc.core.impl;

import org.jetbrains.annotations.NotNull;

public enum ECEnvironment {
    BUNGEE(ECEnvironmentType.PROXY),
    SPIGOT(ECEnvironmentType.BACKEND);

    private final @NotNull ECEnvironmentType type;

    ECEnvironment(@NotNull ECEnvironmentType type) {
        this.type = type;
    }

    public @NotNull ECEnvironmentType getType() {
        return this.type;
    }
}