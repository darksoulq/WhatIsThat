package com.github.darksoulq.wit;

import com.github.darksoulq.wit.api.Info;
import com.github.darksoulq.wit.compatibility.MinecraftCompat;
import com.github.darksoulq.wit.display.WAILAManager;
import com.github.darksoulq.wit.misc.ConfigUtils;
import com.github.darksoulq.wit.misc.ItemGroups;
import com.github.darksoulq.wit.misc.MathUtils;
import net.kyori.adventure.text.Component;
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


public class WITListener implements Listener {
    private static YamlConfiguration config = ConfigUtils.loadConfig();
    private static final File PREF_FOLDER = new File(WIT.instance().getDataFolder(), "cache/players");
    private static final List<Player> players = new ArrayList<>();
    private static final Map<Player, Info> lookingAt = new HashMap<>();
    private static int entityDistance = config.getInt("core.entitydistance", 25);
    private static int blockDistance = config.getInt("core.blockdistance", 25);
    private static boolean isHidden = false;

    public WITListener() {
        if (!PREF_FOLDER.exists()) {
            PREF_FOLDER.mkdirs();
        }
        for (Player player : players) {
            WAILAManager.removeBar(player, getPlayerConfig(player).getString("type"));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isHidden) return;
                for (Player player : players) {
                    if ("sneaking".equals(config.getString("core.mode", "normal")) && !player.isSneaking()) return;
                    updateWAILA(player);
                }
            }
        }.runTaskTimer(WIT.instance(), 0, config.getInt("core.update-delay", 5));
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
            if (config.getBoolean("blocks.enabled", true) && !ItemGroups.getBlACKLISTED_BLOCKS().contains(block.getType())) {
                if (MinecraftCompat.handleBlock(block, player)) {
                    return;
                }
            }
        }
        WAILAManager.setBar(player, Component.text(""));
        lookingAt.put(player, null);

        if (WAILAManager.getDisplays().get(getPlayerConfig(player).getString("type")).isEmpty(player)) {
            WAILAManager.removeBar(player, getPlayerConfig(player).getString("type"));
        }
    }
    public static void setup() {
        config = ConfigUtils.loadConfig();
        if (config.getString("core.mode", "normal").equalsIgnoreCase("hidden")) {
            isHidden = true;
        } else {
            isHidden = false;
            WIT.instance().getLogger().warning("Invalid mode in config.yml, defaulting to normal");
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
        YamlConfiguration pconfig = new YamlConfiguration();
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
                pconfig = YamlConfiguration.loadConfiguration(playerFile);
                pconfig.set("disableWAILA", "disabled".equals(config.getString("core.default_state", "enabled")));
                pconfig.set("type", "bossbar");
                pconfig.save(playerFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            pconfig = YamlConfiguration.loadConfiguration(playerFile);
        }
        boolean disableBossBar = pconfig.getBoolean("disableWAILA", false);
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
    public static Info getLookingAt(Player player) {
        return lookingAt.get(player);
    }
    public static void setLookingAt(Player player, Info value) {
        lookingAt.put(player, value);
    }

    public static YamlConfiguration getConfig() {
        return config;
    }
}
