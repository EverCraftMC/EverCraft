package io.github.evercraftmc.evercraft.spigot;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.evercraftmc.evercraft.shared.Plugin;
import io.github.evercraftmc.evercraft.shared.PluginData;
import io.github.evercraftmc.evercraft.shared.PluginManager;
import io.github.evercraftmc.evercraft.shared.config.FileConfig;
import io.github.evercraftmc.evercraft.shared.config.MySQLConfig;
import io.github.evercraftmc.evercraft.shared.economy.Economy;
import io.github.evercraftmc.evercraft.shared.util.Closable;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.commands.games.GameCommand;
import io.github.evercraftmc.evercraft.spigot.commands.games.JoinCommand;
import io.github.evercraftmc.evercraft.spigot.commands.games.LeaveCommand;
import io.github.evercraftmc.evercraft.spigot.commands.kit.DelKitCommand;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;
import io.github.evercraftmc.evercraft.spigot.commands.kit.SetKitCommand;
import io.github.evercraftmc.evercraft.spigot.commands.player.BungeeCommandCommand;
import io.github.evercraftmc.evercraft.spigot.commands.player.ChestProtectionCommand;
import io.github.evercraftmc.evercraft.spigot.commands.player.PassiveCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.DebugCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.EnderSeeCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.InviSeeCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.ReloadCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.gamemode.AdventureCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.gamemode.CreativeCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.gamemode.GameModeCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.gamemode.SpectatorCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.gamemode.SurvivalCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.DelWarpCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.HomeCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.SetWarpCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.SpawnCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.WarpCommand;
import io.github.evercraftmc.evercraft.spigot.games.Game;
import io.github.evercraftmc.evercraft.spigot.games.pvp.DodgeBowGame;
import io.github.evercraftmc.evercraft.spigot.games.pvp.HideAndSeekGame;
import io.github.evercraftmc.evercraft.spigot.games.pvp.KittedGame;
import io.github.evercraftmc.evercraft.spigot.games.race.BoatRaceGame;
import io.github.evercraftmc.evercraft.spigot.games.race.MazeGame;
import io.github.evercraftmc.evercraft.spigot.listeners.ChestProtectionListener;
import io.github.evercraftmc.evercraft.spigot.listeners.CreativeItemListener;
import io.github.evercraftmc.evercraft.spigot.listeners.JoinListener;
import io.github.evercraftmc.evercraft.spigot.listeners.MessageListener;
import io.github.evercraftmc.evercraft.spigot.listeners.PvPListener;
import io.github.evercraftmc.evercraft.spigot.listeners.ServerIdleListener;
import io.github.evercraftmc.evercraft.spigot.listeners.SpigotListener;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.spigot.util.player.SpigotPlayerResolver;

public class SpigotMain extends JavaPlugin implements Plugin {
    private static SpigotMain Instance;

    private FileConfig<SpigotConfig> config;
    private FileConfig<SpigotMessages> messages;
    private MySQLConfig<PluginData> data;

    private FileConfig<SpigotWarps> warps;
    private FileConfig<SpigotKits> kits;
    private FileConfig<SpigotChests> chests;

    private Economy economy;

    private List<SpigotCommand> commands;
    private List<SpigotListener> listeners;
    private List<Closable> assets;

    private List<Game> registeredGames;

    private String serverName = "unknown";

    @Override
    public void onLoad() {
        SpigotMain.Instance = this;

        PluginManager.register(this);
    }

