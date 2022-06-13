package io.github.evercraftmc.evercraft.shared.util;

import java.time.Instant;

public class TimeUtil {
    public static Long getDifference(Instant to, Instant from) {
        return to.getEpochSecond() - from.getEpochSecond();
    }

    public static String getDifferenceString(Instant to, Instant from) {
        Long difference = Math.abs(getDifference(to, from));

        StringBuilder differentString = new StringBuilder();

        Integer years = 0;
        Integer months = 0;
        Integer weeks = 0;
        Integer days = 0;
        Integer hours = 0;
        Integer minutes = 0;
        Integer seconds = 0;

        while (difference > 1 * 60 * 60 * 24 * 7 * 30 * 365) {
            difference -= 1 * 60 * 60 * 24 * 7 * 30 * 365;

            years++;
        }

        if (years != 0) {
            differentString.append(years + " years ");
        }

        while (difference > 1 * 60 * 60 * 24 * 7 * 30) {
            difference -= 1 * 60 * 60 * 24 * 7 * 30;

            months++;
        }

        if (months != 0) {
            differentString.append(months + " months ");
        }

        while (difference > 1 * 60 * 60 * 24 * 7) {
            difference -= 1 * 60 * 60 * 24 * 7;

            weeks++;
        }

        if (weeks != 0 && years == 0) {
            differentString.append(weeks + " weeks ");
        }

        while (difference > 1 * 60 * 60 * 24) {
            difference -= 1 * 60 * 60 * 24;

            days++;
        }

        if (days != 0 && years == 0 && months == 0) {
            differentString.append(days + " days ");
        }

        while (difference > 1 * 60 * 60) {
            difference -= 1 * 60 * 60;

            hours++;
        }

        if (hours != 0 && years == 0 && months == 0 && weeks == 0) {
            differentString.append(hours + " hours ");
        }

        while (difference > 1 * 60) {
            difference -= 1 * 60;

            minutes++;
        }

        if (minutes != 0 && years == 0 && months == 0 && weeks == 0 && days == 0) {
            differentString.append(minutes + " minutes ");
        }

        while (difference > 1) {
            difference -= 1;

            seconds++;
        }

        if ((seconds != 0 || (years == 0 && months == 0 && weeks == 0 && days == 0 && hours == 0 && minutes == 0)) && years == 0 && months == 0 && weeks == 0 && days == 0 && hours == 0) {
            differentString.append(seconds + " seconds ");
        }

        return differentString.toString().trim();
    }

    public static Instant parseFuture(String string) {
        try {
            if (unitMultiplier(string.replaceAll("[0-9]", "")) != null) {
                return Instant.now().plusSeconds(Integer.parseInt(string.replaceAll("[^0-9]", "")) * unitMultiplier(string.replaceAll("[0-9 ]", "")));
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer unitMultiplier(String unit) {
        if (unit.equalsIgnoreCase("s") || unit.equalsIgnoreCase("sec") || unit.equalsIgnoreCase("second")) {
            return 1;
        } else if (unit.equalsIgnoreCase("m") || unit.equalsIgnoreCase("min") || unit.equalsIgnoreCase("minute")) {
            return 1 * 60;
        } else if (unit.equalsIgnoreCase("h") || unit.equalsIgnoreCase("hour")) {
            return 1 * 60 * 60;
        } else if (unit.equalsIgnoreCase("d") || unit.equalsIgnoreCase("day")) {
            return 1 * 60 * 60 * 24;
        } else if (unit.equalsIgnoreCase("w") || unit.equalsIgnoreCase("week")) {
            return 1 * 60 * 60 * 24 * 7;
        } else if (unit.equalsIgnoreCase("mon") || unit.equalsIgnoreCase("month")) {
            return 1 * 60 * 60 * 24 * 7 * 30;
        } else if (unit.equalsIgnoreCase("y") || unit.equalsIgnoreCase("year")) {
            return 1 * 60 * 60 * 24 * 7 * 30 * 365;
        } else {
            return null;
        }
    }
}