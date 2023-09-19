package io.github.evercraftmc.core.impl.spigot.server.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

public class ECSpigotComponentFormatter {
    protected static final char COLOR_CHAR = '§';
    protected static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";

    private ECSpigotComponentFormatter() {
    }

    public static Component stringToComponent(String string) {
        String[] parts = (COLOR_CHAR + "r" + string).split(String.valueOf(COLOR_CHAR));

        TextComponent component = Component.empty();
        Style style = Style.empty();

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            String text = part.substring(1);
            char code = Character.toLowerCase(part.charAt(0));

            if (code == 'r') {
                style = Style.empty();
            } else if (code == '0') {
                style = style.color(NamedTextColor.BLACK);
            } else if (code == '1') {
                style = style.color(NamedTextColor.DARK_BLUE);
            } else if (code == '2') {
                style = style.color(NamedTextColor.DARK_GREEN);
            } else if (code == '3') {
                style = style.color(NamedTextColor.DARK_AQUA);
            } else if (code == '4') {
                style = style.color(NamedTextColor.DARK_RED);
            } else if (code == '5') {
                style = style.color(NamedTextColor.DARK_PURPLE);
            } else if (code == '6') {
                style = style.color(NamedTextColor.GOLD);
            } else if (code == '7') {
                style = style.color(NamedTextColor.GRAY);
            } else if (code == '8') {
                style = style.color(NamedTextColor.DARK_GRAY);
            } else if (code == '9') {
                style = style.color(NamedTextColor.BLUE);
            } else if (code == 'a') {
                style = style.color(NamedTextColor.GREEN);
            } else if (code == 'b') {
                style = style.color(NamedTextColor.AQUA);
            } else if (code == 'c') {
                style = style.color(NamedTextColor.RED);
            } else if (code == 'd') {
                style = style.color(NamedTextColor.LIGHT_PURPLE);
            } else if (code == 'e') {
                style = style.color(NamedTextColor.YELLOW);
            } else if (code == 'f') {
                style = style.color(NamedTextColor.WHITE);
            } else if (code == 'l') {
                style = style.decorate(TextDecoration.BOLD);
            } else if (code == 'm') {
                style = style.decorate(TextDecoration.STRIKETHROUGH);
            } else if (code == 'n') {
                style = style.decorate(TextDecoration.UNDERLINED);
            } else if (code == 'o') {
                style = style.decorate(TextDecoration.ITALIC);
            } else if (code == 'k') {
                style = style.decorate(TextDecoration.OBFUSCATED);
            }

            TextComponent child = Component.text(text);
            child = child.style(style);
            component = component.append(child);
        }

        return component;
    }

    public static String componentToString(Component component) {
        StringBuilder string = new StringBuilder();

        Style style = component.style();

        if (style.isEmpty()) {
            string.append(COLOR_CHAR).append("r");
        } else {
            if (style.color() == NamedTextColor.BLACK) {
                string.append(COLOR_CHAR).append('0');
            } else if (style.color() == NamedTextColor.DARK_BLUE) {
                string.append(COLOR_CHAR).append('1');
            } else if (style.color() == NamedTextColor.DARK_GREEN) {
                string.append(COLOR_CHAR).append('2');
            } else if (style.color() == NamedTextColor.DARK_AQUA) {
                string.append(COLOR_CHAR).append('3');
            } else if (style.color() == NamedTextColor.DARK_RED) {
                string.append(COLOR_CHAR).append('4');
            } else if (style.color() == NamedTextColor.DARK_PURPLE) {
                string.append(COLOR_CHAR).append('5');
            } else if (style.color() == NamedTextColor.GOLD) {
                string.append(COLOR_CHAR).append('6');
            } else if (style.color() == NamedTextColor.GRAY) {
                string.append(COLOR_CHAR).append('7');
            } else if (style.color() == NamedTextColor.DARK_GRAY) {
                string.append(COLOR_CHAR).append('8');
            } else if (style.color() == NamedTextColor.BLUE) {
                string.append(COLOR_CHAR).append('9');
            } else if (style.color() == NamedTextColor.GREEN) {
                string.append(COLOR_CHAR).append('a');
            } else if (style.color() == NamedTextColor.AQUA) {
                string.append(COLOR_CHAR).append('b');
            } else if (style.color() == NamedTextColor.RED) {
                string.append(COLOR_CHAR).append('c');
            } else if (style.color() == NamedTextColor.LIGHT_PURPLE) {
                string.append(COLOR_CHAR).append('d');
            } else if (style.color() == NamedTextColor.YELLOW) {
                string.append(COLOR_CHAR).append('e');
            } else if (style.color() == NamedTextColor.WHITE) {
                string.append(COLOR_CHAR).append('f');
            }

            if (style.hasDecoration(TextDecoration.BOLD)) {
                string.append(COLOR_CHAR).append('l');
            }
            if (style.hasDecoration(TextDecoration.STRIKETHROUGH)) {
                string.append(COLOR_CHAR).append('m');
            }
            if (style.hasDecoration(TextDecoration.UNDERLINED)) {
                string.append(COLOR_CHAR).append('n');
            }
            if (style.hasDecoration(TextDecoration.ITALIC)) {
                string.append(COLOR_CHAR).append('o');
            }
            if (style.hasDecoration(TextDecoration.OBFUSCATED)) {
                string.append(COLOR_CHAR).append('k');
            }
        }

        if (component instanceof TextComponent textComponent) {
            string.append(textComponent.content());
        }

        for (Component child : component.children()) {
            string.append(componentToString(child));
        }

        return string.toString();
    }
}