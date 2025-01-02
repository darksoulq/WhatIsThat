package me.darksoul.whatIsThat.compatibility;

import dev.lone.itemsadder.api.*;
import me.darksoul.whatIsThat.Information;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/*
Experimental compatibility
*/
public class ItemsAdderCompat {
    private static boolean isIAInstalled;
    private static final List<Function<CustomCrop, String>> suffixIACrop = new ArrayList<>();

    private static void setup() {
        if (WAILAListener.getConfig().getBoolean("itemsadder.blocks.cropinfo", true)) {
            suffixIACrop.add(ItemsAdderCompat::getIAHarvestInfo);
        }
    }
    public static boolean checkIA() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("ItemsAdder");
        boolean isEnabled = pl != null && pl.isEnabled();
        if (isEnabled) {
            setup();
        }
        return isEnabled;
    }
    public static void setIsIAInstalled(boolean isIAInstalled) {
        ItemsAdderCompat.isIAInstalled = isIAInstalled;
    }
    public static boolean getIsIAInstalled() {
        return isIAInstalled;
    }

    public static boolean handleIABlocks(Block block, Player player) {
        CustomBlock IABlock = CustomBlock.byAlreadyPlaced(block);
        CustomFurniture furniture = CustomFurniture.byAlreadySpawned(block);
        CustomFire fire = CustomFire.byAlreadyPlaced(block);
        CustomCrop crop = CustomCrop.byAlreadyPlaced(block);
        if (crop != null) {
            handleIACrop(crop, player);
            return true;
        }
        if (fire != null) {
            handleIAFire(fire, player);
            return true;
        }
        if (furniture != null) {
            handleFurniture(furniture, player);
            return true;
        }
        if (IABlock != null) {
            String name = IABlock.getDisplayName();
            WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                    name);
            return true;
        }
        return false;
    }
    public static boolean handleIAEntity(Entity entity, Player player) {
        CustomEntity IAEntity = CustomEntity.byAlreadySpawned(entity);
        CustomFurniture furniture = CustomFurniture.byAlreadySpawned(entity);
        if (furniture != null) {
            handleFurniture(furniture, player);
            return true;
        }
        if (IAEntity != null) {
            String name = IAEntity.getEntity().getType().name();
            if (entity.customName() != null) {
                name = entity.getCustomName();
            }
            StringBuilder IAEntitySInfo = new StringBuilder();
            String IAEntityPInfo = "";
            StringBuilder info = new StringBuilder();
            for (Function<Entity, String> func : Information.getSuffixIAEntity()) {
                IAEntitySInfo.append(func.apply(entity));
            }
            info.append(IAEntityPInfo).append(name).append(IAEntitySInfo);
            WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                    info.toString());
            return true;
        }
        return false;
    }

    // Internal methods
    private static void handleFurniture(CustomFurniture furniture, Player player) {
        String name = furniture.getNamespacedID();
        CustomStack stack = CustomStack.getInstance(name);
        WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                stack.getDisplayName());
    }
    private static void handleIACrop(CustomCrop crop, Player player) {
        String name = crop.getSeed().getDisplayName();
        StringBuilder IACropSInfo = new StringBuilder();
        String IACropPInfo = "";
        StringBuilder info = new StringBuilder();
        for (Function<CustomCrop, String> func : ItemsAdderCompat.getSuffixIACrop()) {
            IACropSInfo.append(func.apply(crop));
        }
        info.append(IACropPInfo).append(name).append(IACropSInfo);
        WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                info.toString());
    }
    private static void handleIAFire(CustomFire fire, Player player) {
        String name = fire.getDisplayName();
        WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                name);
    }
    private static String getIAHarvestInfo(CustomCrop crop) {
        int age = crop.getAge();
        int maxAge = crop.getMaxAge();
        int percentage = (age / (int) maxAge) * 100;

        if (percentage >= 0 && percentage <= 25) {
            return " " + Information.getColorForPercent((float) percentage) + "\uD83C\uDF31 " + age + "/" + maxAge;
        } else if (percentage > 25 && percentage <= 50) {
            return " " + Information.getColorForPercent((float) percentage) + "\uD83C\uDF3F " + age + "/" + maxAge;
        } else if (percentage > 50 && percentage <= 75) {
            return " " + Information.getColorForPercent((float) percentage) + "\uD83C\uDF3D " + age + "/" + maxAge;
        } else if (percentage > 75) {
            return " | " + Information.getColorForPercent((float) percentage) + "\uD83C\uDF3D " + age + "/" + maxAge;
        }
        return "";
    }
    public static List<Function<CustomCrop, String>> getSuffixIACrop() {
        return suffixIACrop;
    }
}
