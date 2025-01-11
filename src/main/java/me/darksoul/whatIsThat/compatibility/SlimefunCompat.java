package me.darksoul.whatIsThat.compatibility;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlock;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.SolarGenerator;
import io.github.thebusybiscuit.slimefun4.implementation.operations.FuelOperation;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class SlimefunCompat {
    private static boolean isSlimefunInstalled;

    private static List<BiFunction<SlimefunItem, Block, String>> prefixFuncs = new ArrayList<>();
    private static List<BiFunction<SlimefunItem, Block, String>> suffixFuncs = new ArrayList<>();
    private static List<MultiBlock> multiBlocks = new ArrayList<>();

    public static void checkSlimefun() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("Slimefun");
        isSlimefunInstalled = pl != null && pl.isEnabled();
        if (isSlimefunInstalled) {
            setupInfoFuncs();
            WhatIsThat.getInstance().getLogger().info("Hooked into Slimefun");
        } else {
            WhatIsThat.getInstance().getLogger().info("Slimefun not found, skipping hook");
        }
    }
    public static boolean getIsSlimefunInstalled() {
        return isSlimefunInstalled;
    }

    public static boolean handleSlimefunMachines(Block block, Player player) {
        SlimefunItem item = BlockStorage.check(block);
        if (item != null) {
            String name = item.getItemName();
            StringBuilder info = new StringBuilder();
            StringBuilder prefix = new StringBuilder();
            StringBuilder suffix = new StringBuilder();
            for (BiFunction<SlimefunItem, Block, String> func : prefixFuncs) {
                prefix.append(func.apply(item, block));
            }
            for (BiFunction<SlimefunItem, Block, String> func : suffixFuncs) {
                suffix.append(func.apply(item, block));
            }
            if (!prefix.isEmpty()) {
                info.append(prefix).append(" §f| ");
            }
            info.append(name);
            if (!suffix.isEmpty()) {
                info.append(" §f| ").append(suffix);
            }
            WAILAListener.setLookingAt(player, name);
            WAILAListener.setLookingAtPrefix(player, prefix.toString());
            WAILAListener.setLookingAtSuffix(player, suffix.toString());
            WAILAListener.setLookingAtInfo(player, info.toString());
            WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                    info.toString());
            return true;
        }
        return false;
    }

    // Internal Methods
    private static void setupInfoFuncs() {
        if (WAILAListener.getConfig().getBoolean("slimefun.storedenergyinfo", true)) {
            suffixFuncs.add(SlimefunCompat::getStoredEnergy);
        }
        if (WAILAListener.getConfig().getBoolean("slimefun.energygeninfo", true)) {
            suffixFuncs.add(SlimefunCompat::getEnergyGen);
        }
    }
    private static void loadMultiBlocks() {
        multiBlocks = Slimefun.getRegistry().getMultiBlocks();
    }
    private static String getStoredEnergy(SlimefunItem item, Block block) {
        if (item instanceof EnergyNetComponent energyNetComponent) {
            EnergyNetComponentType energyNetComponentType = energyNetComponent.getEnergyComponentType();
            if (energyNetComponentType == EnergyNetComponentType.CAPACITOR
                    || energyNetComponentType == EnergyNetComponentType.GENERATOR
                    || energyNetComponentType == EnergyNetComponentType.CONSUMER) {
                if (energyNetComponent.getCapacity() != 0) {
                    if (energyNetComponent.getCharge(block.getLocation()) == 0) {
                        return " §8⚡ " + energyNetComponent.getCharge(block.getLocation()) + "/" + energyNetComponent.getCapacity();
                    } else {
                        return " §2⚡ " + energyNetComponent.getCharge(block.getLocation()) + "/" + energyNetComponent.getCapacity();
                    }
                }
            }
        }
        return "";
    }
    private static String getEnergyGen(SlimefunItem item, Block block) {
        if (item instanceof  AGenerator gen) {
            int generation = gen.getEnergyProduction();
            FuelOperation operation = gen.getMachineProcessor().getOperation(block.getLocation());
            StringBuilder info = new StringBuilder();
            if (operation != null) {
                info.append(" §2⬆ ").append(generation);
            } else {
                info.append(" §8➖");
            }
            return info.toString();
        }
        if (item instanceof SolarGenerator gen) {
            int dayEnergy = gen.getDayEnergy();
            int nightEnergy = gen.getNightEnergy();
            return " §6☀ " + dayEnergy + " §7\uD83C\uDF12 " + nightEnergy;
        }
        return "";
    }
    // Unfinished  Start
    private static boolean isBlockMultiblock(Block block, MultiBlock multiBlock) {
        int size = (int) Math.cbrt(multiBlock.getStructure().length);

        for (int i = 0; i < multiBlock.getStructure().length; i++) {
            if (block.getType() == multiBlock.getStructure()[i]) {
                if (isCompleteMultiBlock(block, multiBlock.getStructure(), size, i)) {
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean isCompleteMultiBlock(Block block, Material[] pattern, int size, int index) {
        int x = index % size;
        int y = (index / size) % size;
        int z = index / (size * size);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    int relX = x + dx;
                    int relY = y + dy;
                    int relZ = z + dz;

                    if (relX >= 0 && relX < size && relY >= 0 && relY < size && relZ >= 0 && relZ < size) {
                        int relIndex = relX + relY * size + relZ * size * size;
                        Block adjacent = block.getRelative(dx, dy, dz);
                        if (adjacent.getType() != pattern[relIndex]) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    // Unfinished End
}
