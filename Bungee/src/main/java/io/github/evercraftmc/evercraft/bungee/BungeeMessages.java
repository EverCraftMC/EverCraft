package io.github.evercraftmc.evercraft.bungee;

import java.util.Map;

public class BungeeMessages {
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

    public static class Chat {
        public String main = "{player} &r> {message}";
        public String dm = "&8[DM] {player1} &r-> {player2} &r> {message}";
        public String noReplyTo = "&cYou do not have anyone to reply to";
        public String cantDM = "&cYou can't message that person";
        public String staff = "&d&l[Staffchat] &r{player} &r> {message}";
        public String discord = "&b&l[Discord] &r{player} &r> {message}";
        public String commandSpy = "&d&l[Commandspy] &r{player} &rran {message}";
    }

    public static class Server {
        public String disconnectedNoCom = "&cThe server you where on is temporarily down, you have been placed in the fallback server.";
        public String disconnectedError = "&cYour connection to the server encountered an issue, you have been placed in the fallback server.";
    }

    public static class Welcome {
        public String firstJoin = "&b&lWelcome {player} &r&b&lto the server!";
        public String join = "&e{player} &r&ejoined the server";
        public String move = "&e{player} &r&ehas moved to {server}";
        public String quit = "&e{player} &r&eleft the server";
    }

    public static class LastSeen {
        public String lastSeen = "&a{player} &r&awas last seen {lastSeen} ago";
        public String online = "&a{player} &r&ais online right now!";
    }

    public static class Economy {
        public String yourBalance = "&aYou balance is currently {balance}";
        public String otherBalance = "&a{player}&r&a's balance is currently {balance}";
        public String economy = "&aSuccessfully set {player}&r&a's balance to {balance}";
    }

    public static class Friend {
        public String invite = "&aInvited {player} &r&ato be your friend";
        public String invited = "&a{player} &r&ainvited you to be their friend";
        public String alreadyInvited = "&c{player} &r&cis already your friend!";

        public String add = "&a{player} &r&ais now your friend!";
        public String added = "&a{player} &r&ais now your friend!";
        public String alreadyFriends = "&c{player} &r&cis already your friend!";

        public String remove = "&c{player} &r&cis no longer your friend";
        public String removed = "&c{player} &r&cis no longer your friend";
        public String notFriends = "&c{player} &r&cis not your friend!";

        public String list = "&a&lFriends\n&r&a{friends}";
        public String listInvites = list + "\n&a&lInvites\n&r&a{invites}";
    }

    public static class Warp {
        public String hub = "&aSuccessfully went to the hub";
        public String server = "&aSuccessfully went to {server}";
        public String alreadyConnected = "&cYou are already in the hub";
    }

    public static class PlayerInfo {
        public String offline = "&a&l{player}'s User Info\n&r&aUsername: {player}\nUUID: {uuid}\nIP: {ip}\nNickname: {nickname}";
        public String online = offline + "\n\n&r&a&l{player}'s Connection Info\n&r&aPing: {ping}\nVersion: {version} ({protocolVersion})";
    }

    public static class Moderation {
        public static class Kick {
            public static class Broadcast {
                public String noReason = "&a{player}&r&a was kicked by {moderator}";
                public String reason = "&a{player}&r&a was kicked by {moderator} &r&afor {reason}";
            }

            public String noReason = "&cYou where kicked by {moderator}";
            public String reason = "&cYou where kicked by {moderator} &r&cfor {reason}";
            public Broadcast broadcast = new Broadcast();
            public String cantKickSelf = "&cYou can't kick yourself (If you are absolutely sure you want to kick yourself add --confirm to the end of the command";
        }

        public static class Ban {
            public static class Broadcast {
                public String noReason = "&a{player}&r&a was banned by {moderator}";
                public String reason = "&a{player}&r&a was banned by {moderator} &r&afor {reason}";
            }

            public String noReason = "&cYou where banned by {moderator}";
            public String reason = "&cYou where banned by {moderator} &r&cfor {time} {reason}";
            public Broadcast broadcast = new Broadcast();
            public String alreadyBanned = "&c{player}&r&c is already banned";
            public String cantBanSelf = "&cYou can't ban yourself (If you are absolutely sure you want to ban yourself (You can't unban yourself) add --confirm to the end of the command";
        }

