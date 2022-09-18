package io.github.evercraftmc.evercraft.discord.commands.staff;

import java.util.List;
import io.github.evercraftmc.evercraft.discord.DiscordMain;
import io.github.evercraftmc.evercraft.discord.commands.DiscordCommand;
import io.github.evercraftmc.evercraft.discord.util.player.DiscordPlayerResolver;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class UserInfoCommand extends DiscordCommand {
    public UserInfoCommand(String name, String description, List<String> aliases, Permission permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(Message message, String[] args) {
        if (args.length > 0) {
            DiscordMain.getInstance().getGuild().retrieveMemberById(args[0].replace("<@!", "").replace("<@", "").replace(">", "")).queue(user -> {
                message.reply(DiscordMain.getInstance().getPluginMessages().getParsed().userInfo
                    .replace("{user}", user.getUser().getAsTag())
                    .replace("{id}", user.getId())
                    .replace("{nickname}", user.getNickname())
                    .replace("{minecraft}", DiscordMain.getInstance().getPluginData().getParsed().players.get(DiscordPlayerResolver.getUUIDFromID(DiscordMain.getInstance().getPluginData(), user.getId()).toString()).lastName)).queue();
            });
        } else {
            message.reply(DiscordMain.getInstance().getPluginMessages().getParsed().error.invalidArgs).queue();
        }
    }
}