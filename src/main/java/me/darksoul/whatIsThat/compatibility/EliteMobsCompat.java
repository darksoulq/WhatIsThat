package me.darksoul.whatIsThat.compatibility;

import com.magmaguy.elitemobs.entitytracker.EntityTracker;
import com.magmaguy.elitemobs.mobconstructor.EliteEntity;
import me.darksoul.whatIsThat.Information;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EliteMobsCompat {
    private static final List<Function<EliteEntity, String>> suffixEntity = new ArrayList<>();
    private static boolean isInstalled;

    public static void setup() {
        suffixEntity.clear();
        if (WAILAListener.getConfig().getBoolean("elitemobs.healthinfo", true)) {
            suffixEntity.add(Information::eliteMobs_getHealth);
        }
    }
    public static void hook() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("EliteMobs");
        isInstalled = pl != null && pl.isEnabled();
        if (isInstalled) {
            setup();
            WhatIsThat.getInstance().getLogger().info("Hooked into EliteMobs");
        } else {
            WhatIsThat.getInstance().getLogger().info("EliteMobs not found, skipping hook");
        }
    }
    public static boolean getIsInstalled() {
        return isInstalled;
    }

    public static boolean handleEntity(Entity entity, Player player) {
        EliteEntity eliteEntity = EntityTracker.getEliteMobEntity(entity);
        if (eliteEntity != null) {
            String name = eliteEntity.getName();
            StringBuilder EMEntitySInfo = new StringBuilder();
            String EMEntityPInfo = "";
            StringBuilder info = new StringBuilder();
            for (Function<EliteEntity, String> func : suffixEntity) {
                EMEntitySInfo.append(func.apply(eliteEntity));
            }
            info.append(EMEntityPInfo).append(name);
            if (!EMEntitySInfo.isEmpty()) {
                info.append(" §f| ").append(EMEntitySInfo);
            }
            WAILAListener.setLookingAt(player, name);
            WAILAListener.setLookingAtPrefix(player, EMEntityPInfo);
            WAILAListener.setLookingAtSuffix(player, EMEntitySInfo.toString());
            WAILAListener.setLookingAtInfo(player, info.toString());
            WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                    info.toString());
            return true;
        }
        return false;
    }
}
