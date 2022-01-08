package com.kale_ko.kalesutilities;

import com.kale_ko.kalesutilities.commands.info.AboutCommand;
import com.kale_ko.kalesutilities.commands.info.KalesUtilitiesCommand;
import com.kale_ko.kalesutilities.commands.info.RulesCommand;
import com.kale_ko.kalesutilities.commands.info.StaffCommand;
import com.kale_ko.kalesutilities.commands.kits.KitCommand;
import com.kale_ko.kalesutilities.commands.moderation.BanCommand;
import com.kale_ko.kalesutilities.commands.moderation.KickCommand;
import com.kale_ko.kalesutilities.commands.moderation.MuteCommand;
import com.kale_ko.kalesutilities.commands.moderation.UnbanCommand;
import com.kale_ko.kalesutilities.commands.moderation.UnmuteCommand;
import com.kale_ko.kalesutilities.commands.player.NicknameCommand;
import com.kale_ko.kalesutilities.commands.player.PrefixCommand;
import com.kale_ko.kalesutilities.commands.player.SeenCommand;
import com.kale_ko.kalesutilities.commands.player.StatusCommand;
import com.kale_ko.kalesutilities.commands.staff.GamemodeCommand;
import com.kale_ko.kalesutilities.commands.staff.StaffChatCommand;
import com.kale_ko.kalesutilities.commands.staff.SudoCommand;
import com.kale_ko.kalesutilities.commands.warps.SetSpawnCommand;
import com.kale_ko.kalesutilities.commands.warps.SetWarpCommand;
import com.kale_ko.kalesutilities.commands.warps.SpawnCommand;
import com.kale_ko.kalesutilities.commands.warps.WarpCommand;
import com.kale_ko.kalesutilities.listeners.BannedJoinListener;
import com.kale_ko.kalesutilities.listeners.BedwarsListener;
import com.kale_ko.kalesutilities.listeners.ChatFilterListener;
import com.kale_ko.kalesutilities.listeners.ChatFormatListener;
import com.kale_ko.kalesutilities.listeners.CommandSpyListener;
import com.kale_ko.kalesutilities.listeners.MuteListener;
import com.kale_ko.kalesutilities.listeners.PlayerListener;
import com.kale_ko.kalesutilities.listeners.PlayerMoveListener;
import com.kale_ko.kalesutilities.listeners.SeenListener;
import com.kale_ko.kalesutilities.listeners.SignEditorListener;
import com.kale_ko.kalesutilities.listeners.SpawnListener;
import com.kale_ko.kalesutilities.listeners.WelcomeListener;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.permissions.PermissionDefault;
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

@Plugin(name = "KalesUtilities", version = "2.0.0")
@Description("A custom plugin to run on KalesMC")
@Author("Kale Ko")
@LogPrefix("Kales Utilities")
@ApiVersion(ApiVersion.Target.v1_18)
@LoadOrder(PluginLoadOrder.STARTUP)

@Command(name = "kalesutilities", desc = "The main plugin command for Kales Utilities", aliases = { "ks" }, usage = "/kalesutilities [help, reload]")
@Command(name = "about", desc = "See the about", aliases = { "info" }, usage = "/about")
@Command(name = "rules", desc = "See the rules", aliases = {}, usage = "/rules")
@Command(name = "staff", desc = "See the staff", aliases = {}, usage = "/staff")
@Command(name = "spawn", desc = "Go to the spawn", aliases = { "hub", "lobby" }, usage = "/spawn {player (optional)}")
@Command(name = "setspawn", desc = "Sets the spawn to your location", aliases = { "sethub", "setlobby" }, usage = "/setspawn")
@Command(name = "warp", desc = "Go to a warp", aliases = {}, usage = "/warp {warp}")
@Command(name = "setwarp", desc = "Sets a warp at your location", aliases = {}, usage = "/setwarp {warp}")
@Command(name = "kit", desc = "Get a kit", aliases = {}, usage = "/kit {kit}")
@Command(name = "seen", desc = "See when a player was last online", aliases = { "lastseen" }, usage = "/seen {player}")
@Command(name = "nickname", desc = "Sets you nickname", aliases = { "nick" }, usage = "/nickname {nickname}")
@Command(name = "prefix", desc = "Sets you prefix", aliases = {}, usage = "/prefix {prefix}")
@Command(name = "status", desc = "Sets you status", aliases = { "afk" }, usage = "/status {status}")
@Command(name = "gmc", desc = "Sets you gamemode", aliases = { "gms", "gma", "gmsp" }, usage = "/gm(c, s, a, sp)")
@Command(name = "sudo", desc = "Make a player say something or run a command", aliases = {}, usage = "/sudo {player} {message/command}")
@Command(name = "staffchat", desc = "Send a message in the staffchat", aliases = { "sc" }, usage = "/staffchat {message}")
@Command(name = "kick", desc = "Kick a player from the server", aliases = {}, usage = "/kick {player} {message}")
@Command(name = "ban", desc = "Ban a player from the server", aliases = {}, usage = "/ban {player} {message}")
@Command(name = "unban", desc = "Unban a player from the server", aliases = {}, usage = "/unban {player}")
@Command(name = "mute", desc = "Mute a player on the server", aliases = {}, usage = "/mute {player} {message}")
@Command(name = "unmute", desc = "Unmute a player on the server", aliases = {}, usage = "/unmute {player}")

