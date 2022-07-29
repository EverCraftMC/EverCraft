package io.github.evercraftmc.evercraft.shared.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileConfig<T> {
    private static Gson gson;

    static {
        FileConfig.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeSpecialFloatingPointValues().create();
    }

    private File file;

    private Class<T> clazz;

    private T config;

    public FileConfig(Class<T> clazz, String file) {
        this.clazz = clazz;

        this.file = new File(file);

        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
                BufferedWriter writter = new BufferedWriter(new FileWriter(file));
                writter.write("{}");
                writter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public T getParsed() {
        return this.config;
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

            this.config = gson.fromJson(contents.toString(), clazz);

            this.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            BufferedWriter writter = new BufferedWriter(new FileWriter(file));
            writter.write(gson.toJson(this.config, clazz));
            writter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}