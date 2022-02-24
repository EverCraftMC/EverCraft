package com.kale_ko.kalesutilities.shared.economy;

import com.kale_ko.kalesutilities.shared.PluginConfig;
import com.kale_ko.kalesutilities.shared.util.GlobalPlayer;

public class EconomyManager {
    PluginConfig data;

    public EconomyManager(PluginConfig data) {
        this.data = data;
    }

    public float getBalance(String name) {
        Float balance = data.getFloat("players." + name + ".balance");

        if (balance != null) {
            return balance;
        } else {
            setBalance(name, 0);

            return 0;
        }
    }

    public float getBalance(GlobalPlayer player) {
        return getBalance(player.getName());
    }

    public void setBalance(String name, float balance) {
        data.set("players." + name + ".balance", balance);

        data.save();
    }

    public void setBalance(GlobalPlayer player, float balance) {
        setBalance(player.getName(), balance);
    }

    public void depositPlayer(String name, float balance) {
        setBalance(name, getBalance(name) + balance);
    }

    public void depositPlayer(GlobalPlayer player, float balance) {
        depositPlayer(player.getName(), balance);
    }

    public void withdrawPlayer(String name, float balance) {
        setBalance(name, getBalance(name) - balance);
    }

    public void withdrawPlayer(GlobalPlayer player, float balance) {
        withdrawPlayer(player.getName(), balance);
    }
}