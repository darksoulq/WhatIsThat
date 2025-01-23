package me.darksoul.whatIsThat.compatibility;

import com.MT.xxxtrigger50xxx.API.MTAPI;
import com.MT.xxxtrigger50xxx.Devices.Device;
import com.MT.xxxtrigger50xxx.Devices.Mover;
import me.darksoul.whatIsThat.Information;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MinetorioCompat {
    private static boolean isInstalled;
    private static final List<Function<Block, String>> prefixBlock = new ArrayList<>();
    private static final List<Function<Block, String>> suffixBlock = new ArrayList<>();

    public static void setup() {
        prefixBlock.clear();
        suffixBlock.clear();
        if (WAILAListener.getConfig().getBoolean("minetorio.containerinfo", true)) {
            prefixBlock.add(Information::default_getTotalItemsInContainer);
        }
        if (WAILAListener.getConfig().getBoolean("minetorio.powerinfo", true)) {
            suffixBlock.add(Information::minetorio_getPower);
        }
        if (WAILAListener.getConfig().getBoolean("minetorio.smeltinfo", true)) {
            suffixBlock.add(Information::default_getRemainingSmeltTime);
        }
    }

    public static void hook() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("Minetorio");
        isInstalled = pl != null && pl.isEnabled();
        if (isInstalled) {
            setup();
            WhatIsThat.getInstance().getLogger().info("Hooked into Minetorio");
        } else {
            WhatIsThat.getInstance().getLogger().info("Minetorio not found, skipping hook");
        }
    }
    public static boolean getIsInstalled() {
        return MinetorioCompat.isInstalled;
    }

    public static boolean handleBlock(Block block, Player player) {
        Device device = MTAPI.getDevice(block.getLocation());
        Mover mover = Mover.getMover(block.getLocation());
        String deviceName;
        if (device != null || mover != null) {
            if (mover != null) {
                deviceName = "Mover";
            } else {
                deviceName = device.getName();
            }
            StringBuilder MTBlockSInfo = new StringBuilder();
            StringBuilder MTBlockPInfo = new StringBuilder();
            StringBuilder info = new StringBuilder();
            for (Function<Block, String> func : suffixBlock) {
                MTBlockSInfo.append(func.apply(block));
            }
            for (Function<Block, String> func : prefixBlock) {
                MTBlockPInfo.append(func.apply(block));
            }
            if (!MTBlockPInfo.isEmpty()) {
                info.append(MTBlockPInfo).append(" §f| ");
            }
            info.append(deviceName);
            if (!MTBlockSInfo.isEmpty()) {
                info.append(" §f| ").append(MTBlockSInfo);
            }
            WAILAListener.setLookingAt(player, deviceName);
            WAILAListener.setLookingAtPrefix(player, MTBlockPInfo.toString());
            WAILAListener.setLookingAtSuffix(player, MTBlockSInfo.toString());
            WAILAListener.setLookingAtInfo(player, info.toString());
            WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                    info.toString());
            return true;
        }
        return false;
    }
}
