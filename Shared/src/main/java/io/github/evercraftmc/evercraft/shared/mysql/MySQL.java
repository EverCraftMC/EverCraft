package io.github.evercraftmc.evercraft.shared.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import io.github.evercraftmc.evercraft.shared.util.Closable;

public class MySQL implements Closable {
    private String host;
    private Integer port;
    private String database;

    private String username;
    private String password;

    private Connection connection;

    private Integer reconnectTimeout = 0;
    private Long lastReconnect = 0l;

    private Boolean closed = false;

    public MySQL(String host, Integer port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;

        this.username = username;
        this.password = password;

        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private static class Query {
        private static final Query EMPTY;

        static {
            EMPTY = new Query(null, null);
            EMPTY.isEmpty = true;
        }

        private Statement statement;
        private ResultSet results;

        private Boolean isEmpty = false;

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

        public Boolean isEmpty() {
            return this.isEmpty;
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
            Statement statement = this.connection.createStatement();

            statement.execute(query);

            statement.close();
        } catch (SQLException e) {
            if (e.getMessage().startsWith("The last packet successfully received from the server was ")) {
                System.out.println("Lost mysql connection, reconnecting in " + reconnectTimeout + "..");

                if (reconnectTimeout != 0) {
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            if (Instant.now().getEpochSecond() - lastReconnect > 300) {
                                reconnectTimeout = 0;
                            }

                            reconnectTimeout++;
                            lastReconnect = Instant.now().getEpochSecond();

                            reconnect();

                            query(query);
                        }
                    }, reconnectTimeout * 1000);
                } else {
                    if (Instant.now().getEpochSecond() - lastReconnect > 300) {
                        reconnectTimeout = 0;
                    }

                    reconnectTimeout++;
                    lastReconnect = Instant.now().getEpochSecond();

                    reconnect();

                    query(query);
                }
            } else {
                e.printStackTrace();
            }
        }
    }

    public Query queryResponse(String query) {
        try {
            Statement statement = this.connection.createStatement();

            ResultSet results = statement.executeQuery(query);

            return new Query(statement, results);
        } catch (SQLException e) {
            if (e.getMessage().startsWith("The last packet successfully received from the server was ")) {
                System.out.println("Lost mysql connection, reconnecting in " + reconnectTimeout + "..");

                if (reconnectTimeout != 0) {
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            if (Instant.now().getEpochSecond() - lastReconnect > 300) {
                                reconnectTimeout = 0;
                            }

                            reconnectTimeout++;
                            lastReconnect = Instant.now().getEpochSecond();

                            reconnect();

                            query(query);
                        }
                    }, reconnectTimeout * 1000);

                    return Query.EMPTY;
                } else {
                    if (Instant.now().getEpochSecond() - lastReconnect > 300) {
                        reconnectTimeout = 0;
                    }

                    reconnectTimeout++;
                    lastReconnect = Instant.now().getEpochSecond();

                    reconnect();

                    return queryResponse(query);
                }
            } else {
                e.printStackTrace();

                return Query.EMPTY;
            }
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

        if (!query.isEmpty()) {
            try {
                StringBuilder ret = new StringBuilder();

                while (query.getResults().next()) {
                    for (String field : fields) {
                        ret.append(query.getResults().getString(field) + "\t");
                    }

                    ret = new StringBuilder(ret.substring(0, ret.length() - 1) + "\n");
                }

                query.close();

                return ret.toString();
            } catch (SQLException e) {
                e.printStackTrace();

                return "RES:EMPTY";
            }
        } else {
            return "RES:EMPTY";
        }
    }

    public String selectFirst(String table, String field, String condition) {
        Query query = queryResponse("SELECT * FROM " + table + " " + "WHERE " + condition + ";");

        if (!query.isEmpty()) {
            try {
                String res = null;

                if (query.getResults().next()) {
                    res = query.getResults().getString(field);
                }

                query.close();

                return res;
            } catch (SQLException e) {
                e.printStackTrace();

                return "RES:EMPTY";
            }
        } else {
            return "RES:EMPTY";
        }
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

                this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
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