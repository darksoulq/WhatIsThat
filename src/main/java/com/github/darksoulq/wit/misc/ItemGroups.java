package com.github.darksoulq.wit.misc;

import com.github.darksoulq.wit.WITListener;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemGroups {
    private static final List<Material> CONTAINERS = new ArrayList<>();
    private static final List<EntityType> BLACKLISTED_ENTITIES = new ArrayList<>();
    private static final List<Material> BlACKLISTED_BLOCKS = new ArrayList<>();

    static {
        // Containers
        CONTAINERS.add(Material.CHEST);
        CONTAINERS.add(Material.TRAPPED_CHEST);
        CONTAINERS.add(Material.BARREL);
        CONTAINERS.add(Material.HOPPER);
        for (Material mat : Material.values()) if (mat.name().endsWith("SHULKER_BOX")) CONTAINERS.add(mat);

        // BlackListed Blocks
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
}
