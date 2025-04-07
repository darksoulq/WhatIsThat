package me.darksoul.wit.misc;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class ItemGroups {
    private static final List<Material> redstoneProviders = new ArrayList<>();
    private static final List<Material> redstoneComponents = new ArrayList<>();
    private static final List<Material> crops = new ArrayList<>();
    private static final List<Material> honeyProducers = new ArrayList<>();
    private static final List<Material> furnaces = new ArrayList<>();
    private static final List<Material> containers = new ArrayList<>();
    private static final List<EntityType> pets = new ArrayList<>();
    private static final List<EntityType> notRenderEntities = new ArrayList<>();
    private static final List<Material> operatorBlocks = new ArrayList<>();

    static {
        // RedStone
        redstoneProviders.add(Material.OBSERVER);
        redstoneProviders.add(Material.TARGET);
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
        // Operator Blocks
        operatorBlocks.add(Material.BARRIER);
        operatorBlocks.add(Material.LIGHT);
        operatorBlocks.add(Material.STRUCTURE_BLOCK);
        operatorBlocks.add(Material.STRUCTURE_VOID);

        // Pets
        pets.add(EntityType.CAT);
        pets.add(EntityType.WOLF);
        pets.add(EntityType.PARROT);
        pets.add(EntityType.HORSE);
        pets.add(EntityType.MULE);
        pets.add(EntityType.DONKEY);
        pets.add(EntityType.LLAMA);

        // Entities to not render for
        notRenderEntities.add(EntityType.ITEM);
        notRenderEntities.add(EntityType.ITEM);
        notRenderEntities.add(EntityType.ITEM_DISPLAY);
        notRenderEntities.add(EntityType.BLOCK_DISPLAY);
        notRenderEntities.add(EntityType.INTERACTION);
        notRenderEntities.add(EntityType.EVOKER_FANGS);
        notRenderEntities.add(EntityType.EXPERIENCE_BOTTLE);
        notRenderEntities.add(EntityType.EXPERIENCE_ORB);
        notRenderEntities.add(EntityType.FALLING_BLOCK);
        notRenderEntities.add(EntityType.LIGHTNING_BOLT);
        notRenderEntities.add(EntityType.MARKER);
        notRenderEntities.add(EntityType.POTION);
        notRenderEntities.add(EntityType.FIREBALL);
        notRenderEntities.add(EntityType.SMALL_FIREBALL);
        notRenderEntities.add(EntityType.SHULKER_BULLET);
        notRenderEntities.add(EntityType.FIREWORK_ROCKET);
        notRenderEntities.add(EntityType.EYE_OF_ENDER);
        notRenderEntities.add(EntityType.SNOWBALL);
        notRenderEntities.add(EntityType.ENDER_PEARL);
        notRenderEntities.add(EntityType.DRAGON_FIREBALL);
        notRenderEntities.add(EntityType.SPECTRAL_ARROW);
        notRenderEntities.add(EntityType.ARROW);
        notRenderEntities.add(EntityType.TRIDENT);
        notRenderEntities.add(EntityType.WITHER);
        notRenderEntities.add(EntityType.ENDER_DRAGON);
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
    public static List<Material> getOperatorBlocks() {
        return operatorBlocks;
    }

    public static List<EntityType> getPets() {
        return pets;
    }
    public static List<EntityType> getNotRenderEntities() {
        return notRenderEntities;
    }
}
