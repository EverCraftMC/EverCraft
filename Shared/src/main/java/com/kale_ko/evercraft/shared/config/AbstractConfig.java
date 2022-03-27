package com.kale_ko.evercraft.shared.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractConfig implements Config {
    private Map<String, Object> defaults = new HashMap<String, Object>();

    public Object getObject(String key) {
        return getSerializable(key, Object.class);
    }

    public String getString(String key) {
        return getSerializable(key, String.class);
    }

    public List<String> getStringList(String key) {
        return getSerializableList(key, String.class);
    }

    public Integer getInt(String key) {
        return getSerializable(key, Integer.class);
    }

    public List<Integer> getIntList(String key) {
        return getSerializableList(key, Integer.class);
    }

    public Float getFloat(String key) {
        return getSerializable(key, Float.class);
    }

    public List<Float> getFloatList(String key) {
        return getSerializableList(key, Float.class);
    }

    public Double getDouble(String key) {
        return getSerializable(key, Double.class);
    }

    public List<Double> getDoubleList(String key) {
        return getSerializableList(key, Double.class);
    }

    public Long getLong(String key) {
        return getSerializable(key, Long.class);
    }

    public List<Long> getLongList(String key) {
        return getSerializableList(key, Long.class);
    }

    public Boolean getBoolean(String key) {
        return getSerializable(key, Boolean.class);
    }

    public List<Boolean> getBooleanList(String key) {
        return getSerializableList(key, Boolean.class);
    }

    public void addDefault(String key, Object value) {
        this.defaults.put(key, value);
    }

    public void copyDefaults() {
        for (Map.Entry<String, Object> entry : this.defaults.entrySet()) {
            this.set(entry.getKey(), entry.getValue());
        }
    }
}