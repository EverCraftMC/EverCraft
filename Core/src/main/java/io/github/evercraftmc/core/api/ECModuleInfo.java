package io.github.evercraftmc.core.api;

import java.util.Collections;
import java.util.List;

public class ECModuleInfo {
    protected String name;
    protected String version;

    protected String entry;

    protected List<String> depends;

    public ECModuleInfo() {
        this(null, null, null, null);
    }

    public ECModuleInfo(String name, String version, String entry, List<String> depends) {
        this.name = name;
        this.version = version;

        this.entry = entry;

        this.depends = depends;
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

    public List<String> getDepends() {
        return Collections.unmodifiableList(this.depends);
    }
}