package com.github.darksoulq.wit;

import com.github.darksoulq.wit.api.Info;
import com.github.darksoulq.wit.compatibility.MinecraftCompat;
import com.github.darksoulq.wit.display.WAILAManager;
import com.github.darksoulq.wit.misc.ConfigUtils;
import com.github.darksoulq.wit.misc.ItemGroups;
import com.github.darksoulq.wit.misc.MathUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.function.BiFunction;


public class WITListener implements Listener {

    public static final File PREF_FOLDER = new File(WIT.instance().getDataFolder(), "cache/players");
    private static final List<UUID> PLAYERS = new ArrayList<>();
    public static final List<World> DISABLED_WORLDS = new ArrayList<>();
    private static final Map<Player, Info> LOOKING_AT = new HashMap<>();
    private static YamlConfiguration CONFIG = ConfigUtils.loadConfig();
    private static int ENTITY_DISTANCE = CONFIG.getInt("core.entitydistance", 25);
    private static int BLOCK_DISTANCE = CONFIG.getInt("core.blockdistance", 25);
    public static boolean IS_HIDDEN = false;

    public WITListener() {
        if (!PREF_FOLDER.exists()) {
            PREF_FOLDER.mkdirs();
        }
        for (UUID playerID : PLAYERS) {
            Player player = Bukkit.getPlayer(playerID);
            if (player == null) continue;
            WAILAManager.removeBar(player, getPlayerConfig(player).getString("type"));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID playerID : PLAYERS) {
                    Player player = Bukkit.getPlayer(playerID);
                    if (player == null) continue;
                    if ("sneaking".equals(CONFIG.getString("core.mode", "normal")) && !player.isSneaking()) return;
                    updateWAILA(player);
                }
            }
        }.runTaskTimer(WIT.instance(), 0, CONFIG.getInt("core.update-delay", 5));
    }
    private void updateWAILA(Player player) {
        Block block = MathUtils.getLookingAtBlock(player, BLOCK_DISTANCE);
        Entity entity = MathUtils.isLookingAtEntity(player, ENTITY_DISTANCE);
        if (entity != null) {
            for (BiFunction<Entity, Player, Boolean> eHandler : Handlers.getEntityHandlers()) {
                if (eHandler.apply(entity, player)) {
                    return;
                }
            }
            if (CONFIG.getBoolean("entities.enabled", true)) {
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
            if (CONFIG.getBoolean("blocks.enabled", true) && !ItemGroups.getBlACKLISTED_BLOCKS().contains(block.getType())) {
                if (MinecraftCompat.handleBlock(block, player)) {
                    return;
                }
            }
        }
        WAILAManager.setBar(player, Component.text(""));
        LOOKING_AT.put(player, null);

        if (WAILAManager.getDisplays().get(getPlayerConfig(player).getString("type")).isEmpty(player)) {
            WAILAManager.removeBar(player, getPlayerConfig(player).getString("type"));
        }
    }
    public static void setup() {
        CONFIG = ConfigUtils.loadConfig();
        if (CONFIG.getString("core.mode", "normal").equalsIgnoreCase("hidden")) {
            IS_HIDDEN = true;
        } else {
            IS_HIDDEN = false;
            WIT.instance().getLogger().warning("Invalid mode in config.yml, defaulting to normal");
        }
        ENTITY_DISTANCE = CONFIG.getInt("core.entitydistance", 25);
        BLOCK_DISTANCE = CONFIG.getInt("core.blockdistance", 25);
    }

    public static void removePlayer(Player player) {
        PLAYERS.remove(player.getUniqueId());
    }
    public static void addPlayer(Player player) {
        PLAYERS.add(player.getUniqueId());
    }
    public static File getPrefFolder() {
        return PREF_FOLDER;
    }
    public static YamlConfiguration getPlayerConfig(Player player) {
        return YamlConfiguration.loadConfiguration(new File(PREF_FOLDER + "/" + player.getName() + ".yml"));
    }
    public static boolean isHidden() {
        return IS_HIDDEN;
    }
    public static Info getLookingAt(Player player) {
        return LOOKING_AT.get(player);
    }
    public static void setLookingAt(Player player, Info value) {
        LOOKING_AT.put(player, value);
    }

    public static YamlConfiguration getConfig() {
        return CONFIG;
    }
}
