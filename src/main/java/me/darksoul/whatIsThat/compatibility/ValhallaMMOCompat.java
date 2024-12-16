package me.darksoul.whatIsThat.compatibility;

import me.athlaeos.valhallammo.playerstats.profiles.ProfileCache;
import me.athlaeos.valhallammo.playerstats.profiles.implementations.PowerProfile;
import me.athlaeos.valhallaraces.Race;
import me.athlaeos.valhallaraces.RaceManager;
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
    private static boolean isVRacesInstalled;
    private static final List<Function<Player, String>> suffixVMMOEntity = new ArrayList<>();
    private static final List<Function<Player, String>> prefixVMMOEntity = new ArrayList<>();

    private static void setup() {
        if (WAILAListener.getConfig().getBoolean("valhallammo.levelinfo", true)) {
             suffixVMMOEntity.add(ValhallaMMOCompat::getLevel);
        }
        if (WAILAListener.getConfig().getBoolean("valhallammo.raceinfo", true)) {
            prefixVMMOEntity.add(ValhallaMMOCompat::getRace);
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
    public static boolean checkVRaces() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("ValhallaRaces");
        boolean isEnabled = pl != null && pl.isEnabled();
        if (isEnabled) {
            ValhallaMMOCompat.setup();
        }
        return isEnabled;
    }
    public static boolean getIsVMMOInstalled() {
        return isVMMOInstalled;
    }
    public static boolean getIsVRacesInstalled() {
        return isVRacesInstalled;
    }
    public static void setIsVMMOInstalled(boolean isVMMOInstalled) {
        ValhallaMMOCompat.isVMMOInstalled = isVMMOInstalled;
    }
    public static void setIsVRacesInstalled(boolean isVRacesInstalled) {
        ValhallaMMOCompat.isVRacesInstalled = isVRacesInstalled;
    }

    public static boolean handleVMMOEntity(Entity entity, Player player) {
        if (entity instanceof Player pl) {
            String name = pl.getDisplayName();
            StringBuilder prefixInfo = new StringBuilder();
            StringBuilder suffixInfo = new StringBuilder();
            StringBuilder info = new StringBuilder();
            for (Function<Player, String> func : prefixVMMOEntity) {
                prefixInfo.append(func.apply(player));
            }
            for (Function<Player, String> func : suffixVMMOEntity) {
                suffixInfo.append(func.apply(player));
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

    private static String getLevel(Player player) {
        PowerProfile profile = ProfileCache.getOrCache(player, PowerProfile.class);
        int level = profile.getLevel();
        return "§c💪 " + level + "§8 ";
    }
    private static String getRace(Player player) {
        Race race = RaceManager.getRace(player);
        if (race != null) {
            String name = race.getDisplayName();
            return name;
        }
        return "";
    }
}
