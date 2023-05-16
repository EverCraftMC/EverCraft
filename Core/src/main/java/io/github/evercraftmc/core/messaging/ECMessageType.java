package io.github.evercraftmc.core.messaging;

public enum ECMessageType {
    HELLO(1);

    protected int code;

    private ECMessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}