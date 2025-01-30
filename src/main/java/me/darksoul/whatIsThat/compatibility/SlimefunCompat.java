package me.darksoul.whatIsThat.compatibility;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.darksoul.whatIsThat.Information;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.display.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class SlimefunCompat {
    private static boolean isInstalled;

    private static List<BiFunction<SlimefunItem, Block, String>> prefixBlock = new ArrayList<>();
    private static List<BiFunction<SlimefunItem, Block, String>> suffixBlock = new ArrayList<>();

    public static void setup() {
        prefixBlock.clear();
        suffixBlock.clear();
        if (WAILAListener.getConfig().getBoolean("slimefun.storedenergyinfo", true)) {
            suffixBlock.add(Information::slimefun_getStoredEnergy);
        }
        if (WAILAListener.getConfig().getBoolean("slimefun.energygeninfo", true)) {
            suffixBlock.add(Information::slimefun_getEnergyGen);
        }
    }
    public static void hook() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("Slimefun");
        isInstalled = pl != null && pl.isEnabled();
        if (isInstalled) {
            setup();
            WhatIsThat.getInstance().getLogger().info("Hooked into Slimefun");
        } else {
            WhatIsThat.getInstance().getLogger().info("Slimefun not found, skipping hook");
        }
    }
    public static boolean getIsInstalled() {
        return isInstalled;
    }

    public static boolean handleBlock(Block block, Player player) {
        SlimefunItem item = BlockStorage.check(block);
        if (item != null) {
            String name = item.getItemName();
            StringBuilder info = new StringBuilder();
            StringBuilder prefix = new StringBuilder();
            StringBuilder suffix = new StringBuilder();
            for (BiFunction<SlimefunItem, Block, String> func : prefixBlock) {
                prefix.append(func.apply(item, block));
            }
            for (BiFunction<SlimefunItem, Block, String> func : suffixBlock) {
                suffix.append(func.apply(item, block));
            }
            if (!prefix.isEmpty()) {
                info.append(prefix).append(Information.getValuesFile().getString("SPLITTER", " §f| "));
            }
            info.append(name);
            if (!suffix.isEmpty()) {
                info.append(Information.getValuesFile().getString("SPLITTER", " §f| ")).append(suffix);
            }
            WAILAListener.setLookingAt(player, name);
            WAILAListener.setLookingAtPrefix(player, prefix.toString());
            WAILAListener.setLookingAtSuffix(player, suffix.toString());
            WAILAListener.setLookingAtInfo(player, info.toString());
            WAILAManager.setBar(player, info.toString());
            return true;
        }
        return false;
    }
}
