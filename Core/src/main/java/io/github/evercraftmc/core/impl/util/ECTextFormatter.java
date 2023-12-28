package io.github.evercraftmc.core.impl.util;

import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public class ECTextFormatter {
    public static final char FROM_COLOR_CHAR = '&';
    public static final char TO_COLOR_CHAR = '§';

    protected static final @NotNull String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    protected static final @NotNull Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)[" + TO_COLOR_CHAR + "&][0-9A-FK-OR]");

    private ECTextFormatter() {
    }

    public static @NotNull String translateColors(@NotNull String input) {
        char[] inputBytes = input.toCharArray();

        for (int i = 0; i < inputBytes.length - 1; i++) {
            if (inputBytes[i] == FROM_COLOR_CHAR && ALL_CODES.indexOf(inputBytes[i + 1]) > -1) {
                inputBytes[i] = TO_COLOR_CHAR;
                inputBytes[i + 1] = Character.toLowerCase(inputBytes[i + 1]);

                i++;
            }
        }

        return new String(inputBytes);
    }

    public static @NotNull String untranslateColors(@NotNull String input) {
        char[] inputBytes = input.toCharArray();

        for (int i = 0; i < inputBytes.length - 1; i++) {
            if (inputBytes[i] == TO_COLOR_CHAR && ALL_CODES.indexOf(inputBytes[i + 1]) > -1) {
                inputBytes[i] = FROM_COLOR_CHAR;

                i++;
            }
        }

        return new String(inputBytes);
    }

    public static @NotNull String stripColors(@NotNull String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}