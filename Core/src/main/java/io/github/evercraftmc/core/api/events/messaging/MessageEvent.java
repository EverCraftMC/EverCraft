package io.github.evercraftmc.core.api.events.messaging;

import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.messaging.ECMessage;
import io.github.evercraftmc.core.messaging.ECMessenger;

public class MessageEvent extends ECEvent {
    protected ECMessenger messenger;

    protected ECMessage message;

    public MessageEvent(ECMessenger messenger, ECMessage message) {
        this.messenger = messenger;

        this.message = message;
    }

    public ECMessenger getMessenger() {
        return this.messenger;
    }

    public ECMessage getMessage() {
        return this.message;
    }
}