package io.github.evercraftmc.core.messaging;

import io.github.evercraftmc.core.api.server.ECServer;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.ECEnvironmentType;
import java.util.UUID;

public abstract class ECRecipient {
    public static class All extends ECRecipient {
        public All() {
        }

        @Override
        public String toString() {
            return "ALL";
        }
    }

    public static class Server extends ECRecipient {
        protected UUID server;

        public Server(UUID server) {
            this.server = server;
        }

        @Override
        public String toString() {
            return this.server.toString().toUpperCase();
        }
    }

    public static class Environment extends ECRecipient {
        protected ECEnvironment environment;

        public Environment(ECEnvironment environment) {
            this.environment = environment;
        }

        @Override
        public String toString() {
            return this.environment.toString().toUpperCase();
        }
    }

    public static class EnvironmentType extends ECRecipient {
        protected ECEnvironmentType environmentType;

        public EnvironmentType(ECEnvironmentType environmentType) {
            this.environmentType = environmentType;
        }

        @Override
        public String toString() {
            return this.environmentType.toString().toUpperCase();
        }
    }

    @Override
    public abstract String toString();

    public boolean matches(ECServer server) {
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

    public static ECRecipient parse(String string) {
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

    public static ECRecipient fromAll() {
        return new ECRecipient.All();
    }

    public static ECRecipient fromServer(UUID server) {
        return new ECRecipient.Server(server);
    }

    public static ECRecipient fromEnvironment(ECEnvironment environment) {
        return new ECRecipient.Environment(environment);
    }

    public static ECRecipient fromEnvironmentType(ECEnvironmentType environmentType) {
        return new ECRecipient.EnvironmentType(environmentType);
    }
}