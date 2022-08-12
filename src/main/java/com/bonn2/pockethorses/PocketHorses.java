package com.bonn2.pockethorses;

import com.bonn2.pockethorses.listeners.PickupEntity;
import com.bonn2.pockethorses.listeners.PlaceEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class PocketHorses extends JavaPlugin {

    public static PocketHorses plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        getLogger().info("Initializing Config");
        File configyml = new File(getDataFolder() + File.separator + "config.yml");
        if (!configyml.exists()) { // Checks if config file exists
            getLogger().warning("No Config.yml found, making a new one!");
            saveResource("config.yml", false);
        }

        getServer().getPluginManager().registerEvents(new PickupEntity(), this);
        getServer().getPluginManager().registerEvents(new PlaceEntity(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
