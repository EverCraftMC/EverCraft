package com.kale_ko.kalesutilities;

import com.kale_ko.kalesutilities.commands.AboutCommand;
import com.kale_ko.kalesutilities.commands.KalesUtilitiesCommand;
import com.kale_ko.kalesutilities.commands.NicknameCommand;
import com.kale_ko.kalesutilities.commands.PrefixCommand;
import com.kale_ko.kalesutilities.commands.RulesCommand;
import com.kale_ko.kalesutilities.commands.SeenCommand;
import com.kale_ko.kalesutilities.commands.SetSpawnCommand;
import com.kale_ko.kalesutilities.commands.SpawnCommand;
import com.kale_ko.kalesutilities.commands.StatusCommand;
import com.kale_ko.kalesutilities.commands.SudoCommand;
import com.kale_ko.kalesutilities.commands.StaffChatCommand;
import com.kale_ko.kalesutilities.commands.StaffCommand;
import com.kale_ko.kalesutilities.listeners.ChatFilterListener;
import com.kale_ko.kalesutilities.listeners.ChatFormatListener;
import com.kale_ko.kalesutilities.listeners.PlayerListener;
import com.kale_ko.kalesutilities.listeners.PlayerMoveListener;
import com.kale_ko.kalesutilities.listeners.SeenListener;
import com.kale_ko.kalesutilities.listeners.SignEditorListener;
import com.kale_ko.kalesutilities.listeners.SpawnListener;
import com.kale_ko.kalesutilities.listeners.WelcomeListener;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LoadOrder;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.logging.Logger;

@Plugin(name = "KalesUtilities", version = "1.7.4")
@Description("A custom plugin to run on KalesMC")
@Author("Kale_Ko")
@LogPrefix("Kales Utilities")
@ApiVersion(ApiVersion.Target.v1_17)
@LoadOrder(PluginLoadOrder.STARTUP)

@Command(name = "kalesutilities", desc = "The main plugin command for Kales Utilities", aliases = { "ks" }, usage = "/kalesutilities [help, reload]")
@Command(name = "about", desc = "See the about", aliases = {}, usage = "/about")
@Command(name = "rules", desc = "See the rules", aliases = {}, usage = "/rules")
@Command(name = "staff", desc = "See the staff", aliases = {}, usage = "/staff")
@Command(name = "spawn", desc = "Go to the spawn", aliases = { "hub", "lobby" }, usage = "/spawn {player (optional)}")
@Command(name = "setspawn", desc = "Sets the spawn to your location", aliases = { "sethub", "setlobby" }, usage = "/setspawn")
@Command(name = "seen", desc = "See when a player was last online", aliases = { "lastseen" }, usage = "/seen {player}")
@Command(name = "nickname", desc = "Sets you nickname", aliases = { "nick" }, usage = "/nickname {nickname}")
@Command(name = "prefix", desc = "Sets you prefix", aliases = {}, usage = "/prefix {prefix}")
@Command(name = "status", desc = "Sets you status", aliases = { "afk" }, usage = "/status {status}")
@Command(name = "sudo", desc = "Make a player say something or run a command", aliases = {}, usage = "/sudo {player} {message/command}")
@Command(name = "staffchat", desc = "Send a message in the staffchat", aliases = { "sc" }, usage = "/staffchat {message}")

@Permission(name = "kalesutilities.about", desc = "Use /about")
@Permission(name = "kalesutilities.rules", desc = "Use /rules")
@Permission(name = "kalesutilities.staff", desc = "Use /staff")
@Permission(name = "kalesutilities.spawn", desc = "Use /spawn")
@Permission(name = "kalesutilities.setspawn", desc = "Use /setspawn")
@Permission(name = "kalesutilities.seen", desc = "Use /seen")
@Permission(name = "kalesutilities.setnickname", desc = "Use /nickname")
@Permission(name = "kalesutilities.setprefix", desc = "Use /prefix")
@Permission(name = "kalesutilities.setstatus", desc = "Use /status")
@Permission(name = "kalesutilities.sudo", desc = "Use /sudo")
@Permission(name = "kalesutilities.staffchat", desc = "Use /staffchat")

@Permission(name = "kalesutilities.colorchat", desc = "Color you chat")

@Permission(name = "kalesutilities.colorsigns", desc = "Color signs in the world")
@Permission(name = "kalesutilities.editsign", desc = "Edit signs in the world")
public class Main extends JavaPlugin {
    public static Main Instance;

