package io.github.evercraftmc.core;

import io.github.evercraftmc.core.api.ECModule;
import io.github.evercraftmc.core.api.ECModuleInfo;
import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerJoinEvent;
import io.github.evercraftmc.core.api.server.ECServer;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.core.messaging.ECMessager;
import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.elements.ParsedObject;
import io.github.kale_ko.bjsl.parsers.YamlParser;
import io.github.kale_ko.bjsl.processor.ObjectProcessor;
import io.github.kale_ko.ejcl.file.bjsl.YamlFileConfig;
import io.github.kale_ko.ejcl.mysql.StructuredMySQLConfig;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.jar.JarInputStream;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import org.slf4j.Logger;

public class ECPlugin {
    private static class MessagingDetails {
        public String host = "127.0.0.1";
        public int port = 3000;

        public UUID id = UUID.fromString(System.getProperty("serverID"));
    }

    private static class MySQLDetails {
        public String host;
        public int port;

        public String username;
        public String password;

        public String database;
    }

    protected Object handle;

    protected File pluginFile;
    protected File dataDirectory;

    protected ECEnvironment environment;
    protected ECServer server;

    protected Logger logger;
    protected ClassLoader classLoader;

    protected StructuredMySQLConfig<ECPlayerData> data;
    protected ECMessager messager;

    protected ParsedObject translations;

    public ECPlugin(Object handle, File pluginFile, File dataDirectory, ECEnvironment environment, Logger logger, ClassLoader classLoader) {
        this.handle = handle;

        this.pluginFile = pluginFile;
        this.dataDirectory = dataDirectory;

        this.environment = environment;

        this.logger = logger;
        this.classLoader = classLoader;

        ECPluginManager.registerPlugin(this);
    }

    public void load() {
        ECPluginManager.registerPlugin(this);

        try {
            if (!Files.exists(this.getDataDirectory().toPath())) {
                Files.createDirectories(this.getDataDirectory().toPath());
            }
        } catch (Exception e) {
            this.logger.error("Failed to create data dir", e);
            return;
        }

        try {
            this.logger.info("Connecting to Messaging server..");

            YamlFileConfig<MessagingDetails> messagingDetails = new YamlFileConfig<>(MessagingDetails.class, dataDirectory.toPath().resolve("messaging.yml").toFile(), new YamlParser.Builder().build());
            messagingDetails.load(true);
            if (messagingDetails.get().id == null) {
                messagingDetails.get().id = UUID.randomUUID();
                messagingDetails.save();
            }

            this.messager = new ECMessager(this, new InetSocketAddress(messagingDetails.get().host, messagingDetails.get().port), messagingDetails.get().id);
            this.messager.connect();

            this.logger.info("Connected to Messaging server");
        } catch (Exception e) {
            this.logger.error("Failed connecting to Messaging server", e);
            return;
        }

        try {
            YamlFileConfig<MySQLDetails> mySqlDetails = new YamlFileConfig<>(MySQLDetails.class, dataDirectory.toPath().resolve("mysql.yml").toFile(), new YamlParser.Builder().build());
            mySqlDetails.load(true);

            this.logger.info("Connecting to MySQL server..");

            this.data = new StructuredMySQLConfig<>(ECPlayerData.class, mySqlDetails.get().host, mySqlDetails.get().port, mySqlDetails.get().database, "evercraft", mySqlDetails.get().username, mySqlDetails.get().password, new ObjectProcessor.Builder().setIgnoreNulls(true).setIgnoreEmptyObjects(true).setIgnoreDefaults(true).build());
            this.data.connect();

            this.logger.info("Loading plugin data..");

            this.data.load(false);

            this.server.getScheduler().runTaskRepeatAsync(this::loadData, 120 * 20, 120 * 20);

            this.logger.info("Loaded plugin data");
        } catch (Exception e) {
            this.logger.error("Failed loading plugin data", e);
            return;
        }

        try {
            this.logger.info("Downloading minecraft translations..");

            HttpClient httpClient = HttpClient.newBuilder().build();
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI("https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/" + this.getServer().getMinecraftVersion() + "/assets/minecraft/lang/en_us.json")).GET().build();
            String response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();

            this.translations = BJSL.parseJson(response).asObject();

            this.logger.info("Downloaded minecraft translations for en_us.");
        } catch (Exception e) {
            this.logger.error("Failed downloading minecraft translations", e);
        }

