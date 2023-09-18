package io.github.evercraftmc.core.impl.util;

import java.util.regex.Pattern;

public class ECTextFormatter {
    protected static final char COLOR_CHAR = '§';
    protected static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    protected static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)[" + COLOR_CHAR + "&][0-9A-FK-OR]");

    private ECTextFormatter() {
    }

    public static String translateColors(String input) {
        char[] inputBytes = input.toCharArray();

        for (int i = 0; i < inputBytes.length - 1; i++) {
            if (inputBytes[i] == '&' && ALL_CODES.indexOf(inputBytes[i + 1]) > -1) {
                inputBytes[i] = COLOR_CHAR;
                inputBytes[i + 1] = Character.toLowerCase(inputBytes[i + 1]);

                i++;
            }
        }

        return new String(inputBytes);
    }

    public static String untranslateColors(String input) {
        char[] inputBytes = input.toCharArray();

        for (int i = 0; i < inputBytes.length - 1; i++) {
            if (inputBytes[i] == COLOR_CHAR && ALL_CODES.indexOf(inputBytes[i + 1]) > -1) {
                inputBytes[i] = '&';

                i++;
            }
        }

        return new String(inputBytes);
    }

    public static String stripColors(String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}