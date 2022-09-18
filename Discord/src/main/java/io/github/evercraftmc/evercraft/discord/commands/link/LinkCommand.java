package io.github.evercraftmc.evercraft.discord.commands.link;

import java.util.List;
import io.github.evercraftmc.evercraft.discord.DiscordMain;
import io.github.evercraftmc.evercraft.discord.commands.DiscordCommand;
import io.github.evercraftmc.evercraft.discord.util.player.DiscordPlayerResolver;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class LinkCommand extends DiscordCommand {
    public LinkCommand(String name, String description, List<String> aliases, Permission permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(Message message, String[] args) {
        if (args.length == 0) {
            if (DiscordPlayerResolver.getUUIDFromID(DiscordMain.getInstance().getPluginData(), message.getAuthor().getId()) != null) {
                message.reply(DiscordMain.getInstance().getPluginMessages().getParsed().linking.linked.replace("{account}", DiscordMain.getInstance().getPluginData().getParsed().players.get(DiscordPlayerResolver.getUUIDFromID(DiscordMain.getInstance().getPluginData(), message.getAuthor().getId()).toString()).lastName)).queue();
            } else {
                message.reply(DiscordMain.getInstance().getPluginMessages().getParsed().linking.needCode).queue();
            }
        } else {
            if (DiscordMain.getInstance().getPluginData().getParsed().linking.containsKey(args[0])) {
                DiscordMain.getInstance().getPluginData().getParsed().players.get(DiscordMain.getInstance().getPluginData().getParsed().linking.get(args[0]).account).discordAccount = message.getAuthor().getId();

                message.getGuild().addRoleToMember(message.getMember(), message.getGuild().getRoleById(DiscordMain.getInstance().getPluginConfig().getParsed().linkedRole)).queue();

                message.reply(DiscordMain.getInstance().getPluginMessages().getParsed().linking.success.replace("{account}", DiscordMain.getInstance().getPluginData().getParsed().players.get(DiscordMain.getInstance().getPluginData().getParsed().linking.get(args[0]).account).lastName)).queue();

                DiscordMain.getInstance().getPluginData().getParsed().linking.remove(args[0]);
                DiscordMain.getInstance().getPluginData().save();
            } else {
                message.reply(DiscordMain.getInstance().getPluginMessages().getParsed().linking.invalidCode).queue();
            }
        }
    }
}
