package me.darksoul.whatIsThat.compatibility;

import me.athlaeos.valhallammo.playerstats.profiles.ProfileCache;
import me.athlaeos.valhallammo.playerstats.profiles.implementations.PowerProfile;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ValhallaMMOCompat {
    private static boolean isVMMOInstalled;
    private static final List<Function<PowerProfile, String>> suffixVMMOEntity = new ArrayList<>();
    private static final List<Function<PowerProfile, String>> prefixVMMOEntity = new ArrayList<>();

    private static void setup() {
        if (WAILAListener.getConfig().getBoolean("valhallammo.levelinfo", true)) {
            prefixVMMOEntity.add(ValhallaMMOCompat::getLevel);
        }
        if (WAILAListener.getConfig().getBoolean("valhallammo.newgameinfo", false)) {
            prefixVMMOEntity.add(ValhallaMMOCompat::getNewGame);
        }
    }

    public static boolean checkVMMO() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("ValhallaMMO");
        boolean isEnabled = pl != null && pl.isEnabled();
        if (isEnabled) {
            ValhallaMMOCompat.setup();
        }
        return isEnabled;
    }
    public static boolean getIsVMMOInstalled() {
        return isVMMOInstalled;
    }
    public static void setIsVMMOInstalled(boolean isVMMOInstalled) {
        ValhallaMMOCompat.isVMMOInstalled = isVMMOInstalled;
    }

    public static boolean handleVMMOEntity(Entity entity, Player player) {
        if (entity instanceof Player pl) {
            PowerProfile plPowerProfile = ProfileCache.getOrCache(pl, PowerProfile.class);
            String name = pl.getDisplayName();
            StringBuilder prefixInfo = new StringBuilder();
            StringBuilder suffixInfo = new StringBuilder();
            StringBuilder info = new StringBuilder();
            for (Function<PowerProfile, String> func : prefixVMMOEntity) {
                prefixInfo.append(func.apply(plPowerProfile));
            }
            for (Function<PowerProfile, String> func : suffixVMMOEntity) {
                suffixInfo.append(func.apply(plPowerProfile));
            }
            if (!prefixInfo.toString().isEmpty()) {
                info.append(prefixInfo).append(" §f| ");
            }
            info.append(name);
            if (!suffixInfo.toString().isEmpty()) {
                info.append(" §f| ").append(suffixInfo);
            }
            WAILAManager.updateBossBar(player, info.toString());
        }
        return false;
    }

    private static String getLevel(PowerProfile profile) {
        int level = profile.getLevel();
        return "§c💪 " + level + "§8 ";
    }
    private static String getNewGame(PowerProfile profile) {
        int newGamePlus = profile.getNewGamePlus();
        return " §6\uD83C\uDD96 " + newGamePlus;
    }
}
