package io.github.evercraftmc.evercraft.shared.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static List<String> matchPartial(String token, List<String> originals) {
        List<String> results = new ArrayList<String>();

        for (String string : originals) {
            if (string.toLowerCase().startsWith(token.toLowerCase())) {
                results.add(string);
            }
        }

        return results;
    }

    public static String toTtitleCase(String string) {
        StringBuilder converted = new StringBuilder();

        int i = 0;
        for (char ch : string.toCharArray()) {
            if (i == 0 || string.toCharArray()[i - 1] == ' ') {
                ch = Character.toTitleCase(ch);
            }

            converted.append(ch);

            i++;
        }

        return converted.toString();
    }
}