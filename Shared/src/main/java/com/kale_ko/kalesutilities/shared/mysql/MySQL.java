package com.kale_ko.kalesutilities.shared.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
    private Connection connection;

    public MySQL(String url, Integer port, String database, String username, String password) {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + url + ":" + port + "/" + database + "?autoReconnect=true", username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(String name, String data) {
        try {
            String createStatement = "CREATE TABLE IF NOT EXISTS " + name + " " + data + ";";
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(createStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String selectAll(String table, String[] fields) {
        return select(table, fields, "1 = 1");
    }

    public String select(String table, String[] fields, String condition) {
        String selectStatement = "SELECT * FROM " + table + " " + "WHERE " + condition + ";";
        StringBuilder ret = new StringBuilder();

        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret.toString();
    }

    public String selectFirst(String table, String field, String condition) {
        String selectStatement = "SELECT * FROM " + table + " " + "WHERE " + condition + ";";

        try {
            Statement statement = this.connection.createStatement();
            ResultSet result = statement.executeQuery(selectStatement);

            while (result.next()) {
                return result.getString(field);
            }

            statement.close();
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void insert(String table, String values) {
        try {
            String insertStatement = "INSERT INTO " + table + " VALUES " + values + ";";
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(insertStatement);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(String table, String keys, String values) {
        try {
            String insertStatement = "INSERT INTO " + table + " " + keys + " VALUES " + values + ";";
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(insertStatement);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String table, String set, String condition) {
        try {
            String updateStatement = "UPDATE " + table + " SET " + set + " WHERE " + condition + ";";
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(updateStatement);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String table, String condition) {
        try {
            String deleteStatement = "DELETE FROM " + table + " WHERE " + condition + ";";
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(deleteStatement);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}