package me.darksoul.whatIsThat.compatibility;

import com.azlagor.litecore.timeutils.TimeManager;
import com.azlagor.litefarm.API.API;
import com.azlagor.litefarm.data.SimplePlant;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LiteFarmCompat {
    private static boolean isLitefarmInstalled;

    public static void checkLitefarm() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("LiteFarm");
        isLitefarmInstalled = pl != null && pl.isEnabled();
        if (isLitefarmInstalled) {
            WhatIsThat.getInstance().getLogger().info("Hooked into LiteFarm");
        } else {
            WhatIsThat.getInstance().getLogger().info("LiteFarm not found, skipping hook");
        }
    }
    public static boolean getIsLitefarmInstalled() {
        return isLitefarmInstalled;
    }

    public static boolean handleLitefarmCrop(Block block, Player player) {
        if (API.isPlant(block.getLocation())) {
            SimplePlant plant = API.get_plant(block);
            String name = plant.getConfig().displayName;
            String info = "";
            if (WAILAListener.getConfig().getBoolean("litefarm.growthinfo", true)) {
                info = name + " §f| " + TimeManager.formatTime(plant.ht);
            }
            WAILAListener.setLookingAt(player, name);
            WAILAListener.setLookingAtPrefix(player, "");
            WAILAListener.setLookingAtSuffix(player, TimeManager.formatTime(plant.ht));
            WAILAListener.setLookingAtInfo(player, info);
            WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                    info);
            return true;
        }
        return false;
    }
}
