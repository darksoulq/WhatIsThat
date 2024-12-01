package me.darksoul.whatIsThat;

import me.darksoul.whatIsThat.compatibility.*;
import me.darksoul.whatIsThat.misc.ConfigUtils;
import me.darksoul.whatIsThat.misc.ItemGroups;
import me.darksoul.whatIsThat.misc.MathUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class WAILAListener implements Listener {
    private static final YamlConfiguration config = ConfigUtils.loadConfig();
    private static final File PREF_FOLDER = new File(WhatIsThat.getInstance().getDataFolder(), "cache/players");
    private static final List<Player> players = new ArrayList<>();
    private static final int entityDistance = config.getInt("core.entitydistance", 25);
    private static final int blockDistance = config.getInt("core.blockdistance", 25);

    public WAILAListener() {
        if (!PREF_FOLDER.exists()) {
            PREF_FOLDER.mkdirs();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : players) {
                    updateWAILA(player);
                }
            }
        }.runTaskTimer(WhatIsThat.getInstance(), 0, 5);
    }
    private void updateWAILA(Player player) {
        Block block = MathUtils.getLookingAtBlock(player, blockDistance);
        Entity entity = MathUtils.isLookingAtEntity(player, entityDistance);
        if (entity != null) {
            if (config.getBoolean("valhallammo.enabled", true) && ValhallaMMOCompat.getIsVMMOInstalled()) {
                if (ValhallaMMOCompat.handleVMMOEntity(entity, player)) {
                    return;
                }
            }
            if (config.getBoolean("itemsadder.entities.enabled", true) && ItemsAdderCompat.getIsIAInstalled()) {
                ;
                if (ItemsAdderCompat.handleIAEntity(entity, player)) {
                    return;
                }
            }
            if (config.getBoolean("elitemobs.enabled", true) && EliteMobsCompat.isEMInstalled()) {
                if (EliteMobsCompat.handleEMEntity(entity, player)) {
                    return;
                }
            }
            if (config.getBoolean("auramobs.enabled", true) && AuraMobsCompat.getIsAuraMobsInstalled()) {
                if (AuraMobsCompat.handleAuraMobs(entity, player)) {
                    return;
                }
            }
            if (config.getBoolean("entities.enabled", true)) {
                if (MinecraftCompat.handleMinecraftEntityDisplay(entity, player)) {
                    return;
                }
            }
        }
        if (block != null) {
            if (config.getBoolean("minetorio.enabled", true) && MinetorioCompat.getIsMTInstalled()) {
                if (MinetorioCompat.handleMTDisplay(block, player)) {
                    return;
                }
            }
            if (config.getBoolean("itemsadder.blocks.enabled", true) && ItemsAdderCompat.getIsIAInstalled()) {
                if (ItemsAdderCompat.handleIABlocks(block, player)) {
                    return;
                }
            }
            if (config.getBoolean("blocks.enabled", true) && !ItemGroups.getOperatorBlocks().contains(block.getType())) {
                if (MinecraftCompat.handleMinecraftBlockDisplay(block, player)) {
                    return;
                }
            }
        }
        WAILAManager.updateBossBar(player, "");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        WAILAManager.removeBossBar(event.getPlayer());
        players.remove(event.getPlayer());
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        File playerFile = new File(PREF_FOLDER + "/" + event.getPlayer().getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        boolean disableBossBar = config.getBoolean("disableBossBar", false);
        if (!disableBossBar) {
            players.add(event.getPlayer());
            WAILAManager.createBossBar(event.getPlayer());
        }
    }
    public static void removePlayer(Player player) {
        players.remove(player);
    }
    public static void addPlayer(Player player) {
        players.add(player);
    }
    public static File getPrefFolder() {
        return PREF_FOLDER;
    }
    public static YamlConfiguration getConfig() {
        return config;
    }
}
