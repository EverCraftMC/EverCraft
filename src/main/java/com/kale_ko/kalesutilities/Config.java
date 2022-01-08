package com.kale_ko.kalesutilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Config {
    private String fileName;
    private YamlConfiguration config;

    public Config(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
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

    public ItemStack getItemStack(String key) {
        return this.config.getItemStack(key);
    }

    public Location getLocation(String key) {
        return this.config.getLocation(key);
    }

    public Vector getVector(String key) {
        return this.config.getVector(key);
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
        File dataFolder = Main.Instance.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File dataFile = Paths.get(dataFolder.getAbsolutePath(), this.getFileName()).toFile();
        try {
            dataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
        this.config = data;
    }

    public void save() {
        try {
            this.config.save(this.fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config load(String fileName) {
        Config newConfig = new Config(fileName);
        newConfig.reload();
        return newConfig;
    }
}