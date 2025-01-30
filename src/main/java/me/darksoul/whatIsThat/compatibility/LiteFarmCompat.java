package me.darksoul.whatIsThat.compatibility;

import com.azlagor.litecore.timeutils.TimeManager;
import com.azlagor.litefarm.API.API;
import com.azlagor.litefarm.data.SimplePlant;
import me.darksoul.whatIsThat.Information;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.display.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LiteFarmCompat {
    private static boolean isInstalled;

    public static void hook() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("LiteFarm");
        isInstalled = pl != null && pl.isEnabled();
        if (isInstalled) {
            WhatIsThat.getInstance().getLogger().info("Hooked into LiteFarm");
        } else {
            WhatIsThat.getInstance().getLogger().info("LiteFarm not found, skipping hook");
        }
    }
    public static boolean getIsInstalled() {
        return isInstalled;
    }

    public static boolean handleBlock(Block block, Player player) {
        if (API.isPlant(block.getLocation())) {
            SimplePlant plant = API.get_plant(block);
            String name = plant.getConfig().displayName;
            String info = "";
            if (WAILAListener.getConfig().getBoolean("litefarm.growthinfo", true)) {
                info = name + Information.getValuesFile().getString("SPLITTER", " §f| ")
                        + TimeManager.formatTime(plant.ht);
            }
            WAILAListener.setLookingAt(player, name);
            WAILAListener.setLookingAtPrefix(player, "");
            WAILAListener.setLookingAtSuffix(player, TimeManager.formatTime(plant.ht));
            WAILAListener.setLookingAtInfo(player, info);
            WAILAManager.setBar(player, info);
            return true;
        }
        return false;
    }
}