@Permission(name = "kalesutilities.about", desc = "Use /about", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.rules", desc = "Use /rules", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.staff", desc = "Use /staff", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.spawn", desc = "Use /spawn", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.setspawn", desc = "Use /setspawn", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.warps", desc = "Use /warps", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.warp", desc = "Use /warp", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.setwarp", desc = "Use /setwarp", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.kit", desc = "Use /kit", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.seen", desc = "Use /seen", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.setnickname", desc = "Use /nickname", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.setprefix", desc = "Use /prefix", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.setstatus", desc = "Use /status", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.gamemode", desc = "Use /gm", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.sudo", desc = "Use /sudo", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.staffchat", desc = "Use /staffchat", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.commandspy", desc = "Receive command spy notifications", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.kick", desc = "Use /kick", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.ban", desc = "Use /ban and /unban", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.mute", desc = "Use /mute and /unmute", defaultValue = PermissionDefault.OP)

@Permission(name = "kalesutilities.colorchat", desc = "Color you chat", defaultValue = PermissionDefault.OP)

@Permission(name = "kalesutilities.colorsigns", desc = "Color signs in the world", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.editsign", desc = "Edit signs in the world", defaultValue = PermissionDefault.OP)

public class Main extends JavaPlugin {
    public static Main Instance;
    public final Logger Console = getLogger();

    public Config config;
    public Config players;
    public Config spawn;
    public Config warps;
    public Config kits;

    @Override
    public void onEnable() {
        Main.Instance = this;
        config = Config.load("config.yml");
        players = Config.load("players.yml");
        spawn = Config.load("spawn.yml");
        warps = Config.load("warps.yml");
        kits = Config.load("kits.yml");

        Console.info("Loading config");

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
        config.addDefault("messages.warps", "\n{warpList}");
        config.addDefault("messages.warped", "You have warped to {warp}");
        config.addDefault("messages.setwarp", "Successfully set warp {warp}");
        config.addDefault("message.kit", "Successfully received kit {kit}");
        config.addDefault("messages.lastonline", "{player} was last seen {time}!");
        config.addDefault("messages.playeronline", "{player} is online right now!");
        config.addDefault("messages.setnickname", "Successfully set your nickname");
        config.addDefault("messages.setprefix", "Successfully set your prefix");
        config.addDefault("messages.setstatus", "Successfully set your status");
        config.addDefault("messages.gamemode", "Successfully set your gamemode to {gamemode}");
        config.addDefault("messages.sudocommand", "Successfully ran {command} as {player}");
        config.addDefault("messages.sudomessage", "Successfully made {player} say {message}");
        config.addDefault("messages.staffchat", "&l&d[Staffchat] &r{player} &r> {message}");
        config.addDefault("messages.commandspy", "&l&d[CommandSpy] &r{player} &rran {message}");
        config.addDefault("messages.kick", "{player} was kicked by {moderator} for {reason}");
        config.addDefault("messages.ban", "{player} was banned by {moderator} for {reason}");
        config.addDefault("messages.unban", "{player} was unbanned by {moderator}");
        config.addDefault("messages.bannedJoin", "{player} tried to join but is banned");
        config.addDefault("messages.mute", "{player} was muted by {moderator} for {reason}");
        config.addDefault("messages.unmute", "{player} was unmuted by {moderator}");
        config.addDefault("messages.mutedMessage", "{player} tried to say {message}&r but is muted");

        config.copyDefaults();
        config.save();

        Console.info("Finished loading config");

        Console.info("Loading commands");

        this.getCommand("kalesutilities").setExecutor(new KalesUtilitiesCommand());
        this.getCommand("about").setExecutor(new AboutCommand());
        this.getCommand("rules").setExecutor(new RulesCommand());
        this.getCommand("staff").setExecutor(new StaffCommand());
        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        this.getCommand("warp").setExecutor(new WarpCommand());
        this.getCommand("setwarp").setExecutor(new SetWarpCommand());
        this.getCommand("kit").setExecutor(new KitCommand());
        this.getCommand("seen").setExecutor(new SeenCommand());
        this.getCommand("nickname").setExecutor(new NicknameCommand());
        this.getCommand("prefix").setExecutor(new PrefixCommand());
        this.getCommand("status").setExecutor(new StatusCommand());
        this.getCommand("gmc").setExecutor(new GamemodeCommand());
        this.getCommand("sudo").setExecutor(new SudoCommand());
        this.getCommand("staffchat").setExecutor(new StaffChatCommand());
        this.getCommand("kick").setExecutor(new KickCommand());
        this.getCommand("ban").setExecutor(new BanCommand());
        this.getCommand("unban").setExecutor(new UnbanCommand());
        this.getCommand("mute").setExecutor(new MuteCommand());
        this.getCommand("unmute").setExecutor(new UnmuteCommand());

        Console.info("Finished loading commands");

        Console.info("Loading listeners");

        getServer().getPluginManager().registerEvents(new BannedJoinListener(), this);
        getServer().getPluginManager().registerEvents(new MuteListener(), this);
        getServer().getPluginManager().registerEvents(new ChatFilterListener(), this);
        getServer().getPluginManager().registerEvents(new ChatFormatListener(), this);
        getServer().getPluginManager().registerEvents(new BedwarsListener(), this);
        getServer().getPluginManager().registerEvents(new WelcomeListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        getServer().getPluginManager().registerEvents(new CommandSpyListener(), this);
        getServer().getPluginManager().registerEvents(new SeenListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new SignEditorListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);

        Console.info("Finished loading listeners");

        Console.info("Finished loading");
    }

    @Override
    public void onDisable() {
        Console.info("Disabled");
    }
}