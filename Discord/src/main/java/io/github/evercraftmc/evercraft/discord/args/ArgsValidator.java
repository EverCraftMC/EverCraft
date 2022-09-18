package io.github.evercraftmc.evercraft.discord.args;

import java.util.List;
import io.github.evercraftmc.evercraft.discord.BotMain;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class ArgsValidator {
    public enum ArgType {
        Word, String, Float, Integer, Boolean, Duration, User, Member, Role, Channel, TextChannel, Message, VoiceChannel, Emoji
    }

    public record Arg(ArgType type, Boolean optional) {
        public OptionType toOption() {
            switch (type) {
            case Word:
                return OptionType.STRING;
            case String:
                return OptionType.STRING;
            case Float:
                return OptionType.NUMBER;
            case Integer:
                return OptionType.INTEGER;
            case Boolean:
                return OptionType.BOOLEAN;
            case Duration:
                return OptionType.STRING;
            case User:
                return OptionType.USER;
            case Member:
                return OptionType.USER;
            case Role:
                return OptionType.ROLE;
            case Channel:
                return OptionType.CHANNEL;
            case TextChannel:
                return OptionType.CHANNEL;
            case Message:
                return OptionType.STRING;
            case VoiceChannel:
                return OptionType.CHANNEL;
            case Emoji:
                return OptionType.STRING;
            default:
                return OptionType.STRING;
            }
        }
    }

    public static Boolean validateArgs(Message message, List<Arg> expectedArgs) {
        String[] args = ArgsParser.getRawArgs(message);

        for (Integer index = 0; index < expectedArgs.size(); index++) {
            if (index >= args.length - 1 && !expectedArgs.get(index).optional()) {
                BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "Expected more args", message.getAuthor());

                return false;
            } else if (!expectedArgs.get(index).optional()) {
                if (expectedArgs.get(index).type() == ArgType.Float) {
                    try {
                        ArgsParser.getFloatArg(message, index + 1);
                    } catch (NumberFormatException err) {
                        BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "Arg " + (index + 1) + " must be a number", message.getAuthor());

                        return false;
                    }
                } else if (expectedArgs.get(index).type() == ArgType.Integer) {
                    try {
                        ArgsParser.getIntegerArg(message, index + 1);
                    } catch (NumberFormatException err) {
                        BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "Arg " + (index + 1) + " must be a integer", message.getAuthor());

                        return false;
                    }
                } else if (expectedArgs.get(index).type() == ArgType.Boolean) {
                    if (ArgsParser.getBooleanArg(message, index + 1) == null) {
                        BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "Arg " + (index + 1) + " must be a boolean", message.getAuthor());

                        return false;
                    }
                } else if (expectedArgs.get(index).type() == ArgType.Duration) {
                    try {
                        ArgsParser.getDurrationArg(message, index + 1);
                    } catch (NumberFormatException | ArithmeticException err) {
                        BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "Arg " + (index + 1) + " must be a duration", message.getAuthor());

                        return false;
                    }
                } else if (expectedArgs.get(index).type() == ArgType.User) {
                    try {
                        ArgsParser.getUserArg(message, index + 1);
                    } catch (NumberFormatException err) {
                        BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "Arg " + (index + 1) + " must be a user", message.getAuthor());

                        return false;
                    }
                } else if (expectedArgs.get(index).type() == ArgType.Member) {
                    try {
                        Member member = ArgsParser.getMemberArg(message, index + 1);

                        if (!member.getRoles().isEmpty() && member.getRoles().get(0).getPosition() >= message.getMember().getRoles().get(0).getPosition()) {
                            BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "You can't affect that user", message.getAuthor());

                            return false;
                        }
                    } catch (NumberFormatException | ErrorResponseException err) {
                        BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "Arg " + (index + 1) + " must be a guild member", message.getAuthor());

                        return false;
                    }
                } else if (expectedArgs.get(index).type() == ArgType.Role) {
                    if (ArgsParser.getRoleArg(message, index + 1) == null) {
                        BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "Arg " + (index + 1) + " must be a role", message.getAuthor());

                        return false;
                    }
                } else if (expectedArgs.get(index).type() == ArgType.Channel) {
                    if (ArgsParser.getChannelArg(message, index + 1) == null) {
                        BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "Arg " + (index + 1) + " must be a channel", message.getAuthor());

                        return false;
                    }
                } else if (expectedArgs.get(index).type() == ArgType.TextChannel) {
                    if (ArgsParser.getTextChannelArg(message, index + 1) == null) {
                        BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "Arg " + (index + 1) + " must be a text channel", message.getAuthor());

                        return false;
                    }
                } else if (expectedArgs.get(index).type() == ArgType.Message) {
                    if (ArgsParser.getMessageArg(message, index + 1) == null) {
                        BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "Arg " + (index + 1) + " must be a message", message.getAuthor());

                        return false;
                    }
                } else if (expectedArgs.get(index).type() == ArgType.VoiceChannel) {
                    if (ArgsParser.getVoiceChannelArg(message, index + 1) == null) {
                        BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "Arg " + (index + 1) + " must be a voice channel", message.getAuthor());

                        return false;
                    }
                } else if (expectedArgs.get(index).type() == ArgType.Emoji) {
                    if (ArgsParser.getEmojiArg(message, index + 1) == null) {
                        BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", "Arg " + (index + 1) + " must be an emoji", message.getAuthor());

                        return false;
                    }
                }
            }
        }

        return true;
    }
}