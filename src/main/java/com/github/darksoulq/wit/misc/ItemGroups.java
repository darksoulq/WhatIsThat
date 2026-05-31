package com.github.darksoulq.wit.misc;

import com.github.darksoulq.wit.WITListener;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class ItemGroups {
    private static final List<Material> CONTAINERS = new ArrayList<>();
    private static final List<EntityType> BLACKLISTED_ENTITIES = new ArrayList<>();
    private static final List<Material> BlACKLISTED_BLOCKS = new ArrayList<>();
    private static boolean BLOCK_WHITELIST = false;
    private static boolean ENTITY_WHITELIST = false;

    static {
        // Containers
        CONTAINERS.add(Material.BARREL);
        CONTAINERS.add(Material.HOPPER);
        for (Material mat : Material.values()) if (mat.name().endsWith("SHULKER_BOX")) CONTAINERS.add(mat);
        for (Material mat : Material.values()) if (mat.name().endsWith("CHEST")) CONTAINERS.add(mat);

        // BlackListed Blocks
        BLOCK_WHITELIST = WITListener.getConfig().getBoolean("core.block_whitelist");
        ENTITY_WHITELIST = WITListener.getConfig().getBoolean("core.entity_whitelist");
        for (String mat : WITListener.getConfig().getStringList("block-blacklist")) {
            BlACKLISTED_BLOCKS.add(Material.valueOf(mat));
        }

        // Entities to not render for
        for (String type : WITListener.getConfig().getStringList("entity-blacklist")) {
            BLACKLISTED_ENTITIES.add(EntityType.valueOf(type));
        }
    }

    public static void reload() {
        BLACKLISTED_ENTITIES.clear();
        BlACKLISTED_BLOCKS.clear();

        for (String type : WITListener.getConfig().getStringList("entity-blacklist")) {
            BLACKLISTED_ENTITIES.add(EntityType.valueOf(type));
        }
        for (String mat : WITListener.getConfig().getStringList("block-blacklist")) {
            BlACKLISTED_BLOCKS.add(Material.valueOf(mat));
        }
    }
    public static List<Material> getContainers() {
        return CONTAINERS;
    }
    public static List<Material> getBlacklistedBlocks() {
        return BlACKLISTED_BLOCKS;
    }
    public static List<EntityType> getBlacklistedEntities() {
        return BLACKLISTED_ENTITIES;
    }

    public static boolean isBlockWhitelist() {
        return BLOCK_WHITELIST;
    }

    public static boolean isEntityWhitelist() {
        return ENTITY_WHITELIST;
    }

    public static boolean isAllowedBlock(Material type) {
        boolean contains = ItemGroups.getBlacklistedBlocks().contains(type);
        return ItemGroups.isBlockWhitelist() == contains;
    }

    public static boolean isAllowedEntity(EntityType type) {
        boolean contains = ItemGroups.getBlacklistedEntities().contains(type);
        return ItemGroups.isEntityWhitelist() == contains;
    }
}
