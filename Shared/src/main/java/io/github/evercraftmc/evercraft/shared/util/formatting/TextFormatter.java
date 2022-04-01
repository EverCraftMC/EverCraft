package io.github.evercraftmc.evercraft.shared.util.formatting;

import java.util.regex.Pattern;

public class TextFormatter {
    private static final char COLOR_CHAR = '§';
    private static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)[" + COLOR_CHAR + "&][0-9A-FK-ORX]");

    public static String translateColors(String input) {
        char[] inputBytes = input.toCharArray();

        for (Integer i = 0; i < inputBytes.length - 1; i++) {
            if (inputBytes[i] == '&' && ALL_CODES.indexOf(inputBytes[i + 1]) > -1) {
                inputBytes[i] = COLOR_CHAR;
                inputBytes[i + 1] = Character.toLowerCase(inputBytes[i + 1]);
            }
        }

        return new String(inputBytes);
    }

    public static String unTranslateColors(String input) {
        char[] inputBytes = input.toCharArray();

        for (Integer i = 0; i < inputBytes.length - 1; i++) {
            if (inputBytes[i] == COLOR_CHAR && ALL_CODES.indexOf(inputBytes[i + 1]) > -1) {
                inputBytes[i] = '&';
                inputBytes[i + 1] = Character.toLowerCase(inputBytes[i + 1]);
            }
        }

        return new String(inputBytes);
    }

    public static String removeColors(String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static String discordFormat(String input) {
        return removeColors(input.replace("*", "\\*").replace("_", "\\_").replace("~", "\\~").replace("`", "\\`").replace(">", "\\>").replace("|", "\\|"));
    }
}