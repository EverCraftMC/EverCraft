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
        FileConfig.gson = new GsonBuilder().setPrettyPrinting().serializeNulls().serializeSpecialFloatingPointValues().create();
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

        List<String> keys = new ArrayList<>();

        for (String key : this.objects.keySet().toArray(new String[] {})) {
            if ((deep && (path == null || key.startsWith(path + ".")))) {
                keys.add(key);
            } else if (!deep && (path == null || key.startsWith(path + "."))) {
                keys.add(String.join(".", Arrays.asList(key.split("\\.")).subList(0, path.split("\\.").length + 1)));
            }
        }

        return keys;
    }

    public Object getRaw(String key) {
        return this.objects.get(key);
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

        if (obj instanceof List) {
            return (List<T>) obj;
        } else {
            return null;
        }
    }

    public void set(String key, Object value) {
        if (!exists(key)) {
            this.objects.put(key, value);
        } else {
            this.objects.remove(key);
            this.objects.put(key, value);
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

            this.objects = gson.fromJson(contents.toString(), new TypeToken<Map<String, Object>>() { }.getType());
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

    public void close() { }
}