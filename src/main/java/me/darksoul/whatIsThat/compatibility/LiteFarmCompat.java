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

    public static boolean checkLitefarm() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("LiteFarm");
        return pl != null && pl.isEnabled();
    }
    public static boolean getIsLitefarmInstalled() {
        return isLitefarmInstalled;
    }
    public static void setIsLitefarmInstalled(boolean isLiteFarmInstalled) {
        isLitefarmInstalled = isLiteFarmInstalled;
    }

    public static boolean handleLitefarmCrop(Block block, Player player) {
        if (API.isPlant(block.getLocation())) {
            SimplePlant plant = API.get_plant(block);
            String info = plant.getConfig().displayName;
            if (WAILAListener.getConfig().getBoolean("litefarm.growthinfo", true)) {
                info = info + " §f| " + TimeManager.formatTime(plant.ht);
            }
            WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                    info);
            return true;
        }
        return false;
    }
}
