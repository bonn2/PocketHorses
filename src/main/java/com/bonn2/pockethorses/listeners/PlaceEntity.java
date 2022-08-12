package com.bonn2.pockethorses.listeners;

import com.bonn2.pockethorses.PocketHorses;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.bonn2.pockethorses.PocketHorses.plugin;

public class PlaceEntity implements Listener {

    public static NamespacedKey typeKey = new NamespacedKey(plugin, "type");
    public static NamespacedKey dataKey = new NamespacedKey(plugin, "data");

    @EventHandler
    public void onRightClickBlock(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getConfig().getList("AllowedWorlds").contains(player.getWorld().getName())) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();

        // Check if meta is valid
        if (meta == null) return;

        // Check if item has an entity
        if (!meta.getPersistentDataContainer().has(typeKey, PersistentDataType.STRING)) return;
        Block block = event.getClickedBlock();

        // Check if a block was clicked
        if (block == null) return;

        // Check for space to spawn entity
        Location spawnLocation = new Location(block.getWorld(), block.getX() + 0.5, block.getY(), block.getZ() + 0.5);
        if (event.getBlockFace() == BlockFace.UP && block.isCollidable()) spawnLocation.add(0, 1, 0);
        for (int x = -1; x <= 1; x++)
            for (int z = -1; z <= 1; z++)
                for (int y = 0; y <= 1; y++)
                    if (spawnLocation.getWorld().getBlockAt(spawnLocation.clone().add(x, y, z)).isCollidable())
                        return;

        // Entity type switch
        Entity entity;
        switch (Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(typeKey, PersistentDataType.STRING))) {
            case "HORSE" -> entity = player.getWorld().spawnEntity(spawnLocation, EntityType.HORSE);
            case "SKELETONHORSE" -> entity = player.getWorld().spawnEntity(spawnLocation, EntityType.SKELETON_HORSE);
            case "ZOMBIEHORSE" -> entity = player.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE_HORSE);
            case "DONKEY" -> entity = player.getWorld().spawnEntity(spawnLocation, EntityType.DONKEY);
            case "MULE" -> entity = player.getWorld().spawnEntity(spawnLocation, EntityType.MULE);
            case "STRIDER" -> {
                entity = player.getWorld().spawnEntity(spawnLocation, EntityType.STRIDER);
                entity.getPassengers().forEach(Entity::remove);
            }
            case "PIG" -> entity = player.getWorld().spawnEntity(spawnLocation, EntityType.PIG);
            default -> {
                PocketHorses.plugin.getLogger().warning("Unknown Entity Type!");
                return;
            }
        }

        // Parse stored NBT
        NBTContainer nbt = new NBTContainer(Objects.requireNonNull(meta.getPersistentDataContainer().get(dataKey, PersistentDataType.STRING)));
        nbt.removeKey("Pos");
        nbt.removeKey("Motion");
        nbt.removeKey("UUID");

        new NBTEntity(entity).mergeCompound(nbt);

        if (meta.hasDisplayName()) {
            entity.setCustomName(meta.getDisplayName());
        }

        // Remove entity from saddle
        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
    }
}
