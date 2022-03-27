package com.kale_ko.evercraft.shared.config;

import java.util.Collection;
import java.util.List;
import com.kale_ko.evercraft.shared.util.Closable;

public interface Config extends Closable {
    public Collection<String> getKeys(String path, Boolean deep);

    public default Collection<String> getKeys(String path) {
        return this.getKeys(path, false);
    }

    public default Collection<String> getKeys(Boolean deep) {
        return this.getKeys("", deep);
    }

    public default Collection<String> getKeys() {
        return this.getKeys("", false);
    }

    public Boolean exists(String key);

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

    public <T> T getSerializable(String key, Class<T> clazz);

    public <T> List<T> getSerializableList(String key, Class<T> clazz);

    public void set(String key, Object value);

    public void addDefault(String key, Object value);

    public void copyDefaults();

    public void reload();

    public void save();

    public void close();
}