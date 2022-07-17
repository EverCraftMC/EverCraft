package io.github.evercraftmc.evercraft.shared.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.evercraftmc.evercraft.shared.mysql.MySQL;

public class MySQLConfig {
    private static Gson gson;

    static {
        MySQLConfig.gson = new GsonBuilder().disableHtmlEscaping().serializeSpecialFloatingPointValues().create();
    }

    private MySQL mysql;
    private String tableName;

    public MySQLConfig(String host, Integer port, String database, String tableName, String username, String password) {
        this.mysql = new MySQL(host, port, database, username, password);
        this.tableName = tableName;

        this.mysql.createTable(this.tableName, "(keyid LONGTEXT NOT NULL, keyvalue LONGTEXT NOT NULL)");
    }

    public Boolean exists(String key) {
        return mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'") != null;
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
        String res = mysql.selectFirst(this.tableName, "keyvalue", "keyid = '" + key + "'");

        return res;
    }

    public String getString(String key) {
        return getSerializable(key, String.class);
    }

    public List<String> getStringList(String key) {
        return getSerializableList(key, String.class);
    }

    public Integer getInteger(String key) {
        try {
            return getSerializable(key, Integer.class);
        } catch (ClassCastException e) {
            try {
                return Math.round((float) ((double) getSerializable(key, Double.class)));
            } catch (ClassCastException e2) {
                try {
                    return Math.round(getSerializable(key, Float.class));
                } catch (ClassCastException e3) {
                    try {
                        return Math.round(getSerializable(key, Long.class));
                    } catch (ClassCastException e4) {
                        return null;
                    }
                }
            }
        }
    }

    public List<Integer> getIntegerList(String key) {
        return getSerializableList(key, Integer.class);
    }

    public Float getFloat(String key) {
        try {
            return getSerializable(key, Float.class);
        } catch (ClassCastException e) {
            try {
                return (float) ((double) getSerializable(key, Double.class));
            } catch (ClassCastException e2) {
                try {
                    return (float) ((double) getSerializable(key, Long.class));
                } catch (ClassCastException e3) {
                    try {
                        return (float) ((double) getSerializable(key, Integer.class));
                    } catch (ClassCastException e4) {
                        return null;
                    }
                }
            }
        }
    }

    public List<Float> getFloatList(String key) {
        return getSerializableList(key, Float.class);
    }

    public Double getDouble(String key) {
        try {
            return getSerializable(key, Double.class);
        } catch (ClassCastException e) {
            try {
                return (double) getSerializable(key, Float.class);
            } catch (ClassCastException e2) {
                try {
                    return (double) getSerializable(key, Long.class);
                } catch (ClassCastException e3) {
                    try {
                        return (double) getSerializable(key, Integer.class);
                    } catch (ClassCastException e4) {
                        return null;
                    }
                }
            }
        }
    }

    public List<Double> getDoubleList(String key) {
        return getSerializableList(key, Double.class);
    }

    public Long getLong(String key) {
        try {
            return getSerializable(key, Long.class);
        } catch (ClassCastException e) {
            try {
                return (long) ((int) getSerializable(key, Integer.class));
            } catch (ClassCastException e2) {
                try {
                    return (long) ((double) getSerializable(key, Double.class));
                } catch (ClassCastException e3) {
                    try {
                        return (long) ((double) getSerializable(key, Float.class));
                    } catch (ClassCastException e4) {
                        return null;
                    }
                }
            }
        }
    }

    public List<Long> getLongList(String key) {
        return getSerializableList(key, Long.class);
    }

    public Boolean getBoolean(String key) {
        Boolean value = getSerializable(key, Boolean.class);

        if (value != null) {
            return value;
        } else {
            return false;
        }
    }

    public List<Boolean> getBooleanList(String key) {
        return getSerializableList(key, Boolean.class);
    }

    public <T> T getSerializable(String key, Class<T> clazz) {
        return gson.fromJson(getRaw(key), new TypeToken<T>() {}.getType());
    }

    public <T> List<T> getSerializableList(String key, Class<T> clazz) {
        return gson.fromJson(getRaw(key), new TypeToken<List<T>>() {}.getType());
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

    public void reload() {
        mysql.reconnect();
    }

    public void close() {
        mysql.close();
    }
}