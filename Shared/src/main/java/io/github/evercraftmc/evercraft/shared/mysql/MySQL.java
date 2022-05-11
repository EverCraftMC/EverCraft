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
            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?tlsmode=verify-full", username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class Query {
        private Statement statement;
        private ResultSet results;

        protected Query(Statement statement, ResultSet results) {
            this.statement = statement;
            this.results = results;
        }

        public Statement getStatement() {
            return this.statement;
        }

        public ResultSet getResults() {
            return this.results;
        }

        public void close() {
            try {
                if (this.results != null) {
                    this.results.close();
                }

                this.statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void query(String query) {
        try {
            if (!this.connection.isClosed()) {
                Statement statement = this.connection.createStatement();

                statement.execute(query);

                statement.close();
            } else {
                this.reconnect();

                this.query(query);
            }
        } catch (SQLException e) {
            this.reconnect();

            this.query(query);
        }
    }

    public Query queryResponse(String query) {
        try {
            if (!this.connection.isClosed()) {
                Statement statement = this.connection.createStatement();

                ResultSet results = statement.executeQuery(query);

                return new Query(statement, results);
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
        Query query = queryResponse("SELECT * FROM " + table + " " + "WHERE " + condition + ";");

        StringBuilder ret = new StringBuilder();

        try {
            while (query.getResults().next()) {
                for (String field : fields) {
                    ret.append(query.getResults().getString(field) + "\t");
                }

                ret = new StringBuilder(ret.substring(0, ret.length() - 1) + "\n");
            }

            query.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret.toString();
    }

    public String selectFirst(String table, String field, String condition) {
        Query query = queryResponse("SELECT * FROM " + table + " " + "WHERE " + condition + ";");

        try {
            String res = null;

            if (query.getResults().next()) {
                res = query.getResults().getString(field);
            }

            query.close();

            return res;
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

                this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?tlsmode=verify-full", username, password);
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