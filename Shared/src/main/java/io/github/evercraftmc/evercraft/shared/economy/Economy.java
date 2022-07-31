package io.github.evercraftmc.evercraft.shared.economy;

import java.util.UUID;
import io.github.evercraftmc.evercraft.shared.PluginData;
import io.github.evercraftmc.evercraft.shared.config.Config;

public class Economy {
    private Config<PluginData> config;

    public Economy(Config<PluginData> config) {
        this.config = config;
    }

    public Float getBalance(UUID uuid) {
        if (this.config.getParsed().players.get(uuid.toString()).balance != null) {
            return this.config.getParsed().players.get(uuid.toString()).balance;
        } else {
            this.setBalance(uuid, 0f);

            return 0f;
        }
    }

    public void setBalance(UUID uuid, Float balance) {
        config.getParsed().players.get(uuid.toString()).balance = balance;
    }

    public void deposit(UUID uuid, Float balance) {
        setBalance(uuid, getBalance(uuid) + balance);
    }

    public void withdraw(UUID uuid, Float balance) {
        setBalance(uuid, getBalance(uuid) - balance);
    }
}