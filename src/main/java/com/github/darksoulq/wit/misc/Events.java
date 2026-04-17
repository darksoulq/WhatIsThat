package com.github.darksoulq.wit.misc;

import com.github.darksoulq.wit.Information;
import com.github.darksoulq.wit.WITListener;
import com.github.darksoulq.wit.api.API;
import com.github.darksoulq.wit.display.DisplayManager;
import io.papermc.paper.event.block.BlockBreakProgressUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Events implements Listener {
    public static final Map<Long, Float> BREAK_PROGRESS = new ConcurrentHashMap<>();

    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        API.fireReload();
    }

    @EventHandler
    public void onBlockBreakProgress(BlockBreakProgressUpdateEvent event) {
        if (ItemGroups.getBlacklistedBlocks().contains(event.getBlock().getType())) return;
        long key = MathUtils.getBlockKey(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
        float progress = event.getProgress();
        if (progress == 1f || progress == 0f) {
            BREAK_PROGRESS.remove(key);
        } else {
            BREAK_PROGRESS.put(key, progress);
        }
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        if (WITListener.DISABLED_WORLDS.contains(event.getPlayer().getWorld().getName())) {
            DisplayManager.removeBar(event.getPlayer(), WITListener.getSettings(event.getPlayer()).type);
            WITListener.removePlayer(event.getPlayer());
        } else {
            WITListener.addPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        DisplayManager.removeBar(event.getPlayer(), WITListener.getSettings(event.getPlayer()).type);
        WITListener.removePlayer(event.getPlayer());
        WITListener.unloadSettings(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        WITListener.loadSettings(event.getPlayer(), () -> {
            WITListener.PlayerSettings settings = WITListener.getSettings(event.getPlayer());
            if (!settings.disabled && !WITListener.DISABLED_WORLDS.contains(event.getPlayer().getWorld().getName())) {
                WITListener.addPlayer(event.getPlayer());
            }
        });
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