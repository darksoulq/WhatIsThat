package me.darksoul.whatIsThat.compatibility;

import com.magmaguy.elitemobs.entitytracker.EntityTracker;
import com.magmaguy.elitemobs.mobconstructor.EliteEntity;
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
    private static final List<Function<EliteEntity, String>> suffixEMEntity = new ArrayList<>();
    private static boolean isEMInstalled;

    private static void setup() {
        if (WAILAListener.getConfig().getBoolean("elitemobs.healthinfo", true)) {
            suffixEMEntity.add(EliteMobsCompat::getHealth);
        }
    }
    public static boolean checkEM() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("EliteMobs");
        boolean isEnabled = pl != null && pl.isEnabled();
        if (isEnabled) {
            setup();
        }
        return isEnabled;
    }
    public static boolean isEMInstalled() {
        return isEMInstalled;
    }
    public static void setEMInstalled(boolean EMInstalled) {
        isEMInstalled = EMInstalled;
    }

    public static boolean handleEMEntity(Entity entity, Player player) {
        EliteEntity eliteEntity = EntityTracker.getEliteMobEntity(entity);
        if (eliteEntity != null) {
            String name = eliteEntity.getName();
            StringBuilder EMEntitySInfo = new StringBuilder();
            String EMEntityPInfo = "";
            StringBuilder info = new StringBuilder();
            for (Function<EliteEntity, String> func : suffixEMEntity) {
                EMEntitySInfo.append(func.apply(eliteEntity));
            }
            info.append(EMEntityPInfo).append(name);
            if (!EMEntitySInfo.isEmpty()) {
                info.append(" §f| ").append(EMEntitySInfo);
            }
            WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                    info.toString());
            return true;
        }
        return false;
    }

    private static String getHealth(EliteEntity entity) {
        int health = (int) entity.getHealth();
        int maxHealth = (int) entity.getMaxHealth();

        return " §c❤ " + health + "/" + maxHealth;
    }
}
