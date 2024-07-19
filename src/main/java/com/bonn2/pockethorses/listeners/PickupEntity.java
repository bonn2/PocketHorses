package com.bonn2.pockethorses.listeners;

import de.tr7zw.nbtapi.NBTEntity;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.bonn2.pockethorses.PocketHorses.plugin;

public class PickupEntity implements Listener {

    public static NamespacedKey typeKey = new NamespacedKey(plugin, "type");
    public static NamespacedKey dataKey = new NamespacedKey(plugin, "data");

    @EventHandler
    public void onClickEntity(@NotNull PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getConfig().getList("AllowedWorlds").contains(player.getWorld().getName())) return;

        if (!player.isSneaking()) return;
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (!heldItem.getType().equals(Material.AIR)) return;
        Entity clickedEntity = event.getRightClicked();
        ItemStack saddle = new ItemStack(Material.SADDLE);
        switch (clickedEntity.getType()) {
            case HORSE -> {
                // Convert horse to json
                Horse horse = (Horse) clickedEntity;
                NBTEntity nbtHorse = new NBTEntity(horse);

                if (horse.getInventory().getSaddle() == null) return;
                if (!horse.getPassengers().isEmpty()) return;

                // Check if saddle has horse in it
                if (horse.getInventory().getStorageContents()[0].getItemMeta().getPersistentDataContainer().has(typeKey, PersistentDataType.STRING))
                    return;

                // Set persistent data in temp item meta
                ItemMeta meta = saddle.getItemMeta();
                meta.getPersistentDataContainer().set(typeKey, PersistentDataType.STRING, "HORSE");
                meta.getPersistentDataContainer().set(dataKey, PersistentDataType.STRING, nbtHorse.toString());
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.DURABILITY, 1, false);

                try {
                    meta.displayName(Component.text(horse.getCustomName()));
                } catch (NullPointerException ignored) {}

                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("§dType: Horse"));
                lore.add(Component.text(String.format("§cHealth: %d / %d", (int) horse.getHealth(), (int) horse.getMaxHealth())));
                lore.add(Component.text(String.format("§eColor: %s", horse.getColor())));
                lore.add(Component.text(String.format("§eStyle: %s", horse.getStyle())));

                meta.lore(lore);

                horse.remove();

