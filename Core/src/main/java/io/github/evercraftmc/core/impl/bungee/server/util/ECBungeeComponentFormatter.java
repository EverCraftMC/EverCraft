package io.github.evercraftmc.core.impl.bungee.server.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.TextComponent;

public class ECBungeeComponentFormatter {
    protected static final char COLOR_CHAR = '§';
    protected static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";

    private ECBungeeComponentFormatter() {
    }

    public static BaseComponent stringToComponent(String string) {
        String[] parts = (COLOR_CHAR + "r" + string).split(String.valueOf(COLOR_CHAR));

        TextComponent component = new TextComponent();
        TextComponent format = new TextComponent();
        format.retain(FormatRetention.NONE);

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            String text = part.substring(1);
            char code = Character.toLowerCase(part.charAt(0));

            if (code == 'r') {
                format = new TextComponent();
            } else if (code == '0') {
                format.setColor(ChatColor.BLACK);
            } else if (code == '1') {
                format.setColor(ChatColor.DARK_BLUE);
            } else if (code == '2') {
                format.setColor(ChatColor.DARK_GREEN);
            } else if (code == '3') {
                format.setColor(ChatColor.DARK_AQUA);
            } else if (code == '4') {
                format.setColor(ChatColor.DARK_RED);
            } else if (code == '5') {
                format.setColor(ChatColor.DARK_PURPLE);
            } else if (code == '6') {
                format.setColor(ChatColor.GOLD);
            } else if (code == '7') {
                format.setColor(ChatColor.GRAY);
            } else if (code == '8') {
                format.setColor(ChatColor.DARK_GRAY);
            } else if (code == '9') {
                format.setColor(ChatColor.BLUE);
            } else if (code == 'a') {
                format.setColor(ChatColor.GREEN);
            } else if (code == 'b') {
                format.setColor(ChatColor.AQUA);
            } else if (code == 'c') {
                format.setColor(ChatColor.RED);
            } else if (code == 'd') {
                format.setColor(ChatColor.LIGHT_PURPLE);
            } else if (code == 'e') {
                format.setColor(ChatColor.YELLOW);
            } else if (code == 'f') {
                format.setColor(ChatColor.WHITE);
            } else if (code == 'l') {
                format.setBold(true);
            } else if (code == 'm') {
                format.setStrikethrough(true);
            } else if (code == 'n') {
                format.setUnderlined(true);
            } else if (code == 'o') {
                format.setItalic(true);
            } else if (code == 'k') {
                format.setObfuscated(true);
            }

            TextComponent child = new TextComponent(text);
            child.copyFormatting(format);
            component.addExtra(child);
        }

        return component;
    }

    public static String componentToString(BaseComponent component) {
        StringBuilder string = new StringBuilder();

        if (component.isReset()) {
            string.append(COLOR_CHAR).append("r");
        } else {
            if (component.getColor() == ChatColor.BLACK) {
                string.append(COLOR_CHAR).append('0');
            } else if (component.getColor() == ChatColor.DARK_BLUE) {
                string.append(COLOR_CHAR).append('1');
            } else if (component.getColor() == ChatColor.DARK_GREEN) {
                string.append(COLOR_CHAR).append('2');
            } else if (component.getColor() == ChatColor.DARK_AQUA) {
                string.append(COLOR_CHAR).append('3');
            } else if (component.getColor() == ChatColor.DARK_RED) {
                string.append(COLOR_CHAR).append('4');
            } else if (component.getColor() == ChatColor.DARK_PURPLE) {
                string.append(COLOR_CHAR).append('5');
            } else if (component.getColor() == ChatColor.GOLD) {
                string.append(COLOR_CHAR).append('6');
            } else if (component.getColor() == ChatColor.GRAY) {
                string.append(COLOR_CHAR).append('7');
            } else if (component.getColor() == ChatColor.DARK_GRAY) {
                string.append(COLOR_CHAR).append('8');
            } else if (component.getColor() == ChatColor.BLUE) {
                string.append(COLOR_CHAR).append('9');
            } else if (component.getColor() == ChatColor.GREEN) {
                string.append(COLOR_CHAR).append('a');
            } else if (component.getColor() == ChatColor.AQUA) {
                string.append(COLOR_CHAR).append('b');
            } else if (component.getColor() == ChatColor.RED) {
                string.append(COLOR_CHAR).append('c');
            } else if (component.getColor() == ChatColor.LIGHT_PURPLE) {
                string.append(COLOR_CHAR).append('d');
            } else if (component.getColor() == ChatColor.YELLOW) {
                string.append(COLOR_CHAR).append('e');
            } else if (component.getColor() == ChatColor.WHITE) {
                string.append(COLOR_CHAR).append('f');
            }

            if (component.isBold()) {
                string.append(COLOR_CHAR).append('l');
            }
            if (component.isStrikethrough()) {
                string.append(COLOR_CHAR).append('m');
            }
            if (component.isUnderlined()) {
                string.append(COLOR_CHAR).append('n');
            }
            if (component.isItalic()) {
                string.append(COLOR_CHAR).append('o');
            }
            if (component.isObfuscated()) {
                string.append(COLOR_CHAR).append('k');
            }
        }

        if (component instanceof net.kyori.adventure.text.TextComponent textComponent) {
            string.append(textComponent.content());
        }

        for (BaseComponent child : component.getExtra()) {
            string.append(componentToString(child));
        }

        return string.toString();
    }
}