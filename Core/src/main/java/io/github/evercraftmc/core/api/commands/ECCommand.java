package io.github.evercraftmc.core.api.commands;

import io.github.evercraftmc.core.api.server.player.ECPlayer;
import java.util.List;

public interface ECCommand {
    String getName();

    String getDescription();

    List<String> getAlias();

    String getPermission();

    void run(ECPlayer player, String[] args, boolean sendFeedback);

    List<String> tabComplete(ECPlayer player, String[] args);
}