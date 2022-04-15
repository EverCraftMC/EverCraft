package io.github.evercraftmc.evercraft.bungee.util.formatting;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

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

    public static String componentToJson(BaseComponent[] component) {
        return ComponentSerializer.toString(component);
    }

    public static String stringToJson(String string) {
        return componentToJson(stringToComponent(string));
    }

    public static BaseComponent[] jsonToComponent(String json) {
        return ComponentSerializer.parse(json);
    }
}