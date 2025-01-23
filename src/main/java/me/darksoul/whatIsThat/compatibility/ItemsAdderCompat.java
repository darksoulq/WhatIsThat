package me.darksoul.whatIsThat.compatibility;

import dev.lone.itemsadder.api.*;
import me.darksoul.whatIsThat.Information;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ItemsAdderCompat {
    private static boolean isInstalled;
    private static final List<Function<CustomCrop, String>> suffixCrop = new ArrayList<>();
    private static final List<Function<Entity, String>> prefixEntity = new ArrayList<>();
    private static final List<Function<Entity, String>> suffixEntity = new ArrayList<>();
    private static final List<EntityType> elligibleFurniture = new ArrayList<>();

    public static void setup() {
        // Clean Up
        prefixEntity.clear();
        suffixEntity.clear();
        suffixCrop.clear();
        // Entities
        if (WAILAListener.getConfig().getBoolean("itemsadder.entities.healthinfo", true)) {
            suffixEntity.add(Information::default_getEntityHealth);
        }
        elligibleFurniture.add(EntityType.ARMOR_STAND);
        elligibleFurniture.add(EntityType.ITEM_DISPLAY);
        // Blocks
        if (WAILAListener.getConfig().getBoolean("itemsadder.blocks.cropinfo", true)) {
            suffixCrop.add(Information::itemsAdder_getCropAge);
        }
    }
    public static void hook() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("ItemsAdder");
        isInstalled = pl != null && pl.isEnabled();
        if (isInstalled) {
            setup();
            WhatIsThat.getInstance().getLogger().info("Hooked into ItemsAdder");
        } else {
            WhatIsThat.getInstance().getLogger().info("ItemsAdder not found, skipping hook");
        }
    }
    public static boolean getIsInstalled() {
        return isInstalled;
    }

    public static boolean handleBlock(Block block, Player player) {
        CustomBlock IABlock = CustomBlock.byAlreadyPlaced(block);
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
        if (IABlock != null) {
            String name = IABlock.getDisplayName();
            WAILAListener.setLookingAt(player, name);
            WAILAListener.setLookingAtPrefix(player, "");
            WAILAListener.setLookingAtSuffix(player, "");
            WAILAListener.setLookingAtInfo(player, name);
            WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                    name);
            return true;
        }
        return false;
    }
    public static boolean handleEntity(Entity entity, Player player) {
        CustomEntity IAEntity = CustomEntity.byAlreadySpawned(entity);
        CustomFurniture furniture = null;
        if (elligibleFurniture.contains(entity.getType())) {
            furniture = CustomFurniture.byAlreadySpawned(entity);
        }
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
            for (Function<Entity, String> func : suffixEntity) {
                IAEntitySInfo.append(func.apply(entity));
            }
            info.append(IAEntityPInfo).append(name).append(IAEntitySInfo);
            WAILAListener.setLookingAt(player, name);
            WAILAListener.setLookingAtPrefix(player, IAEntityPInfo);
            WAILAListener.setLookingAtSuffix(player, IAEntitySInfo.toString());
            WAILAListener.setLookingAtInfo(player, info.toString());
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
        WAILAListener.setLookingAt(player, stack.getDisplayName());
        WAILAListener.setLookingAtPrefix(player, "");
        WAILAListener.setLookingAtSuffix(player, "");
        WAILAListener.setLookingAtInfo(player, stack.getDisplayName());
        WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                stack.getDisplayName());
    }
    private static void handleIACrop(CustomCrop crop, Player player) {
        String name = crop.getSeed().getDisplayName();
        StringBuilder IACropSInfo = new StringBuilder();
        String IACropPInfo = "";
        StringBuilder info = new StringBuilder();
        for (Function<CustomCrop, String> func : ItemsAdderCompat.getSuffixCrop()) {
            IACropSInfo.append(func.apply(crop));
        }
        info.append(IACropPInfo).append(name).append(IACropSInfo);
        WAILAListener.setLookingAt(player, name);
        WAILAListener.setLookingAtPrefix(player, IACropPInfo);
        WAILAListener.setLookingAtSuffix(player, IACropSInfo.toString());
        WAILAListener.setLookingAtInfo(player, info.toString());
        WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                info.toString());
    }
    private static void handleIAFire(CustomFire fire, Player player) {
        String name = fire.getDisplayName();
        WAILAListener.setLookingAt(player, name);
        WAILAListener.setLookingAtPrefix(player, "");
        WAILAListener.setLookingAtSuffix(player, "");
        WAILAListener.setLookingAtInfo(player, name);
        WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                name);
    }
    public static List<Function<CustomCrop, String>> getSuffixCrop() {
        return suffixCrop;
    }
}
