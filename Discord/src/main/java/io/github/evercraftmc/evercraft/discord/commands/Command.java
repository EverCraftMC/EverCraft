package io.github.evercraftmc.evercraft.discord.commands;

import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class Command {
    public String name;

    public ArgsValidator.Arg[] args;

    public Permission[] permissions;

    public Command(String name, ArgsValidator.Arg[] args, Permission[] permissions) {
        this.name = name;

        this.args = args;

        Integer index = 0;
        for (ArgsValidator.Arg arg : args) {
            if (arg.type() == ArgsValidator.ArgType.String && index != args.length - 1) {
                throw new Error("String can only be the last arg");
            }

            index++;
        }

        this.permissions = permissions;
    }

    public void run(Message message) { }
}