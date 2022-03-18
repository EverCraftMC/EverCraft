package com.kale_ko.evercraft.spigot;

import com.kale_ko.evercraft.shared.Plugin;
import com.kale_ko.evercraft.shared.economy.EconomyManager;
import com.kale_ko.evercraft.shared.mysql.MySQLConfig;
import com.kale_ko.evercraft.spigot.commands.economy.BalanceCommand;
import com.kale_ko.evercraft.spigot.commands.economy.BalanceTopCommand;
import com.kale_ko.evercraft.spigot.commands.economy.EconomyCommand;
import com.kale_ko.evercraft.spigot.commands.info.AboutCommand;
import com.kale_ko.evercraft.spigot.commands.info.HelpCommand;
import com.kale_ko.evercraft.spigot.commands.info.EverCraftCommand;
import com.kale_ko.evercraft.spigot.commands.info.ListCommand;
import com.kale_ko.evercraft.spigot.commands.info.RulesCommand;
import com.kale_ko.evercraft.spigot.commands.info.StaffCommand;
import com.kale_ko.evercraft.spigot.commands.kits.KitCommand;
import com.kale_ko.evercraft.spigot.commands.kits.KitsCommand;
import com.kale_ko.evercraft.spigot.commands.moderation.BanCommand;
import com.kale_ko.evercraft.spigot.commands.moderation.KickCommand;
import com.kale_ko.evercraft.spigot.commands.moderation.MuteCommand;
import com.kale_ko.evercraft.spigot.commands.moderation.UnbanCommand;
import com.kale_ko.evercraft.spigot.commands.moderation.UnmuteCommand;
import com.kale_ko.evercraft.spigot.commands.player.NicknameCommand;
import com.kale_ko.evercraft.spigot.commands.player.PvPCommand;
import com.kale_ko.evercraft.spigot.commands.player.SeenCommand;
import com.kale_ko.evercraft.spigot.commands.player.StatusCommand;
import com.kale_ko.evercraft.spigot.commands.staff.CommandSpyCommand;
import com.kale_ko.evercraft.spigot.commands.staff.EnderChestSeeCommand;
import com.kale_ko.evercraft.spigot.commands.staff.GamemodeCommand;
import com.kale_ko.evercraft.spigot.commands.staff.InventorySeeCommand;
import com.kale_ko.evercraft.spigot.commands.staff.StaffChatCommand;
import com.kale_ko.evercraft.spigot.commands.staff.SudoCommand;
import com.kale_ko.evercraft.spigot.commands.warps.SetSpawnCommand;
import com.kale_ko.evercraft.spigot.commands.warps.SetWarpCommand;
import com.kale_ko.evercraft.spigot.commands.warps.SpawnCommand;
import com.kale_ko.evercraft.spigot.commands.warps.WarpCommand;
import com.kale_ko.evercraft.spigot.commands.warps.WarpsCommand;
import com.kale_ko.evercraft.spigot.listeners.BannedJoinListener;
import com.kale_ko.evercraft.spigot.listeners.ChatFormatListener;
import com.kale_ko.evercraft.spigot.listeners.ChatListener;
import com.kale_ko.evercraft.spigot.listeners.CommandSpyListener;
import com.kale_ko.evercraft.spigot.listeners.GlobalMessageListener;
import com.kale_ko.evercraft.spigot.listeners.MentionListener;
import com.kale_ko.evercraft.spigot.listeners.MuteListener;
import com.kale_ko.evercraft.spigot.listeners.PlayerListener;
import com.kale_ko.evercraft.spigot.listeners.PlayerMoveListener;
import com.kale_ko.evercraft.spigot.listeners.PvPListener;
import com.kale_ko.evercraft.spigot.listeners.SeenListener;
import com.kale_ko.evercraft.spigot.listeners.SignEditorListener;
import com.kale_ko.evercraft.spigot.listeners.SpawnListener;
import com.kale_ko.evercraft.spigot.listeners.WelcomeListener;
import com.kale_ko.evercraft.spigot.scoreboard.ScoreBoard;
import java.util.Arrays;
import java.util.logging.Logger;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class SpigotPlugin extends JavaPlugin implements Plugin {
    public static SpigotPlugin Instance;

    public final Logger Console = getLogger();

    public SpigotConfig config;
    public MySQLConfig players;
    public SpigotConfig spawn;
    public SpigotConfig warps;
    public SpigotConfig kits;

    public LuckPerms luckperms;

    public EconomyManager eco;

    private ScoreBoard scoreboard;

    @Override
    public void onEnable() {
        SpigotPlugin.Instance = this;

        Console.info("Loading config..");

        config = SpigotConfig.load("config.yml");

        config.addDefault("config.serverName", "");
        config.addDefault("config.proxyHost", "localhost");
        config.addDefault("config.proxyPort", 25565);
        config.addDefault("config.requestTimeout", 2);
        config.addDefault("config.prefix", "&3&l[EverCraft]&r");
        config.addDefault("config.chatFormat", "{prefix}{player} > {message}");
        config.addDefault("config.about", "Kales Minecraft Server!");
        config.addDefault("config.rules", "\n1. No Hacking\n2. No Griefing\n3. Be Respectful\n4. No Profanity\n5. Just Don't Be Rude/Annoying.\n\nBreaking rules could result in a kick, ban, or mute");
        config.addDefault("config.staff", "");
        config.addDefault("config.tablistHeader", "&7--------------------\n&3&lEverCraft");
        config.addDefault("config.tablistFooter", "&7--------------------");
        config.addDefault("config.scoreboardTitle", "&3&lEverCraft  ");
        config.addDefault("config.scoreboardLines", Arrays.asList("&7----------------", "&7&lUsername", "&f%username%", "&7&lHealth", "&f%health%", "&7&lPing", "&f%ping% ms", "&7&lOnline", "&f%onlineproxyplayers% / %maxproxyplayers%", "&7&lThis Server", "&f%onlineplayers% / %maxplayers%"));
        config.addDefault("config.clearOnWarp", false);
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
        config.addDefault("messages.joinMessage", "");
        config.addDefault("messages.quitMessage", "");
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
        config.addDefault("messages.togglepvp", "Successfully turned pvp {value}");
        config.addDefault("messages.lastonline", "{player} was last seen {time}!");
        config.addDefault("messages.playeronline", "{player} is online right now!");
        config.addDefault("messages.setnickname", "Successfully set your nickname");
        config.addDefault("messages.setprefix", "Successfully set your prefix");
        config.addDefault("messages.setstatus", "Successfully set your status");
        config.addDefault("messages.gamemode", "Successfully set your gamemode to {gamemode}");
        config.addDefault("messages.sudocommand", "Successfully ran {command} as {player}");
        config.addDefault("messages.sudomessage", "Successfully made {player} say {message}");
        config.addDefault("messages.staffchat", "&d&l[Staffchat] &r{player} &r> {message}");
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
        spawn = SpigotConfig.load("spawn.yml");
        warps = SpigotConfig.load("warps.yml");
        kits = SpigotConfig.load("kits.yml");

        Console.info("Finished loading data");

        Console.info("Loading permissions..");

        getServer().getPluginManager().addPermission(new Permission("evercraft.*", "Use everything", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("evercraft.commands.*", "evercraft.features.*"), Arrays.asList(true, true))));

        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.*", "Use all commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("evercraft.commands.info.*", "evercraft.commands.warps.*", "evercraft.commands.kits.*", "evercraft.commands.player.*", "evercraft.commands.economy.*", "evercraft.commands.staff.*", "evercraft.commands.moderation.*"), Arrays.asList(true, true, true, true, true, true, true))));
        getServer().getPluginManager().addPermission(new Permission("evercraft.features.*", "Use all features", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("evercraft.features.colorchat", "evercraft.features.colorsigns", "evercraft.features.editsigns"), Arrays.asList(true, true, true))));

        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.info.*", "Use info commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("evercraft.commands.info.evercraft", "evercraft.commands.info.help", "evercraft.commands.info.about", "evercraft.commands.info.rules", "evercraft.commands.info.staff", "evercraft.commands.info.list"), Arrays.asList(true, true, true, true, true, true))));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.warps.*", "Use warp commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("evercraft.commands.warps.spawn", "evercraft.commands.warps.setspawn", "evercraft.commands.warps.warp", "evercraft.commands.warps.warps", "evercraft.commands.warps.setwarp"), Arrays.asList(true, true, true, true, true))));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.kits.*", "Use kit commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("evercraft.commands.kits.kit", "evercraft.commands.kits.kits"), Arrays.asList(true, true))));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.player.*", "Use player commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("evercraft.commands.player.seen", "evercraft.commands.player.nickname", "evercraft.commands.player.status"), Arrays.asList(true, true, true))));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.economy.*", "Use economy commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("evercraft.commands.economy.balance", "evercraft.commands.economy.balancetop", "evercraft.commands.economy.economy"), Arrays.asList(true, true, true))));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.staff.*", "Use staff commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("evercraft.commands.staff.gamemode", "evercraft.commands.staff.staffchat", "evercraft.commands.staff.commandspy", "evercraft.commands.staff.sudo"), Arrays.asList(true, true, true, true))));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.moderation.*", "Use moderation commands", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("evercraft.commands.moderation.kick", "evercraft.commands.moderation.ban", "evercraft.commands.moderation.mute"), Arrays.asList(true, true, true))));

        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.info.evercraft", "Use /evercraft", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.info.help", "Use /help", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.info.about", "Use /about", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.info.rules", "Use /rules", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.info.staff", "Use /staff", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.info.list", "Use /list", PermissionDefault.TRUE));

        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.warps.spawn", "Use /spawn", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.warps.setspawn", "Use /setspawn", PermissionDefault.OP));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.warps.warp", "Use /warp", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.warps.warps", "Use /warps", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.warps.setwarp", "Use /setwarp", PermissionDefault.OP));

        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.kits.kit", "Use /kit", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.kits.kits", "Use /kits", PermissionDefault.TRUE));

        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.player.seen", "Use /seen", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.player.nickname", "Use /nickname", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.player.status", "Use /status", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.player.pvp", "Use /pvp", PermissionDefault.TRUE));

        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.economy.balance", "Use /balance", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.economy.balancetop", "Use /balancetop", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.economy.economy", "Use /economy", PermissionDefault.OP));

        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.staff.gamemode.*", "Use /gamemode", PermissionDefault.FALSE, Util.mapFromLists(Arrays.asList("evercraft.commands.staff.gamemode.use", "evercraft.commands.staff.gamemode.creative", "evercraft.commands.staff.gamemode.survival", "evercraft.commands.staff.gamemode.adventure", "evercraft.commands.staff.gamemode.spectator"), Arrays.asList(true, true, true, true))));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.staff.gamemode.use", "Use /gamemode", PermissionDefault.OP));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.staff.gamemode.creative", "Use /gmc", PermissionDefault.OP));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.staff.gamemode.survival", "Use /gms", PermissionDefault.OP));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.staff.gamemode.adventure", "Use /gma", PermissionDefault.OP));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.staff.gamemode.spectator", "Use /gmsp", PermissionDefault.OP));

        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.staff.inventorysee", "Use /inventorysee", PermissionDefault.OP));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.staff.staffchat", "Use /staffchat", PermissionDefault.OP));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.staff.commandspy", "Use /commandspy", PermissionDefault.OP));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.staff.sudo", "Use /sudo", PermissionDefault.OP));

        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.moderation.kick", "Use /prefix", PermissionDefault.OP));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.moderation.ban", "Use /prefix", PermissionDefault.OP));
        getServer().getPluginManager().addPermission(new Permission("evercraft.commands.moderation.mute", "Use /prefix", PermissionDefault.OP));

        getServer().getPluginManager().addPermission(new Permission("evercraft.features.colorchat", "Color you chat", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.features.colorsigns", "Color you sings", PermissionDefault.TRUE));
        getServer().getPluginManager().addPermission(new Permission("evercraft.features.editsigns", "Edit you sings", PermissionDefault.OP));

        Console.info("Finished loading permissions");

        Console.info("Loading commands..");

        ((CraftServer) getServer()).getCommandMap().register(getName(), new EverCraftCommand("evercraft", "The main plugin command for EverCraft", Arrays.asList("ku", "ks"), "/evercraft [help, reload]", "evercraft.commands.info.evercraft"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new HelpCommand("help", "See the help", Arrays.asList("h", "howto"), "/help {player (optional)}", "evercraft.commands.info.help"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new AboutCommand("about", "See the about", Arrays.asList("info", "ip", "discord", "apply", "feedback"), "/about {player (optional)}", "evercraft.commands.info.about"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new RulesCommand("rules", "See the rules", Arrays.asList("ruleslist"), "/rules {player (optional)}", "evercraft.commands.info.rules"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new StaffCommand("staff", "See the staff", Arrays.asList("stafflist"), "/staff {player (optional)}", "evercraft.commands.info.staff"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new ListCommand("list", "See the list", Arrays.asList("players", "playerlist", "listplayers"), "/list {player (optional)}", "evercraft.commands.info.list"));

        ((CraftServer) getServer()).getCommandMap().register(getName(), new SpawnCommand("spawn", "Go to the spawn", Arrays.asList(), "/spawn {player (optional)}", "evercraft.commands.warps.spawn"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new SetSpawnCommand("setspawn", "Sets the spawn to your location", Arrays.asList(), "/setspawn", "evercraft.commands.warps.setspawn"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new WarpCommand("warp", "Go to a warp", Arrays.asList(), "/warp {player (optional)} {warp}", "evercraft.commands.warps.warp"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new WarpsCommand("warps", "List the warps", Arrays.asList("listwarps", "warpslist"), "/warps {player (optional)}", "evercraft.commands.warps.warps"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new SetWarpCommand("setwarp", "Sets a warp at your location", Arrays.asList(), "/setwarp {warp}", "evercraft.commands.warps.setwarp"));

        ((CraftServer) getServer()).getCommandMap().register(getName(), new KitCommand("kit", "Get a kit", Arrays.asList("getkit"), "/kit {player (optional)} {kit}", "evercraft.commands.kits.kit"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new KitsCommand("kits", "List the kits", Arrays.asList("listkits", "kitslist"), "/kits {player (optional)}", "evercraft.commands.kits.kits"));

        ((CraftServer) getServer()).getCommandMap().register(getName(), new SeenCommand("seen", "See when a player was last online", Arrays.asList("lastseen", "online", "lastonline"), "/seen {player}", "evercraft.commands.player.seen"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new NicknameCommand("nickname", "Sets you nickname", Arrays.asList("nick", "setnickname", "setnick"), "/nickname {player (optional)} {nickname}", "evercraft.commands.player.nickname"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new StatusCommand("status", "Sets you status", Arrays.asList("setstatus"), "/status {status}", "evercraft.commands.player.status"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new PvPCommand("pvp", "Sets you pvp status", Arrays.asList("setpvp", "togglepvp"), "/pvp", "evercraft.commands.player.pvp"));

        ((CraftServer) getServer()).getCommandMap().register(getName(), new BalanceCommand("balance", "Check your balance", Arrays.asList("bal"), "/balance", "evercraft.commands.economy.balance"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new BalanceTopCommand("balancetop", "Check the top balances", Arrays.asList("baltop"), "/balancetop", "evercraft.commands.economy.balancetop"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new EconomyCommand("economy", "Manage the economy", Arrays.asList("eco"), "/economy", "evercraft.commands.economy.economy"));

        ((CraftServer) getServer()).getCommandMap().register(getName(), new GamemodeCommand("gamemode", "Change your gamemode", Arrays.asList("gmc", "gms", "gma", "gmsp"), "/gamemode {gamemode} {player (optional)}", "evercraft.commands.staff.gamemode.use"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new InventorySeeCommand("inventorysee", "View someones inventory", Arrays.asList("invsee"), "/inventorysee {player}", "evercraft.commands.staff.inventorysee"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new EnderChestSeeCommand("enderchestsee", "View someones ender chest", Arrays.asList("endersee"), "/enderchestsee {player}", "evercraft.commands.staff.inventorysee"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new StaffChatCommand("staffchat", "Send a message in the staffchat", Arrays.asList("sc"), "/staffchat {message}", "evercraft.commands.staff.staffchat"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new CommandSpyCommand("commandspy", "Receive command spy notifications", Arrays.asList("cs"), "/commandspy", "evercraft.commands.staff.commandspy"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new SudoCommand("sudo", "Make a player say something or run a command", Arrays.asList(), "/sudo {player} {message/command}", "evercraft.commands.staff.sudo"));

        ((CraftServer) getServer()).getCommandMap().register(getName(), new KickCommand("kick", "Kick a player from the server", Arrays.asList("boot"), "/kick {player} {message}", "evercraft.commands.moderation.kick"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new BanCommand("ban", "Ban a player from the server", Arrays.asList("permban"), "/ban {player} {message}", "evercraft.commands.moderation.ban"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new UnbanCommand("unban", "Unban a player from the server", Arrays.asList(), "/unban {player}", "evercraft.commands.moderation.ban"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new MuteCommand("mute", "Mute a player on the server", Arrays.asList("permmute"), "/mute {player} {message}", "evercraft.commands.moderation.mute"));
        ((CraftServer) getServer()).getCommandMap().register(getName(), new UnmuteCommand("unmute", "Unmute a player on the server", Arrays.asList(), "/unmute {player}", "evercraft.commands.moderation.mute"));

        Console.info("Finished loading commands");

        Console.info("Loading event listeners..");

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new GlobalMessageListener());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BannedJoinListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        getServer().getPluginManager().registerEvents(new WelcomeListener(), this);
        getServer().getPluginManager().registerEvents(new MuteListener(), this);
        getServer().getPluginManager().registerEvents(new ChatFormatListener(), this);
        getServer().getPluginManager().registerEvents(new MentionListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new SeenListener(), this);
        getServer().getPluginManager().registerEvents(new CommandSpyListener(), this);
        getServer().getPluginManager().registerEvents(new PvPListener(), this);
        getServer().getPluginManager().registerEvents(new SignEditorListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);

        Console.info("Finished loading event listeners");

        Console.info("Loading scoreboard..");

        scoreboard = new ScoreBoard();

        Console.info("Finished loading scoreboard..");

        Console.info("Loading luckperms integration..");

        luckperms = LuckPermsProvider.get();

        Console.info("Finished loading luckperms integration..");

        Console.info("Loading economy..");

        eco = new EconomyManager(players);

        Console.info("Finished loading economy..");

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

        players.close();

        Console.info("Finished closing data");

        Console.info("Removing permissions..");

        for (Permission permission : getServer().getPluginManager().getPermissions()) {
            if (permission.getName().startsWith("evercraft.")) {
                getServer().getPluginManager().removePermission(permission);
            }
        }

        Console.info("Finished removing permissions");

        Console.info("Removing commands..");

        for (org.bukkit.command.Command command : ((CraftServer) getServer()).getCommandMap().getCommands()) {
            if (command.getLabel().startsWith(getName())) {
                command.unregister(((CraftServer) getServer()).getCommandMap());
            }
        }

        Console.info("Finished removing commands");

        Console.info("Removing event listeners..");

        getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        getServer().getMessenger().unregisterIncomingPluginChannel(this);

        HandlerList.unregisterAll(this);

        Console.info("Finished removing event listeners");

        Console.info("Removing scoreboard..");

        scoreboard.close();

        Console.info("Finished removing scoreboard..");

        Console.info("Reseting player names..");

        for (Player player : getServer().getOnlinePlayers()) {
            Util.resetPlayerName(player);
        }

        Console.info("Finished reseting player names");

        Console.info("Finished disabling");
    }

    @Override
    public void reload() {
        Console.info("Reloading..");

        Console.info("Disabling..");

        onDisable();

        Console.info("Enabling..");

        onEnable();

        Console.info("Finished reloading");
    }
}