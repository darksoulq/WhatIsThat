package me.darksoul.whatIsThat.compatibility;

import me.darksoul.whatIsThat.Information;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.display.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ValhallaMMOCompat {
    private static boolean isInstalled;
    private static boolean isRaceInstalled;
    private static final List<Function<Player, String>> suffixEntity = new ArrayList<>();
    private static final List<Function<Player, String>> prefixEntity = new ArrayList<>();

    public static void setup() {
        if (WAILAListener.getConfig().getBoolean("valhallammo.levelinfo", true)) {
             suffixEntity.add(Information::vmmo_getLevel);
        }
        if (WAILAListener.getConfig().getBoolean("valhallammo.raceinfo", true) && ValhallaMMOCompat.getIsRaceInstalled()) {
            prefixEntity.add(Information::vmmo_getRace);
        }
    }

    public static void hook() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("ValhallaMMO");
        isInstalled = pl != null && pl.isEnabled();
        if (isInstalled) {
            ValhallaMMOCompat.setup();
            WhatIsThat.getInstance().getLogger().info("Hooked into ValhallaMMO");
        } else {
            WhatIsThat.getInstance().getLogger().info("ValhallaMMO not found, skipping hook");
        }
    }
    public static void hookRaces() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("ValhallaRaces");
        isRaceInstalled = pl != null && pl.isEnabled();
        if (isRaceInstalled) {
            ValhallaMMOCompat.setup();
            WhatIsThat.getInstance().getLogger().info("Hooked into ValhallaRaces");
        } else {
            WhatIsThat.getInstance().getLogger().info("ValhallaRaces not found, skipping hook");
        }
    }
    public static boolean getIsInstalled() {
        return isInstalled;
    }
    public static boolean getIsRaceInstalled() {
        return isRaceInstalled;
    }

    public static boolean handleEntity(Entity entity, Player player) {
        if (entity instanceof Player pl) {
            String name = pl.getDisplayName();
            StringBuilder prefixInfo = new StringBuilder();
            StringBuilder suffixInfo = new StringBuilder();
            StringBuilder info = new StringBuilder();
            for (Function<Player, String> func : prefixEntity) {
                prefixInfo.append(func.apply(player));
            }
            for (Function<Player, String> func : suffixEntity) {
                suffixInfo.append(func.apply(player));
            }
            if (!prefixInfo.toString().isEmpty()) {
                info.append(prefixInfo).append(Information.getValuesFile().getString("SPLITTER", " §f| "));
            }
            info.append(name);
            if (!suffixInfo.toString().isEmpty()) {
                info.append(Information.getValuesFile().getString("SPLITTER", " §f| ")).append(suffixInfo);
            }
            WAILAListener.setLookingAt(player, name);
            WAILAListener.setLookingAtPrefix(player, prefixInfo.toString());
            WAILAListener.setLookingAtSuffix(player, suffixInfo.toString());
            WAILAListener.setLookingAtInfo(player, info.toString());
            WAILAManager.setBar(player, info.toString());
        }
        return false;
    }
}
