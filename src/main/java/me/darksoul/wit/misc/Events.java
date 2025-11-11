package me.darksoul.wit.misc;

import io.papermc.paper.event.block.BlockBreakProgressUpdateEvent;
import me.darksoul.wit.Information;
import me.darksoul.wit.api.API;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.HashMap;
import java.util.Map;

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
