package com.github.darksoulq.wit;

import com.github.darksoulq.wit.api.Info;
import com.github.darksoulq.wit.compatibility.MinecraftCompat;
import com.github.darksoulq.wit.display.DisplayManager;
import com.github.darksoulq.wit.misc.ConfigUtils;
import com.github.darksoulq.wit.misc.ItemGroups;
import com.github.darksoulq.wit.misc.MathUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class WITListener implements Listener {

    public static final File PREF_FOLDER = new File(WIT.instance().getDataFolder(), "cache/players");
    private static final Set<UUID> PLAYERS = ConcurrentHashMap.newKeySet();
    public static final Set<String> DISABLED_WORLDS = ConcurrentHashMap.newKeySet();
    private static final Map<UUID, Info> LOOKING_AT = new ConcurrentHashMap<>();
    private static final Map<UUID, PlayerSettings> SETTINGS = new ConcurrentHashMap<>();

    private static YamlConfiguration CONFIG;
    private static int ENTITY_DISTANCE;
    private static int BLOCK_DISTANCE;
    public static boolean IS_HIDDEN = false;
    public static boolean SNEAKING_MODE = false;
    public static boolean ENTITIES_ENABLED = true;
    public static boolean BLOCKS_ENABLED = true;
    public static boolean DEFAULT_DISABLED = false;

    public enum ActionBarProgressMode {
        OFF, BAR, PERCENT, UNDERLINE
    }

    public static class PlayerSettings {
        public String type;
        public boolean disabled;
        public ActionBarProgressMode abProgressMode;

        public PlayerSettings(String type, boolean disabled, ActionBarProgressMode abProgressMode) {
            this.type = type;
            this.disabled = disabled;
            this.abProgressMode = abProgressMode;
        }
    }

    public WITListener() {
        if (!PREF_FOLDER.exists()) {
            PREF_FOLDER.mkdirs();
        }

        for (UUID playerID : PLAYERS) {
            Player player = Bukkit.getPlayer(playerID);
            if (player != null) {
                DisplayManager.removeBar(player, getSettings(player).type);
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (IS_HIDDEN) return;
                for (UUID playerID : PLAYERS) {
                    Player player = Bukkit.getPlayer(playerID);
                    if (player == null || (SNEAKING_MODE && !player.isSneaking())) continue;
                    updateWAILA(player);
                }
            }
        }.runTaskTimer(WIT.instance(), 0, Math.max(1, CONFIG.getInt("core.update-delay", 5)));
    }

    private void updateWAILA(Player player) {
        if (DISABLED_WORLDS.contains(player.getWorld().getName())) return;

        Entity entity = MathUtils.isLookingAtEntity(player, ENTITY_DISTANCE);
        if (entity != null) {
            for (BiFunction<Entity, Player, Boolean> eHandler : Handlers.getEntityHandlers()) {
                if (eHandler.apply(entity, player)) return;
            }
            if (ENTITIES_ENABLED && !ItemGroups.getBlacklistedEntities().contains(entity.getType())) {
                if (MinecraftCompat.handleEntity(entity, player)) return;
            }
        }

        Block block = MathUtils.getLookingAtBlock(player, BLOCK_DISTANCE);
        if (block != null) {
            for (BiFunction<Block, Player, Boolean> bHandler : Handlers.getBlockHandlers()) {
                if (bHandler.apply(block, player)) return;
            }
            if (BLOCKS_ENABLED && !ItemGroups.getBlacklistedBlocks().contains(block.getType())) {
                if (MinecraftCompat.handleBlock(block, player)) return;
            }
        }

        DisplayManager.setBar(player, new Info());
        LOOKING_AT.remove(player.getUniqueId());

        if (DisplayManager.getDisplays().get(getSettings(player).type).isEmpty(player)) {
            DisplayManager.removeBar(player, getSettings(player).type);
        }
    }

    public static void setup() {
        CONFIG = ConfigUtils.loadConfig();
        String mode = CONFIG.getString("core.mode", "normal");
        IS_HIDDEN = "hidden".equalsIgnoreCase(mode);
        SNEAKING_MODE = "sneaking".equalsIgnoreCase(mode);
        ENTITY_DISTANCE = CONFIG.getInt("core.entitydistance", 25);
        BLOCK_DISTANCE = CONFIG.getInt("core.blockdistance", 25);
        ENTITIES_ENABLED = CONFIG.getBoolean("entities.enabled", true);
        BLOCKS_ENABLED = CONFIG.getBoolean("blocks.enabled", true);
        DEFAULT_DISABLED = "disabled".equalsIgnoreCase(CONFIG.getString("core.default_state", "enabled"));

        DISABLED_WORLDS.clear();
        DISABLED_WORLDS.addAll(CONFIG.getStringList("core.disabled-worlds"));
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

    public static boolean isHidden() {
        return IS_HIDDEN;
    }

    public static Info getLookingAt(Player player) {
        return LOOKING_AT.get(player.getUniqueId());
    }

    public static void setLookingAt(Player player, Info value) {
        if (value == null) {
            LOOKING_AT.remove(player.getUniqueId());
        } else {
            LOOKING_AT.put(player.getUniqueId(), value);
        }
    }

    public static YamlConfiguration getConfig() {
        return CONFIG;
    }

    public static void loadSettings(Player player, Runnable callback) {
        Bukkit.getScheduler().runTaskAsynchronously(WIT.instance(), () -> {
            File file = new File(PREF_FOLDER, player.getName() + ".yml");
            YamlConfiguration conf = new YamlConfiguration();
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    conf.set("disableWAILA", DEFAULT_DISABLED);
                    conf.set("type", "bossbar");
                    conf.set("abProgressMode", "BAR");
                    conf.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                conf = YamlConfiguration.loadConfiguration(file);
            }

            ActionBarProgressMode mode;
            try {
                mode = ActionBarProgressMode.valueOf(conf.getString("abProgressMode", "BAR").toUpperCase());
            } catch (IllegalArgumentException e) {
                mode = ActionBarProgressMode.BAR;
            }

            SETTINGS.put(player.getUniqueId(), new PlayerSettings(
                conf.getString("type", "bossbar"),
                conf.getBoolean("disableWAILA", DEFAULT_DISABLED),
                mode
            ));

            if (callback != null) {
                Bukkit.getScheduler().runTask(WIT.instance(), callback);
            }
        });
    }

    public static void unloadSettings(Player player) {
        SETTINGS.remove(player.getUniqueId());
    }

    public static PlayerSettings getSettings(Player player) {
        return SETTINGS.getOrDefault(player.getUniqueId(), new PlayerSettings("bossbar", DEFAULT_DISABLED, ActionBarProgressMode.BAR));
    }

    public static YamlConfiguration getPlayerConfig(Player player) {
        YamlConfiguration conf = new YamlConfiguration();
        PlayerSettings s = getSettings(player);
        conf.set("type", s.type);
        conf.set("disableWAILA", s.disabled);
        conf.set("abProgressMode", s.abProgressMode.name());
        return conf;
    }
}