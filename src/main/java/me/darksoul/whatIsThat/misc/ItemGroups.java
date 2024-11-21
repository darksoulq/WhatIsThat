package me.darksoul.whatIsThat.misc;

import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.ArrayList;
import java.util.List;

public class ItemGroups {
    private static final List<Material> redstoneProviders = new ArrayList<>();
    private static final List<Material> redstoneComponents = new ArrayList<>();
    static {
        redstoneProviders.add(Material.OBSERVER);
        redstoneProviders.add(Material.TARGET);
        redstoneProviders.add(Material.REDSTONE_BLOCK);
        redstoneProviders.add(Material.REPEATER);
        redstoneComponents.add(Material.REDSTONE_WIRE);
        redstoneComponents.add(Material.COMPARATOR);
        redstoneComponents.add(Material.REDSTONE_LAMP);
        redstoneComponents.add(Material.PISTON);
        redstoneComponents.add(Material.MOVING_PISTON);
        redstoneComponents.add(Material.STICKY_PISTON);
        redstoneComponents.add(Material.PISTON_HEAD);
    }

    public static List<Material> getRedstoneProviders() {
        return redstoneProviders;
    }

    public static List<Material> getRedstoneComponents() {
        return redstoneComponents;
    }
}
