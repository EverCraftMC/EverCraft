package io.github.evercraftmc.evercraft.shared.util;

public class ModerationUtil {
    private static final String[] inappropriateRegexes = new String[] {};
    // More Strict: { "ass(hole)?(s)?", "bastard(s)?", "bitch(es)?", "blowjob(s)?", "clitoris(es)?", "cock(s)?", "cocksucker(s)?", "cunt(s)?", "cum(s)?", "cumsock(s)?", "dick(s)?", "fu(c)?k(er)?(s)?", "fuck(ing)?", "motherfuck(er)?(s)?", "gay", "nigg(a)?(s)?", "nig(g)?er(s)?", "penis(es)?", "pp(s)", "pervert(s)?", "porn", "pornstar(s)?", "prostitute(s)?", "pussy", "pussies", "shit(s)?", "shit(er)?", "slut(s)?", "sex(y)?", "vag", "vagina(s)?", "whore(s)?" }
    private static final String[] inappropriateWords = new String[] {};
    // More Strict: { "fu", ".|." }

    private static final String[] superInappropriateRegexes = new String[] { "nig(g)?(a)(s)?", "nig(g)?er(s)?" };
    private static final String[] superInappropriateWords = new String[] {};

    public static boolean isInappropriateString(String string) {
        if (isSuperInappropriateString(string)) {
            return true;
        }

        for (String word : string.replace("-", " ").replace("_", " ").split(" ")) {
            if (isInappropriateWord(word)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInappropriateWord(String word) {
        if (isSuperInappropriateWord(word)) {
            return true;
        }

        for (String inappropriateRegex : inappropriateRegexes) {
            if (word.toLowerCase().matches(inappropriateRegex)) {
                return true;
            }
        }

        for (String inappropriateWord : inappropriateWords) {
            if (word.equalsIgnoreCase(inappropriateWord)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isSuperInappropriateString(String string) {
        for (String word : string.replace("-", " ").replace("_", " ").split(" ")) {
            if (isSuperInappropriateWord(word)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isSuperInappropriateWord(String word) {
        for (String superInappropriateRegex : superInappropriateRegexes) {
            if (word.toLowerCase().matches(superInappropriateRegex)) {
                return true;
            }
        }

        for (String superInappropriateWord : superInappropriateWords) {
            if (word.equalsIgnoreCase(superInappropriateWord)) {
                return true;
            }
        }

        return false;
    }
}