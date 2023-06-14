package io.github.evercraftmc.core.messaging;

public class ECMessage {
    protected ECSender sender;
    protected ECRecipient recipient;

    protected byte[] data;
    protected int size;

    public ECMessage(ECSender sender, ECRecipient recipient, byte[] data) {
        this(sender, recipient, data, data.length);
    }

    public ECMessage(ECSender sender, ECRecipient recipient, byte[] data, int size) {
        this.sender = sender;
        this.recipient = recipient;

        this.data = data;
        this.size = size;
    }

    public ECSender getSender() {
        return this.sender;
    }

    public ECRecipient getRecipient() {
        return this.recipient;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getSize() {
        return this.size;
    }
}