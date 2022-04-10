package io.github.evercraftmc.evercraft.shared.config;

import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.evercraftmc.evercraft.shared.mysql.MySQL;
import java.util.ArrayList;
import java.util.Arrays;

public class MySQLConfig extends AbstractConfig {
    private static Gson gson;

    static {
        MySQLConfig.gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().serializeSpecialFloatingPointValues().create();
    }

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
        if (path.equalsIgnoreCase("")) {
            path = null;
        }

        List<String> keys = new ArrayList<String>();

        for (String rawkey : mysql.selectAll(this.tableName, new String[] { "keyid" }).split("\n")) {
            String key;

            if ((deep && (path == null || rawkey.startsWith(path + ".")))) {
                key = rawkey;
            } else if (!deep && (path == null || rawkey.startsWith(path + "."))) {
                key = String.join(".", Arrays.asList(rawkey.split("\\.")).subList(0, path != null ? path.split("\\.").length + 1 : 1));
            } else {
                continue;
            }

            if (!keys.contains(key)) {
                keys.add(key);
            }
        }

        return keys;
    }

    public String getRaw(String key) {
        return mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'");
    }

    public <T> T getSerializable(String key, Class<T> clazz) {
        return gson.fromJson(getRaw(key), new TypeToken<T>() { }.getType());
    }

    public <T> List<T> getSerializableList(String key, Class<T> clazz) {
        return gson.fromJson(getRaw(key), new TypeToken<List<T>>() { }.getType());
    }

    public void set(String key, Object value) {
        String json = null;
        if (value != null) {
            json = gson.toJson(value);
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

    public void reload() { }

    public void save() { }

    public void close() {
        mysql.close();
    }
}