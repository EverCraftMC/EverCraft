package io.github.evercraftmc.evercraft.bungee.util.formatting;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ComponentFormatter {
    public static BaseComponent[] stringToComponent(String string) {
        return TextComponent.fromLegacyText(string);
    }

    public static String componentToString(BaseComponent[] component) {
        return TextComponent.toLegacyText(component);
    }

    public static BaseComponent flatenComponent(BaseComponent[] component) {
        return new TextComponent(component);
    }
}