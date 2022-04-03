package io.github.evercraftmc.evercraft.shared.util;

import java.time.Instant;
import java.util.Date;

public class TimeUtil {
    public Date parseFuture(String string) {
        Instant date = Instant.now();

        return Date.from(date);
    }
}