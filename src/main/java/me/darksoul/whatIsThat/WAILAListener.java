package me.darksoul.whatIsThat;

import me.darksoul.whatIsThat.compatibility.Minetorio;
import me.darksoul.whatIsThat.misc.ConfigUtils;
import me.darksoul.whatIsThat.misc.ItemGroups;
import me.darksoul.whatIsThat.misc.MathUtils;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
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
    private static final YamlConfiguration vanillaLang = ConfigUtils.loadVanillaBlocksLang();
    private static final YamlConfiguration vanillaEntitiesLang = ConfigUtils.loadVanillaEntitiesLang();
    private static final YamlConfiguration config = ConfigUtils.loadConfig();
    private static final File PREF_FOLDER = new File(WhatIsThat.getInstance().getDataFolder(), "cache/players");
    private static final List<Player> players = new ArrayList<>();

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
        WAILAManager.createBossBar(player);
        Block block = MathUtils.getLookingAtBlock(player, 50);
        Entity entity = MathUtils.isLookingAtEntity(player, 50);
        if (entity != null) {
            EntityType type = entity.getType();
            // Entities
            if (config.getBoolean("entities.enabled", true)) {
                for (EntityType not : ItemGroups.getNotRender()) {
                    if (type != not) {
                        String entityName = vanillaEntitiesLang.getString("entity." + entity.getType(), entity.getType().name());
                        WAILAManager.updateBossBar(player, Informations.getEntityOwner(entity)
                                + Informations.getVillagerProfession(entity)
                                + entityName
                                + Informations.getHealth(entity)
                                + Informations.getEntityAgeLeft(entity)
                                + Informations.getIsLeashed(entity));
                        return;
                    }
                }
            }
        }
        if (block != null) {
            // Minetorio Blocks
            if (config.getBoolean("minetorio.enabled", true)) {
                if (Minetorio.getDeviceBlock(block, player)) {
                    return;
                }
            }
            // Vanilla Blocks
            if (config.getBoolean("blocks.enabled", true)) {
                String blockName = vanillaLang.getString("block." + block.getType().name(), block.getType().name());
                WAILAManager.updateBossBar(player, Informations.getTotalItemsInContainer(block)
                        + blockName
                        + Informations.getRedstoneInfo(block)
                        + Informations.getHarvestInfo(block)
                        + Informations.getHoneyInfo(block)
                        + Informations.getBeaconEffect(block)
                        + Informations.getSpawnerInfo(block)
                        + Informations.getNoteblockInfo(block)
                        + Informations.getFarmlandHydration(block)
                        + Informations.getRemainingSmeltTime(block));
                return;
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
        }
    }

    public static File getPrefFolder() {
        return PREF_FOLDER;
    }

    public static void removePlayer(Player player) {
        players.remove(player);
    }

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static YamlConfiguration getConfig() {
        return config;
    }
}