    public final Logger CONSOLE = getLogger();
    public FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        Main.Instance = this;

        CONSOLE.info("Loading config");

        config.addDefault("config.prefix", "&6&l[Kales Utilities]&r");
        config.addDefault("config.chatFormat", "{prefix}{player} > {message}");
        config.addDefault("config.about", "Kales Minecraft Server!");
        config.addDefault("config.rules", "\n1. No Hacking\n2. No Griefing\n3. Be Respectful\n4. No Profanity\n5. Just Don't Be Rude/Annoying.\n\nBreaking rules could result in a kick, ban, or mute");
        config.addDefault("config.staff", "&4&l[Owner] Kale_Ko\n&1&l[Admin] JMbuilder");
        config.addDefault("config.banned-words", List.of("anal", "anus", "arse", "ass", "ballsack", "balls", "bitch", "biatch", "blowjob", "bollock", "bollok", "boner", "boob", "bum", "butt", "buttplug", "clitoris", "cock", "coon", "cunt", "dick", "dildo", "dyke", "fag", "feck", "fellate", "fellatio", "felching", "fuck", "fucking", "fudgepacker", "flange", "goddamn", "hell", "homo", "jizz", "knobend", "labia", "muff", "nigger", "nigga", "penis", "piss", "poop", "pube", "pussy", "queer", "scrotum", "sex", "shit", "sh1t", "slut", "smegma", "spunk", "tit", "tosser", "turd", "twat", "vagina", "wank", "whore"));
        config.addDefault("messages.invalidCommand", "{command} is not a command");
        config.addDefault("messages.noperms", "You need the permission {permission} to run that command");
        config.addDefault("messages.noconsole", "You can't use that command from the console");
        config.addDefault("messages.playernotfound", "{player} can't be found");
        config.addDefault("messages.usage", "Usage: {usage}");
        config.addDefault("messages.joinMessage", "&e{player} &ehas joined the game!");
        config.addDefault("messages.quitMessage", "&e{player} &ehas left the game");
        config.addDefault("messages.help", "\n{commandList}");
        config.addDefault("messages.reload", "Config Reloaded");
        config.addDefault("messages.spawned", "You have been sent to spawn");
        config.addDefault("messages.spawnedplayer", "Successfully sent {player} to spawn");
        config.addDefault("messages.setspawn", "Successfully set the spawn");
        config.addDefault("messages.lastonline", "{player} was last seen {time}!");
        config.addDefault("messages.playeronline", "{player} is online right now!");
        config.addDefault("messages.setnickname", "Successfully set your nickname");
        config.addDefault("messages.setprefix", "Successfully set your prefix");
        config.addDefault("messages.setstatus", "Successfully set your status");
        config.addDefault("messages.sudocommand", "Successfully ran {command} as {player}");
        config.addDefault("messages.sudomessage", "Successfully made {player} say {message}");
        config.addDefault("messages.staffchat", "&l&d[Staffchat] &r{player} > {message}");

        config.options().copyDefaults(true);
        this.saveConfig();

        CONSOLE.info("Finished loading config");

        CONSOLE.info("Loading commands");

        this.getCommand("kalesutilities").setExecutor(new KalesUtilitiesCommand());
        this.getCommand("about").setExecutor(new AboutCommand());
        this.getCommand("rules").setExecutor(new RulesCommand());
        this.getCommand("staff").setExecutor(new StaffCommand());
        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        this.getCommand("seen").setExecutor(new SeenCommand());
        this.getCommand("nickname").setExecutor(new NicknameCommand());
        this.getCommand("prefix").setExecutor(new PrefixCommand());
        this.getCommand("status").setExecutor(new StatusCommand());
        this.getCommand("sudo").setExecutor(new SudoCommand());
        this.getCommand("staffchat").setExecutor(new StaffChatCommand());

        CONSOLE.info("Finished loading commands");

        CONSOLE.info("Loading listeners");

        getServer().getPluginManager().registerEvents(new ChatFilterListener(), this);
        getServer().getPluginManager().registerEvents(new ChatFormatListener(), this);
        getServer().getPluginManager().registerEvents(new WelcomeListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        getServer().getPluginManager().registerEvents(new SeenListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new SignEditorListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);

        CONSOLE.info("Finished loading listeners");

        CONSOLE.info("Finished loading");
    }

    @Override
    public void onDisable() {
        CONSOLE.info("Disabled");
    }
}