    @Override
    public void onEnable() {
        this.getLogger().info("Loading plugin..");

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        this.getLogger().info("Loading config..");

        this.config = new FileConfig<SpigotConfig>(SpigotConfig.class, this.getDataFolder().getAbsolutePath() + File.separator + "config.json");
        this.config.reload();

        if (this.config.getParsed() != null) {
            this.config.save();
        }

        this.serverName = this.config.getParsed().serverName;

        this.messages = new FileConfig<SpigotMessages>(SpigotMessages.class, this.getDataFolder().getAbsolutePath() + File.separator + "messages.json");
        this.messages.reload();

        if (this.messages.getParsed() != null) {
            this.messages.save();
        }

        this.getLogger().info("Finished loading config");

        this.getLogger().info("Loading player data..");

        this.data = new MySQLConfig<PluginData>(PluginData.class, this.config.getParsed().database.host, this.config.getParsed().database.port, this.config.getParsed().database.name, this.config.getParsed().database.tableName, "data", this.config.getParsed().database.username, this.config.getParsed().database.password);

        this.getLogger().info("Finished loading player data");

        this.getLogger().info("Loading other data..");

        this.warps = new FileConfig<SpigotWarps>(SpigotWarps.class, this.getDataFolder().getAbsolutePath() + File.separator + "warps.json");
        this.warps.reload();

        if (this.warps.getParsed() != null) {
            this.warps.save();
        }

        this.kits = new FileConfig<SpigotKits>(SpigotKits.class, this.getDataFolder().getAbsolutePath() + File.separator + "kits.json");
        this.kits.reload();

        if (this.kits.getParsed() != null) {
            this.kits.save();
        }

        if (this.getPluginConfig().getParsed().chestProtection.enabled) {
            this.chests = new FileConfig<SpigotChests>(SpigotChests.class, this.getDataFolder().getAbsolutePath() + File.separator + "chests.json");
            this.chests.reload();

            if (this.chests.getParsed() != null) {
                this.chests.save();
            }
        } else {
            this.chests = null;
        }

        this.getLogger().info("Finished loading other data..");

        this.getLogger().info("Loading economy..");

        this.economy = new Economy(this.data);

        this.getLogger().info("Finished loading economy");

        this.getLogger().info("Loading commands..");

        this.commands = new ArrayList<SpigotCommand>();

        this.commands.add(new WarpCommand("warp", "Teleport to a warp", Arrays.asList(), "evercraft.commands.warp.warp").register());
        this.commands.add(new SpawnCommand("spawn", "Teleport to the spawn", Arrays.asList(), "evercraft.commands.warp.spawn").register());
        this.commands.add(new HomeCommand("home", "Teleport to your home (bed)", Arrays.asList("bed"), "evercraft.commands.warp.home").register());
        this.commands.add(new SetWarpCommand("setwarp", "Set a warp to your location", Arrays.asList(), "evercraft.commands.warp.setwarp").register());
        this.commands.add(new DelWarpCommand("delwarp", "Delete a warp", Arrays.asList(), "evercraft.commands.warp.delwarp").register());

        this.commands.add(new KitCommand("kit", "Get a kit", Arrays.asList(), "evercraft.commands.kit.kit").register());
        this.commands.add(new SetKitCommand("setkit", "Set a kit", Arrays.asList(), "evercraft.commands.kit.setkit").register());
        this.commands.add(new DelKitCommand("delkit", "Delete a kit", Arrays.asList(), "evercraft.commands.kit.delkit").register());

        if (this.getPluginConfig().getParsed().passiveEnabled) {
            this.commands.add(new PassiveCommand("passive", "Toggle passive mode on/off", Arrays.asList("togglepassive", "pvp"), "evercraft.commands.player.passive").register());
        }

        if (this.getPluginConfig().getParsed().chestProtection.enabled) {
            this.commands.add(new ChestProtectionCommand("chestprotection", "Manage you chest protections", Arrays.asList("cp", "chests"), "evercraft.commands.player.chestprotection").register());
        }

        this.commands.add(new GameModeCommand("gamemode", "Change your gamemode", Arrays.asList("gm"), "evercraft.commands.gamemode.use").register());
        this.commands.add(new SurvivalCommand("gms", "Change your gamemode to survival", Arrays.asList("gm0"), "evercraft.commands.gamemode.survival").register());
        this.commands.add(new CreativeCommand("gmc", "Change your gamemode to creative", Arrays.asList("gm1"), "evercraft.commands.gamemode.creative").register());
        this.commands.add(new AdventureCommand("gma", "Change your gamemode to adventure", Arrays.asList("gm2"), "evercraft.commands.gamemode.adventure").register());
        this.commands.add(new SpectatorCommand("gmsp", "Change your gamemode to spectator", Arrays.asList("gm3"), "evercraft.commands.gamemode.spectator").register());

        this.commands.add(new InviSeeCommand("inventorysee", "View and modify a players inventory", Arrays.asList("invsee", "invisee"), "evercraft.commands.staff.invisee").register());
        this.commands.add(new EnderSeeCommand("enderchestsee", "View and modify a players enderchest", Arrays.asList("endersee", "endsee", "ecsee"), "evercraft.commands.staff.endersee").register());

        this.commands.add(new BungeeCommandCommand("bungeecommand", "Run a command on the bungee sever", Arrays.asList(), null).register());

        this.commands.add(new ReloadCommand("evercraftreload", "Reload the plugin", Arrays.asList("ecreload"), "evercraft.commands.staff.reload").register());
        this.commands.add(new DebugCommand("evercraftdebug", "Debug the plugin", Arrays.asList("ecdebug"), "evercraft.commands.staff.debug").register());

        this.getLogger().info("Finished loading commands");

        this.getLogger().info("Loading listeners..");

        this.listeners = new ArrayList<SpigotListener>();

        this.listeners.add(new ServerIdleListener().register());
        this.listeners.add(new MessageListener().register());
        this.listeners.add(new JoinListener().register());

        if (this.getPluginConfig().getParsed().chestProtection.enabled) {
            this.listeners.add(new ChestProtectionListener().register());
        }

        if (this.getPluginConfig().getParsed().creativeProtectionEnabled) {
            this.listeners.add(new CreativeItemListener().register());
        }

        if (this.getPluginConfig().getParsed().passiveEnabled) {
            this.listeners.add(new PvPListener().register());
        }

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());

