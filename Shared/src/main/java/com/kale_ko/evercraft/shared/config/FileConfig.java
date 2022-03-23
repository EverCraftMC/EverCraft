package com.kale_ko.evercraft.shared.config;

import java.util.List;
import java.util.Map;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class FileConfig extends AbstractConfig {
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

        this.reload();
    }

    @Override
    public Boolean exists(String key) {
        return objects.containsKey(key);
    }

    public List<String> getKeys(String path, Boolean deep) {
        return Arrays.asList(objects.keySet().toArray(new String[] {}));
    }

    private Object getRaw(String key) {
        return objects.get(key);
    }

    public <T> T getSerializable(String key, Class<T> clazz) {
        try {
            return (T) getRaw(key);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public <T> List<T> getSerializableList(String key, Class<T> clazz) {
        try {
            return (List<T>) getRaw(key);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void set(String key, Object value) {
        if (!exists(key)) {
            objects.put(key, value);
        } else {
            objects.remove(key);
            objects.put(key, value);
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

            objects = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create().fromJson(contents.toString(), new TypeToken<Map<String, Object>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            BufferedWriter writter = new BufferedWriter(new FileWriter(file));
            writter.write(new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create().toJson(objects));
            writter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() { }
}