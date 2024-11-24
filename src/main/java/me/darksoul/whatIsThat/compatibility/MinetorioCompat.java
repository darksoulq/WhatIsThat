package me.darksoul.whatIsThat.compatibility;

import com.MT.xxxtrigger50xxx.Devices.Device;
import com.MT.xxxtrigger50xxx.Devices.Mover;
import me.darksoul.whatIsThat.Informations;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import me.darksoul.whatIsThat.misc.ConfigUtils;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

public class MinetorioCompat {
    private static YamlConfiguration mtLang;
    private static boolean isMTInstalled;

    public static boolean checkMT() {
        mtLang = ConfigUtils.loadMTLang();
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
        String deviceName = "";
        if (device != null || mover != null) {
            if (mover != null) {
                deviceName = mtLang.getString("device.com.MT.xxxtrigger50xxx.Devices.Mover", "Mover");
            } else {
                deviceName = mtLang.getString("device." + device.getName(), device.getName());
            }
            StringBuilder MTBlockSInfo = new StringBuilder();
            StringBuilder MTBlockPInfo = new StringBuilder();
            StringBuilder info = new StringBuilder();
            for (Function<Block, String> func : Informations.getSuffixMTBlocks()) {
                MTBlockSInfo.append(func.apply(block));
            }
            for (Function<Block, String> func : Informations.getPrefixMTBlocks()) {
                MTBlockPInfo.append(func.apply(block));
            }
            info.append(MTBlockPInfo).append(deviceName).append(MTBlockSInfo);
            WAILAManager.updateBossBar(player, info.toString());
            return true;
        }
        return false;
    }
}
