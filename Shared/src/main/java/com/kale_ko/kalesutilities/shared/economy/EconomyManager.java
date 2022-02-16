package com.kale_ko.kalesutilities.shared.economy;

import com.kale_ko.kalesutilities.shared.PluginConfig;
import com.kale_ko.kalesutilities.shared.util.GlobalPlayer;

public class EconomyManager {
    PluginConfig data;

    public EconomyManager(PluginConfig data) {
        this.data = data;
    }

    public float getBalance(GlobalPlayer player) {
        Float balance = data.getFloat(player.getName() + ".balance");

        if (balance != null) {
            return balance;
        } else {
            setBalance(player, 0);

            return 0;
        }
    }

    public void setBalance(GlobalPlayer player, float balance) {
        data.set(player.getName() + ".balance", balance);

        data.save();
    }

    public void depositPlayer(GlobalPlayer player, float balance) {
        setBalance(player, getBalance(player) + balance);
    }

    public void withdrawPlayer(GlobalPlayer player, float balance) {
        setBalance(player, getBalance(player) - balance);
    }
}