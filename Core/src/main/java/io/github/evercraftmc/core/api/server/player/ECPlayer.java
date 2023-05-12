/*
 * Decompiled with CFR 0.152.
 */
package io.github.evercraftmc.core.api.server.player;

import java.util.UUID;

public interface ECPlayer {
    public UUID getUuid();

    public String getName();

    public String getDisplayName();

    public void setDisplayName(String var1);

    public void sendMessage(String var1);
}

