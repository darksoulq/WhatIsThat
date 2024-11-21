package me.darksoul.whatIsThat;

import com.MT.xxxtrigger50xxx.Devices.Battery2;
import com.MT.xxxtrigger50xxx.Devices.Device;
import me.darksoul.whatIsThat.misc.ItemGroups;
import me.darksoul.whatIsThat.misc.LanguageUtils;
import me.darksoul.whatIsThat.misc.MTMachines;
import me.darksoul.whatIsThat.misc.MathUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Switch;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


public class WAILAListener implements Listener {
    private MTMachines mtMachines;
    private YamlConfiguration mtLang;
    private YamlConfiguration vanillaLang;
    private static File PREF_FOLDER = new File(WhatIsThat.getInstance().getDataFolder(), "cache/players");

    public WAILAListener() {
        mtLang = LanguageUtils.loadMTLang();
        vanillaLang = LanguageUtils.loadVanillaBlocksLang();
        if (!PREF_FOLDER.exists()) {
            PREF_FOLDER.mkdirs();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : WhatIsThat.getInstance().getServer().getOnlinePlayers()) {
                    updateWAILA(player);
                }
            }
        }.runTaskTimer(WhatIsThat.getInstance(), 0, 5);

        if (WhatIsThat.getIsMTInstalled()) {
            mtMachines = new MTMachines();
        }
    }

    private void updateWAILA(Player player) {
        File playerFile = new File(PREF_FOLDER + "/" + player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        boolean disableBossBar = config.getBoolean("disableBossBar", false);

        if (disableBossBar) {
            return;
        }

        WAILAManager.createBossBar(player);
        Block block = MathUtils.getLookingAtBlock(player, 50);
        Entity entity = MathUtils.isLookingAtEntity(player, 50);
        if (block != null && entity == null) {
            if (WhatIsThat.getIsMTInstalled()) {
                Device device = Device.getDevice(block.getLocation());
                if (device != null) {
                    String deviceName = mtLang.getString("device." + device.getName(), device.getName());
                    WAILAManager.updateBossBar(player, deviceName + " | " + Power(device));
                    return;
                }
            }
            String blockName = vanillaLang.getString("block." + block.getType().name(), block.getType().name());
            WAILAManager.updateBossBar(player, blockName + getRedstoneInfo(block));
            return;
        }
        WAILAManager.updateBossBar(player, "");
    }

    @org.bukkit.event.EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        WAILAManager.removeBossBar(event.getPlayer());
    }

    private String Power(Device device) {
        if (mtMachines.isGenerator(device)) {
            if (device.isProducingPower()) {
                return "§a⚡ " + device.getPowerGen();
            } else {
                return "§8⚡ 0";
            }
        } else if (device instanceof Battery2) {
            float fillPercentage = ((float) ((Battery2) device).getStored() / ((Battery2) device).getCapacity()) * 100;
            String color = getColorForPercent(fillPercentage);
            return color + "⚡ " + ((Battery2) device).getStored();
        } else {
            if (device.isPowered()) {
                if (device.getIdlePower() == 0) {
                    return "§c⚡ " + device.getActionPower();
                }
                return "§c⚡ " + device.getActionPower() / device.getIdlePower();
            } else {
                return "§e⚡ 0";
            }
        }
    }

    private String getColorForPercent(float percent) {
        if ( percent > 0 && percent <= 25) {
            return "§c";
        } else if (percent >= 25 && percent <= 50 ) {
            return "§e";
        } else if (percent >= 50 && percent <= 75) {
            return "§a";
        } else if (percent >= 75 && percent <= 100) {
            return "§2";
        }
        return "§8";
    }

    private String getRedstoneInfo(Block block) {
        boolean isPowerSource = block.isBlockIndirectlyPowered();
        int power = block.getBlockPower();
        //RedStone components
        for (Material type : ItemGroups.getRedstoneComponents()) {
            if (block.getType() == type) {
                if (power > 0 || isPowerSource) {
                    return " | §c● " + power;
                } else if (power == 0) {
                    return " | §8● ";
                }
            }
        }
        // All others
        if (power > 0 || isPowerSource) {
            return " | §c● ";
        } else if (power == 0) {
            return " | §8● ";
        }

        return "";
    }

    public static File getPrefFolder() {
        return PREF_FOLDER;
    }
}
