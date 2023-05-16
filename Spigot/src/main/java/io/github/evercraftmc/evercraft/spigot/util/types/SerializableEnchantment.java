package io.github.evercraftmc.evercraft.spigot.util.types;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class SerializableEnchantment {
    private String enchantment;
    private Integer level;

    public SerializableEnchantment() {
        this(null, 1);
    }

    public SerializableEnchantment(Enchantment enchantment, Integer level) {
        this.enchantment = enchantment.getKey().getKey().toUpperCase();
        this.level = level;
    }

    public Enchantment getEnchantment() {
        return Enchantment.getByKey(NamespacedKey.minecraft(this.enchantment.toLowerCase()));
    }

    public Integer getLevel() {
        return this.level;
    }

    public Enchantment toBukkitEnchantment() {
        return this.getEnchantment();
    }

    public static SerializableEnchantment fromBukkitEnchantment(Enchantment enchantment) {
        return new SerializableEnchantment(enchantment, enchantment.getStartLevel());
    }

    public static SerializableEnchantment fromBukkitEnchantment(Enchantment enchantment, Integer level) {
        return new SerializableEnchantment(enchantment, level);
    }
}