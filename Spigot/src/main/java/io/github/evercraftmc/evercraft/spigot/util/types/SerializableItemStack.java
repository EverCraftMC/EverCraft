package io.github.evercraftmc.evercraft.spigot.util.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import net.kyori.adventure.text.Component;

public class SerializableItemStack {
    private Material type;
    private String name;
    private Integer amount;
    private Integer damage;

    private List<String> lore;
    private List<SerializableEnchantment> enchantments;

    public SerializableItemStack() {
        this(Material.STONE, null, 1, 0, Arrays.asList(), Arrays.asList());
    }

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
        ItemMeta meta = stack.getItemMeta();

        if (this.getName() != null) {
            meta.displayName(ComponentFormatter.stringToComponent(TextFormatter.translateColors(this.getName())));
        }

        if (this.getDamage() != null && meta instanceof Damageable damageable) {
            damageable.setDamage(this.getDamage());
        }

        if (this.getLore() != null && this.getLore().size() > 0) {
            List<Component> loreComponents = new ArrayList<Component>();

            for (String line : this.getLore()) {
                loreComponents.add(ComponentFormatter.stringToComponent(line));
            }

            meta.lore(loreComponents);
        }

        if (this.getEnchantments() != null) {
            for (SerializableEnchantment enchantment : this.getEnchantments()) {
                meta.addEnchant(enchantment.getEnchantment(), enchantment.getLevel(), true);
            }
        }

        stack.setItemMeta(meta);

        return stack;
    }

    public static SerializableItemStack fromBukkitItemStack(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();

        List<String> lore = new ArrayList<String>();

        if (meta.hasLore()) {
            for (Component component : meta.lore()) {
                lore.add(ComponentFormatter.componentToString(component));
            }
        }

        List<SerializableEnchantment> enchantments = new ArrayList<SerializableEnchantment>();

        if (meta.hasEnchants()) {
            for (Map.Entry<Enchantment, Integer> enchantment : meta.getEnchants().entrySet()) {
                enchantments.add(SerializableEnchantment.fromBukkitEnchantment(enchantment.getKey(), enchantment.getValue()));
            }
        }

        return new SerializableItemStack(stack.getType(), meta.hasDisplayName() ? ComponentFormatter.componentToString(meta.displayName()) : null, stack.getAmount(), meta instanceof Damageable damageable && damageable.hasDamage() ? damageable.getDamage() : null, lore, enchantments);
    }
}