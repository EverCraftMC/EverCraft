package com.kale_ko.evercraft.shared.economy;

import java.util.UUID;
import com.kale_ko.evercraft.shared.config.Config;

public class Economy {
    private Config config;

    public Economy(Config config) {
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
        this.config.save();
    }

    public void deposit(UUID uuid, Float balance) {
        setBalance(uuid, getBalance(uuid) + balance);
    }

    public void withdraw(UUID uuid, Float balance) {
        setBalance(uuid, getBalance(uuid) - balance);
    }
}