package io.github.evercraftmc.evercraft.limbo.util.formatting;

import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ComponentFormatter {
    private static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder().character('§').extractUrls(Pattern.compile("/(http(s)?://[^ ]*)/", Pattern.CASE_INSENSITIVE), Style.style(TextDecoration.UNDERLINED)).flattener(ComponentFlattener.basic()).build();
    private static final GsonComponentSerializer JSON_COMPONENT_SERIALIZER = GsonComponentSerializer.builder().build();

    public static Component stringToComponent(String string) {
        return LEGACY_COMPONENT_SERIALIZER.deserialize(string);
    }

    public static String componentToString(Component component) {
        return LEGACY_COMPONENT_SERIALIZER.serialize(component);
    }

    public static String componentToJson(Component component) {
        return JSON_COMPONENT_SERIALIZER.serialize(component);
    }

    public static String stringToJson(String string) {
        return componentToJson(stringToComponent(string));
    }

    public static Component jsonToComponent(String json) {
        return JSON_COMPONENT_SERIALIZER.deserialize(json);
    }
}