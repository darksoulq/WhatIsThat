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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AuraMobsCompat {
    private static boolean isInstalled;
    private static final List<Function<Entity, String>> prefixEntity = new ArrayList<>();

    public static void setup() {
        prefixEntity.clear();
        if (WAILAListener.getConfig().getBoolean("auramobs.enabled", false)) {
            prefixEntity.add(Information::auraMobs_getEntityLevel);
        }
    }

    public static void hook() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("AuraMobs");
        isInstalled = pl != null && pl.isEnabled();
        if (isInstalled) {
            WhatIsThat.getInstance().getLogger().info("Hooked into AuraMobs");
        } else {
            WhatIsThat.getInstance().getLogger().info("AuraMobs not found, skipping hook");
        }
    }
    public static boolean getIsInstalled() {
        return isInstalled;
    }

    public static boolean handleEntity(Entity entity, Player player) {
        if (AuraMobsAPI.isAuraMob(entity)) {
            String key = "entity.minecraft." + entity.getType().toString().toLowerCase();
            String EntityName = ConfigUtils.getTranslatedName(key);
            if (entity.getCustomName() != null) {
                EntityName = entity.getCustomName();
            }
            String EntitySuffix = "";
            StringBuilder EntityPrefix = new StringBuilder();
            StringBuilder info = new StringBuilder();
            for (Function<Entity, String> func : prefixEntity) {
                EntityPrefix.append(func.apply(entity));
            }
            if (!EntityPrefix.isEmpty()) {
                info.append(EntityPrefix).append(" §f| ");
            }
            info.append(EntityName).append(EntitySuffix);
            WAILAListener.setLookingAt(player, EntityName);
            WAILAListener.setLookingAtPrefix(player, EntityPrefix.toString());
            WAILAListener.setLookingAtSuffix(player, EntitySuffix);
            WAILAListener.setLookingAtInfo(player, info.toString());
            WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                    info.toString());
            return true;
        }
        return false;
    }

}
