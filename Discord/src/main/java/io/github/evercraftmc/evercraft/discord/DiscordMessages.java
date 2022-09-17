package io.github.evercraftmc.evercraft.discord;

public class DiscordMessages {
    public static class Error {
        public String noPerms = "&cYou need the permission \"{permission}\" to do that";
        public String userNotFound = "&cCouldn't find user \"{player}\"";
        public String invalidArgs = "&cInvalid arguments";
    }

    public static class Reload {
        public String reloading = "&aReloading plugin..";
        public String reloaded = "&aSuccessfully reloaded";
    }

    public Error error = new Error();

    public Reload reload = new Reload();
}