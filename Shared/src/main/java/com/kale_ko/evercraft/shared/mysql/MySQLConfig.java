package com.kale_ko.evercraft.shared.mysql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.kale_ko.evercraft.shared.PluginConfig;
import java.util.Arrays;

public class MySQLConfig implements PluginConfig {
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

    private String getRaw(String key) {
        return mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'");
    }

    public Object getObject(String key) {
        return new Gson().fromJson(getRaw(key), Object.class);
    }

    public String getString(String key) {
        return new Gson().fromJson(getRaw(key), String.class);
    }

    public List<String> getStringList(String key) {
        return Arrays.asList(new Gson().fromJson(getRaw(key), String[].class));
    }

    public Integer getInt(String key) {
        return new Gson().fromJson(getRaw(key), Integer.class);
    }

    public List<Integer> getIntList(String key) {
        return Arrays.asList(new Gson().fromJson(getRaw(key), Integer[].class));
    }

    public Float getFloat(String key) {
        return new Gson().fromJson(getRaw(key), Float.class);
    }

    public List<Float> getFloatList(String key) {
        return Arrays.asList(new Gson().fromJson(getRaw(key), Float[].class));
    }

    public Double getDouble(String key) {
        return new Gson().fromJson(getRaw(key), Double.class);
    }

    public List<Double> getDoubleList(String key) {
        return Arrays.asList(new Gson().fromJson(getRaw(key), Double[].class));
    }

    public Long getLong(String key) {
        return new Gson().fromJson(getRaw(key), Long.class);
    }

    public List<Long> getLongList(String key) {
        return Arrays.asList(new Gson().fromJson(getRaw(key), Long[].class));
    }

    public Boolean getBoolean(String key) {
        return new Gson().fromJson(getRaw(key), Boolean.class);
    }

    public List<Boolean> getBooleanList(String key) {
        return Arrays.asList(new Gson().fromJson(getRaw(key), Boolean[].class));
    }

    public <T> T getSerializable(String key, Class<T> clazz) {
        return new Gson().fromJson(getRaw(key), clazz);
    }

    public <T> List<T> getListSerializable(String key, Class<T> clazz) {
        return Arrays.asList(new Gson().fromJson(getRaw(key), clazz));
    }

    public void set(String key, Object value) {
        String json = null;
        if (value != null) {
            json = new Gson().toJson(value);
        }

        if (json != null && mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'") == null) {
            mysql.insert(this.tableName, "('" + key + "', '" + json + "')");
        } else {
            if (json != null) {
                mysql.update(this.tableName, "keyvalue = '" + json + "'", "keyid = '" + key + "'");
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

    public void reload() { }

    public void save() { }

    public void close() {
        mysql.close();
    }

    public static MySQLConfig load(String url, Integer port, String database, String tableName, String username, String password) {
        return new MySQLConfig(url, port, database, tableName, username, password);
    }
}