package com.kale_ko.kales_utilities.listeners;

import com.kale_ko.kales_utilities.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChange implements Listener {
    @EventHandler
    public void onSignUpdate(SignChangeEvent event) {
        event.setLine(0, Util.formatMessage(event.getLine(0)));
        event.setLine(1, Util.formatMessage(event.getLine(1)));
        event.setLine(2, Util.formatMessage(event.getLine(2)));
        event.setLine(3, Util.formatMessage(event.getLine(3)));
    }
}