package com.kale_ko.kalesutilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
    private String fileName;
    private YamlConfiguration config;

    public Config(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public YamlConfiguration getConfig() {
        return this.config;
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