package io.github.evercraftmc.core.api.commands;

import java.util.List;
import io.github.evercraftmc.core.api.server.player.ECPlayer;

public interface ECCommand {
    public String getName();

    public String getDescription();

    public List<String> getAlias();

    public String getPermission();

    public void run(ECPlayer player, String[] args, boolean sendFeedback);

    public List<String> tabComplete(ECPlayer player, String[] args);
}