package io.github.evercraftmc.evercraft.spigot.util.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import net.kyori.adventure.text.Component;

public class SerializableItemStack {
    private Material type;
    private String name;
    private Integer amount;
    private Integer damage;

    private List<String> lore;
    private List<SerializableEnchantment> enchantments;

    public SerializableItemStack(Material type, String name, Integer amount, Integer damage, List<String> lore, List<SerializableEnchantment> enchantments) {
        this.type = type;
        this.name = name;
        this.amount = amount;
        this.damage = damage;

        this.lore = lore;
        this.enchantments = enchantments;
    }

    public Material getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public Integer getDamage() {
        return this.damage;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public List<SerializableEnchantment> getEnchantments() {
        return this.enchantments;
    }

    public ItemStack toBukkitItemStack() {
        ItemStack stack = new ItemStack(this.getType(), this.getAmount());

        if (this.getName() != null) {
            stack.setDisplayName(this.getName());
        }

        if (this.getDamage() != null) {
            stack.setDamage(this.getDamage());
        }

        if (this.getLore().size() > 0) {
            List<Component> loreComponents = new ArrayList<Component>();

            for (String line : this.getLore()) {
                loreComponents.add(ComponentFormatter.stringToComponent(line));
            }

            stack.lore(loreComponents);
        }

        for (SerializableEnchantment enchantment : this.getEnchantments()) {
            stack.addUnsafeEnchantment(enchantment.getEnchantment(), enchantment.getLevel());
        }

        return stack;
    }

    public static SerializableItemStack fromBukkitItemStack(ItemStack stack) {
        List<String> lore = new ArrayList<String>();

        if (stack.hasLore()) {
            for (Component component : stack.lore()) {
                lore.add(ComponentFormatter.componentToString(component));
            }
        }

        List<SerializableEnchantment> enchantments = new ArrayList<SerializableEnchantment>();

        if (stack.hasEnchants()) {
            for (Map.Entry<Enchantment, Integer> enchantment : stack.getEnchants().entrySet()) {
                enchantments.add(SerializableEnchantment.fromBukkitEnchantment(enchantment.getKey(), enchantment.getValue()));
            }
        }

        return new SerializableItemStack(stack.getType(), stack.hasDisplayName() ? ComponentFormatter.componentToString(stack.displayName()) : null, stack.getAmount(), stack.hasDamage() ? stack.getDamage() : null, lore, enchantments);
    }
}