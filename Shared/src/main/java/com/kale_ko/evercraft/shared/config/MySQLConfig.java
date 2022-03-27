package com.kale_ko.evercraft.shared.config;

import java.util.List;
import com.google.gson.GsonBuilder;
import com.kale_ko.evercraft.shared.mysql.MySQL;
import java.util.ArrayList;
import java.util.Arrays;

public class MySQLConfig extends AbstractConfig {
    private MySQL mysql;
    private String tableName;

    public MySQLConfig(String host, Integer port, String database, String tableName, String username, String password) {
        this.mysql = new MySQL(host, port, database, username, password);
        this.tableName = tableName;

        this.mysql.createTable(this.tableName, "(keyid LONGTEXT NOT NULL, keyvalue LONGTEXT NOT NULL)");
    }

    @Override
    public Boolean exists(String key) {
        return mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'") == null;
    }

    public List<String> getKeys(String path, Boolean deep) {
        List<String> keys = new ArrayList<>();

        for (String key : mysql.selectAll(this.tableName, new String[] { "keyid" }).split("\n")) {
            if (deep && key.startsWith(path + ".") || (!deep && key.startsWith(path + ".") && key.split("\\.").length - 1 == path.split("\\.").length + 1)) {
                keys.add(String.join(".", Arrays.asList(key.split("\\.")).subList(0, path.split("\\.").length + 1)));
            }
        }

        return keys;
    }

    private String getRaw(String key) {
        return mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'");
    }

    public <T> T getSerializable(String key, Class<T> clazz) {
        return new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create().fromJson(getRaw(key), clazz);
    }

    public <T> List<T> getSerializableList(String key, Class<T> clazz) {
        return Arrays.asList(new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create().fromJson(getRaw(key), clazz));
    }

    public void set(String key, Object value) {
        String json = null;
        if (value != null) {
            json = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create().toJson(value);
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

    public void reload() {
    }

    public void save() {
    }

    public void close() {
        mysql.close();
    }
}