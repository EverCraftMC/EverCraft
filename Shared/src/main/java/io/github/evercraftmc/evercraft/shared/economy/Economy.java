package io.github.evercraftmc.evercraft.shared.economy;

import java.util.UUID;
import io.github.evercraftmc.evercraft.shared.config.MySQLConfig;

public class Economy {
    private MySQLConfig config;

    public Economy(MySQLConfig config) {
        this.config = config;
    }

    public Float getBalance(UUID uuid) {
        if (this.config.getFloat("players." + uuid + ".balance") != null) {
            return this.config.getFloat("players." + uuid + ".balance");
        } else {
            this.setBalance(uuid, 0f);

            return 0f;
        }
    }

    public void setBalance(UUID uuid, Float balance) {
        this.config.set("players." + uuid + ".balance", balance);
    }

    public void deposit(UUID uuid, Float balance) {
        setBalance(uuid, getBalance(uuid) + balance);
    }

    public void withdraw(UUID uuid, Float balance) {
        setBalance(uuid, getBalance(uuid) - balance);
    }
}