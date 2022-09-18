package io.github.evercraftmc.evercraft.discord;

import java.util.Map;

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

    public static class Linking {
        public String success = "Successfully linked your account to {account}!";
        public String unsuccess = "Successfully unlinked your account";
        public String linked = "Your account is linked to {account}";
        public String notLinked = "Your account is not linked";
        public String needCode = "To link your account to Minecraft, join the server and type /link";
        public String invalidCode = "That code is not a valid/known code";
    }

    public Error error = new Error();

    public Reload reload = new Reload();

    public Map<String, String> info = Map.ofEntries(Map.entry("about", ""), Map.entry("ip", ""), Map.entry("vote", ""), Map.entry("staff", ""));

    public Linking linking = new Linking();

    public String userInfo = "{user}'s User Info\nName: {user}\nID: {id}\nNickname: {nickname}\nMinecraft: {minecraft}";
}