        this.getLogger().info("Finished loading listeners");

        this.getLogger().info("Loading games..");

        this.registeredGames = new ArrayList<Game>();

        if (this.config.getParsed().games.enabled) {
            this.commands.add(new GameCommand("game", "Join/leave a game", Arrays.asList("games"), "evercraft.commands.games.game").register());
            this.commands.add(new JoinCommand("join", "Join a game", Arrays.asList(), "evercraft.commands.games.game").register());
            this.commands.add(new LeaveCommand("leave", "Leave a game", Arrays.asList(), "evercraft.commands.games.game").register());

            this.registeredGames.add(new KittedGame("plainspvp", "~plainspvp", "pvp"));
            this.registeredGames.add(new KittedGame("endpvp", "~endpvp", "end"));
            this.registeredGames.add(new KittedGame("crystalpvp", "~crystalpvp", "crystal"));
            this.registeredGames.add(new KittedGame("ventpvp", "~ventpvp", "vents"));

            this.registeredGames.add(new KittedGame("sumo", "~sumo", "sumo"));

            this.registeredGames.add(new HideAndSeekGame("deserthideandseek", "~deserthideandseek", 15, Arrays.asList("hiders", "seekers"), "~deserthideandseek-start", "hideandseek"));
            this.registeredGames.add(new HideAndSeekGame("cavehideandseek", "~cavehideandseek", 15, Arrays.asList("hiders", "seekers"), "~cavehideandseek-start", "hideandseek"));

            this.registeredGames.add(new DodgeBowGame("dodgebow", "~dodgebow", 15, Arrays.asList("runners", "bowers"), "~dodgebow-runners", "~dodgebow-bowers", "dodgebow"));

            this.registeredGames.add(new MazeGame("hedgemaze", "~maze", "~maze-finish"));
            this.registeredGames.add(new MazeGame("puzzlemaze", "~puzzlemaze", "~puzzlemaze-finish"));

            this.registeredGames.add(new BoatRaceGame("boatrace", "~boatrace", "boatrace"));
        }

        this.getLogger().info("Finished loading games");

        this.getLogger().info("Loading other assets..");

        this.assets = new ArrayList<Closable>();

        for (Player player : this.getServer().getOnlinePlayers()) {
            player.displayName(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotPlayerResolver.getDisplayName(data, player.getUniqueId()))));
            player.playerListName(player.displayName());
        }

        this.getLogger().info("Finished loading other assets..");

        this.getLogger().info("Finished loading plugin");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabling plugin..");

        this.getLogger().info("Closing config..");

        this.config.close();
        this.messages.close();

        this.getLogger().info("Finished closing config..");

        this.getLogger().info("Closing player data..");

        this.data.close();

        this.getLogger().info("Finished closing player data..");

        this.getLogger().info("Closing other data..");

        this.warps.close();
        this.kits.close();

        if (this.chests != null) {
            this.chests.close();
        }

        this.getLogger().info("Finished closing other data..");

        this.getLogger().info("Unregistering commands..");

        for (SpigotCommand command : this.commands) {
            command.unregister();
        }

        this.getLogger().info("Finished unregistering commands..");

        this.getLogger().info("Unregistering listeners..");

        for (SpigotListener listener : this.listeners) {
            listener.unregister();
        }

        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

        this.getLogger().info("Finished unregistering listeners..");

        this.getLogger().info("Closing assets..");

        for (Closable asset : this.assets) {
            asset.close();
        }

        for (Player player : this.getServer().getOnlinePlayers()) {
            player.displayName(ComponentFormatter.stringToComponent(player.getName()));
            player.playerListName(player.displayName());
        }

        this.getLogger().info("Finished closing assets..");

        this.getLogger().info("Finished disabling plugin");
    }

    @Override
    public void reload() {
        this.getLogger().info("Reloading plugin..");

        this.onDisable();

        this.onEnable();

        this.getLogger().info("Finished reloading plugin");
    }

    public static SpigotMain getInstance() {
        return SpigotMain.Instance;
    }

    public FileConfig<SpigotConfig> getPluginConfig() {
        return this.config;
    }

    public FileConfig<SpigotMessages> getPluginMessages() {
        return this.messages;
    }

    public MySQLConfig<PluginData> getPluginData() {
        return this.data;
    }

    public FileConfig<SpigotWarps> getWarps() {
        return this.warps;
    }

    public FileConfig<SpigotKits> getKits() {
        return this.kits;
    }

    public FileConfig<SpigotChests> getChests() {
        return this.chests;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public List<SpigotCommand> getCommands() {
        return this.commands;
    }

    public List<SpigotListener> getListeneres() {
        return this.listeners;
    }

    public List<Closable> getAssets() {
        return this.assets;
    }

    public List<Game> getRegisteredGames() {
        return this.registeredGames;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String value) {
        this.serverName = value;

        this.config.getParsed().serverName = value;
    }
}