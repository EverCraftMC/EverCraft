package io.github.evercraftmc.evercraft.discord.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.evercraftmc.evercraft.discord.BotMain;

public class DataParser<T extends DataParseable> {
    private static Gson gson;

    static {
        DataParser.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().serializeSpecialFloatingPointValues().create();
    }

    private String fileName;

    private T data;

    public DataParser(Class<T> clazz, String fileName) {
        this.fileName = fileName;

        try {
            File file = new File(BotMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().resolve(fileName));

            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder contents = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                contents.append(line + "\n");
                line = reader.readLine();
            }
            reader.close();

            if (contents.isEmpty()) {
                contents = new StringBuilder("{}");

                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(contents.toString());
                writer.close();
            }

            this.data = gson.fromJson(contents.toString(), clazz);
            this.data.setParser(this);
            this.save();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public T getData() {
        return this.data;
    }

    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(BotMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().resolve(fileName))));
            writer.write(gson.toJson(data));
            writer.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}