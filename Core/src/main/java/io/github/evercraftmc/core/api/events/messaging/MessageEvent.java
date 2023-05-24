package io.github.evercraftmc.core.api.events.messaging;

import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.messaging.ECMessage;
import io.github.evercraftmc.core.messaging.ECMessager;

public class MessageEvent extends ECEvent {
    protected ECMessager messager;

    protected ECMessage message;

    public MessageEvent(ECMessager messager, ECMessage message) {
        this.messager = messager;

        this.message = message;
    }

    public ECMessager getMessager() {
        return this.messager;
    }

    public ECMessage getMessage() {
        return this.message;
    }
}