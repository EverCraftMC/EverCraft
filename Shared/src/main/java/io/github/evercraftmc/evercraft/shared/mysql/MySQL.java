package io.github.evercraftmc.evercraft.shared.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import io.github.evercraftmc.evercraft.shared.util.Closable;

public class MySQL implements Closable {
    private String host;
    private Integer port;
    private String database;

    private String username;
    private String password;

    private Connection connection;

    private List<Statement> currentStatements = new ArrayList<Statement>();

    private Boolean closed = false;

    public MySQL(String host, Integer port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;

        this.username = username;
        this.password = password;

        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void query(String query) {
        try {
            if (!this.connection.isClosed()) {
                Statement statement = this.connection.createStatement();
                this.currentStatements.add(statement);

                statement.executeUpdate(query);

                statement.close();
                this.currentStatements.remove(statement);
            } else {
                this.reconnect();

                this.query(query);
            }
        } catch (SQLException e) {
            this.reconnect();

            this.query(query);
        }
    }

    public ResultSet queryResponse(String query) {
        try {
            if (!this.connection.isClosed()) {
                Statement statement = this.connection.createStatement();
                this.currentStatements.add(statement);

                ResultSet result = statement.executeQuery(query);

                statement.close();
                this.currentStatements.remove(statement);

                return result;
            } else {
                this.reconnect();

                return this.queryResponse(query);
            }
        } catch (SQLException e) {
            this.reconnect();

            return this.queryResponse(query);
        }
    }

    public void createTable(String name, String data) {
        query("CREATE TABLE IF NOT EXISTS " + name + " " + data + ";");
    }

    public String selectAll(String table, String[] fields) {
        return select(table, fields, "1 = 1");
    }

    public String select(String table, String[] fields, String condition) {
        ResultSet result = queryResponse("SELECT * FROM " + table + " " + "WHERE " + condition + ";");

        StringBuilder ret = new StringBuilder();

        try {
            while (result.next()) {
                for (String field : fields) {
                    ret.append(result.getString(field) + "\t");
                }

                ret = new StringBuilder(ret.substring(0, ret.length() - 1) + "\n");
            }

            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret.toString();
    }

    public String selectFirst(String table, String field, String condition) {
        ResultSet result = queryResponse("SELECT * FROM " + table + " " + "WHERE " + condition + ";");

        try {
            while (result.next()) {
                return result.getString(field);
            }

            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void insert(String table, String values) {
        query("INSERT INTO " + table + " VALUES " + values + ";");
    }

    public void insert(String table, String keys, String values) {
        query("INSERT INTO " + table + " " + keys + " VALUES " + values + ";");
    }

    public void update(String table, String set, String condition) {
        query("UPDATE " + table + " SET " + set + " WHERE " + condition + ";");
    }

    public void delete(String table, String condition) {
        query("DELETE FROM " + table + " WHERE " + condition + ";");
    }

    public void reconnect() {
        if (!this.isClosed()) {
            try {
                this.connection.close();

                this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("MySQL is already closed, can't reconnect");
        }
    }

    public Boolean isClosed() {
        return this.closed;
    }

    public void close() {
        try {
            for (Statement statement : this.currentStatements) {
                statement.close();
            }

            this.connection.close();

            this.closed = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}