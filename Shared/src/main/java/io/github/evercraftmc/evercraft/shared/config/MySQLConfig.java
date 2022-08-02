package io.github.evercraftmc.evercraft.shared.config;

import java.time.Instant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.evercraftmc.evercraft.shared.mysql.MySQL;

public class MySQLConfig<T> extends Config<T> {
    private static Gson gson;

    static {
        MySQLConfig.gson = new GsonBuilder().disableHtmlEscaping().serializeSpecialFloatingPointValues().create();
    }

    private MySQL mysql;
    private String tableName;
    private String keyName;

    private T config;
    private Long configExpiresAt = 0l;

    public MySQLConfig(Class<T> clazz, String host, Integer port, String database, String tableName, String keyName, String username, String password) {
        super(clazz);

        this.mysql = new MySQL(host, port, database, username, password);
        this.tableName = tableName;
        this.keyName = keyName;

        this.mysql.createTable(this.tableName, "(keyName LONGTEXT NOT NULL, data LONGTEXT NOT NULL)");
    }

    public T getParsed() {
        if (Instant.now().getEpochSecond() > this.configExpiresAt) {
            this.config = gson.fromJson(mysql.selectFirst(this.tableName, "data", "keyName = '" + this.keyName + "'"), clazz);
            this.configExpiresAt = Instant.now().getEpochSecond() + 1;
        }

        return this.config;
    }

    public void reload() {
        mysql.reconnect();
    }

    public void save() {
        String json = gson.toJson(this.config);

        if (json != null) {
            if (mysql.selectFirst(this.tableName, "data", "keyName = '" + this.keyName + "'") == null) {
                mysql.insert(this.tableName, "('" + this.keyName + "', '" + json + "')");
            } else {
                mysql.update(this.tableName, "data = '" + json + "'", "keyName = '" + this.keyName + "'");
            }
        }
    }

    public void close() {
        mysql.close();
    }
}