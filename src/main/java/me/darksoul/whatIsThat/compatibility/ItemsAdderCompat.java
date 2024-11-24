package me.darksoul.whatIsThat.compatibility;

import dev.lone.itemsadder.api.*;
import me.darksoul.whatIsThat.Informations;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;
/*
Experimental compatibility
*/
public class ItemsAdderCompat {
    private static boolean isIAInstalled;

    public static boolean checkIA() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("ItemsAdder");
        return pl != null && pl.isEnabled();
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
            WAILAManager.updateBossBar(player, name);
            return true;
        }
        return false;
    }
    public static boolean handleIAEntity(Entity entity, CustomEntity IAEntity, Player player) {
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
            for (Function<Entity, String> func : Informations.getSuffixIAEntity()) {
                IAEntitySInfo.append(func.apply(entity));
            }
            info.append(IAEntityPInfo).append(name).append(IAEntitySInfo);
            WAILAManager.updateBossBar(player, info.toString());
            return true;
        }
        return false;
    }
    // Internal methods
    private static void handleFurniture(CustomFurniture furniture, Player player) {
        String name = furniture.getNamespacedID();
        CustomStack stack = CustomStack.getInstance(name);
        WAILAManager.updateBossBar(player, stack.getDisplayName());
    }
    private static void handleIACrop(CustomCrop crop, Player player) {
        String name = crop.getSeed().getDisplayName();
        StringBuilder IACropSInfo = new StringBuilder();
        String IACropPInfo = "";
        StringBuilder info = new StringBuilder();
        for (Function<CustomCrop, String> func : Informations.getSuffixIACrop()) {
            IACropSInfo.append(func.apply(crop));
        }
        info.append(IACropPInfo).append(name).append(IACropSInfo);
        WAILAManager.updateBossBar(player, info.toString());
    }
    private static void handleIAFire(CustomFire fire, Player player) {
        String name = fire.getDisplayName();
        WAILAManager.updateBossBar(player, name);
    }
}
