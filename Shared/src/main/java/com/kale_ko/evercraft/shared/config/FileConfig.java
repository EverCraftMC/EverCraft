package com.kale_ko.evercraft.shared.config;

import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.NonPrintableStyle;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileConfig extends AbstractConfig {
    private Yaml yaml;

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

        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setAllowDuplicateKeys(false);
        loaderOptions.setAllowRecursiveKeys(false);
        loaderOptions.setEnumCaseSensitive(false);
        loaderOptions.setProcessComments(false);

        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setAllowUnicode(false);
        dumperOptions.setCanonical(false);
        dumperOptions.setDefaultFlowStyle(FlowStyle.BLOCK);
        dumperOptions.setIndent(2);
        dumperOptions.setNonPrintableStyle(NonPrintableStyle.ESCAPE);
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setProcessComments(false);
        dumperOptions.setSplitLines(false);

        this.yaml = new Yaml(new Constructor(loaderOptions), new Representer(dumperOptions), dumperOptions, loaderOptions);
    }

    @Override
    public Boolean exists(String key) {
        return objects.containsKey(key);
    }

    public List<String> getKeys(String path, Boolean deep) {
        List<String> keys = new ArrayList<>();

        for (String key : objects.keySet().toArray(new String[] {})) {
            if (deep && key.startsWith(path + ".") || (!deep && key.startsWith(path + ".") && key.split("\\.").length == path.split("\\.").length + 1)) {
                keys.add(key);
            }
        }

        return keys;
    }

    public Object getRaw(String key) {
        return objects.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getSerializable(String key, Class<T> clazz) {
        try {
            return (T) getRaw(key);
        } catch (ClassCastException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getSerializableList(String key, Class<T> clazz) {
        try {
            return (List<T>) getRaw(key);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void addType(TypeDescription type) {
        this.yaml.addTypeDescription(type);
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

            objects = yaml.load(contents.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            BufferedWriter writter = new BufferedWriter(new FileWriter(file));
            writter.write(yaml.dump(objects));
            writter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() { }
}