package com.kale_ko.evercraft.shared.config;

import java.util.ArrayList;
import java.util.List;
import com.kale_ko.evercraft.shared.util.KeyValuePair;

public abstract class AbstractConfig implements Config {
    private List<KeyValuePair<String, Object>> defaults = new ArrayList<KeyValuePair<String, Object>>();

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
        Boolean value = getSerializable(key, Boolean.class);

        if (value != null) {
            return value;
        } else {
            return false;
        }
    }

    public List<Boolean> getBooleanList(String key) {
        return getSerializableList(key, Boolean.class);
    }

    public void addDefault(String key, Object value) {
        this.defaults.add(new KeyValuePair<String, Object>(key, value));
    }

    public void copyDefaults() {
        for (KeyValuePair<String, Object> entry : this.defaults) {
            if (!this.exists(entry.key())) {
                this.set(entry.key(), entry.value());
            }
        }

        this.save();
    }
}