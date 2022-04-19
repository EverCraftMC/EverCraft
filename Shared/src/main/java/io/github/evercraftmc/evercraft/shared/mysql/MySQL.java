package io.github.evercraftmc.evercraft.shared.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import io.github.evercraftmc.evercraft.shared.util.Closable;

public class MySQL implements Closable {
    private String host;
    private Integer port;
    private String database;

    private String username;
    private String password;

    private Connection connection;

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

    public void createTable(String name, String data) {
        try {
            if (!this.connection.isClosed()) {
                String createStatement = "CREATE TABLE IF NOT EXISTS " + name + " " + data + ";";
                Statement statement = this.connection.createStatement();

                statement.executeUpdate(createStatement);
            } else {
                this.reconnect();

                this.createTable(name, data);
            }
        } catch (SQLException e) {
            this.reconnect();

            this.createTable(name, data);
        }
    }

    public String selectAll(String table, String[] fields) {
        return select(table, fields, "1 = 1");
    }

    public String select(String table, String[] fields, String condition) {
        String selectStatement = "SELECT * FROM " + table + " " + "WHERE " + condition + ";";
        StringBuilder ret = new StringBuilder();

        try {
            if (!this.connection.isClosed()) {
                Statement statement = this.connection.createStatement();
                ResultSet result = statement.executeQuery(selectStatement);

                while (result.next()) {
                    for (String field : fields) {
                        ret.append(result.getString(field) + "\t");
                    }

                    ret = new StringBuilder(ret.substring(0, ret.length() - 1) + "\n");
                }

                statement.close();
                result.close();
            } else {
                this.reconnect();

                return this.select(table, fields, condition);
            }
        } catch (SQLException e) {
            this.reconnect();

            return this.select(table, fields, condition);
        }

        return ret.toString();
    }

    public String selectFirst(String table, String field, String condition) {
        String selectStatement = "SELECT * FROM " + table + " " + "WHERE " + condition + ";";

        try {
            if (!this.connection.isClosed()) {
                Statement statement = this.connection.createStatement();
                ResultSet result = statement.executeQuery(selectStatement);

                while (result.next()) {
                    return result.getString(field);
                }

                statement.close();
                result.close();
            } else {
                this.reconnect();

                return this.selectFirst(table, field, condition);
            }
        } catch (SQLException e) {
            this.reconnect();

            return this.selectFirst(table, field, condition);
        }

        return null;
    }

    public void insert(String table, String values) {
        try {
            if (!this.connection.isClosed()) {
                String insertStatement = "INSERT INTO " + table + " VALUES " + values + ";";
                Statement statement = this.connection.createStatement();

                statement.executeUpdate(insertStatement);
                statement.close();
            } else {
                this.reconnect();

                this.insert(table, values);
            }
        } catch (SQLException e) {
            this.reconnect();

            this.insert(table, values);
        }
    }

    public void insert(String table, String keys, String values) {
        try {
            if (!this.connection.isClosed()) {
                String insertStatement = "INSERT INTO " + table + " " + keys + " VALUES " + values + ";";
                Statement statement = this.connection.createStatement();

                statement.executeUpdate(insertStatement);
                statement.close();
            } else {
                this.reconnect();

                this.insert(table, keys, values);
            }
        } catch (SQLException e) {
            this.reconnect();

            this.insert(table, keys, values);
        }
    }

    public void update(String table, String set, String condition) {
        try {
            if (!this.connection.isClosed()) {
                String updateStatement = "UPDATE " + table + " SET " + set + " WHERE " + condition + ";";
                Statement statement = this.connection.createStatement();

                statement.executeUpdate(updateStatement);
                statement.close();
            } else {
                this.reconnect();

                this.update(table, set, condition);
            }
        } catch (SQLException e) {
            this.reconnect();

            this.update(table, set, condition);
        }
    }

    public void delete(String table, String condition) {
        try {
            if (!this.connection.isClosed()) {
                String deleteStatement = "DELETE FROM " + table + " WHERE " + condition + ";";
                Statement statement = this.connection.createStatement();

                statement.executeUpdate(deleteStatement);
                statement.close();
            } else {
                this.reconnect();

                this.delete(table, condition);
            }
        } catch (SQLException e) {
            this.reconnect();

            this.delete(table, condition);
        }
    }

    public void reconnect() {
        if (!this.closed) {
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
            this.connection.close();

            this.closed = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}