                // Give player the saddle
                saddle.setItemMeta(meta);
                player.getInventory().setItemInMainHand(saddle);
            }
            case SKELETON_HORSE -> {
                // Convert horse to json
                SkeletonHorse horse = (SkeletonHorse) clickedEntity;
                NBTEntity nbtHorse = new NBTEntity(horse);

                if (horse.getInventory().getSaddle() == null) return;
                if (!horse.getPassengers().isEmpty()) return;

                // Check if saddle has horse in it
                if (horse.getInventory().getStorageContents()[0].getItemMeta().getPersistentDataContainer().has(typeKey, PersistentDataType.STRING))
                    return;

                // Set persistent data in temp item meta
                ItemMeta meta = saddle.getItemMeta();
                meta.getPersistentDataContainer().set(typeKey, PersistentDataType.STRING, "SKELETON_HORSE");
                meta.getPersistentDataContainer().set(dataKey, PersistentDataType.STRING, nbtHorse.toString());
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.DURABILITY, 1, false);

                try {
                    meta.displayName(Component.text(horse.getCustomName()));
                } catch (NullPointerException ignored) {}

                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("§dType: Skeleton Horse"));
                lore.add(Component.text(String.format("§cHealth: %d / %d", (int) horse.getHealth(), (int) horse.getMaxHealth())));

                meta.lore(lore);

                horse.remove();

                // Give player the saddle
                saddle.setItemMeta(meta);
                player.getInventory().setItemInMainHand(saddle);
            }
            case ZOMBIE_HORSE -> {
                // Convert horse to json
                ZombieHorse horse = (ZombieHorse) clickedEntity;
                NBTEntity nbtHorse = new NBTEntity(horse);

                if (horse.getInventory().getSaddle() == null) return;
                if (!horse.getPassengers().isEmpty()) return;

                // Check if saddle has horse in it
                if (horse.getInventory().getStorageContents()[0].getItemMeta().getPersistentDataContainer().has(typeKey, PersistentDataType.STRING))
                    return;

                // Set persistent data in temp item meta
                ItemMeta meta = saddle.getItemMeta();
                meta.getPersistentDataContainer().set(typeKey, PersistentDataType.STRING, "ZOMBIE_HORSE");
                meta.getPersistentDataContainer().set(dataKey, PersistentDataType.STRING, nbtHorse.toString());
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.DURABILITY, 1, false);

                try {
                    meta.displayName(Component.text(horse.getCustomName()));
                } catch (NullPointerException ignored) {}

                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("§dType: Zombie Horse"));
                lore.add(Component.text(String.format("§cHealth: %d / %d", (int) horse.getHealth(), (int) horse.getMaxHealth())));

                meta.lore(lore);

                horse.remove();

                // Give player the saddle
                saddle.setItemMeta(meta);
                player.getInventory().setItemInMainHand(saddle);
            }
            case DONKEY -> {
                // Convert horse to json
                Donkey horse = (Donkey) clickedEntity;
                NBTEntity nbtHorse = new NBTEntity(horse);

                if (horse.getInventory().getSaddle() == null) return;
                if (!horse.getPassengers().isEmpty()) return;

                // Check for empty inventory
                ItemStack[] inventory = horse.getInventory().getStorageContents();
                for (int i = 1; i < inventory.length; i++)
                    if (inventory[i] != null)
                        return;

                // Check if saddle has horse in it
                if (inventory[0].getItemMeta().getPersistentDataContainer().has(typeKey, PersistentDataType.STRING))
                    return;

                // Set persistent data in temp item meta
                ItemMeta meta = saddle.getItemMeta();
                meta.getPersistentDataContainer().set(typeKey, PersistentDataType.STRING, "DONKEY");
                meta.getPersistentDataContainer().set(dataKey, PersistentDataType.STRING, nbtHorse.toString());
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.DURABILITY, 1, false);

                try {
                    meta.displayName(Component.text(horse.getCustomName()));
                } catch (NullPointerException ignored) {}

                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("§dType: Donkey"));
                lore.add(Component.text(String.format("§cHealth: %d / %d", (int) horse.getHealth(), (int) horse.getMaxHealth())));

                meta.lore(lore);

                horse.remove();

                // Give player the saddle
                saddle.setItemMeta(meta);
                player.getInventory().setItemInMainHand(saddle);
            }
            case MULE -> {
                // Convert horse to json
                Mule horse = (Mule) clickedEntity;
                NBTEntity nbtHorse = new NBTEntity(horse);

                if (horse.getInventory().getSaddle() == null) return;
                if (!horse.getPassengers().isEmpty()) return;

                // Check for empty inventory
                ItemStack[] inventory = horse.getInventory().getStorageContents();
                for (int i = 1; i < inventory.length; i++)
                    if (inventory[i] != null)
                        return;

                // Check if saddle has horse in it
                if (inventory[0].getItemMeta().getPersistentDataContainer().has(typeKey, PersistentDataType.STRING))
                    return;

                // Set persistent data in temp item meta
                ItemMeta meta = saddle.getItemMeta();
                meta.getPersistentDataContainer().set(typeKey, PersistentDataType.STRING, "MULE");
                meta.getPersistentDataContainer().set(dataKey, PersistentDataType.STRING, nbtHorse.toString());
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.DURABILITY, 1, false);

                try {
                    meta.displayName(Component.text(horse.getCustomName()));
                } catch (NullPointerException ignored) {}

                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("§dType: Mule"));
                lore.add(Component.text(String.format("§cHealth: %d / %d", (int) horse.getHealth(), (int) horse.getMaxHealth())));

                meta.lore(lore);

                horse.remove();

                // Give player the saddle
                saddle.setItemMeta(meta);
                player.getInventory().setItemInMainHand(saddle);
            }
            case STRIDER -> {
                Strider strider = (Strider) event.getRightClicked();
                if (!strider.hasSaddle()) return;
                if (!strider.getPassengers().isEmpty()) return;

                NBTEntity nbtStrider = new NBTEntity(strider);

                // Set persistent data in temp item meta
                ItemMeta meta = saddle.getItemMeta();
                meta.getPersistentDataContainer().set(typeKey, PersistentDataType.STRING, "STRIDER");
                meta.getPersistentDataContainer().set(dataKey, PersistentDataType.STRING, nbtStrider.toString());
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.DURABILITY, 1, false);

                try {
                    meta.displayName(Component.text(strider.getCustomName()));
                } catch (NullPointerException ignored) {}

                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("§dType: Strider"));
                lore.add(Component.text(String.format("§cHealth: %d / %d", (int) strider.getHealth(), (int) strider.getMaxHealth())));

                meta.lore(lore);

                strider.remove();

                // Give player the saddle
                saddle.setItemMeta(meta);
                player.getInventory().setItemInMainHand(saddle);
            }
            case PIG -> {
                Pig pig = (Pig) event.getRightClicked();
                if (!pig.hasSaddle()) return;
                if (!pig.getPassengers().isEmpty()) return;

                NBTEntity nbtStrider = new NBTEntity(pig);

                // Set persistent data in temp item meta
                ItemMeta meta = saddle.getItemMeta();
                meta.getPersistentDataContainer().set(typeKey, PersistentDataType.STRING, "PIG");
                meta.getPersistentDataContainer().set(dataKey, PersistentDataType.STRING, nbtStrider.toString());
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.DURABILITY, 1, false);

                try {
                    meta.displayName(Component.text(pig.getCustomName()));
                } catch (NullPointerException ignored) {}

                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("§dType: Pig"));
                lore.add(Component.text(String.format("§cHealth: %d / %d", (int) pig.getHealth(), (int) pig.getMaxHealth())));

                meta.lore(lore);

                pig.remove();

                // Give player the saddle
                saddle.setItemMeta(meta);
                player.getInventory().setItemInMainHand(saddle);
            }
        }
        player.closeInventory();
        event.setCancelled(true);
    }
}
