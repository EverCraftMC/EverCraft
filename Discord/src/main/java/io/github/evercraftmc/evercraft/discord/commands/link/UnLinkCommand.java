package io.github.evercraftmc.evercraft.discord.commands.link;

import java.util.List;
import io.github.evercraftmc.evercraft.discord.DiscordMain;
import io.github.evercraftmc.evercraft.discord.commands.DiscordCommand;
import io.github.evercraftmc.evercraft.discord.util.player.DiscordPlayerResolver;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class UnLinkCommand extends DiscordCommand {
    public UnLinkCommand(String name, String description, List<String> aliases, Permission permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(Message message, String[] args) {
        if (DiscordPlayerResolver.getUUIDFromID(DiscordMain.getInstance().getPluginData(), message.getAuthor().getId()) != null) {
            DiscordMain.getInstance().getPluginData().getParsed().players.get(DiscordPlayerResolver.getUUIDFromID(DiscordMain.getInstance().getPluginData(), message.getAuthor().getId()).toString()).discordAccount = null;
            DiscordMain.getInstance().getPluginData().save();

            message.getGuild().removeRoleFromMember(message.getMember(), message.getGuild().getRoleById(DiscordMain.getInstance().getPluginConfig().getParsed().linkedRole)).queue();

            message.reply(DiscordMain.getInstance().getPluginMessages().getParsed().linking.unsuccess).queue();
        } else {
            message.reply(DiscordMain.getInstance().getPluginMessages().getParsed().linking.notLinked).queue();
        }
    }
}