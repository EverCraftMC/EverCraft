package io.github.evercraftmc.evercraft.shared.util;

import java.time.Instant;

public class TimeUtil {
    public static Instant parseFuture(String string) {
        try {
            if (unitMultiplier(string.replaceAll("[0-9]", "")) != null) {
                return Instant.now().plusSeconds(Integer.parseInt(string.replaceAll("[^0-9]", "")) * unitMultiplier(string.replaceAll("[0-9]", "")));
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer unitMultiplier(String unit) {
        if (unit.equalsIgnoreCase("s")) {
            return 1;
        } else if (unit.equalsIgnoreCase("m")) {
            return 60;
        } else if (unit.equalsIgnoreCase("h")) {
            return 60 * 60;
        } else if (unit.equalsIgnoreCase("d")) {
            return 60 * 60 * 24;
        } else if (unit.equalsIgnoreCase("w")) {
            return 60 * 60 * 24 * 7;
        } else {
            return null;
        }
    }
}