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

        while (difference > 1l * 60l * 60l * 24l * 7l * 30l * 365l) {
            difference -= 1l * 60l * 60l * 24l * 7l * 30l * 365l;

            years++;
        }

        if (years != 0) {
            differentString.append(years + " years ");
        }

        while (difference > 1l * 60l * 60l * 24l * 7l * 30l) {
            difference -= 1l * 60l * 60l * 24l * 7l * 30l;

            months++;
        }

        if (months != 0) {
            differentString.append(months + " months ");
        }

        while (difference > 1l * 60l * 60l * 24l * 7l) {
            difference -= 1l * 60l * 60l * 24l * 7l;

            weeks++;
        }

        if (weeks != 0 && years == 0) {
            differentString.append(weeks + " weeks ");
        }

        while (difference > 1l * 60l * 60l * 24l) {
            difference -= 1l * 60l * 60l * 24l;

            days++;
        }

        if (days != 0 && years == 0 && months == 0) {
            differentString.append(days + " days ");
        }

        while (difference > 1l * 60l * 60l) {
            difference -= 1l * 60l * 60l;

            hours++;
        }

        if (hours != 0 && years == 0 && months == 0 && weeks == 0) {
            differentString.append(hours + " hours ");
        }

        while (difference > 1l * 60l) {
            difference -= 1l * 60l;

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

    public static Long unitMultiplier(String unit) {
        if (unit.equalsIgnoreCase("s") || unit.equalsIgnoreCase("sec") || unit.equalsIgnoreCase("second")) {
            return 1l;
        } else if (unit.equalsIgnoreCase("m") || unit.equalsIgnoreCase("min") || unit.equalsIgnoreCase("minute")) {
            return 1l * 60l;
        } else if (unit.equalsIgnoreCase("h") || unit.equalsIgnoreCase("hour")) {
            return 1l * 60l * 60l;
        } else if (unit.equalsIgnoreCase("d") || unit.equalsIgnoreCase("day")) {
            return 1l * 60l * 60l * 24l;
        } else if (unit.equalsIgnoreCase("w") || unit.equalsIgnoreCase("week")) {
            return 1l * 60l * 60l * 24l * 7l;
        } else if (unit.equalsIgnoreCase("mon") || unit.equalsIgnoreCase("month")) {
            return 1l * 60l * 60l * 24l * 7l * 30l;
        } else if (unit.equalsIgnoreCase("y") || unit.equalsIgnoreCase("year")) {
            return 1l * 60l * 60l * 24l * 7l * 30l * 365l;
        } else {
            return null;
        }
    }
}