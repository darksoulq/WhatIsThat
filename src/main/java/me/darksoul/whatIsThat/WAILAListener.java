package me.darksoul.whatIsThat;

import com.MT.xxxtrigger50xxx.Devices.Battery2;
import com.MT.xxxtrigger50xxx.Devices.Device;
import me.darksoul.whatIsThat.misc.ItemGroups;
import me.darksoul.whatIsThat.misc.LanguageUtils;
import me.darksoul.whatIsThat.misc.MTMachines;
import me.darksoul.whatIsThat.misc.MathUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Campfire;
import org.bukkit.block.Furnace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;


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
            WAILAManager.updateBossBar(player, getTotalItemsInContainer(block)
                    + blockName
                    + getRedstoneInfo(block)
                    + getHarvestInfo(block)
                    + getHoneyInfo(block)
                    + getRemainingSmeltTime(block));
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
        if ( percent >= 0 && percent <= 25) {
            return "§c";
        } else if (percent > 25 && percent <= 50 ) {
            return "§e";
        } else if (percent > 50 && percent <= 75) {
            return "§a";
        } else if (percent > 75) {
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
        // Redstone Providers
        for (Material type : ItemGroups.getRedstoneProviders()) {
            if (block.getType() == type) {
                if (power > 0 || isPowerSource) {
                    return " | §c● ";
                } else if (power == 0) {
                    return " | §8● ";
                }
            }
        }

        return "";
    }

    private String getHarvestInfo(Block block) {
        for (Material type : ItemGroups.getCrops()) {
            if (block.getType() == type) {
                BlockData data = block.getBlockData();
                if (data instanceof Ageable) {
                    int age = ((Ageable) data).getAge();
                    int maxAge = ((Ageable) data).getMaximumAge();

                    double percentage = (age / (double) maxAge) * 100;

                    if (percentage >= 0 && percentage <= 25) {
                        return " | " + getColorForPercent((float) percentage) + "\uD83C\uDF31 " + age + "/" + maxAge;
                    } else if (percentage > 25 && percentage <= 50) {
                        return " | " + getColorForPercent((float) percentage) + "\uD83C\uDF3F " + age + "/" + maxAge;
                    } else if (percentage > 50 && percentage <= 75) {
                        return " | " + getColorForPercent((float) percentage) + "\uD83C\uDF3D " + age + "/" + maxAge;
                    } else if (percentage > 75) {
                        return " | " + getColorForPercent((float) percentage) + "\uD83C\uDF3D " + age + "/" + maxAge;
                    }
                }
            }
        }

        return "";
    }

    private String getHoneyInfo(Block block) {
        for (Material type : ItemGroups.getHoneyProducers()) {
            if (block.getType() == type) {
                BlockData data = block.getBlockData();
                if (data instanceof Beehive) {
                    int honeyLevel = ((Beehive) data).getHoneyLevel();
                    int maxHoneyLevel = ((Beehive) data).getMaximumHoneyLevel();

                    double percentage = (honeyLevel / (double) maxHoneyLevel) * 100;

                    return " | " + getColorForPercent((float) percentage) + "\uD83D\uDC1D "
                            + honeyLevel + "/"
                            + maxHoneyLevel;
                }
            }
        }

        return "";
    }

    private String getRemainingSmeltTime(Block block) {
        for (Material type : ItemGroups.getFurnaces()) {
            if (block.getType() == type) {
                BlockState state = block.getState();
                InventoryHolder ih = (InventoryHolder) state;
                FurnaceInventory inventory = (FurnaceInventory) ih.getInventory();

                int cookTime = ((Furnace) state).getCookTime();
                int cookTimeTotal = ((Furnace) state).getCookTimeTotal();

                int ticksRemaining = cookTimeTotal - cookTime;
                int secondsRemaining = ticksRemaining / 20;
                float percentage = ((float) cookTime / cookTimeTotal) * 100;
                if (inventory.getSmelting() != null && cookTime != 0) {
                    return " | " + getColorForPercent(percentage) + "⌛ " + secondsRemaining + "s";
                }
            }
        }
        return "";
    }

    private String getTotalItemsInContainer(Block block) {
        for (Material type : ItemGroups.getContainers()) {
            if (block.getType() == type) {
                BlockState state = block.getState();
                InventoryHolder ih = (InventoryHolder) state;
                Inventory inventory = ih.getInventory();

                int totalItems = 0;

                for (ItemStack item : inventory.getContents()) {
                    if (item != null) {
                        totalItems += item.getAmount();
                    }
                }

                return "§6\uD83D\uDCE6 " + totalItems + " §f| ";
            }
        }

        return "";
    }

    private String isBedSpawn(Block block, Player player) {
        for (Material type : ItemGroups.getBeds()) {
            if (block.getType() == type) {
                Location playerBedSpawn = player.getRespawnLocation();
                if (playerBedSpawn != null) {
                    Vector bedLoc = new Vector(block.getX(), block.getY(), block.getZ());
                    Vector playerSpawnLoc = new Vector(playerBedSpawn.getX(), playerBedSpawn.getY(), playerBedSpawn.getZ());
                    if (bedLoc.equals(playerSpawnLoc)) {
                        return " | " + "§a\uD83D\uDECF";
                    }
                }
                return " | " + "§c\uD83D\uDECF";
            }
        }

        return "";
    }

    public static File getPrefFolder() {
        return PREF_FOLDER;
    }
}
