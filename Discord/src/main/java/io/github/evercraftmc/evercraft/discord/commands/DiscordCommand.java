package io.github.evercraftmc.evercraft.discord.commands;

import java.util.List;
import io.github.evercraftmc.evercraft.discord.DiscordMain;
import io.github.evercraftmc.evercraft.shared.PluginCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public abstract class DiscordCommand implements PluginCommand {
    private String name;
    private String description;
    private List<String> aliases;

    private Permission permission;

    protected DiscordCommand(String name, String description, List<String> aliases, Permission permission) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.permission = permission;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public Permission getPermission() {
        return this.permission;
    }

    public void execute(Message message, String[] args) {
        if (this.hasPermission(message.getAuthor())) {
            this.run(message, args);
        } else {
            message.reply(DiscordMain.getInstance().getPluginMessages().getParsed().error.noPerms.replace("{permission}", this.getPermission().toString())).queue();
        }
    }

    public boolean hasPermission(User sender) {
        return this.getPermission() == null || DiscordMain.getInstance().getGuild().getMember(sender).hasPermission(this.getPermission());
    }

    public abstract void run(Message message, String[] args);

    public DiscordCommand register() {
        return this;
    }

    public void unregister() {}
}