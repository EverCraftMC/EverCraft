package io.github.evercraftmc.core.messaging;

public class ECMessage {
    protected String sender;

    protected int size;
    protected byte[] data;

    public ECMessage(String sender, byte[] data) {
        this.sender = sender;

        this.size = data.length;
        this.data = data;
    }

    public ECMessage(String sender, int size, byte[] data) {
        this.sender = sender;

        this.size = size;
        this.data = data;
    }

    public String getSender() {
        return this.sender;
    }

    public int getSize() {
        return this.size;
    }

    public byte[] getData() {
        return this.data;
    }
}