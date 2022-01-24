package com.kale_ko.kalesutilities.spigot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class SpigotConfig {
    private String fileName;
    private String filePath;
    private File file;
    private YamlConfiguration config;

    public SpigotConfig(String fileName) {
        this.fileName = fileName;

        File dataFolder = Main.Instance.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        this.filePath = Paths.get(dataFolder.getAbsolutePath(), this.fileName).toString();
        this.file = new File(this.filePath);
        try {
            this.file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getKeys() {
        return this.config.getKeys(false);
    }

    public Object getObject(String key) {
        return this.config.get(key);
    }

    public String getString(String key) {
        return this.config.getString(key);
    }

    public List<String> getStringList(String key) {
        return this.config.getStringList(key);
    }

    public Integer getInt(String key) {
        return this.config.getInt(key);
    }

    public List<Integer> getIntList(String key) {
        return this.config.getIntegerList(key);
    }

    public Float getFloat(String key) {
        return Float.parseFloat(this.config.getString(key));
    }

    public List<Float> getFloatList(String key) {
        return this.config.getFloatList(key);
    }

    public Double getDouble(String key) {
        return this.config.getDouble(key);
    }

    public List<Double> getDoubleList(String key) {
        return this.config.getDoubleList(key);
    }

    public Long getLong(String key) {
        return this.config.getLong(key);
    }

    public List<Long> getLongList(String key) {
        return this.config.getLongList(key);
    }

    public Boolean getBoolean(String key) {
        return this.config.getBoolean(key);
    }

    public List<Boolean> getBooleanList(String key) {
        return this.config.getBooleanList(key);
    }

    public <T extends ConfigurationSerializable> T getSerializable(String key, Class<T> clazz) {
        return this.config.getSerializable(key, clazz);
    }

    public void set(String key, Object value) {
        this.config.set(key, value);
    }

    public void addDefault(String key, Object value) {
        this.config.addDefault(key, value);
    }

    public void copyDefaults() {
        this.config.options().copyDefaults(true);
        this.save();
    }

    public void reload() {
        YamlConfiguration data = YamlConfiguration.loadConfiguration(this.file);
        this.config = data;
    }

    public void save() {
        try {
            this.config.save(this.filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SpigotConfig load(String fileName) {
        SpigotConfig newConfig = new SpigotConfig(fileName);
        newConfig.reload();
        return newConfig;
    }
}