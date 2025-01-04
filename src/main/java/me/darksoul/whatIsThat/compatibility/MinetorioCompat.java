package me.darksoul.whatIsThat.compatibility;

import com.MT.xxxtrigger50xxx.Devices.Device;
import com.MT.xxxtrigger50xxx.Devices.Mover;
import me.darksoul.whatIsThat.Information;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import me.darksoul.whatIsThat.misc.ConfigUtils;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

public class MinetorioCompat {
    private static boolean isMTInstalled;

    public static boolean checkMT() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("Minetorio");
        return pl != null && pl.isEnabled();
    }
    public static boolean getIsMTInstalled() {
        return MinetorioCompat.isMTInstalled;
    }
    public static void setIsMTInstalled(boolean isMTInstalled) {
        MinetorioCompat.isMTInstalled = isMTInstalled;
    }

    public static boolean handleMTDisplay(Block block, Player player) {
        Device device = Device.getDevice(block.getLocation());
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
            for (Function<Block, String> func : Information.getSuffixMTBlocks()) {
                MTBlockSInfo.append(func.apply(block));
            }
            for (Function<Block, String> func : Information.getPrefixMTBlocks()) {
                MTBlockPInfo.append(func.apply(block));
            }
            if (!MTBlockPInfo.isEmpty()) {
                info.append(MTBlockPInfo).append(" §f| ");
            }
            info.append(deviceName);
            if (!MTBlockSInfo.isEmpty()) {
                info.append(" §f| ").append(MTBlockSInfo);
            }
            WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                    info.toString());
            return true;
        }
        return false;
    }
}
