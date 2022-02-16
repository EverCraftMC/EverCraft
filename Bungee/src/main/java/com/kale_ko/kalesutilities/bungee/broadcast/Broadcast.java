package com.kale_ko.kalesutilities.bungee.broadcast;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import com.kale_ko.kalesutilities.bungee.BungeePlugin;
import com.kale_ko.kalesutilities.bungee.Util;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class Broadcast {
    public long intervail;

    private ScheduledTask task;

    public Broadcast(long intervail) {
        this.intervail = intervail;

        Random random = new Random();

        task = BungeePlugin.Instance.getProxy().getScheduler().schedule(BungeePlugin.Instance, new Runnable() {
            public void run() {
                List<String> broadcasts = BungeePlugin.Instance.config.getStringList("config.broadcastMessages");

                Util.broadcastMessage(broadcasts.get((int) random.nextDouble(broadcasts.size())));
            }
        }, 0, intervail, TimeUnit.SECONDS);
    }

    public void close() {
        task.cancel();
    }
}