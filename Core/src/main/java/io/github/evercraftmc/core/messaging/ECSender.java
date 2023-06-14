package io.github.evercraftmc.core.messaging;

import java.util.UUID;
import io.github.evercraftmc.core.api.server.ECServer;

public abstract class ECSender {
    public static class Server extends ECSender {
        protected UUID server;

        public Server(UUID server) {
            this.server = server;
        }

        @Override
        public String toString() {
            return this.server.toString().toUpperCase();
        }
    }

    @Override
    public abstract String toString();

    public boolean matches(ECServer server) {
        if (this instanceof ECSender.Server && this.toString().equalsIgnoreCase(server.getPlugin().getMessager().id.toString())) {
            return true;
        }

        return false;
    }

    public static ECSender parse(String string) {
        if (string.length() == 36) {
            return ECSender.fromServer(UUID.fromString(string));
        } else {
            throw new RuntimeException("Failed to parse sender \"" + string + "\"");
        }
    }

    public static ECSender fromServer(UUID server) {
        return new ECSender.Server(server);
    }
}