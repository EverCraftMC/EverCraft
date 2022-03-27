package com.kale_ko.evercraft.shared.util;

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
}