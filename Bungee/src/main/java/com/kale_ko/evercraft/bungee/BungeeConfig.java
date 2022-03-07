package com.kale_ko.evercraft.bungee;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kale_ko.evercraft.shared.PluginConfig;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeConfig implements PluginConfig  {
    private String fileName;
    private String filePath;
    private File file;
    private Configuration config;

    private Map<String, Object> defaults = new HashMap<String, Object>();

    public BungeeConfig(String fileName) {
        this.fileName = fileName;

        File dataFolder = BungeePlugin.Instance.getDataFolder();
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

    public Collection<String> getKeys() {
        return this.config.getKeys();
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
        return this.config.getIntList(key);
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

    public <T> T getSerializable(String key, T clazz) {
        return this.config.get(key, clazz);
    }

    public void set(String key, Object value) {
        this.config.set(key, value);
    }

    public void addDefault(String key, Object value) {
        this.defaults.put(key, value);
    }

    public void copyDefaults() {
        for (Map.Entry<String, Object> entry : this.defaults.entrySet()) {
            if (this.config.get(entry.getKey()) == null) {
                this.config.set(entry.getKey(), entry.getValue());
            }
        }

        this.save();
    }

    public void reload() {
        try {
            Configuration data = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
            this.config = data;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BungeeConfig load(String fileName) {
        BungeeConfig newConfig = new BungeeConfig(fileName);
        newConfig.reload();
        return newConfig;
    }
}