package com.kale_ko.kalesutilities.shared.mysql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class MySQLConfig {
    private MySQL mysql;
    private String tableName;

    private Map<String, Object> defaults = new HashMap<String, Object>();

    public MySQLConfig(String url, Integer port, String database, String tableName, String username, String password) {
        this.mysql = new MySQL(url, port, database, username, password);
        this.tableName = tableName;

        this.mysql.createTable(this.tableName, "(keyid LONGTEXT NOT NULL, keyvalue LONGTEXT NOT NULL)");
    }

    public List<String> getKeys() {
        return Arrays.asList(mysql.selectAll(this.tableName, new String[] { "key" }).split("\n"));
    }

    public String getString(String key) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'"), String.class);
    }

    // public List<String> getStringList(String key) {

    // }

    public Integer getInt(String key) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'"), Integer.class);
    }

    // public List<Integer> getIntList(String key) {

    // }

    public Float getFloat(String key) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'"), Float.class);
    }

    // public List<Float> getFloatList(String key) {

    // }

    public Double getDouble(String key) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'"), Double.class);
    }

    // public List<Double> getDoubleList(String key) {

    // }

    public Long getLong(String key) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'"), Long.class);
    }

    // public List<Long> getLongList(String key) {

    // }

    public Boolean getBoolean(String key) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'"), Boolean.class);
    }

    // public List<Boolean> getBooleanList(String key) {

    // }

    public <T> T getSerializable(String key, Class<T> clazz) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'"), clazz);
    }

    public void set(String key, Object value) {
        if (value != null && mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'") == null) {
            mysql.insert(this.tableName, "('" + key + "', '" + Serializer.serialize(value) + "')");
        } else {
            if (value != null) {
                mysql.update(this.tableName, "keyvalue", Serializer.serialize(value), "keyid = '" + key + "'");
            } else {
                mysql.delete(this.tableName, "keyid = '" + key + "'");
            }
        }
    }

    public void addDefault(String key, Object value) {
        this.defaults.put(key, value);
    }

    public void copyDefaults() {
        for (Map.Entry<String, Object> entry : this.defaults.entrySet()) {
            this.set(entry.getKey(), entry.getValue());
        }
    }

    public static MySQLConfig load(String url, Integer port, String database, String tableName, String username, String password) {
        return new MySQLConfig(url, port, database, tableName, username, password);
    }
}