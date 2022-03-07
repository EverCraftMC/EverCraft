package com.kale_ko.evercraft.shared;

import java.util.Collection;
import java.util.List;

public interface PluginConfig {
    public Collection<String> getKeys();

    public Object getObject(String key);

    public String getString(String key);

    public List<String> getStringList(String key);

    public Integer getInt(String key);

    public List<Integer> getIntList(String key);

    public Float getFloat(String key);

    public List<Float> getFloatList(String key);

    public Double getDouble(String key);

    public List<Double> getDoubleList(String key);

    public Long getLong(String key);

    public List<Long> getLongList(String key);

    public Boolean getBoolean(String key);

    public List<Boolean> getBooleanList(String key);

    // public <T> T getSerializable(String key, T clazz);

    public void set(String key, Object value);

    public void addDefault(String key, Object value);

    public void copyDefaults();

    public void reload();

    public void save();
}