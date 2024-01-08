package io.github.evercraftmc.core.messaging;

import org.jetbrains.annotations.NotNull;

public class ECMessage {
    protected final @NotNull ECSender sender;
    protected final @NotNull ECRecipient recipient;

    protected final byte @NotNull [] data;
    protected final int size;

    public ECMessage(@NotNull ECSender sender, @NotNull ECRecipient recipient, byte @NotNull [] data) {
        this(sender, recipient, data, data.length);
    }

    public ECMessage(@NotNull ECSender sender, @NotNull ECRecipient recipient, byte @NotNull [] data, int size) {
        this.sender = sender;
        this.recipient = recipient;

        this.data = data;
        this.size = size;
    }

    public @NotNull ECSender getSender() {
        return this.sender;
    }

    public @NotNull ECRecipient getRecipient() {
        return this.recipient;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getSize() {
        return this.size;
    }
}