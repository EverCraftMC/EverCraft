package io.github.evercraftmc.core.messaging;

import io.github.evercraftmc.core.api.server.ECServer;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.ECEnvironmentType;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public abstract class ECRecipient {
    public static class All extends ECRecipient {
        public All() {
        }

        @Override
        public @NotNull String toString() {
            return "ALL";
        }
    }

    public static class Server extends ECRecipient {
        protected final @NotNull UUID server;

        public Server(@NotNull UUID server) {
            this.server = server;
        }

        @Override
        public @NotNull String toString() {
            return this.server.toString().toUpperCase();
        }
    }

    public static class Environment extends ECRecipient {
        protected final @NotNull ECEnvironment environment;

        public Environment(@NotNull ECEnvironment environment) {
            this.environment = environment;
        }

        @Override
        public @NotNull String toString() {
            return this.environment.toString().toUpperCase();
        }
    }

    public static class EnvironmentType extends ECRecipient {
        protected final @NotNull ECEnvironmentType environmentType;

        public EnvironmentType(@NotNull ECEnvironmentType environmentType) {
            this.environmentType = environmentType;
        }

        @Override
        public @NotNull String toString() {
            return this.environmentType.toString().toUpperCase();
        }
    }

    @Override
    public abstract @NotNull String toString();

    public boolean matches(@NotNull ECServer server) {
        if (this instanceof ECRecipient.All) {
            return true;
        }

        if (this instanceof ECRecipient.Server && this.toString().equalsIgnoreCase(server.getPlugin().getMessenger().id.toString())) {
            return true;
        }

        if (this instanceof ECRecipient.Environment && this.toString().equalsIgnoreCase(server.getEnvironment().toString())) {
            return true;
        }

        return this instanceof EnvironmentType && this.toString().equalsIgnoreCase(server.getEnvironment().getType().toString());
    }

    public static @NotNull ECRecipient parse(@NotNull String string) {
        if (string.equalsIgnoreCase("ALL")) {
            return ECRecipient.fromAll();
        } else if (string.length() == 36) {
            return ECRecipient.fromServer(UUID.fromString(string.toUpperCase()));
        } else if (ECEnvironmentType.valueOf(string.toUpperCase()) != null) {
            return ECRecipient.fromEnvironmentType(ECEnvironmentType.valueOf(string.toUpperCase()));
        } else if (ECEnvironment.valueOf(string.toUpperCase()) != null) {
            return ECRecipient.fromEnvironment(ECEnvironment.valueOf(string.toUpperCase()));
        } else {
            throw new RuntimeException("Failed to parse recipient \"" + string + "\"");
        }
    }

    public static @NotNull ECRecipient fromAll() {
        return new ECRecipient.All();
    }

    public static @NotNull ECRecipient fromServer(@NotNull UUID server) {
        return new ECRecipient.Server(server);
    }

    public static @NotNull ECRecipient fromEnvironment(@NotNull ECEnvironment environment) {
        return new ECRecipient.Environment(environment);
    }

    public static @NotNull ECRecipient fromEnvironmentType(@NotNull ECEnvironmentType environmentType) {
        return new ECRecipient.EnvironmentType(environmentType);
    }
}