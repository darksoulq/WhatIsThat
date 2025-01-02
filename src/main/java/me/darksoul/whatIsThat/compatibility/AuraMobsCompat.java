package me.darksoul.whatIsThat.compatibility;

import dev.aurelium.auramobs.api.AuraMobsAPI;
import me.darksoul.whatIsThat.Information;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import me.darksoul.whatIsThat.misc.ConfigUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

public class AuraMobsCompat {
    private static boolean isAuraMobsInstalled;

    public static boolean checkAuraMobs() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("AuraMobs");
        return pl != null && pl.isEnabled();
    }
    public static boolean getIsAuraMobsInstalled() {
        return isAuraMobsInstalled;
    }
    public static void setIsAuraMobsInstalled(boolean isAuraMobsInstalled) {
        AuraMobsCompat.isAuraMobsInstalled = isAuraMobsInstalled;
    }

    public static boolean handleAuraMobs(Entity entity, Player player) {
        if (AuraMobsAPI.isAuraMob(entity)) {
            String key = "entity.minecraft." + entity.getType().toString().toLowerCase();
            String entityName = ConfigUtils.getTranslatedVName(key);
            if (entity.getCustomName() != null) {
                entityName = entity.getCustomName();
            }
            String ASEntitySInfo = "";
            StringBuilder ASEntityPInfo = new StringBuilder();
            StringBuilder info = new StringBuilder();
            for (Function<Entity, String> func : Information.getPrefixASEntity()) {
                ASEntityPInfo.append(func.apply(entity));
            }
            if (!ASEntityPInfo.isEmpty()) {
                info.append(ASEntityPInfo).append(" §f| ");
            }
            info.append(entityName).append(ASEntitySInfo);
            WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                    info.toString());
            return true;
        }
        return false;
    }

}
