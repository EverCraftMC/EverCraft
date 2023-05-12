package io.github.evercraftmc.core.api;

public class ECModuleInfo {
    protected String name;
    protected String version;

    protected String entry;

    public ECModuleInfo(String name, String version, String entry) {
        this.name = name;
        this.version = version;

        this.entry = entry;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String getEntry() {
        return this.entry;
    }
}