package io.github.evercraftmc.evercraft.spigot;

public class SpigotMessages {
    public static class Error {
        public String noPerms = "&cYou need the permission \"{permission}\" to do that";
        public String noConsole = "&cYou can't do that from the console";
        public String playerNotFound = "&cCouldn't find player \"{player}\"";
        public String invalidArgs = "&cInvalid arguments";
    }

    public static class Reload {
        public String reloading = "&aReloading plugin..";
        public String reloaded = "&aSuccessfully reloaded";
    }

    public static class Warp {
        public String warped = "&aSuccessfully warped to {warp}";
        public String setWarp = "&aSuccessfully set warp {warp}&r&a to your location";
        public String delWarp = "&aSuccessfully deleted warp {warp}";
        public String notFound = "&cWarp {warp}&r&c does not exist";
    }

    public static class Kit {
        public String kit = "&aSuccessfully got kit {kit}";
        public String setKit = "&aSuccessfully set kit {kit}&r&a to your inventory";
        public String delKit = "&cSuccessfully deleted kit {kit}";
        public String notFound = "&cKit {kit}&r&c does not exist";
    }

    public static class ChestProtection {
        public String claimed = "&aThis block is now claimed";
        public String unclaimed = "&cThis block is no longer claimed";
        public String wasProtected = "&aThis block is now protected";
        public String unprotected = "&cThis block is no longer protected";
        public String allowedUse = "&aThis block is now usable by others";
        public String disallowedUse = "&cThis block is no longer usable by others";
        public String notYours = "&cIm sorry but that isn't yours >:(";
        public String noBlock = "&cYou must be looking at a block to do that";
    }

    public static class Games {
        public String joined = "&aYou have joined {game} &r&a({players} / {max})";
        public String join = "&a{player} &r&ahas joined the game ({players} / {max})";
        public String left = "&aYou have left {game}";
        public String leave = "&a{player} &r&ahas left the game ({players} / {max})";
        public String joinedTeam = "&aYou have joined team {team}";
        public String teamJoin = "&a{player} &r&ahas joined team {team}";
        public String leftTeam = "&aYou have left team {team}";
        public String teamLeave = "&a{player} &r&ahas left team {team}";
        public String start = "&aThat game has started";
        public String stop = "&aThat game has stopped";
        public String full = "&cThat game is full";
        public String started = "&cThat game has already started";
        public String notFound = "&cGame {game} &r&ccould not be found";
        public String alreadyInGame = "&cYou are already in a game";
        public String notInGame = "&cYou are not in a game";
    }

    public Error error = new Error();

    public Reload reload = new Reload();

    public String gamemode = "&aSuccessfully set your gamemode to {gamemode}";

    public Warp warp = new Warp();

    public Kit kit = new Kit();

    public String passive = "&aSuccessfully toggled passive mode {value}";
    public ChestProtection chestProtection = new ChestProtection();

    public Games games = new Games();
}
