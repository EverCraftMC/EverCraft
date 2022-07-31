package io.github.evercraftmc.evercraft.shared.config;

public abstract class Config<T> {
    protected Class<T> clazz;

    protected Config(Class<T> clazz) {
        this.clazz = clazz;
    }

    public abstract T getParsed();

    public abstract void reload();

    public abstract void save();

    public abstract void close();
}