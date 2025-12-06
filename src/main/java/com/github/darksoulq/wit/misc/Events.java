package com.github.darksoulq.wit.misc;

import com.github.darksoulq.wit.WITListener;
import com.github.darksoulq.wit.display.WAILAManager;
import io.papermc.paper.event.block.BlockBreakProgressUpdateEvent;
import com.github.darksoulq.wit.Information;
import com.github.darksoulq.wit.api.API;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.github.darksoulq.wit.WITListener.*;

public class Events implements Listener {
    public static Map<Block, Float> BREAK_PROGRESS = new HashMap<>();

    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        API.fireReload();
    }

    @EventHandler
    public void onBlockBreakProgress(BlockBreakProgressUpdateEvent event) {
        if (ItemGroups.getBlACKLISTED_BLOCKS().contains(event.getBlock().getType())) return;
        BREAK_PROGRESS.put(event.getBlock(), event.getProgress());
        if (event.getProgress() == 1f || event.getProgress() == 0f) {
            BREAK_PROGRESS.remove(event.getBlock());
        }
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        if (DISABLED_WORLDS.contains(event.getPlayer().getWorld())) {
            WAILAManager.removeBar(event.getPlayer(), getPlayerConfig(event.getPlayer()).getString("type", "bossbar"));
            WITListener.removePlayer(event.getPlayer());
        } else {
            WITListener.addPlayer(event.getPlayer());
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        WAILAManager.removeBar(event.getPlayer(), getPlayerConfig(event.getPlayer()).getString("type", "bossbar"));
        WITListener.removePlayer(event.getPlayer());
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        File playerFile = new File(PREF_FOLDER + "/" + event.getPlayer().getName() + ".yml");
        YamlConfiguration pconfig = new YamlConfiguration();
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
                pconfig = YamlConfiguration.loadConfiguration(playerFile);
                pconfig.set("disableWAILA", "disabled".equals(WITListener.getConfig().getString("core.default_state", "enabled")));
                pconfig.set("type", "bossbar");
                pconfig.save(playerFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            pconfig = YamlConfiguration.loadConfiguration(playerFile);
        }
        boolean disableBossBar = pconfig.getBoolean("disableWAILA", false);
        if (!disableBossBar && !DISABLED_WORLDS.contains(event.getPlayer().getWorld())) {
            WITListener.addPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onTierRegistration(Information.StartTierRegistrationEvent e) {
        Information.addTier(Information.WOODEN_SWORD);
        Information.addTier(Information.WOODEN_AXE);
        Information.addTier(Information.WOODEN_PICKAXE);
        Information.addTier(Information.WOODEN_SHOVEL);
        Information.addTier(Information.WOODEN_HOE);

        Information.addTier(Information.STONE_SWORD);
        Information.addTier(Information.STONE_AXE);
        Information.addTier(Information.STONE_PICKAXE);
        Information.addTier(Information.STONE_SHOVEL);
        Information.addTier(Information.STONE_HOE);

        Information.addTier(Information.GOLD_SWORD);
        Information.addTier(Information.GOLD_AXE);
        Information.addTier(Information.GOLD_PICKAXE);
        Information.addTier(Information.GOLD_SHOVEL);
        Information.addTier(Information.GOLD_HOE);

        Information.addTier(Information.COPPER_SWORD);
        Information.addTier(Information.COPPER_AXE);
        Information.addTier(Information.COPPER_PICKAXE);
        Information.addTier(Information.COPPER_SHOVEL);
        Information.addTier(Information.COPPER_HOE);

        Information.addTier(Information.IRON_SWORD);
        Information.addTier(Information.IRON_AXE);
        Information.addTier(Information.IRON_PICKAXE);
        Information.addTier(Information.IRON_SHOVEL);
        Information.addTier(Information.IRON_HOE);

        Information.addTier(Information.DIAMOND_SWORD);
        Information.addTier(Information.DIAMOND_AXE);
        Information.addTier(Information.DIAMOND_PICKAXE);
        Information.addTier(Information.DIAMOND_SHOVEL);
        Information.addTier(Information.DIAMOND_HOE);

        Information.addRequiredTierGetter(Information.VANILLA_BLOCK_TIER_GETTER);
        Information.addTierGetter(Information.VANILLA_TIER_GETTER);
    }
}
