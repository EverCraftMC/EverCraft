/*
 * Decompiled with CFR 0.152.
 */
package io.github.evercraftmc.core.api.commands;

import io.github.evercraftmc.core.api.server.player.ECPlayer;
import java.util.List;

public interface ECCommand {
    public String getName();

    public String getDescription();

    public List<String> getAlias();

    public String getPermission();

    public void run(ECPlayer var1, String[] var2);

    public List<String> tabComplete(ECPlayer var1, String[] var2);
}

