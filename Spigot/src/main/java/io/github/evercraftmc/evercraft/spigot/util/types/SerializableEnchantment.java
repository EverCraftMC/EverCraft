package io.github.evercraftmc.evercraft.spigot.util.types;

import org.bukkit.enchantments.Enchantment;

public class SerializableEnchantment {
    private Enchantment enchantment;
    private Integer level;

    public SerializableEnchantment(Enchantment enchantment, Integer level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
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