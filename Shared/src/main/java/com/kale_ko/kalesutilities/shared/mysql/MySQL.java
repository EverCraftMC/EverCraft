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
            connection = DriverManager.getConnection("jdbc:mysql://" + url + ":" + port + "/" + database, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MySQL(String url, String database, String username, String password) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + url + ":3306/" + database, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String select(String table, String[] fields) {
        return select(table, fields, "1 = 1");
    }

    public String select(String table, String[] fields, String condition) {
        String selectStatement = "SELECT * FROM " + table + " " + "WHERE " + condition;
        StringBuilder ret = new StringBuilder();

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(selectStatement);

            while (result.next()) {
                for (String field : fields) {
                    String currentFieldValue = result.getString(field);

                    if (currentFieldValue != null) {
                        ret.append(result.getString(field) + "\t");
                    }
                }

                ret = new StringBuilder(ret.substring(0, ret.length() - 1) + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret.toString();
    }

    public void insert(String table, String values) {
        try {
            String selectStatement = "INSERT INTO " + table + " VALUES " + values;
            Statement statement = connection.createStatement();

            statement.executeUpdate(selectStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(String table, String keys, String values) {
        try {
            String selectStatement = "INSERT INTO " + table + " " + keys + " VALUES " + values;
            Statement statement = connection.createStatement();

            statement.executeUpdate(selectStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String table, String key, String value, String condition) {
        try {
            String selectStatement = "UPDATE " + table + " SET " + key + " = " + value + " WHERE " + condition;
            Statement statement = connection.createStatement();

            statement.executeUpdate(selectStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String table, String condition) {
        try {
            String selectStatement = "DELETE FROM " + table + " WHERE " + condition;
            Statement statement = connection.createStatement();

            statement.executeUpdate(selectStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}