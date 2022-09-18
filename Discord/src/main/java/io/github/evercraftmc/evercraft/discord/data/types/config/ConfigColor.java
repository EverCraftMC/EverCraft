package io.github.evercraftmc.evercraft.discord.data.types.config;

import java.awt.Color;

public class ConfigColor {
    private Integer red = 0;
    private Integer green = 0;
    private Integer blue = 0;

    public Integer getRed() {
        return this.red;
    }

    public Integer getGreen() {
        return this.green;
    }

    public Integer getBlue() {
        return this.blue;
    }

    public Color toColor() {
        return new Color(red, green, blue);
    }
}