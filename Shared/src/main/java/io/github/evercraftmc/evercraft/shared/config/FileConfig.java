package io.github.evercraftmc.evercraft.shared.config;

import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FileConfig extends AbstractConfig {
    private static Gson gson;

    static {
        FileConfig.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().serializeSpecialFloatingPointValues().create();
    }

    private File file;

    private Map<String, Object> objects = new HashMap<String, Object>();

    public FileConfig(String file) {
        this.file = new File(file);

        try {
            if (!this.file.exists()) {
                this.file.createNewFile();
                this.save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean exists(String key) {
        return this.objects.containsKey(key);
    }

    @Override
    public Integer getInteger(String key) {
        return Math.round(getFloat(key));
    }

    @Override
    public Float getFloat(String key) {
        return (float) ((double) getDouble(key));
    }

    public List<String> getKeys(String path, Boolean deep) {
        if (path.equalsIgnoreCase("")) {
            path = null;
        }

        List<String> keys = new ArrayList<String>();

        for (String rawkey : this.objects.keySet().toArray(new String[] {})) {
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

    public Object getRaw(String key) {
        if (this.objects.containsKey(key)) {
            return this.objects.get(key);
        } else if (this.getDefaults().containsKey(key)) {
            return this.getDefaults().get(key);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getSerializable(String key, Class<T> clazz) {
        Object obj = getRaw(key);

        if (obj == null) {
            return null;
        }

        if (obj instanceof LinkedTreeMap) {
            LinkedTreeMap<String, Object> tree = (LinkedTreeMap<String, Object>) obj;

            return gson.fromJson(gson.toJsonTree(tree).toString(), clazz);
        } else if (obj instanceof String || obj instanceof Float || obj instanceof Double || obj instanceof Integer || obj instanceof Long || obj instanceof Boolean) {
            return (T) obj;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getSerializableList(String key, Class<T> clazz) {
        Object obj = getRaw(key);

        if (obj == null) {
            return null;
        }

        if (obj instanceof LinkedTreeMap) {
            LinkedTreeMap<String, Object> tree = (LinkedTreeMap<String, Object>) obj;

            List<Object> list = gson.fromJson(gson.toJsonTree(tree).toString(), List.class);

            List<T> parsedList = new ArrayList<T>();

            for (Object item : list) {
                if (item != null) {
                    if (item instanceof LinkedTreeMap) {
                        LinkedTreeMap<String, Object> itemtree = (LinkedTreeMap<String, Object>) item;

                        parsedList.add(gson.fromJson(gson.toJsonTree(itemtree).toString(), clazz));
                    } else if (item instanceof String || item instanceof Float || item instanceof Double || item instanceof Integer || item instanceof Long || item instanceof Boolean) {
                        parsedList.add((T) item);
                    }
                }
            }

            return parsedList;
        } else if (obj instanceof List) {
            List<Object> list = (List<Object>) obj;

            List<T> parsedList = new ArrayList<T>();

            for (Object item : list) {
                if (item != null) {
                    if (item instanceof LinkedTreeMap) {
                        LinkedTreeMap<String, Object> itemtree = (LinkedTreeMap<String, Object>) item;

                        parsedList.add(gson.fromJson(gson.toJsonTree(itemtree).toString(), clazz));
                    } else if (item instanceof String || item instanceof Float || item instanceof Double || item instanceof Integer || item instanceof Long || item instanceof Boolean) {
                        parsedList.add((T) item);
                    }
                }
            }

            return parsedList;
        } else if (obj instanceof Object[]) {
            Object[] list = (Object[]) obj;

            List<T> parsedList = new ArrayList<T>();

            for (Object item : list) {
                if (item != null) {
                    if (item instanceof LinkedTreeMap) {
                        LinkedTreeMap<String, Object> itemtree = (LinkedTreeMap<String, Object>) item;

                        parsedList.add(gson.fromJson(gson.toJsonTree(itemtree).toString(), clazz));
                    } else if (item instanceof String || item instanceof Float || item instanceof Double || item instanceof Integer || item instanceof Long || item instanceof Boolean) {
                        parsedList.add((T) item);
                    }
                }
            }

            return parsedList;
        } else {
            return null;
        }
    }

    public void set(String key, Object value) {
        if (value != null) {
            if (!exists(key)) {
                this.objects.put(key, value);
            } else {
                this.objects.remove(key);
                this.objects.put(key, value);
            }
        } else {
            if (exists(key)) {
                this.objects.remove(key);
            }
        }
    }

    public void reload() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder contents = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                contents.append(line + "\n");
            }
            reader.close();

            this.objects = gson.fromJson(contents.toString(), new TypeToken<Map<String, Object>>() {}.getType());

            for (Map.Entry<String, Object> entry : this.objects.entrySet()) {
                if (entry.getValue() == null) {
                    this.objects.remove(entry.getKey());
                }
            }

            this.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            BufferedWriter writter = new BufferedWriter(new FileWriter(file));
            writter.write(gson.toJson(this.objects));
            writter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {}
}