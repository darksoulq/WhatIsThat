package me.darksoul.whatIsThat.compatibility;

import dev.aurelium.auramobs.api.AuraMobsAPI;
import me.darksoul.whatIsThat.Information;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
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
            String entityName = MinecraftCompat.getVanillaEntitiesLang().getString("entity." + entity.getType(), entity.getName());
            if (entity.getCustomName() != null) {
                entityName = entity.getCustomName();
            }
            String ASEntitySInfo = "";
            StringBuilder ASEntityPInfo = new StringBuilder();
            StringBuilder info = new StringBuilder();
            for (Function<Entity, String> func : Information.getPrefixASEntity()) {
                ASEntityPInfo.append(func.apply(entity));
            }
            info.append(ASEntityPInfo).append(entityName).append(ASEntitySInfo);
            WAILAManager.updateBossBar(player, info.toString());
            return true;
        }
        return false;
    }

}
