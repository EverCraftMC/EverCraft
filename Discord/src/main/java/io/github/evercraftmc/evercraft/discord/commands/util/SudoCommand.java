package io.github.evercraftmc.evercraft.discord.commands.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.external.JDAWebhookClient;
import club.minnced.discord.webhook.receive.ReadonlyMessage;
import club.minnced.discord.webhook.send.AllowedMentions;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Webhook;

public class SudoCommand extends Command {
    public SudoCommand() {
        super("sudo", Arrays.asList(), Arrays.asList(new ArgsValidator.Arg(ArgsValidator.ArgType.Member, false), new ArgsValidator.Arg(ArgsValidator.ArgType.String, false)), Arrays.asList(Permission.ADMINISTRATOR));
    }

    @Override
    public void run(Message message) {
        Member member = ArgsParser.getMemberArg(message, 1);

        Webhook webhook = message.getChannel().asTextChannel().createWebhook("Sudo").complete();
        webhook.getManager().setName(member.getNickname() != null ? member.getNickname() : member.getUser().getName()).complete();
        try {
            URLConnection connection = new URL(member.getUser().getAvatarUrl() != null ? member.getUser().getAvatarUrl() : member.getUser().getDefaultAvatarUrl()).openConnection();
            connection.setRequestProperty("User-Agent", "Bot evercraft_bot");
            webhook.getManager().setAvatar(Icon.from(connection.getInputStream())).complete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JDAWebhookClient client = WebhookClientBuilder.fromJDA(webhook).setAllowedMentions(AllowedMentions.none()).buildJDA();
        client.send(ArgsParser.getStringArg(message, 2)).whenComplete(new BiConsumer<ReadonlyMessage, Throwable>() {
            @Override
            public void accept(ReadonlyMessage message, Throwable throwable) {
                client.close();

                webhook.delete().queue();
            }
        });
    }
}