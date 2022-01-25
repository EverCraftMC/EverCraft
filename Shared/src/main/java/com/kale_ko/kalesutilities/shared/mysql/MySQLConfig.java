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
        return mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'");
    }

    // public List<String> getStringList(String key) {

    // }

    public Integer getInt(String key) {
        return Integer.parseInt(mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'"));
    }

    // public List<Integer> getIntList(String key) {

    // }

    public Float getFloat(String key) {
        return Float.parseFloat(mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'"));
    }

    // public List<Float> getFloatList(String key) {

    // }

    public Double getDouble(String key) {
        return Double.parseDouble(mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'"));
    }

    // public List<Double> getDoubleList(String key) {

    // }

    public Long getLong(String key) {
        return Long.parseLong(mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'"));
    }

    // public List<Long> getLongList(String key) {

    // }

    public Boolean getBoolean(String key) {
        return Boolean.parseBoolean(mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'"));
    }

    // public List<Boolean> getBooleanList(String key) {

    // }

    // public <T> T getSerializable(String key, Class<T> clazz) {

    // }

    public void set(String key, Object value) {
        if (value != null && mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'") == null) {
            mysql.insert(this.tableName, "('" + key + "', '" + value.toString() + "')");
        } else {
            if (value != null) {
                mysql.update(this.tableName, "keyvalue = '" + value.toString() + "'", "keyid = '" + key + "'");
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

    public void close() {
        mysql.close();
    }

    public static MySQLConfig load(String url, Integer port, String database, String tableName, String username, String password) {
        return new MySQLConfig(url, port, database, tableName, username, password);
    }
}