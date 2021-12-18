package com.kale_ko.kales_utilities;

import java.util.logging.Logger;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LoadOrder;
import org.bukkit.configuration.file.FileConfiguration;
import com.kale_ko.kales_utilities.commands.KalesUtilities;
import com.kale_ko.kales_utilities.listeners.SignChange;

@Plugin(name = "kales_utilities", version = "1.0.0")
@Description("A custom plugin to run on KalesMC")
@Author("Kale_Ko")
@LogPrefix("Kales Utilities")
@ApiVersion(ApiVersion.Target.v1_17)
@LoadOrder(PluginLoadOrder.STARTUP)
public class Main extends JavaPlugin {
    public static Main Instance;

    public final Logger CONSOLE = getLogger();
    public FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        Main.Instance = this;

        CONSOLE.info("Loading config");

        config.addDefault("config.prefix", "&6&l[Kales Utilities]&r");
        config.addDefault("config.messageFormat", "&7");
        config.addDefault("messages.invalidCommand", "{command} is not a command");
        config.addDefault("messages.usage", "Usage: {usage}");
        config.addDefault("messages.reload", "Config Reloaded");
        config.addDefault("messages.help", "\n{commandList}");
        config.options().copyDefaults(true);
        this.saveConfig();

        CONSOLE.info("Finished loading config");

        CONSOLE.info("Loading commands");

        this.getCommand("kalesutilities").setExecutor(new KalesUtilities());

        CONSOLE.info("Finished loading commands");

        CONSOLE.info("Loading listeners");

        getServer().getPluginManager().registerEvents(new SignChange(), this);

        CONSOLE.info("Finished loading listeners");

        CONSOLE.info("Finished loading");
    }

    @Override
    public void onDisable() {
        CONSOLE.info("Disabled");
    }
}