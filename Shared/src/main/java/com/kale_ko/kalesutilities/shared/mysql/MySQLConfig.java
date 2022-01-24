package com.kale_ko.kalesutilities.shared.mysql;

import java.util.Collection;
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

        this.mysql.createTable(this.tableName, "(key TEXT CHARACTER SET utf8 BINARY, value TEXT CHARACTER SET utf8 BINARY)");
    }

    public List<String> getKeys() {
        return Arrays.asList(mysql.selectAll(this.tableName, new String[] { "key" }).split("\n"));
    }

    public Object getObject(String key) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, new String[] { "value" }, "key = " + key), Object.class);
    }

    public String getString(String key) {
        return mysql.selectFirst(this.tableName, new String[] { "value" }, "key = " + key);
    }

    // public List<String> getStringList(String key) {

    // }

    public Integer getInt(String key) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, new String[] { "value" }, "key = " + key), Integer.class);
    }

    // public List<Integer> getIntList(String key) {

    // }

    public Float getFloat(String key) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, new String[] { "value" }, "key = " + key), Float.class);
    }

    // public List<Float> getFloatList(String key) {

    // }

    public Double getDouble(String key) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, new String[] { "value" }, "key = " + key), Double.class);
    }

    // public List<Double> getDoubleList(String key) {

    // }

    public Long getLong(String key) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, new String[] { "value" }, "key = " + key), Long.class);
    }

    // public List<Long> getLongList(String key) {

    // }

    public Boolean getBoolean(String key) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, new String[] { "value" }, "key = " + key), Boolean.class);
    }

    // public List<Boolean> getBooleanList(String key) {

    // }

    public <T> T getSerializable(String key, Class<T> clazz) {
        return Serializer.deserialize(mysql.selectFirst(this.tableName, new String[] { "value" }, "key = " + key), clazz);
    }

    public void set(String key, Object value) {
        if (value != null && mysql.selectFirst(this.tableName, new String[] { "value" }, "key = " + key) == null) {
            mysql.insert(this.tableName, "('" + key + "', '" + Serializer.serialize(value) + "')");
        } else {
            if (value != null) {
                mysql.update(this.tableName, "value", Serializer.serialize(value), "key = " + key);
            } else {
                mysql.delete(this.tableName, "key = " + key);
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