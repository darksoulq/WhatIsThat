package me.darksoul.wit.misc;

import io.papermc.paper.event.block.BlockBreakProgressUpdateEvent;
import me.darksoul.wit.api.API;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.HashMap;
import java.util.Map;

public class Events implements Listener {
    public static Map<Block, Float> progressMap = new HashMap<>();

    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        API.fireReload();
    }

    @EventHandler
    public void onBlockBreakProgress(BlockBreakProgressUpdateEvent event) {
        if (ItemGroups.getBlACKLISTED_BLOCKS().contains(event.getBlock().getType())) return;
        progressMap.put(event.getBlock(), event.getProgress());
        if (event.getProgress() == 1f || event.getProgress() == 0f) {
            progressMap.remove(event.getBlock());
        }
    }
}
