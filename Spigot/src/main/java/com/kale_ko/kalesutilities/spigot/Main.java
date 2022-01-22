package com.kale_ko.kalesutilities.spigot;

import com.kale_ko.kalesutilities.spigot.commands.info.AboutCommand;
import com.kale_ko.kalesutilities.spigot.commands.info.HelpCommand;
import com.kale_ko.kalesutilities.spigot.commands.info.KalesUtilitiesCommand;
import com.kale_ko.kalesutilities.spigot.commands.info.ListCommand;
import com.kale_ko.kalesutilities.spigot.commands.info.RulesCommand;
import com.kale_ko.kalesutilities.spigot.commands.info.StaffCommand;
import com.kale_ko.kalesutilities.spigot.commands.kits.KitCommand;
import com.kale_ko.kalesutilities.spigot.commands.kits.KitsCommand;
import com.kale_ko.kalesutilities.spigot.commands.moderation.BanCommand;
import com.kale_ko.kalesutilities.spigot.commands.moderation.KickCommand;
import com.kale_ko.kalesutilities.spigot.commands.moderation.MuteCommand;
import com.kale_ko.kalesutilities.spigot.commands.moderation.UnbanCommand;
import com.kale_ko.kalesutilities.spigot.commands.moderation.UnmuteCommand;
import com.kale_ko.kalesutilities.spigot.commands.player.NicknameCommand;
import com.kale_ko.kalesutilities.spigot.commands.player.PrefixCommand;
import com.kale_ko.kalesutilities.spigot.commands.player.SeenCommand;
import com.kale_ko.kalesutilities.spigot.commands.player.StatusCommand;
import com.kale_ko.kalesutilities.spigot.commands.staff.CommandSpyCommand;
import com.kale_ko.kalesutilities.spigot.commands.staff.GamemodeCommand;
import com.kale_ko.kalesutilities.spigot.commands.staff.StaffChatCommand;
import com.kale_ko.kalesutilities.spigot.commands.staff.SudoCommand;
import com.kale_ko.kalesutilities.spigot.commands.warps.SetSpawnCommand;
import com.kale_ko.kalesutilities.spigot.commands.warps.SetWarpCommand;
import com.kale_ko.kalesutilities.spigot.commands.warps.SpawnCommand;
import com.kale_ko.kalesutilities.spigot.commands.warps.WarpCommand;
import com.kale_ko.kalesutilities.spigot.commands.warps.WarpsCommand;
import com.kale_ko.kalesutilities.spigot.listeners.BannedJoinListener;
import com.kale_ko.kalesutilities.spigot.listeners.ChatFormatListener;
import com.kale_ko.kalesutilities.spigot.listeners.CommandSpyListener;
import com.kale_ko.kalesutilities.spigot.listeners.MuteListener;
import com.kale_ko.kalesutilities.spigot.listeners.PlayerListener;
import com.kale_ko.kalesutilities.spigot.listeners.PlayerMoveListener;
import com.kale_ko.kalesutilities.spigot.listeners.SeenListener;
import com.kale_ko.kalesutilities.spigot.listeners.SignEditorListener;
import com.kale_ko.kalesutilities.spigot.listeners.SpawnListener;
import com.kale_ko.kalesutilities.spigot.listeners.WelcomeListener;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.permission.ChildPermission;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LoadOrder;
import org.bukkit.plugin.java.annotation.plugin.Website;

@Plugin(name = "KalesUtilities", version = "5.0.0")
@Description("A custom plugin to run on KalesMC")
@Website("https://github.com/Kale-Ko/Kales-Utilities-V2")
@Author("Kale Ko")
@ApiVersion(ApiVersion.Target.v1_18)
@LoadOrder(PluginLoadOrder.POSTWORLD)

@Permission(name = "kalesutilities.*", desc = "Use all kalesutilities things", defaultValue = PermissionDefault.FALSE, children = {
        @ChildPermission(name = "kalesutilities.commands.*", inherit = true),
        @ChildPermission(name = "kalesutilities.features.*", inherit = true)
})

