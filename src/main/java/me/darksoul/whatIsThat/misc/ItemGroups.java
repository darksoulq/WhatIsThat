package me.darksoul.whatIsThat.misc;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.data.type.Door;

import java.util.ArrayList;
import java.util.List;

public class ItemGroups {
    private static final List<Material> redstoneProviders = new ArrayList<>();
    private static final List<Material> redstoneComponents = new ArrayList<>();
    private static final List<Material> crops = new ArrayList<>();
    private static final List<Material> honeyProducers = new ArrayList<>();
    private static final List<Material> furnaces = new ArrayList<>();
    private static final List<Material> containers = new ArrayList<>();
    private static final List<Material> beds = new ArrayList<>();

    static {
        // RedStone
        redstoneProviders.add(Material.OBSERVER);
        redstoneProviders.add(Material.TARGET);
        redstoneProviders.add(Material.REDSTONE_BLOCK);
        redstoneProviders.add(Material.REPEATER);
        for (Material type : Material.values()) {
            String typeString = type.toString();
            if (typeString.endsWith("_DOOR")
                    || typeString.endsWith("_TRAPDOOR")
                    || typeString.endsWith("_FENCE_GATE")
                    || typeString.endsWith("_PRESSURE_PLATE")
                    || typeString.endsWith("_BUTTON")
                    || typeString.endsWith("_BULB")
                    || type == Material.LEVER) {
                redstoneProviders.add(type);
            }
            if (typeString.endsWith("_WOOL")) {
                redstoneComponents.add(type);
            }
            if (typeString.endsWith("_BED")) {
                beds.add(type);
            }
        }
        redstoneProviders.add(Material.BELL);
        redstoneProviders.add(Material.REDSTONE_LAMP);
        redstoneProviders.add(Material.ACTIVATOR_RAIL);
        redstoneProviders.add(Material.DETECTOR_RAIL);
        redstoneProviders.add(Material.DAYLIGHT_DETECTOR);
        redstoneProviders.add(Material.POWERED_RAIL);
        redstoneProviders.add(Material.NOTE_BLOCK);
        redstoneProviders.add(Material.OBSERVER);
        redstoneProviders.add(Material.JUKEBOX);
        redstoneProviders.add(Material.TRAPPED_CHEST);
        redstoneProviders.add(Material.HOPPER);
        redstoneProviders.add(Material.DROPPER);
        redstoneProviders.add(Material.TARGET);
        redstoneProviders.add(Material.PISTON);
        redstoneProviders.add(Material.PISTON_HEAD);
        redstoneProviders.add(Material.STICKY_PISTON);
        redstoneProviders.add(Material.CRAFTER);
        redstoneProviders.add(Material.DISPENSER);
        redstoneProviders.add(Material.TRIPWIRE_HOOK);
        redstoneProviders.add(Material.SCULK_SHRIEKER);
        redstoneProviders.add(Material.SCULK_SENSOR);
        redstoneProviders.add(Material.CALIBRATED_SCULK_SENSOR);
        redstoneProviders.add(Material.REDSTONE_TORCH);
        redstoneComponents.add(Material.REDSTONE_WIRE);
        redstoneComponents.add(Material.COMPARATOR);

        // Crops
        crops.add(Material.WHEAT);
        crops.add(Material.BEETROOTS);
        crops.add(Material.MELON_STEM);
        crops.add(Material.PUMPKIN_STEM);
        crops.add(Material.POTATOES);
        crops.add(Material.CARROTS);
        crops.add(Material.COCOA);

        // Honey blocks
        honeyProducers.add(Material.BEEHIVE);
        honeyProducers.add(Material.BEE_NEST);

        // Furnaces
        furnaces.add(Material.FURNACE);
        furnaces.add(Material.BLAST_FURNACE);
        furnaces.add(Material.SMOKER);

        // Containers
        containers.add(Material.CHEST);
        containers.add(Material.TRAPPED_CHEST);
        containers.add(Material.BARREL);
        containers.add(Material.HOPPER);
    }

    public static List<Material> getRedstoneProviders() {
        return redstoneProviders;
    }

    public static List<Material> getRedstoneComponents() {
        return redstoneComponents;
    }

    public static List<Material> getCrops() {
        return crops;
    }

    public static List<Material> getHoneyProducers() {
        return honeyProducers;
    }

    public static List<Material> getFurnaces() {
        return furnaces;
    }

    public static List<Material> getContainers() {
        return containers;
    }

    public static List<Material> getBeds() {
        return beds;
    }
}
