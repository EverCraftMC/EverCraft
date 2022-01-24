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

    public void createTable(String name, String data) {
        try {
            String createStatement = "CREATE TABLE IF NOT EXISTS " + name + " " + data;
            Statement statement = connection.createStatement();

            statement.executeUpdate(createStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String selectAll(String table, String[] fields) {
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

    public String selectFirst(String table, String[] fields, String condition) {
        String selectStatement = "SELECT * FROM " + table + " " + "WHERE " + condition;

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(selectStatement);

            while (result.next()) {
                for (String field : fields) {
                    String currentFieldValue = result.getString(field);

                    return result.getString(field);
                }
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
            String insertStatement = "INSERT INTO " + table + " VALUES " + values;
            Statement statement = connection.createStatement();

            statement.executeUpdate(insertStatement);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(String table, String keys, String values) {
        try {
            String insertStatement = "INSERT INTO " + table + " " + keys + " VALUES " + values;
            Statement statement = connection.createStatement();

            statement.executeUpdate(insertStatement);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String table, String key, String value, String condition) {
        try {
            String updateStatement = "UPDATE " + table + " SET " + key + " = " + value + " WHERE " + condition;
            Statement statement = connection.createStatement();

            statement.executeUpdate(updateStatement);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String table, String condition) {
        try {
            String deleteStatement = "DELETE FROM " + table + " WHERE " + condition;
            Statement statement = connection.createStatement();

            statement.executeUpdate(deleteStatement);
            statement.close();
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