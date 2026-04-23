package com.github.darksoulq.wit.misc;

import com.github.darksoulq.wit.Information;
import com.github.darksoulq.wit.WITListener;
import com.github.darksoulq.wit.api.API;
import com.github.darksoulq.wit.api.ProgressProviders;
import com.github.darksoulq.wit.display.DisplayManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;

public class Events implements Listener {

    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        API.fireReload();
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        if (ItemGroups.getBlacklistedBlocks().contains(event.getBlock().getType())) return;
        long duration = MathUtils.calculateBreakTimeMs(event.getBlock(), event.getPlayer());
        if (duration >= 0) {
            ProgressProviders.SESSIONS.put(event.getPlayer().getUniqueId(), new MiningSession(event.getBlock(), System.currentTimeMillis(), duration));
        }
    }

    @EventHandler
    public void onBlockDamageAbort(BlockDamageAbortEvent event) {
        ProgressProviders.SESSIONS.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ProgressProviders.SESSIONS.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        ProgressProviders.SESSIONS.remove(event.getPlayer().getUniqueId());
        if (WITListener.DISABLED_WORLDS.contains(event.getPlayer().getWorld().getName())) {
            DisplayManager.removeBar(event.getPlayer(), WITListener.getSettings(event.getPlayer()).type);
            WITListener.removePlayer(event.getPlayer());
        } else {
            WITListener.addPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ProgressProviders.SESSIONS.remove(event.getPlayer().getUniqueId());
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