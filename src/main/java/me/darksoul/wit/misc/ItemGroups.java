package me.darksoul.wit.misc;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemGroups {
    private static final List<Material> REDSTONE_PROVIDERS = new ArrayList<>();
    private static final List<Material> REDSTONE_COMPONENTS = new ArrayList<>();
    private static final List<Material> CROPS = new ArrayList<>();
    private static final List<Material> HONEY_PRODUCERS = new ArrayList<>();
    private static final List<Material> FURNACES = new ArrayList<>();
    private static final List<Material> CONTAINERS = new ArrayList<>();
    private static final List<EntityType> PETS = new ArrayList<>();
    private static final List<EntityType> NOT_RENDER_ENTITIES = new ArrayList<>();
    private static final List<Material> OPERATOR_BLOCKS = new ArrayList<>();
    public static final List<ItemStack> HOES = List.of(new ItemStack(Material.WOODEN_HOE), new ItemStack(Material.STONE_HOE), new ItemStack(Material.GOLDEN_HOE),
            new ItemStack(Material.IRON_HOE), new ItemStack(Material.DIAMOND_HOE), new ItemStack(Material.NETHERITE_HOE));
    public static final List<ItemStack> SHOVELS = List.of(new ItemStack(Material.WOODEN_SHOVEL), new ItemStack(Material.STONE_SHOVEL), new ItemStack(Material.GOLDEN_SHOVEL),
            new ItemStack(Material.IRON_SHOVEL), new ItemStack(Material.DIAMOND_SHOVEL), new ItemStack(Material.NETHERITE_SHOVEL));
    public static final List<ItemStack> AXES = List.of(new ItemStack(Material.WOODEN_AXE), new ItemStack(Material.STONE_AXE), new ItemStack(Material.GOLDEN_AXE),
            new ItemStack(Material.IRON_AXE), new ItemStack(Material.DIAMOND_AXE), new ItemStack(Material.NETHERITE_AXE));
    public static final List<ItemStack> PICKAXES = List.of(new ItemStack(Material.WOODEN_PICKAXE), new ItemStack(Material.STONE_PICKAXE), new ItemStack(Material.GOLDEN_PICKAXE),
            new ItemStack(Material.IRON_PICKAXE), new ItemStack(Material.DIAMOND_PICKAXE), new ItemStack(Material.NETHERITE_PICKAXE));

    static {
        // RedStone
        REDSTONE_PROVIDERS.add(Material.OBSERVER);
        REDSTONE_PROVIDERS.add(Material.TARGET);
        REDSTONE_PROVIDERS.add(Material.REPEATER);
        for (Material type : Material.values()) {
            String typeString = type.toString();
            if (typeString.endsWith("_DOOR")
                    || typeString.endsWith("_TRAPDOOR")
                    || typeString.endsWith("_FENCE_GATE")
                    || typeString.endsWith("_PRESSURE_PLATE")
                    || typeString.endsWith("_BUTTON")
                    || typeString.endsWith("_BULB")
                    || type == Material.LEVER) {
                REDSTONE_PROVIDERS.add(type);
            }
            if (typeString.endsWith("_WOOL")) {
                REDSTONE_COMPONENTS.add(type);
            }
        }
        REDSTONE_PROVIDERS.add(Material.BELL);
        REDSTONE_PROVIDERS.add(Material.REDSTONE_LAMP);
        REDSTONE_PROVIDERS.add(Material.ACTIVATOR_RAIL);
        REDSTONE_PROVIDERS.add(Material.DETECTOR_RAIL);
        REDSTONE_PROVIDERS.add(Material.DAYLIGHT_DETECTOR);
        REDSTONE_PROVIDERS.add(Material.POWERED_RAIL);
        REDSTONE_PROVIDERS.add(Material.NOTE_BLOCK);
        REDSTONE_PROVIDERS.add(Material.OBSERVER);
        REDSTONE_PROVIDERS.add(Material.JUKEBOX);
        REDSTONE_PROVIDERS.add(Material.TRAPPED_CHEST);
        REDSTONE_PROVIDERS.add(Material.HOPPER);
        REDSTONE_PROVIDERS.add(Material.DROPPER);
        REDSTONE_PROVIDERS.add(Material.TARGET);
        REDSTONE_PROVIDERS.add(Material.PISTON);
        REDSTONE_PROVIDERS.add(Material.PISTON_HEAD);
        REDSTONE_PROVIDERS.add(Material.STICKY_PISTON);
        REDSTONE_PROVIDERS.add(Material.CRAFTER);
        REDSTONE_PROVIDERS.add(Material.DISPENSER);
        REDSTONE_PROVIDERS.add(Material.TRIPWIRE_HOOK);
        REDSTONE_PROVIDERS.add(Material.SCULK_SHRIEKER);
        REDSTONE_PROVIDERS.add(Material.SCULK_SENSOR);
        REDSTONE_PROVIDERS.add(Material.CALIBRATED_SCULK_SENSOR);
        REDSTONE_PROVIDERS.add(Material.REDSTONE_TORCH);
        REDSTONE_COMPONENTS.add(Material.REDSTONE_WIRE);
        REDSTONE_COMPONENTS.add(Material.COMPARATOR);

        // Crops
        CROPS.add(Material.WHEAT);
        CROPS.add(Material.BEETROOTS);
        CROPS.add(Material.MELON_STEM);
        CROPS.add(Material.PUMPKIN_STEM);
        CROPS.add(Material.POTATOES);
        CROPS.add(Material.CARROTS);
        CROPS.add(Material.COCOA);

        // Honey blocks
        HONEY_PRODUCERS.add(Material.BEEHIVE);
        HONEY_PRODUCERS.add(Material.BEE_NEST);

        // Furnaces
        FURNACES.add(Material.FURNACE);
        FURNACES.add(Material.BLAST_FURNACE);
        FURNACES.add(Material.SMOKER);

        // Containers
        CONTAINERS.add(Material.CHEST);
        CONTAINERS.add(Material.TRAPPED_CHEST);
        CONTAINERS.add(Material.BARREL);
        CONTAINERS.add(Material.HOPPER);
        // Operator Blocks
        OPERATOR_BLOCKS.add(Material.BARRIER);
        OPERATOR_BLOCKS.add(Material.LIGHT);
        OPERATOR_BLOCKS.add(Material.STRUCTURE_BLOCK);
        OPERATOR_BLOCKS.add(Material.STRUCTURE_VOID);

        // Pets
        PETS.add(EntityType.CAT);
        PETS.add(EntityType.WOLF);
        PETS.add(EntityType.PARROT);
        PETS.add(EntityType.HORSE);
        PETS.add(EntityType.MULE);
        PETS.add(EntityType.DONKEY);
        PETS.add(EntityType.LLAMA);

        // Entities to not render for
        NOT_RENDER_ENTITIES.add(EntityType.ITEM);
        NOT_RENDER_ENTITIES.add(EntityType.ITEM);
        NOT_RENDER_ENTITIES.add(EntityType.ITEM_DISPLAY);
        NOT_RENDER_ENTITIES.add(EntityType.BLOCK_DISPLAY);
        NOT_RENDER_ENTITIES.add(EntityType.INTERACTION);
        NOT_RENDER_ENTITIES.add(EntityType.EVOKER_FANGS);
        NOT_RENDER_ENTITIES.add(EntityType.EXPERIENCE_BOTTLE);
        NOT_RENDER_ENTITIES.add(EntityType.EXPERIENCE_ORB);
        NOT_RENDER_ENTITIES.add(EntityType.FALLING_BLOCK);
        NOT_RENDER_ENTITIES.add(EntityType.LIGHTNING_BOLT);
        NOT_RENDER_ENTITIES.add(EntityType.MARKER);
        NOT_RENDER_ENTITIES.add(EntityType.POTION);
        NOT_RENDER_ENTITIES.add(EntityType.FIREBALL);
        NOT_RENDER_ENTITIES.add(EntityType.SMALL_FIREBALL);
        NOT_RENDER_ENTITIES.add(EntityType.SHULKER_BULLET);
        NOT_RENDER_ENTITIES.add(EntityType.FIREWORK_ROCKET);
        NOT_RENDER_ENTITIES.add(EntityType.EYE_OF_ENDER);
        NOT_RENDER_ENTITIES.add(EntityType.SNOWBALL);
        NOT_RENDER_ENTITIES.add(EntityType.ENDER_PEARL);
        NOT_RENDER_ENTITIES.add(EntityType.DRAGON_FIREBALL);
        NOT_RENDER_ENTITIES.add(EntityType.SPECTRAL_ARROW);
        NOT_RENDER_ENTITIES.add(EntityType.ARROW);
        NOT_RENDER_ENTITIES.add(EntityType.TRIDENT);
        NOT_RENDER_ENTITIES.add(EntityType.WITHER);
        NOT_RENDER_ENTITIES.add(EntityType.ENDER_DRAGON);
    }

    public static List<Material> getRedstoneProviders() {
        return REDSTONE_PROVIDERS;
    }
    public static List<Material> getRedstoneComponents() {
        return REDSTONE_COMPONENTS;
    }
    public static List<Material> getCrops() {
        return CROPS;
    }
    public static List<Material> getHoneyProducers() {
        return HONEY_PRODUCERS;
    }
    public static List<Material> getFurnaces() {
        return FURNACES;
    }
    public static List<Material> getContainers() {
        return CONTAINERS;
    }
    public static List<Material> getOperatorBlocks() {
        return OPERATOR_BLOCKS;
    }

    public static List<EntityType> getPets() {
        return PETS;
    }
    public static List<EntityType> getNotRenderEntities() {
        return NOT_RENDER_ENTITIES;
    }
}
