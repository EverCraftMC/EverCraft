package io.github.evercraftmc.core.messaging;

import io.github.evercraftmc.core.api.server.ECServer;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public abstract class ECSender {
    public static class Server extends ECSender {
        protected @NotNull UUID server;

        public Server(@NotNull UUID server) {
            this.server = server;
        }

        @Override
        public @NotNull String toString() {
            return this.server.toString().toUpperCase();
        }
    }

    @Override
    public abstract @NotNull String toString();

    public boolean matches(@NotNull ECServer server) {
        return this instanceof Server && this.toString().equalsIgnoreCase(server.getPlugin().getMessenger().id.toString());
    }

    public static @NotNull ECSender parse(@NotNull String string) {
        if (string.length() == 36) {
            return ECSender.fromServer(UUID.fromString(string));
        } else {
            throw new RuntimeException("Failed to parse sender \"" + string + "\"");
        }
    }

    public static @NotNull ECSender fromServer(@NotNull UUID server) {
        return new ECSender.Server(server);
    }
}