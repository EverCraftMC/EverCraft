package com.kale_ko.evercraft.spigot.util.formatting;

import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;

public class ComponentFormatter {
    public static Component stringToComponent(String string) {
        return PaperComponents.legacySectionSerializer().deserialize(string);
    }

    public static String componentToString(Component component) {
        return PaperComponents.legacySectionSerializer().serialize(component);
    }
}