@Permission(name = "kalesutilities.commands.*", desc = "Use all kalesutilities commands", defaultValue = PermissionDefault.FALSE, children = {
        @ChildPermission(name = "kalesutilities.commands.info.*", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.warps.*", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.kits.*", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.player.*", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.staff.*", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.moderation.*", inherit = true)
})

@Permission(name = "kalesutilities.features.*", desc = "Use all kalesutilities features", defaultValue = PermissionDefault.FALSE, children = {
        @ChildPermission(name = "kalesutilities.features.colorchat", inherit = true),
        @ChildPermission(name = "kalesutilities.features.colorsigns", inherit = true),
        @ChildPermission(name = "kalesutilities.features.editsigns", inherit = true),
        @ChildPermission(name = "kalesutilities.features.commandspy", inherit = true)
})

@Permission(name = "kalesutilities.commands.info.*", desc = "Use all kalesutilities info commands", defaultValue = PermissionDefault.FALSE, children = {
        @ChildPermission(name = "kalesutilities.commands.info.help", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.info.list", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.info.about", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.info.rules", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.info.staff", inherit = true)
})

@Permission(name = "kalesutilities.commands.warps.*", desc = "Use all kalesutilities warps commands", defaultValue = PermissionDefault.FALSE, children = {
        @ChildPermission(name = "kalesutilities.commands.warps.spawn", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.warps.setspawn", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.warps.warp", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.warps.warps", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.warps.setwarp", inherit = true)
})

@Permission(name = "kalesutilities.commands.kits.*", desc = "Use all kalesutilities warps commands", defaultValue = PermissionDefault.FALSE, children = {
        @ChildPermission(name = "kalesutilities.commands.kits.kit", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.kits.kits", inherit = true)
})

@Permission(name = "kalesutilities.commands.player.*", desc = "Use all kalesutilities player commands", defaultValue = PermissionDefault.FALSE, children = {
        @ChildPermission(name = "kalesutilities.commands.player.seen", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.player.nickname", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.player.prefix", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.player.status", inherit = true)
})

@Permission(name = "kalesutilities.commands.staff.*", desc = "Use all kalesutilities staff commands", defaultValue = PermissionDefault.FALSE, children = {
        @ChildPermission(name = "kalesutilities.commands.staff.gamemode", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.staff.sudo", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.staff.staffchat", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.staff.commandspy", inherit = true)
})

@Permission(name = "kalesutilities.commands.moderation.*", desc = "Use all kalesutilities moderation commands", defaultValue = PermissionDefault.FALSE, children = {
        @ChildPermission(name = "kalesutilities.commands.moderation.kick", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.moderation.ban", inherit = true),
        @ChildPermission(name = "kalesutilities.commands.moderation.mute", inherit = true)
})

@Permission(name = "kalesutilities.commands.info.help", desc = "Use /help", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.commands.info.list", desc = "Use /list", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.commands.info.about", desc = "Use /about", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.commands.info.rules", desc = "Use /rules", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.commands.info.staff", desc = "Use /staff", defaultValue = PermissionDefault.TRUE)

@Permission(name = "kalesutilities.commands.warps.spawn", desc = "Use /spawn", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.commands.warps.setspawn", desc = "Use /setspawn", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.commands.warps.warp", desc = "Use /warp", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.commands.warps.warps", desc = "Use /warps", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.commands.warps.setwarp", desc = "Use /setwarp", defaultValue = PermissionDefault.OP)

@Permission(name = "kalesutilities.commands.kits.kit", desc = "Use /kit", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.commands.kits.kits", desc = "Use /kits", defaultValue = PermissionDefault.TRUE)

@Permission(name = "kalesutilities.commands.player.seen", desc = "Use /seen", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.commands.player.nickname", desc = "Use /nickname", defaultValue = PermissionDefault.TRUE)
@Permission(name = "kalesutilities.commands.player.prefix", desc = "Use /prefix", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.commands.player.status", desc = "Use /status", defaultValue = PermissionDefault.TRUE)

@Permission(name = "kalesutilities.commands.staff.gamemode", desc = "Use /gm", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.commands.staff.sudo", desc = "Use /sudo", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.commands.staff.staffchat", desc = "Use /staffchat", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.commands.staff.commandspy", desc = "Use /commandspy", defaultValue = PermissionDefault.OP)

@Permission(name = "kalesutilities.commands.moderation.kick", desc = "Use /kick", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.commands.moderation.ban", desc = "Use /ban and /unban", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.commands.moderation.mute", desc = "Use /mute and /unmute", defaultValue = PermissionDefault.OP)

@Permission(name = "kalesutilities.features.colorchat", desc = "Color you chat", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.features.colorsigns", desc = "Color signs in the world", defaultValue = PermissionDefault.OP)
@Permission(name = "kalesutilities.features.editsigns", desc = "Edit signs in the world", defaultValue = PermissionDefault.OP)

@Command(name = "kalesutilities", desc = "The main plugin command for Kales Utilities", aliases = { "ku", "ks" }, usage = "/kalesutilities [help, reload]")
@Command(name = "help", desc = "See the help", aliases = { "h", "howto" }, usage = "/help {player (optional)}", permission = "kalesutilities.commands.info.help")
@Command(name = "list", desc = "See the list", aliases = { "players", "playerlist", "listplayers" }, usage = "/list {player (optional)}", permission = "kalesutilities.commands.info.list")
@Command(name = "about", desc = "See the about", aliases = { "info" }, usage = "/about {player (optional)}", permission = "kalesutilities.commands.info.about")
@Command(name = "rules", desc = "See the rules", aliases = { "ruleslist" }, usage = "/rules {player (optional)}", permission = "kalesutilities.commands.info.rules")
@Command(name = "staff", desc = "See the staff", aliases = { "stafflist" }, usage = "/staff {player (optional)}", permission = "kalesutilities.commands.info.staff")
@Command(name = "spawn", desc = "Go to the spawn", aliases = { "hub", "lobby" }, usage = "/spawn {player (optional)}", permission = "kalesutilities.commands.warps.spawn")
@Command(name = "setspawn", desc = "Sets the spawn to your location", aliases = { "sethub", "setlobby" }, usage = "/setspawn", permission = "kalesutilities.commands.warps.setspawn")
@Command(name = "warp", desc = "Go to a warp", aliases = {}, usage = "/warp {player (optional)} {warp}", permission = "kalesutilities.commands.warps.warp")
@Command(name = "warps", desc = "List the warps", aliases = { "listwarps", "warpslist" }, usage = "/warps {player (optional)}", permission = "kalesutilities.commands.warps.warps")
@Command(name = "setwarp", desc = "Sets a warp at your location", aliases = {}, usage = "/setwarp {warp}", permission = "kalesutilities.commands.warps.setwarp")
@Command(name = "kit", desc = "Get a kit", aliases = { "getkit" }, usage = "/kit {player (optional)} {kit}", permission = "kalesutilities.commands.kits.kit")
@Command(name = "kits", desc = "List the kits", aliases = { "listkits", "kitslist" }, usage = "/kits {player (optional)}", permission = "kalesutilities.commands.kits.kits")
@Command(name = "seen", desc = "See when a player was last online", aliases = { "lastseen", "online", "lastonline" }, usage = "/seen {player}", permission = "kalesutilities.commands.player.seen")
@Command(name = "nickname", desc = "Sets you nickname", aliases = { "nick", "setnickname", "setnick" }, usage = "/nickname {player (optional)} {nickname}", permission = "kalesutilities.commands.player.nickname")
@Command(name = "prefix", desc = "Sets you prefix", aliases = { "setprefix" }, usage = "/prefix {player (optional)} {prefix}", permission = "kalesutilities.commands.player.prefix")
@Command(name = "status", desc = "Sets you status", aliases = { "setstatus" }, usage = "/status {status}", permission = "kalesutilities.commands.player.status")
@Command(name = "gmc", desc = "Sets you gamemode", aliases = { "gms", "gma", "gmsp" }, usage = "/gm(c, s, a, sp) {player (optional)}", permission = "kalesutilities.commands.staff.gamemode")
@Command(name = "sudo", desc = "Make a player say something or run a command", aliases = {}, usage = "/sudo {player} {message/command}", permission = "kalesutilities.commands.staff.sudo")
@Command(name = "staffchat", desc = "Send a message in the staffchat", aliases = { "sc" }, usage = "/staffchat {message}", permission = "kalesutilities.commands.staff.staffchat")
@Command(name = "commandspy", desc = "Receive command spy notifications", aliases = { "cs" }, usage = "/commandspy", permission = "kalesutilities.commands.staff.commandspy")
@Command(name = "kick", desc = "Kick a player from the server", aliases = {}, usage = "/kick {player} {message}", permission = "kalesutilities.commands.moderation.kick")
@Command(name = "ban", desc = "Ban a player from the server", aliases = {}, usage = "/ban {player} {message}", permission = "kalesutilities.commands.moderation.ban")
@Command(name = "unban", desc = "Unban a player from the server", aliases = {}, usage = "/unban {player}", permission = "kalesutilities.commands.moderation.ban")
@Command(name = "mute", desc = "Mute a player on the server", aliases = {}, usage = "/mute {player} {message}", permission = "kalesutilities.commands.moderation.mute")
@Command(name = "unmute", desc = "Unmute a player on the server", aliases = {}, usage = "/unmute {player}", permission = "kalesutilities.commands.moderation.mute")

public class Main extends JavaPlugin {
    public static Main Instance;

    public final Logger Console = getLogger();

    public Config config;
    public Config players;
    public Config seen;
    public Config spawn;
    public Config warps;
    public Config kits;

    @Override
    public void onEnable() {
        Main.Instance = this;

        Console.info("Loading config..");

        config = Config.load("config.yml");
        players = Config.load("players.yml");
        seen = Config.load("seen.yml");
        spawn = Config.load("spawn.yml");
        warps = Config.load("warps.yml");
        kits = Config.load("kits.yml");

        config.addDefault("config.prefix", "&6&l[Kales Utilities]&r");
        config.addDefault("config.chatFormat", "{prefix}{player} > {message}");
        config.addDefault("config.about", "Kales Minecraft Server!");
        config.addDefault("config.rules", "\n1. No Hacking\n2. No Griefing\n3. Be Respectful\n4. No Profanity\n5. Just Don't Be Rude/Annoying.\n\nBreaking rules could result in a kick, ban, or mute");
        config.addDefault("config.staff", "");
        config.addDefault("messages.invalidCommand", "{command} is not a command");
        config.addDefault("messages.noperms", "You need the permission {permission} to run that command");
        config.addDefault("messages.noconsole", "You can't use that command from the console");
        config.addDefault("messages.playernotfound", "{player} can't be found");
        config.addDefault("messages.usage", "Usage: {usage}");
        config.addDefault("messages.joinMessage", "&e{player} &ehas joined the game!");
        config.addDefault("messages.quitMessage", "&e{player} &ehas left the game");
        config.addDefault("messages.help", "\n{commandList}");
        config.addDefault("messages.list", "\n{playerList}");
        config.addDefault("messages.reload", "Config Reloaded");
        config.addDefault("messages.spawned", "You have been sent to spawn");
        config.addDefault("messages.spawnedplayer", "Successfully sent {player} to spawn");
        config.addDefault("messages.setspawn", "Successfully set the spawn");
        config.addDefault("messages.warps", "\n{warpList}");
        config.addDefault("messages.warped", "You have warped to {warp}");
        config.addDefault("messages.warpedplayer", "Successfully sent {player} to {warp}");
        config.addDefault("messages.setwarp", "Successfully set warp {warp}");
        config.addDefault("messages.kits", "\n{kitList}");
        config.addDefault("messages.kit", "Successfully received kit {kit}");
        config.addDefault("messages.lastonline", "{player} was last seen {time}!");
        config.addDefault("messages.playeronline", "{player} is online right now!");
        config.addDefault("messages.setnickname", "Successfully set your nickname");
        config.addDefault("messages.setprefix", "Successfully set your prefix");
        config.addDefault("messages.setstatus", "Successfully set your status");
        config.addDefault("messages.gamemode", "Successfully set your gamemode to {gamemode}");
        config.addDefault("messages.sudocommand", "Successfully ran {command} as {player}");
        config.addDefault("messages.sudomessage", "Successfully made {player} say {message}");
        config.addDefault("messages.staffchat", "&l&d[Staffchat] &r{player} &r> {message}");
        config.addDefault("messages.togglecommandspy", "Successfully turned commandspy {value}");
        config.addDefault("messages.commandspy", "&l&d[CommandSpy] &r{player} &rran {message}");
        config.addDefault("messages.kick", "{player} was kicked by {moderator} for {reason}");
        config.addDefault("messages.ban", "{player} was banned by {moderator} for {reason}");
        config.addDefault("messages.unban", "{player} was unbanned by {moderator}");
        config.addDefault("messages.bannedJoin", "{player} tried to join but is banned");
        config.addDefault("messages.mute", "{player} was muted by {moderator} for {reason}");
        config.addDefault("messages.unmute", "{player} was unmuted by {moderator}");
        config.addDefault("messages.mutedMessage", "{player} tried to say {message}&r but is muted");

        config.copyDefaults();

        Console.info("Finished loading config");

        Console.info("Loading commands..");

        this.getCommand("kalesutilities").setExecutor(new KalesUtilitiesCommand());
        this.getCommand("help").setExecutor(new HelpCommand());
        this.getCommand("list").setExecutor(new ListCommand());
        this.getCommand("about").setExecutor(new AboutCommand());
        this.getCommand("rules").setExecutor(new RulesCommand());
        this.getCommand("staff").setExecutor(new StaffCommand());

        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        this.getCommand("warp").setExecutor(new WarpCommand());
        this.getCommand("warps").setExecutor(new WarpsCommand());
        this.getCommand("setwarp").setExecutor(new SetWarpCommand());

        this.getCommand("kit").setExecutor(new KitCommand());
        this.getCommand("kits").setExecutor(new KitsCommand());

        this.getCommand("seen").setExecutor(new SeenCommand());
        this.getCommand("nickname").setExecutor(new NicknameCommand());
        this.getCommand("prefix").setExecutor(new PrefixCommand());
        this.getCommand("status").setExecutor(new StatusCommand());

        this.getCommand("gmc").setExecutor(new GamemodeCommand());
        this.getCommand("sudo").setExecutor(new SudoCommand());
        this.getCommand("staffchat").setExecutor(new StaffChatCommand());
        this.getCommand("commandspy").setExecutor(new CommandSpyCommand());

        this.getCommand("kick").setExecutor(new KickCommand());
        this.getCommand("ban").setExecutor(new BanCommand());
        this.getCommand("unban").setExecutor(new UnbanCommand());
        this.getCommand("mute").setExecutor(new MuteCommand());
        this.getCommand("unmute").setExecutor(new UnmuteCommand());

        Console.info("Finished loading commands");

        Console.info("Loading event listeners..");

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BannedJoinListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        getServer().getPluginManager().registerEvents(new WelcomeListener(), this);
        getServer().getPluginManager().registerEvents(new MuteListener(), this);
        getServer().getPluginManager().registerEvents(new ChatFormatListener(), this);
        getServer().getPluginManager().registerEvents(new SeenListener(), this);
        getServer().getPluginManager().registerEvents(new CommandSpyListener(), this);
        getServer().getPluginManager().registerEvents(new SignEditorListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);

        Console.info("Finished loading event listeners");

        Console.info("Updating player names..");

        for (Player player : getServer().getOnlinePlayers()) {
            Util.updatePlayerName(player);
        }

        Console.info("Finished updating player names");

        Console.info("Finished enabling");
    }

    @Override
    public void onDisable() {
        Console.info("Removing event listeners..");

        HandlerList.unregisterAll(Main.Instance);

        Console.info("Finished removing event listeners");

        Console.info("Reseting player names..");

        for (Player player : getServer().getOnlinePlayers()) {
            Util.resetPlayerName(player);
        }

        Console.info("Finished reseting player names");

        Console.info("Finished disabling");
    }

    public void reload() {
        Console.info("Reloading..");

        Console.info("Disabling..");

        this.onDisable();

        Console.info("Enabling..");

        this.onEnable();

        Console.info("Finished reloading");
    }
}