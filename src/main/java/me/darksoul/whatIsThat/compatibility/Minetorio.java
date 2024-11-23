package me.darksoul.whatIsThat.compatibility;

import com.MT.xxxtrigger50xxx.Devices.Device;
import me.darksoul.whatIsThat.Informations;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import me.darksoul.whatIsThat.misc.ConfigUtils;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Minetorio {
    private static final YamlConfiguration mtLang = ConfigUtils.loadMTLang();
    private static boolean isMTInstalled;

    public static void checkMT() {
        WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("Minetorio");
    }

    public static boolean getIsMTInstalled() {
        return Minetorio.isMTInstalled;
    }

    public static boolean getDeviceBlock(Block block, Player player) {
        if (Minetorio.getIsMTInstalled()) {
            Device device = Device.getDevice(block.getLocation());
            if (device != null) {
                String deviceName = mtLang.getString("device." + device.getName(), device.getName());
                WAILAManager.updateBossBar(player, deviceName + " | "
                        + Informations.getTotalItemsInContainer(block)
                        + Informations.getRemainingSmeltTime(block)
                        + Informations.Power(device));
                return true;
            }
        }
        return false;
    }
}