        try {
            Path modulesDirectory = this.getDataDirectory().toPath().resolve("modules");
            if (!Files.exists(modulesDirectory)) {
                Files.createDirectories(modulesDirectory);
            }

            try (Stream<Path> files = Files.list(modulesDirectory)) {
                for (Path file : files.toList()) {
                    if (file.getFileName().toString().toLowerCase().endsWith(".jar")) {
                        this.loadModule(file);
                    }
                }
            }
        } catch (IOException e) {
            this.logger.error("Failed loading modules", e);
        }
    }

    protected void loadModule(Path file) {
        try {
            ECModuleInfo moduleInfo = null;

            try (JarInputStream jar = new JarInputStream(new BufferedInputStream(new FileInputStream(file.toFile())))) {
                ZipEntry entry;
                while ((entry = jar.getNextEntry()) != null) {
                    if (entry.getName().equalsIgnoreCase("evercraft.yml")) {
                        StringBuilder string = new StringBuilder();

                        int read;
                        while ((read = jar.read()) != -1) {
                            string.appendCodePoint(read);
                        }

                        moduleInfo = BJSL.parseYaml(string.toString(), ECModuleInfo.class);
                    }
                }
            }

            if (moduleInfo != null) {
                try {
                    ECModuleClassLoader moduleClassLoader = new ECModuleClassLoader(this.classLoader, file.toFile());
                    Class<?> moduleClass = moduleClassLoader.loadClass(moduleInfo.getEntry());

                    if (ECModule.class.isAssignableFrom(moduleClass)) {
                        ECModule module = null;
                        for (Constructor<?> constructor : moduleClass.getConstructors()) {
                            if (constructor.getParameterCount() == 0) {
                                try {
                                    module = (ECModule) constructor.newInstance();
                                    module.setPlugin(this);
                                    module.setInfo(moduleInfo);

                                    break;
                                } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException ignored) {
                                }
                            }
                        }

                        if (module != null) {
                            this.logger.info("Enabling module " + module.getInfo().getName() + " v" + module.getInfo().getVersion() + "..");

                            ECPluginManager.registerModule(module);

                            try {
                                module.load();

                                this.logger.info("Enabled module " + module.getInfo().getName());
                            } catch (Exception e) {
                                this.logger.error("Error loading module \"" + file.getFileName() + "\"", e);
                            }
                        } else {
                            this.logger.error("Error loading module \"" + file.getFileName() + "\"\n  Entry class has no 0 args constructor");
                        }
                    } else {
                        this.logger.error("Error loading module \"" + file.getFileName() + "\"\n  Entry class does not implement ECModule");
                    }
                } catch (ClassNotFoundException e) {
                    this.logger.error("Error loading module \"" + file.getFileName() + "\"\n  Entry class could not be found (\"" + moduleInfo.getEntry() + "\")");
                }
            } else {
                this.logger.error("Error loading module \"" + file.getFileName() + "\"\n  Jar does not contain a module file (evercraft.yml)");
            }
        } catch (IOException e) {
            this.logger.error("Error loading module \"" + file.getFileName() + "\"", e);
        }
    }

    public void unload() {
        for (ECModule module : ECPluginManager.getModules()) {
            this.logger.info("Disabling module " + module.getInfo().getName() + " v" + module.getInfo().getVersion());

            module.unload();

            ECPluginManager.unregisterModule(module);
        }

        if (this.messager != null) {
            this.messager.close();
        }

        ECPluginManager.unregisterPlugin();
    }

    public Object getHandle() {
        return this.handle;
    }

    public File getPluginFile() {
        return this.pluginFile;
    }

    public File getDataDirectory() {
        return this.dataDirectory;
    }

    public ECEnvironment getEnvironment() {
        return this.environment;
    }

    public ECServer getServer() {
        return this.server;
    }

    public void setServer(ECServer server) {
        this.server = server;

        this.server.getEventManager().register(new ECListener() {
            private final ECPlugin parent = ECPlugin.this;

            @ECHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                parent.getPlayerData().players.get(event.getPlayer().getUuid().toString()).uuid = event.getPlayer().getUuid();
                parent.getPlayerData().players.get(event.getPlayer().getUuid().toString()).name = event.getPlayer().getName();

                if (parent.getPlayerData().players.get(event.getPlayer().getUuid().toString()).displayName == null) {
                    parent.getPlayerData().players.get(event.getPlayer().getUuid().toString()).displayName = event.getPlayer().getName();
                }

                parent.saveData();

                event.getPlayer().setDisplayName(ECTextFormatter.translateColors((parent.getPlayerData().players.get(event.getPlayer().getUuid().toString()).prefix != null ? parent.getPlayerData().players.get(event.getPlayer().getUuid().toString()).prefix + "&r " : "&r") + parent.getPlayerData().players.get(event.getPlayer().getUuid().toString()).displayName + "&r"));
            }
        });
    }

    public Logger getLogger() {
        return this.logger;
    }

    public ECMessager getMessager() {
        return this.messager;
    }

    public ECPlayerData getPlayerData() {
        return this.data.get();
    }

    public void loadData() {
        try {
            this.data.load(false);
        } catch (IOException e) {
            this.logger.error("Failed to load player data", e);
        }
    }

    public void saveData() {
        try {
            this.data.save();
        } catch (IOException e) {
            this.logger.error("Failed to save player data", e);
        }
    }

    public ParsedObject getTranslations() {
        return this.translations;
    }
}