        public static class Unban {
            public static class Broadcast {
                public String noReason = "&a{player}&r&a was unbanned by {moderator}";
                public String reason = "&a{player}&r&a was unbanned by {moderator} &r&afor {reason}";
            }

            public String noReason = "&cYou where unbanned by {moderator}";
            public String reason = "&cYou where unbanned by {moderator} &r&cfor {reason}";
            public Broadcast broadcast = new Broadcast();
            public String notBanned = "&c{player}&r&c is not banned";
            public String cantUnbanSelf = "&cYou can't unban yourself";
        }

        public static class Mute {
            public static class Broadcast {
                public String noReason = "&a{player}&r&a was muted by {moderator}&r&a for {time}";
                public String reason = "&a{player}&r&a was muted by {moderator} &r&afor {time} {reason}";
            }

            public String noReason = "&cYou where muted by {moderator}";
            public String reason = "&cYou where muted by {moderator} &r&cfor {time} {reason}";
            public Broadcast broadcast = new Broadcast();
            public String alreadyMuted = "&c{player}&r&c is already muted";
            public String cantMuteSelf = "&cYou can't mute yourself (If you are absolutely sure you want to mute yourself (You can't unmute yourself) add --confirm to the end of the command";
        }

        public static class Unmute {
            public static class Broadcast {
                public String noReason = "&a{player}&r&a was unmuted by {moderator}";
                public String reason = "&a{player}&r&a was unmuted by {moderator} &r&afor {reason}";
            }

            public String noReason = "&cYou where unmuted by {moderator}";
            public String reason = "&cYou where unmuted by {moderator} &r&cfor {reason}";
            public Broadcast broadcast = new Broadcast();
            public String notMuted = "&c{player}&r&c is not muted";
            public String cantUnmuteSelf = "&cYou can't unmute yourself";
        }

        public static class Chatlock {
            public String toggle = "&aSuccessfully toggled chat lock mode {value}";
            public String chat = "&cChat lock is currently enabled";
        }

        public static class Maintenance {
            public String toggle = "&aSuccessfully toggled maintenance mode {value}";
            public String kick = "&cSorry but the server is currently in maintenance mode, please come back later";
            public String motd = "              &cCurrently under maintenance";
        }

        public static class Chat {
            public String warning = "&a{player}&r&a was warned by CONSOLE for Inappropriate language";
        }

        public Kick kick = new Kick();

        public Ban ban = new Ban();
        public Unban unban = new Unban();

        public Mute mute = new Mute();
        public Unmute unmute = new Unmute();

        public Chatlock chatLock = new Chatlock();

        public Maintenance maintenance = new Maintenance();

        public Chat chat = new Chat();
    }

    public static class Sudo {
        public String message = "&aSuccessfully said {message}&r&a as {player}";
        public String command = "&aSuccessfully ran {command}&r&a as {player}";
    }

    public Error error = new Error();

    public Reload reload = new Reload();

    public String globalMessage = "[{server}&r] {message}";
    public Chat chat = new Chat();

    public Server server = new Server();

    public Map<String, String> info = Map.ofEntries(Map.entry("about", ""), Map.entry("discord", ""), Map.entry("vote", ""), Map.entry("staff", ""));

    public Welcome welcome = new Welcome();

    public String nickname = "&aSuccessfully changed your nickname to {nickname}";

    public LastSeen lastSeen = new LastSeen();

    public Economy economy = new Economy();

    public String vote = "&aThanks so much for voting {player}&r&a! /vote";

    public Friend friend = new Friend();

    public String linking = "&aNow type \"!link {code}\" into #link in the Discord";
    public String unlinked = "&aSuccessfully unlinked your account";
    public String linked = "&aYour account is linked to {account}";
    public String notLinked = "&cYour account is not linked";

    public String settings = "&aSuccessfully set {setting} to {value}";

    public Warp warp = new Warp();

    public PlayerInfo playerInfo = new PlayerInfo();

    public Moderation moderation = new Moderation();

    public String commandSpy = "&aSuccessfully toggled your commandspy {value}";

    public Sudo sudo = new Sudo();
}