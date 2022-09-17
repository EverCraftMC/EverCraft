package io.github.evercraftmc.evercraft.discord;

public class DiscordMessages {
    public static class Error {
        public String noPerms = "You need the permission \"{permission}\" to do that";
        public String userNotFound = "Couldn't find user \"{player}\"";
        public String invalidArgs = "Invalid arguments";
    }

    public static class Reload {
        public String reloading = "Reloading plugin..";
        public String reloaded = "Successfully reloaded";
    }

    public Error error = new Error();

    public Reload reload = new Reload();
}