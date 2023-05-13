package io.github.evercraftmc.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.parsers.YamlParser;
import io.github.kale_ko.ejcl.file.bjsl.YamlFileConfig;
import io.github.kale_ko.ejcl.mysql.MySQLConfig;

public class ECPlugin {
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

    protected MySQLConfig<ECData> data;

    public ECPlugin(Object handle, File pluginFile, File dataDirectory, ECEnvironment environment, Logger logger, ClassLoader classLoader) {
        this.handle = handle;

        this.pluginFile = pluginFile;
        this.dataDirectory = dataDirectory;

        this.environment = environment;

        this.logger = logger;
        this.classLoader = classLoader;

        ECPluginManager.registerPlugin(this);

        try {
            YamlFileConfig<MySQLDetails> mySqlDetails = new YamlFileConfig<MySQLDetails>(MySQLDetails.class, dataDirectory.toPath().resolve("mysql.yml").toFile(), new YamlParser.Builder().build());
            mySqlDetails.load(true);

            this.data = new MySQLConfig<ECData>(ECData.class, ((MySQLDetails) mySqlDetails.get()).host, ((MySQLDetails) mySqlDetails.get()).port, ((MySQLDetails) mySqlDetails.get()).database, "evercraft2", ((MySQLDetails) mySqlDetails.get()).username, ((MySQLDetails) mySqlDetails.get()).password);
            this.data.load(false);
        } catch (Exception e) {
            this.data = null;

            this.logger.error("Failed to load player data", e);
        }
    }

    public void load() {
        ECPluginManager.registerPlugin(this);

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
                            ECPluginManager.registerModule(module);

                            this.logger.info("Enabling module " + module.getInfo().getName() + " v" + module.getInfo().getVersion());

                            module.load();
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

                parent.getData().players.get(event.getPlayer().getUuid().toString()).displayName = event.getPlayer().getDisplayName();
                event.getPlayer().setDisplayName(event.getPlayer().getDisplayName());

                parent.getData().players.get(event.getPlayer().getUuid().toString()).lastIp = event.getPlayer().getAddress();
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