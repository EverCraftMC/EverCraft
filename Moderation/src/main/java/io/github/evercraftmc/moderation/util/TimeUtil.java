package io.github.evercraftmc.moderation.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class TimeUtil {
    private TimeUtil() {
    }

    private static final Map<String, Long> longUnits = new LinkedHashMap<>();
    private static final Map<String, Long> shortUnits = new LinkedHashMap<>();

    private static final Map<String, Long> allUnits = new LinkedHashMap<>();
    private static final DateFormat timeFormat = DateFormat.getDateTimeInstance();

    static {
        shortUnits.put("y", 60L * 60L * 24L * 365L);
        shortUnits.put("w", 60L * 60L * 24L * 7L);
        shortUnits.put("d", 60L * 60L * 24L);
        shortUnits.put("h", 60L * 60L);
        shortUnits.put("m", 60L);
        shortUnits.put("s", 1L);

        longUnits.put("year", 60L * 60L * 24L * 365L);
        longUnits.put("month", 60L * 60L * 24L * 30L);
        longUnits.put("week", 60L * 60L * 24L * 7L);
        longUnits.put("day", 60L * 60L * 24L);
        longUnits.put("hour", 60L * 60L);
        longUnits.put("minute", 60L);
        longUnits.put("second", 1L);

        allUnits.putAll(shortUnits);
        allUnits.putAll(longUnits);

        allUnits.put("min", 60L);
        allUnits.put("sec", 1L);
    }

    public static Instant parseTime(String string) {
        try {
            return timeFormat.parse(string).toInstant();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Instant parseFuture(String string) {
        if (string.equalsIgnoreCase("forever") || string.equalsIgnoreCase("infinity")) {
            return Instant.ofEpochMilli(0);
        }

        long total = 0;
        for (String part : string.split("[-_ ]")) {
            String unitName = part.replaceAll("[^a-zA-Z]", "").toLowerCase();
            if (unitName.length() > 3 && unitName.endsWith("s")) {
                unitName = unitName.substring(0, unitName.length() - 1);
            }
            long unit = allUnits.get(unitName);
            double multi = Double.parseDouble(part.replaceAll("[^0-9.]", ""));

            total += Math.round(unit * multi);
        }

        return Instant.now().plus(total, ChronoUnit.SECONDS);
    }

    public static String stringifyTime(Instant instant) {
        return timeFormat.format(Date.from(instant));
    }

    public static String stringifyFuture(Instant instant) {
        return stringifyFuture(instant, false);
    }

    public static String stringifyFuture(Instant instant, boolean useLong) {
        if (instant.toEpochMilli() == 0) {
            return "forever";
        }

        long difference = instant.getEpochSecond() - Instant.now().getEpochSecond();
        boolean ripple = true;
        StringBuilder string = new StringBuilder();

        for (Map.Entry<String, Long> unit : (useLong ? longUnits : shortUnits).entrySet()) {
            long count = 0;
            while (difference >= unit.getValue()) {
                difference -= unit.getValue();
                count++;
            }

            if (!ripple || count > 0) {
                string.append(count).append(useLong ? " " : "").append(unit.getKey()).append((useLong && count != 1) ? "s" : "").append(" ");
                ripple = false;
            }
        }

        return string.toString().trim();
    }

    public static boolean isPast(Instant instant) {
        return isPast(Instant.now(), instant);
    }

    public static boolean isPast(Instant now, Instant instant) {
        if (instant.toEpochMilli() == 0) {
            return false;
        }

        return now.isAfter(instant);
    }
}