package io.github.evercraftmc.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import org.slf4j.Logger;
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
import io.github.kale_ko.bjsl.parsers.YamlParser;
import io.github.kale_ko.bjsl.processor.ObjectProcessor;
import io.github.kale_ko.ejcl.file.bjsl.YamlFileConfig;
import io.github.kale_ko.ejcl.mysql.MySQLConfig;

public class ECPlugin {
    private class MessagingDetails {
        public InetAddress host;
        public int port;

        public boolean useSSL = true;

        public String id;
    }

    private class MySQLDetails {
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

    protected Logger logger;
    protected ClassLoader classLoader;

    protected ECServer server;

    protected ECMessager messager;

    protected MySQLConfig<ECData> data;

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
            this.logger.info("Connecting to Messaging server..");

            YamlFileConfig<MessagingDetails> messagingDetails = new YamlFileConfig<MessagingDetails>(MessagingDetails.class, dataDirectory.toPath().resolve("messaging.yml").toFile(), new YamlParser.Builder().build());
            messagingDetails.load(true);

            this.messager = new ECMessager(this, new InetSocketAddress(messagingDetails.get().host, messagingDetails.get().port), messagingDetails.get().useSSL, messagingDetails.get().id);
            this.messager.connect();

            this.logger.info("Connected to Messaging server");
        } catch (Exception e) {
            this.logger.error("Failed connecting to Messaging server", e);
        }

        try {
            YamlFileConfig<MySQLDetails> mySqlDetails = new YamlFileConfig<MySQLDetails>(MySQLDetails.class, dataDirectory.toPath().resolve("mysql.yml").toFile(), new YamlParser.Builder().build());
            mySqlDetails.load(true);

            this.logger.info("Connecting to MySQL server..");

            this.data = new MySQLConfig<ECData>(ECData.class, ((MySQLDetails) mySqlDetails.get()).host, ((MySQLDetails) mySqlDetails.get()).port, ((MySQLDetails) mySqlDetails.get()).database, "evercraft2", ((MySQLDetails) mySqlDetails.get()).username, ((MySQLDetails) mySqlDetails.get()).password, new ObjectProcessor.Builder().setIgnoreNulls(true).setIgnoreEmptyObjects(true).setIgnoreDefaults(true).build());
            this.data.connect();

            this.logger.info("Loading plugin data..");

            this.data.load(false);

            this.logger.info("Loaded plugin data");
        } catch (Exception e) {
            this.logger.error("Failed loading plugin data", e);
        }

        File modulesDirectory = this.getDataDirectory().toPath().resolve("modules").toFile();
        if (!modulesDirectory.exists()) {
            modulesDirectory.mkdirs();
        }

        for (File file : modulesDirectory.listFiles()) {
            if (file.getName().toLowerCase().endsWith(".jar")) {
                this.loadModule(file);
            }
        }
    }

    protected void loadModule(File file) {
        try {
            ECModuleInfo moduleInfo = null;

            JarInputStream jar = new JarInputStream(new BufferedInputStream(new FileInputStream(file)));
            ZipEntry entry;
            while ((entry = jar.getNextEntry()) != null) {
                if (entry.getName().equalsIgnoreCase("evercraft.yml")) {
                    StringBuilder data = new StringBuilder();

                    int read;
                    while ((read = jar.read()) != -1) {
                        data.appendCodePoint(read);
                    }

                    moduleInfo = BJSL.parseYaml(data.toString(), ECModuleInfo.class);
                }
            }
            jar.close();

            if (moduleInfo != null) {
                try {
                    ECModuleClassLoader moduleClassLoader = new ECModuleClassLoader(this.classLoader, file);
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
                                } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException exception) {
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
                                this.logger.error("Error loading module \"" + file.getName() + "\"", e);
                            }
                        } else {
                            this.logger.error("Error loading module \"" + file.getName() + "\"\n  Entry class has no 0 args constructor");
                        }
                    } else {
                        this.logger.error("Error loading module \"" + file.getName() + "\"\n  Entry class does not implement ECModule");
                    }
                } catch (ClassNotFoundException e) {
                    this.logger.error("Error loading module \"" + file.getName() + "\"\n  Entry class could not be found (\"" + moduleInfo.getEntry() + "\")");
                }
            } else {
                this.logger.error("Error loading module \"" + file.getName() + "\"\n  Jar does not contain a module file (evercraft.yml)");
            }
        } catch (IOException e) {
            this.logger.error("Error loading module \"" + file.getName() + "\"", e);
        }
    }

    public void unload() {
        for (ECModule module : ECPluginManager.getModules()) {
            this.logger.info("Disabling module " + module.getInfo().getName() + " v" + module.getInfo().getVersion());

            module.unload();

            ECPluginManager.unregisterModule(module);
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

    public Logger getLogger() {
        return this.logger;
    }

    public ECServer getServer() {
        return this.server;
    }

    public void setServer(ECServer server) {
        this.server = server;

        this.server.getEventManager().register(new ECListener() {
            protected final ECPlugin parent = ECPlugin.this;

            @ECHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                parent.getData().players.get(event.getPlayer().getUuid().toString()).uuid = event.getPlayer().getUuid();
                parent.getData().players.get(event.getPlayer().getUuid().toString()).name = event.getPlayer().getName();

                if (parent.getData().players.get(event.getPlayer().getUuid().toString()).displayName == null) {
                    parent.getData().players.get(event.getPlayer().getUuid().toString()).displayName = event.getPlayer().getName();
                }

                event.getPlayer().setDisplayName(ECTextFormatter.translateColors((parent.getData().players.get(event.getPlayer().getUuid().toString()).prefix != null ? parent.getData().players.get(event.getPlayer().getUuid().toString()).prefix + " " : "") + parent.getData().players.get(event.getPlayer().getUuid().toString()).displayName));
            }
        });
    }

    public ECData getData() {
        return this.data.get();
    }

    public Object getData(String path) {
        return this.data.get(path);
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
}