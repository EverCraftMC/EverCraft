package io.github.evercraftmc.evercraft.discord.commands;

import java.util.List;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public abstract class Command {
    private String name;
    private List<String> aliases;

    private List<ArgsValidator.Arg> args;

    private List<Permission> permissions;

    protected Command(String name, List<String> aliases, List<ArgsValidator.Arg> args, List<Permission> permissions) {
        this.name = name;
        this.aliases = aliases;

        this.args = args;

        Integer index = 0;
        for (ArgsValidator.Arg arg : args) {
            if (arg.type() == ArgsValidator.ArgType.String && index != args.size() - 1) {
                throw new Error("String can only be the last arg");
            }

            index++;
        }

        this.permissions = permissions;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public List<ArgsValidator.Arg> getArgs() {
        return this.args;
    }

    public List<Permission> getPermissions() {
        return this.permissions;
    }

    public abstract void run(Message message);
}