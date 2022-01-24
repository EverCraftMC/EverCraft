package com.kale_ko.kalesutilities.shared.mysql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLConfig {
    private String tableName;
    private MySQL mysql;

    private Map<String, Object> defaults = new HashMap<String, Object>();

    public MySQLConfig(String url, Integer port, String database, String tableName, String username, String password) {
        this.mysql = new MySQL(url, port, database, username, password);
    }

    public Collection<String> getKeys() {

    }

    public Object getObject(String key) {

    }

    public String getString(String key) {

    }

    public List<String> getStringList(String key) {

    }

    public Integer getInt(String key) {

    }

    public List<Integer> getIntList(String key) {

    }

    public Float getFloat(String key) {

    }

    public List<Float> getFloatList(String key) {

    }

    public Double getDouble(String key) {

    }

    public List<Double> getDoubleList(String key) {

    }

    public Long getLong(String key) {

    }

    public List<Long> getLongList(String key) {

    }

    public Boolean getBoolean(String key) {

    }

    public List<Boolean> getBooleanList(String key) {

    }

    public <T> T getSerializable(String key, T clazz) {

    }

    public void set(String key, Object value) {

    }

    public void addDefault(String key, Object value) {
        this.defaults.put(key, value);
    }

    public void copyDefaults() {
        for (Map.Entry<String, Object> entry : this.defaults.entrySet()) {

        }
    }

    public static MySQLConfig load(String url, Integer port, String database, String tableName, String username, String password) {
        return new MySQLConfig(url, port, database, tableName, username, password);
    }
}