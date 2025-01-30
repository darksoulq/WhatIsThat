package me.darksoul.whatIsThat;

import me.darksoul.whatIsThat.compatibility.MinecraftCompat;
import me.darksoul.whatIsThat.display.WAILAManager;
import me.darksoul.whatIsThat.misc.ConfigUtils;
import me.darksoul.whatIsThat.misc.ItemGroups;
import me.darksoul.whatIsThat.misc.MathUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;


public class WAILAListener implements Listener {
    private static YamlConfiguration config = ConfigUtils.loadConfig();
    private static final File PREF_FOLDER = new File(WhatIsThat.getInstance().getDataFolder(), "cache/players");
    private static final List<Player> players = new ArrayList<>();
    private static final Map<Player, String> lookingAt = new HashMap<>();
    private static final Map<Player, String> lookingAtPrefix = new HashMap<>();
    private static final Map<Player, String> lookingAtSuffix = new HashMap<>();
    private static final Map<Player, String> lookingAtInfo = new HashMap<>();
    private static int entityDistance = config.getInt("core.entitydistance", 25);
    private static int blockDistance = config.getInt("core.blockdistance", 25);
    private static boolean isHidden = false;

    public WAILAListener() {
        if (!PREF_FOLDER.exists()) {
            PREF_FOLDER.mkdirs();
        }
        for (Player player : players) {
            WAILAManager.removeBar(player, getPlayerConfig(player).getString("type"));
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
            for (BiFunction<Entity, Player, Boolean> eHandler : Handlers.getEntityHandlers()) {
                if (eHandler.apply(entity, player)) {
                    return;
                }
            }
            if (config.getBoolean("entities.enabled", true)) {
                if (MinecraftCompat.handleEntity(entity, player)) {
                    return;
                }
            }
        }
        if (block != null) {
            for (BiFunction<Block, Player, Boolean> bHandler : Handlers.getBlockHandlers()) {
                if (bHandler.apply(block, player)) {
                    return;
                }
            }
            if (config.getBoolean("blocks.enabled", true) && !ItemGroups.getOperatorBlocks().contains(block.getType())) {
                if (MinecraftCompat.handleBlock(block, player)) {
                    return;
                }
            }
        }
        WAILAManager.setBar(player, "");
        lookingAt.put(player, "");
        lookingAtPrefix.put(player, "");
        lookingAtSuffix.put(player, "");
        lookingAtInfo.put(player, "");
    }
    public static void setup() {
        config = ConfigUtils.loadConfig();
        if (config.getString("core.mode", "normal").equalsIgnoreCase("hidden")) {
            isHidden = true;
        } else if (config.getString("core.mode", "normal").equalsIgnoreCase("normal")) {
            isHidden = false;
        } else {
            isHidden = false;
            WhatIsThat.getInstance().getLogger().warning("Invalid mode in config.yml, defaulting to normal");
        }
        entityDistance = config.getInt("core.entitydistance", 25);
        blockDistance = config.getInt("core.blockdistance", 25);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        WAILAManager.removeBar(event.getPlayer(), getPlayerConfig(event.getPlayer()).getString("type", "bossbar"));
        players.remove(event.getPlayer());
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        File playerFile = new File(PREF_FOLDER + "/" + event.getPlayer().getName() + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
                config = YamlConfiguration.loadConfiguration(playerFile);
                config.set("disableWAILA", false);
                config.set("type", "bossbar");
                config.save(playerFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            config = YamlConfiguration.loadConfiguration(playerFile);
        }
        boolean disableBossBar = config.getBoolean("disableWAILA", false);
        if (!disableBossBar) {
            players.add(event.getPlayer());
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
    public static YamlConfiguration getPlayerConfig(Player player) {
        return YamlConfiguration.loadConfiguration(new File(PREF_FOLDER + "/" + player.getName() + ".yml"));
    }
    public static boolean isHidden() {
        return isHidden;
    }
    public static String getLookingAt(Player player) {
        return lookingAt.get(player);
    }
    public static String getLookingAtPrefix(Player player) {
        return lookingAtPrefix.get(player);
    }
    public static String getLookingAtSuffix(Player player) {
        return lookingAtSuffix.get(player);
    }
    public static String getLookingAtInfo(Player player) {
        return lookingAtInfo.get(player);
    }
    public static void setLookingAt(Player player, String value) {
        lookingAt.put(player, value);
    }
    public static void setLookingAtPrefix(Player player, String value) {
        lookingAtPrefix.put(player, value);
    }
    public static void setLookingAtSuffix(Player player, String value) {
        lookingAtSuffix.put(player, value);
    }
    public static void setLookingAtInfo(Player player, String value) {
        lookingAtInfo.put(player, value);
    }

    public static YamlConfiguration getConfig() {
        return config;
    }
}
