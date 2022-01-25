package com.kale_ko.kalesutilities.spigot;

import com.kale_ko.kalesutilities.shared.mysql.MySQLConfig;
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
import java.util.Arrays;
import java.util.logging.Logger;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main Instance;

    public final Logger Console = getLogger();

    public SpigotConfig config;
    public MySQLConfig players;
    public MySQLConfig seen;
    public SpigotConfig spawn;
    public SpigotConfig warps;
    public SpigotConfig kits;

    @Override
    public void onEnable() {
        Main.Instance = this;

        Console.info("Loading config..");

        config = SpigotConfig.load("config.yml");

        config.addDefault("config.prefix", "&6&l[Kales Utilities]&r");
        config.addDefault("config.chatFormat", "{prefix}{player} > {message}");
        config.addDefault("config.about", "Kales Minecraft Server!");
        config.addDefault("config.rules", "\n1. No Hacking\n2. No Griefing\n3. Be Respectful\n4. No Profanity\n5. Just Don't Be Rude/Annoying.\n\nBreaking rules could result in a kick, ban, or mute");
        config.addDefault("config.staff", "");
        config.addDefault("database.url", "localhost");
        config.addDefault("database.port", "3306");
        config.addDefault("database.database", "minecraft");
        config.addDefault("database.username", "root");
        config.addDefault("database.password", "");
        config.addDefault("database.tablePrefix", "kalesutilities_");
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

        Console.info("Loading data..");

        players = MySQLConfig.load(config.getString("database.url"), config.getInt("database.port"), config.getString("database.database"), config.getString("database.tablePrefix") + "players", config.getString("database.username"), config.getString("database.password"));
        seen = MySQLConfig.load(config.getString("database.url"), config.getInt("database.port"), config.getString("database.database"), config.getString("database.tablePrefix") + "seen", config.getString("database.username"), config.getString("database.password"));
        spawn = SpigotConfig.load("spawn.yml");
        warps = SpigotConfig.load("warps.yml");
        kits = SpigotConfig.load("kits.yml");

        Console.info("Finished loading data");

        Console.info("Loading permissions..");

        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.*", "Use everything", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("kalesutilities.commands.*", "kalesutilities.features.*"), Arrays.asList(true, true))));

        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.*", "Use all commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("kalesutilities.commands.info.*", "kalesutilities.commands.warps.*", "kalesutilities.commands.kits.*", "kalesutilities.commands.player.*", "kalesutilities.commands.moderation.*"), Arrays.asList(true, true, true, true, true))));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.features.*", "Use all features", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("kalesutilities.features.colorchat", "kalesutilities.features.colorsigns", "kalesutilities.features.editsigns"), Arrays.asList(true, true, true))));

        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.info.*", "Use info commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("kalesutilities.commands.info.help", "kalesutilities.commands.info.about", "kalesutilities.commands.info.rules", "kalesutilities.commands.info.staff", "kalesutilities.commands.info.list"), Arrays.asList(true, true, true, true, true))));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.warps.*", "Use warp commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("kalesutilities.commands.warps.spawn", "kalesutilities.commands.warps.setspawn", "kalesutilities.commands.warps.warp", "kalesutilities.commands.warps.warps", "kalesutilities.commands.warps.setwarp"), Arrays.asList(true, true, true, true, true))));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.kits.*", "Use kit commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("kalesutilities.commands.kits.kit", "kalesutilities.commands.kits.kits"), Arrays.asList(true, true))));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.player.*", "Use player commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("kalesutilities.commands.player.seen", "kalesutilities.commands.player.nickname", "kalesutilities.commands.player.prefix", "kalesutilities.commands.player.status"), Arrays.asList(true, true, true, true))));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.moderation.*", "Use moderation commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("kalesutilities.commands.moderation.kick", "kalesutilities.commands.moderation.ban", "kalesutilities.commands.moderation.mute"), Arrays.asList(true, true, true))));

        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.info.help", "Use /help", PermissionDefault.TRUE));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.info.about", "Use /about", PermissionDefault.TRUE));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.info.rules", "Use /rules", PermissionDefault.TRUE));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.info.staff", "Use /staff", PermissionDefault.TRUE));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.info.list", "Use /list", PermissionDefault.TRUE));

        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.warps.spawn", "Use /spawn", PermissionDefault.TRUE));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.warps.setspawn", "Use /setspawn", PermissionDefault.OP));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.warps.warp", "Use /warp", PermissionDefault.TRUE));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.warps.warps", "Use /warps", PermissionDefault.TRUE));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.warps.setwarp", "Use /setwarp", PermissionDefault.OP));

        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.kits.kit", "Use /kit", PermissionDefault.TRUE));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.kits.kits", "Use /kits", PermissionDefault.TRUE));

        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.player.seen", "Use /seen", PermissionDefault.TRUE));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.player.nickname", "Use /nickname", PermissionDefault.TRUE));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.player.prefix", "Use /prefix", PermissionDefault.OP));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.player.status", "Use /status", PermissionDefault.TRUE));

        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.moderation.kick", "Use /prefix", PermissionDefault.OP));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.moderation.ban", "Use /prefix", PermissionDefault.OP));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.commands.moderation.mute", "Use /prefix", PermissionDefault.OP));

        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.features.colorchat", "Color you chat", PermissionDefault.TRUE));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.features.colorsigns", "Color you sings", PermissionDefault.TRUE));
        this.getServer().getPluginManager().addPermission(new Permission("kalesutilities.features.editsigns", "Edit you sings", PermissionDefault.OP));

        Console.info("Finished loading permissions");

        Console.info("Loading commands..");

        ((CraftServer) this.getServer()).getCommandMap().register("kalesutilities", new KalesUtilitiesCommand("kalesutilities", "The main plugin command for Kales Utilities", Arrays.asList("ku", "ks"), "/kalesutilities [help, reload]", null));
        ((CraftServer) this.getServer()).getCommandMap().register("help", new HelpCommand("help", "See the help", Arrays.asList("h", "howto"), "/help {player (optional)}", "kalesutilities.commands.info.help"));
        ((CraftServer) this.getServer()).getCommandMap().register("about", new AboutCommand("about", "See the about", Arrays.asList("info", "ip", "discord", "apply", "feedback"), "/about {player (optional)}", "kalesutilities.commands.info.about"));
        ((CraftServer) this.getServer()).getCommandMap().register("rules", new RulesCommand("rules", "See the rules", Arrays.asList("ruleslist"), "/rules {player (optional)}", "kalesutilities.commands.info.rules"));
        ((CraftServer) this.getServer()).getCommandMap().register("staff", new StaffCommand("staff", "See the staff", Arrays.asList("stafflist"), "/staff {player (optional)}", "kalesutilities.commands.info.staff"));
        ((CraftServer) this.getServer()).getCommandMap().register("list", new ListCommand("list", "See the list", Arrays.asList("players", "playerlist", "listplayers"), "/list {player (optional)}", "kalesutilities.commands.info.list"));

        ((CraftServer) this.getServer()).getCommandMap().register("spawn", new SpawnCommand("spawn", "Go to the spawn", Arrays.asList(), "/spawn {player (optional)}", "kalesutilities.commands.warps.spawn"));
        ((CraftServer) this.getServer()).getCommandMap().register("setspawn", new SetSpawnCommand("setspawn", "Sets the spawn to your location", Arrays.asList(), "/setspawn", "kalesutilities.commands.warps.setspawn"));
        ((CraftServer) this.getServer()).getCommandMap().register("warp", new WarpCommand("warp", "Go to a warp", Arrays.asList(), "/warp {player (optional)} {warp}", "kalesutilities.commands.warps.warp"));
        ((CraftServer) this.getServer()).getCommandMap().register("warps", new WarpsCommand("warps", "List the warps", Arrays.asList("listwarps", "warpslist"), "/warps {player (optional)}", "kalesutilities.commands.warps.warps"));
        ((CraftServer) this.getServer()).getCommandMap().register("setwarp", new SetWarpCommand("setwarp", "Sets a warp at your location", Arrays.asList(), "/setwarp {warp}", "kalesutilities.commands.warps.setwarp"));

        ((CraftServer) this.getServer()).getCommandMap().register("kit", new KitCommand("kit", "Get a kit", Arrays.asList("getkit"), "/kit {player (optional)} {kit}", "kalesutilities.commands.kits.kit"));
        ((CraftServer) this.getServer()).getCommandMap().register("kits", new KitsCommand("kits", "List the kits", Arrays.asList("listkits", "kitslist"), "/kits {player (optional)}", "kalesutilities.commands.kits.kits"));

        ((CraftServer) this.getServer()).getCommandMap().register("seen", new SeenCommand("seen", "See when a player was last online", Arrays.asList("lastseen", "online", "lastonline"), "/seen {player}", "kalesutilities.commands.player.seen"));
        ((CraftServer) this.getServer()).getCommandMap().register("nickname", new NicknameCommand("nickname", "Sets you nickname", Arrays.asList("nick", "setnickname", "setnick"), "/nickname {player (optional)} {nickname}", "kalesutilities.commands.player.nickname"));
        ((CraftServer) this.getServer()).getCommandMap().register("prefix", new PrefixCommand("prefix", "Sets you prefix", Arrays.asList("setprefix"), "/prefix {player (optional)} {prefix}", "kalesutilities.commands.player.prefix"));
        ((CraftServer) this.getServer()).getCommandMap().register("status", new StatusCommand("status", "Sets you status", Arrays.asList("setstatus"), "/status {status}", "kalesutilities.commands.player.status"));

        ((CraftServer) this.getServer()).getCommandMap().register("gmc", new GamemodeCommand("gmc", "Sets you gamemode", Arrays.asList("gms", "gma", "gmsp"), "/gm(c, s, a, sp) {player (optional)}", "kalesutilities.commands.staff.gamemode"));
        ((CraftServer) this.getServer()).getCommandMap().register("staffchat", new StaffChatCommand("staffchat", "Send a message in the staffchat", Arrays.asList("sc"), "/staffchat {message}", "kalesutilities.commands.staff.staffchat"));
        ((CraftServer) this.getServer()).getCommandMap().register("commandspy", new CommandSpyCommand("commandspy", "Receive command spy notifications", Arrays.asList("cs"), "/commandspy", "kalesutilities.commands.staff.commandspy"));
        ((CraftServer) this.getServer()).getCommandMap().register("sudo", new SudoCommand("sudo", "Make a player say something or run a command", Arrays.asList(), "/sudo {player} {message/command}", "kalesutilities.commands.staff.sudo"));

        ((CraftServer) this.getServer()).getCommandMap().register("kick", new KickCommand("kick", "Kick a player from the server", Arrays.asList("boot"), "/kick {player} {message}", "kalesutilities.commands.moderation.kick"));
        ((CraftServer) this.getServer()).getCommandMap().register("ban", new BanCommand("ban", "Ban a player from the server", Arrays.asList("permban"), "/ban {player} {message}", "kalesutilities.commands.moderation.ban"));
        ((CraftServer) this.getServer()).getCommandMap().register("unban", new UnbanCommand("unban", "Unban a player from the server", Arrays.asList(), "/unban {player}", "kalesutilities.commands.moderation.ban"));
        ((CraftServer) this.getServer()).getCommandMap().register("mute", new MuteCommand("mute", "Mute a player on the server", Arrays.asList("permmute"), "/mute {player} {message}", "kalesutilities.commands.moderation.mute"));
        ((CraftServer) this.getServer()).getCommandMap().register("unmute", new UnmuteCommand("unmute", "Unmute a player on the server", Arrays.asList(), "/unmute {player}", "kalesutilities.commands.moderation.mute"));

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
        Console.info("Closing data..");

        this.players.close();
        this.seen.close();

        Console.info("Finished closing data");

        Console.info("Removing commands..");

        ((CraftServer) this.getServer()).getCommandMap().clearCommands();

        Console.info("Finished removing commands");

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