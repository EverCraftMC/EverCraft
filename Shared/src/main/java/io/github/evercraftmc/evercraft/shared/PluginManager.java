package io.github.evercraftmc.evercraft.shared;

import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;

public class PluginManager {
    private static List<Plugin> registeredPlugins = new ArrayList<Plugin>();

    public static Plugin getInstance() {
        return PluginManager.registeredPlugins.get(0);
    }

    public static List<Plugin> getInstances() {
        return PluginManager.registeredPlugins;
    }

    public static void register(Plugin plugin) {
        if (!PluginManager.registeredPlugins.contains(plugin)) {
            PluginManager.registeredPlugins.add(plugin);
        }
    }

    public static void unregister(Plugin plugin) {
        if (PluginManager.registeredPlugins.contains(plugin)) {
            PluginManager.registeredPlugins.remove(plugin);
        }
    }

    public static Logger createLogger(String title) {
        return createLogger(title, "[{timeC} {typeU}]: [{name}] {message}");
    }

    public static Logger createLogger(String title, String format) {
        Logger logger = Logger.getLogger(title);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);

        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        ConsoleHandler handler = new ConsoleHandler();

        try {
            handler.setEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Formatter formatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                return format.replace("{type}", record.getLevel().getName()).replace("{typeU}", record.getLevel().getName().toUpperCase()).replace("{typeL}", record.getLevel().getName().toLowerCase()).replace("{typeT}", StringUtils.toTtitleCase(record.getLevel().getName().toLowerCase())).replace("{time}", DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault()).format(record.getInstant())).replace("{timeC}", DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault()).format(record.getInstant())).replace("{timeD}", DateTimeFormatter.ofPattern("HH-mm-ss").withZone(ZoneId.systemDefault()).format(record.getInstant())).replace("{name}", record.getLoggerName()).replace("{message}", record.getMessage()) + "\n";
            }
        };

        handler.setFormatter(formatter);

        logger.addHandler(handler);

        return logger;
    }
}