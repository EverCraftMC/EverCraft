package io.github.evercraftmc.evercraft.discord.data.types.config;

import java.awt.Color;

public class ConfigColor {
    private Integer red = 0;
    private Integer green = 0;
    private Integer blue = 0;

    public Color toColor() {
        return new Color(red, green, blue);
    }
}