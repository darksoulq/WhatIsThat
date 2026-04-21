package com.github.darksoulq.wit;

import io.papermc.paper.datacomponent.DataComponentTypes;
import com.github.darksoulq.wit.misc.ConfigUtils;
import com.github.darksoulq.wit.misc.ItemGroups;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.object.ObjectContents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WebBlock;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.spawner.Spawner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class Information {
    private static YamlConfiguration valuesFile;
    public static final MiniMessage mm = MiniMessage.miniMessage();
    private static final List<Function<ItemStack, ToolTier>> TIER_GETTERS = new ArrayList<>();
    private static final List<Function<Block, ToolTier>> BLOCK_TIER_GETTERS = new ArrayList<>();
    private static final List<ToolTier> TIERS = new ArrayList<>();
    private static final Map<Material, ToolTier> VANILLA_TIER_CACHE = new EnumMap<>(Material.class);

    public static final ToolTier WOODEN_AXE = new ToolTier(new NamespacedKey("default", "wooden_axe"), ItemStack.of(Material.WOODEN_AXE), 0) {};
    public static final ToolTier WOODEN_PICKAXE = new ToolTier(new NamespacedKey("default", "wooden_pickaxe"), ItemStack.of(Material.WOODEN_PICKAXE), 0) {};
    public static final ToolTier WOODEN_SHOVEL = new ToolTier(new NamespacedKey("default", "wooden_shovel"), ItemStack.of(Material.WOODEN_SHOVEL), 0) {};
    public static final ToolTier WOODEN_HOE = new ToolTier(new NamespacedKey("default", "wooden_hoe"), ItemStack.of(Material.WOODEN_HOE), 0) {};
    public static final ToolTier WOODEN_SWORD = new ToolTier(new NamespacedKey("default", "wooden_sword"), ItemStack.of(Material.WOODEN_SWORD), 0) {};

    public static final ToolTier STONE_AXE = new ToolTier(new NamespacedKey("default", "stone_axe"), ItemStack.of(Material.STONE_AXE), 1) {};
    public static final ToolTier STONE_PICKAXE = new ToolTier(new NamespacedKey("default", "stone_pickaxe"), ItemStack.of(Material.STONE_PICKAXE), 1) {};
    public static final ToolTier STONE_SHOVEL = new ToolTier(new NamespacedKey("default", "stone_shovel"), ItemStack.of(Material.STONE_SHOVEL), 1) {};
    public static final ToolTier STONE_HOE = new ToolTier(new NamespacedKey("default", "stone_hoe"), ItemStack.of(Material.STONE_HOE), 1) {};
    public static final ToolTier STONE_SWORD = new ToolTier(new NamespacedKey("default", "stone_sword"), ItemStack.of(Material.STONE_SWORD), 1) {};

    public static final ToolTier COPPER_AXE = new ToolTier(new NamespacedKey("default", "copper_axe"), ItemStack.of(Material.COPPER_AXE), 2) {};
    public static final ToolTier COPPER_PICKAXE = new ToolTier(new NamespacedKey("default", "copper_pickaxe"), ItemStack.of(Material.COPPER_PICKAXE), 2) {};
    public static final ToolTier COPPER_SHOVEL = new ToolTier(new NamespacedKey("default", "copper_shovel"), ItemStack.of(Material.COPPER_SHOVEL), 2) {};
    public static final ToolTier COPPER_HOE = new ToolTier(new NamespacedKey("default", "copper_hoe"), ItemStack.of(Material.COPPER_HOE), 2) {};
    public static final ToolTier COPPER_SWORD = new ToolTier(new NamespacedKey("default", "copper_sword"), ItemStack.of(Material.COPPER_SWORD), 2) {};

    public static final ToolTier GOLD_AXE = new ToolTier(new NamespacedKey("default", "gold_axe"), ItemStack.of(Material.GOLDEN_AXE), 3) {};
    public static final ToolTier GOLD_PICKAXE = new ToolTier(new NamespacedKey("default", "gold_pickaxe"), ItemStack.of(Material.GOLDEN_PICKAXE), 3) {};
    public static final ToolTier GOLD_SHOVEL = new ToolTier(new NamespacedKey("default", "gold_shovel"), ItemStack.of(Material.GOLDEN_SHOVEL), 3) {};
    public static final ToolTier GOLD_HOE = new ToolTier(new NamespacedKey("default", "gold_hoe"), ItemStack.of(Material.GOLDEN_HOE), 3) {};
    public static final ToolTier GOLD_SWORD = new ToolTier(new NamespacedKey("default", "gold_sword"), ItemStack.of(Material.GOLDEN_SWORD), 3) {};

    public static final ToolTier IRON_AXE = new ToolTier(new NamespacedKey("default", "iron_axe"), ItemStack.of(Material.IRON_AXE), 4) {};
    public static final ToolTier IRON_PICKAXE = new ToolTier(new NamespacedKey("default", "iron_pickaxe"), ItemStack.of(Material.IRON_PICKAXE), 4) {};
    public static final ToolTier IRON_SHOVEL = new ToolTier(new NamespacedKey("default", "iron_shovel"), ItemStack.of(Material.IRON_SHOVEL), 4) {};
    public static final ToolTier IRON_HOE = new ToolTier(new NamespacedKey("default", "iron_hoe"), ItemStack.of(Material.IRON_HOE), 4) {};
    public static final ToolTier IRON_SWORD = new ToolTier(new NamespacedKey("default", "iron_sword"), ItemStack.of(Material.IRON_SWORD), 4) {};

    public static final ToolTier DIAMOND_AXE = new ToolTier(new NamespacedKey("default", "diamond_axe"), ItemStack.of(Material.DIAMOND_AXE), 5) {};
    public static final ToolTier DIAMOND_PICKAXE = new ToolTier(new NamespacedKey("default", "diamond_pickaxe"), ItemStack.of(Material.DIAMOND_PICKAXE), 5) {};
    public static final ToolTier DIAMOND_SHOVEL = new ToolTier(new NamespacedKey("default", "diamond_shovel"), ItemStack.of(Material.DIAMOND_SHOVEL), 5) {};
    public static final ToolTier DIAMOND_HOE = new ToolTier(new NamespacedKey("default", "diamond_hoe"), ItemStack.of(Material.DIAMOND_HOE), 5) {};
    public static final ToolTier DIAMOND_SWORD = new ToolTier(new NamespacedKey("default", "diamond_sword"), ItemStack.of(Material.DIAMOND_SWORD), 45) {};

    public static final ToolTier SHEARS = new ToolTier(new NamespacedKey("default", "shears"), ItemStack.of(Material.SHEARS), 0) {};

    private static String STR_RS_ON, STR_RS_OFF, STR_RS_LVL;
    private static String STR_CROP_AGE_1, STR_CROP_AGE_2, STR_CROP_AGE_3, STR_CROP_AGE_4;
    private static String STR_HONEY_LVL, STR_SMELT_TIME, STR_CONTAINER, STR_BEACON_1, STR_BEACON_2;
    private static String STR_SPAWNER, STR_NOTEBLOCK, STR_FARM_HYD, STR_FARM_DRY;
    private static String STR_AGE, STR_OWNER, STR_LEASH, STR_HEALTH, STR_PROFESSION, STR_TNT, STR_HORSE_SPD, STR_HORSE_JMP;
    private static String CLR_25, CLR_50, CLR_75, CLR_100, CLR_DEF;
    private static String CLR_E_25, CLR_E_50, CLR_E_75, CLR_E_100, CLR_E_DEF;

    private static final Map<Profession, String> PROFESSIONS = new HashMap<>();

    static {
        reloadValuesFile();
    }

    public static final Function<ItemStack, ToolTier> VANILLA_TIER_GETTER = item -> {
        Material mat = item.getType();

        if (mat == Material.SHEARS) return SHEARS;

        ToolTier cached = VANILLA_TIER_CACHE.get(mat);
        if (cached != null) return cached;

        String name = mat.name();
        String material = "";
        String type = "";

        if (name.startsWith("WOODEN_")) material = "wooden";
        else if (name.startsWith("STONE_")) material = "stone";
        else if (name.startsWith("IRON_")) material = "iron";
        else if (name.startsWith("GOLDEN_")) material = "gold";
        else if (name.startsWith("DIAMOND_")) material = "diamond";
        else if (name.startsWith("NETHERITE_")) material = "netherite";

        if (name.endsWith("_SWORD")) type = "sword";
        else if (name.endsWith("_PICKAXE")) type = "pickaxe";
        else if (name.endsWith("_AXE")) type = "axe";
        else if (name.endsWith("_SHOVEL")) type = "shovel";
        else if (name.endsWith("_HOE")) type = "hoe";

        if (!type.isEmpty() && !material.isEmpty()) {
            NamespacedKey key = new NamespacedKey("default", material + "_" + type);
            for (ToolTier tier : TIERS) {
                if (tier.id.equals(key)) {
                    VANILLA_TIER_CACHE.put(mat, tier);
                    return tier;
                }
            }
        }
        return null;
    };

    public static final Function<Block, ToolTier> VANILLA_BLOCK_TIER_GETTER = block -> {
        net.minecraft.world.level.block.state.BlockState nmsState = ((CraftBlockState) block.getState()).getHandle();
        return getToolTier(nmsState);
    };

    private static @Nullable ToolTier getToolTier(net.minecraft.world.level.block.state.BlockState state) {
        if (state.is(BlockTags.LEAVES) || state.is(BlockTags.WOOL)) {
            return SHEARS;
        } else if (state.is(Blocks.COBWEB)) {
            return WOODEN_SWORD;
        }

        ToolTier baseTier = null;
        String preferredType = null;

        if (state.is(BlockTags.MINEABLE_WITH_AXE)) preferredType = "axe";
        else if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) preferredType = "pickaxe";
        else if (state.is(BlockTags.MINEABLE_WITH_SHOVEL)) preferredType = "shovel";
        else if (state.is(BlockTags.MINEABLE_WITH_HOE)) preferredType = "hoe";
        else if (state.is(BlockTags.SWORD_EFFICIENT) || state.is(BlockTags.SWORD_INSTANTLY_MINES)) preferredType = "sword";

        if (state.is(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)) baseTier = getTierByType(preferredType, "diamond");
        else if (state.is(BlockTags.INCORRECT_FOR_IRON_TOOL)) baseTier = getTierByType(preferredType, "diamond");
        else if (state.is(BlockTags.INCORRECT_FOR_STONE_TOOL)) baseTier = getTierByType(preferredType, "iron");
        else if (state.is(BlockTags.INCORRECT_FOR_COPPER_TOOL)) baseTier = getTierByType(preferredType, "stone");
        else if (state.is(BlockTags.INCORRECT_FOR_GOLD_TOOL)) baseTier = getTierByType(preferredType, "copper");
        else if (state.is(BlockTags.INCORRECT_FOR_WOODEN_TOOL)) baseTier = getTierByType(preferredType, "gold");

        if (state.is(BlockTags.NEEDS_DIAMOND_TOOL)) baseTier = getTierByType(preferredType, "diamond");
        else if (state.is(BlockTags.NEEDS_IRON_TOOL)) baseTier = getTierByType(preferredType, "iron");
        else if (state.is(BlockTags.NEEDS_STONE_TOOL)) baseTier = getTierByType(preferredType, "stone");

        if (baseTier == null && preferredType != null) {
            baseTier = getTierByType(preferredType, "wooden");
        }
        return baseTier;
    }

    public static abstract class ToolTier {
        final NamespacedKey id;
        final ItemStack stack;
        final int weight;

        public ToolTier(NamespacedKey id, ItemStack stack, int weight) {
            this.id = id;
            this.stack = stack;
            this.weight = weight;
        }

        public boolean isTier(ItemStack stack) {
            return this.stack.isSimilar(stack);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ToolTier toolTier)) return false;
            return id.equals(toolTier.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }

    public static class StartTierRegistrationEvent extends Event {
        public static final HandlerList HANDLERS = new HandlerList();

        @Override
        public @NotNull HandlerList getHandlers() {
            return HANDLERS;
        }

        public static HandlerList getHandlerList() {
            return HANDLERS;
        }
    }

    public static Component defaultGetRedstoneInfo(Block block) {
        if (block.getType() == Material.REDSTONE_BLOCK) {
            return mm.deserialize(STR_RS_ON);
        }

        BlockData data = block.getBlockData();
        int power = 0;
        boolean powered = false;

        if (data instanceof AnaloguePowerable a) {
            power = a.getPower();
        } else if (data instanceof Powerable p) {
            powered = p.isPowered();
        } else {
            power = block.getBlockPower();
            powered = block.isBlockPowered();
            if (power == 0 && !powered) return Component.empty();
        }

        if (power > 0) {
            return mm.deserialize(STR_RS_LVL, Placeholder.unparsed("power", String.valueOf(power)));
        }

        return mm.deserialize(powered ? STR_RS_ON : STR_RS_OFF);
    }

    public static Component defaultGetCropAge(Block block) {
        BlockData data = block.getBlockData();
        if (!(data instanceof Ageable ageable)) return Component.empty();

        int age = ageable.getAge();
        int maxAge = ageable.getMaximumAge();
        int percent = (int) ((age / (double) maxAge) * 100);
        int stage = Math.min(3, percent / 25) + 1;

        String template = switch (stage) {
            case 1 -> STR_CROP_AGE_1;
            case 2 -> STR_CROP_AGE_2;
            case 3 -> STR_CROP_AGE_3;
            default -> STR_CROP_AGE_4;
        };

        return mm.deserialize(template,
            Placeholder.parsed("color_by_percent", getColorForPercent(percent)),
            Placeholder.parsed("color_by_percent_end", getColorForPercentEnd(percent)),
            Placeholder.unparsed("age", String.valueOf(age)),
            Placeholder.unparsed("max_age", String.valueOf(maxAge))
        );
    }

    public static Component defaultGetHoneyLevel(Block block) {
        BlockData data = block.getBlockData();
        if (!(data instanceof Beehive hive)) return Component.empty();

        int honey = hive.getHoneyLevel();
        int max = hive.getMaximumHoneyLevel();
        float percent = (float) (honey / (double) max * 100);

        return mm.deserialize(STR_HONEY_LVL,
            Placeholder.component("honey_icon", getSprite(Atlases.ITEMS.key, "item/honeycomb")),
            Placeholder.parsed("color_by_percent", getColorForPercent(percent)),
            Placeholder.parsed("color_by_percent_end", getColorForPercentEnd(percent)),
            Placeholder.unparsed("honey_level", String.valueOf(honey)),
            Placeholder.unparsed("honey_max_level", String.valueOf(max))
        );
    }

    public static Component defaultGetRemainingSmeltTime(Block block) {
        BlockState state = block.getState();
        if (!(state instanceof Furnace furnace)) return Component.empty();

        FurnaceInventory inv = furnace.getInventory();
        if (inv.getSmelting() == null || furnace.getCookTime() == 0) return Component.empty();

        int cookTime = furnace.getCookTime();
        int cookTotal = furnace.getCookTimeTotal();
        int secondsRemaining = (cookTotal - cookTime) / 20;
        float percent = cookTime / (float) cookTotal * 100;

        return mm.deserialize(STR_SMELT_TIME,
            Placeholder.parsed("color_by_percent", getColorForPercent(percent)),
            Placeholder.parsed("color_by_percent_end", getColorForPercentEnd(percent)),
            Placeholder.unparsed("seconds_remaining", String.valueOf(secondsRemaining))
        );
    }

    public static Component defaultGetTotalItemsInContainer(Block block) {
        if (!ItemGroups.getContainers().contains(block.getType())) {
            return Component.empty();
        }

        BlockState state = block.getState();
        if (!(state instanceof InventoryHolder holder)) {
            return Component.empty();
        }

        int totalItems = 0;
        for (ItemStack item : holder.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                totalItems += item.getAmount();
            }
        }

        return mm.deserialize(STR_CONTAINER, Placeholder.parsed("items", String.valueOf(totalItems)));
    }

    public static Component defaultGetBeaconEffect(Block block) {
        if (block.getType() == Material.BEACON) {
            Beacon state = (Beacon) block.getState();
            PotionEffect primaryEffect = state.getPrimaryEffect();
            PotionEffect secondaryEffect = state.getSecondaryEffect();

            if (primaryEffect != null) {
                if (secondaryEffect != null) {
                    return mm.deserialize(STR_BEACON_2,
                        Placeholder.component("effect_icon_1", getEffectSprite(primaryEffect.getType().key())),
                        Placeholder.unparsed("primary_amplifier", String.valueOf(primaryEffect.getAmplifier() + 1)),
                        Placeholder.component("effect_icon_2", getEffectSprite(secondaryEffect.getType().key())),
                        Placeholder.unparsed("secondary_amplifier", String.valueOf(secondaryEffect.getAmplifier() + 1))
                    );
                }
                return mm.deserialize(STR_BEACON_1,
                    Placeholder.component("effect_icon", getEffectSprite(primaryEffect.getType().key())),
                    Placeholder.unparsed("primary_amplifier", String.valueOf(primaryEffect.getAmplifier() + 1))
                );
            }
        }
        return Component.empty();
    }

    public static Component defaultGetSpawnerInfo(Block block) {
        if (block.getType() == Material.SPAWNER) {
            Spawner state = (Spawner) block.getState();
            EntityType type = state.getSpawnedType();
            if (type != null) {
                return mm.deserialize(STR_SPAWNER,
                    Placeholder.component("egg_icon", getSprite(Atlases.ITEMS.key, "item/egg")),
                    Placeholder.component("spawning_entity", Component.translatable(type.translationKey()))
                );
            }
        }
        return Component.empty();
    }

    public static Component defaultGetNoteblockInfo(Block block) {
        if (block.getType() == Material.NOTE_BLOCK) {
            NoteBlock data = (NoteBlock) block.getBlockData();
            return mm.deserialize(STR_NOTEBLOCK,
                Placeholder.unparsed("instrument", ConfigUtils.toProperCase(data.getInstrument().name())),
                Placeholder.unparsed("tone", ConfigUtils.toProperCase(data.getNote().getTone().name())),
                Placeholder.unparsed("octave", String.valueOf(data.getNote().getOctave()))
            );
        }
        return Component.empty();
    }

    public static Component defaultGetFarmlandHydration(Block block) {
        if (block.getType() == Material.FARMLAND) {
            Farmland data = (Farmland) block.getBlockData();
            return mm.deserialize(data.getMoisture() > 0 ? STR_FARM_HYD : STR_FARM_DRY);
        }
        return Component.empty();
    }

    public static Component defaultGetToolToBreak(Block block, Player player) {
        ToolTier reqTier = getRequiredTier(block);
        if (reqTier == null) return Component.empty();

        Key baseKey = reqTier.stack.getDataOrDefault(DataComponentTypes.ITEM_MODEL,
            new NamespacedKey("minecraft", reqTier.stack.getType().name().toLowerCase(Locale.ROOT)));

        return getSprite(Atlases.ITEMS.key, new NamespacedKey(baseKey.namespace(), "item/" + baseKey.value())).append(Component.space());
    }

    public static Component defaultGetEntityAgeLeft(Entity entity) {
        if (entity instanceof org.bukkit.entity.Ageable data) {
            int age = data.getAge();
            if (age < 0) {
                return mm.deserialize(STR_AGE,
                    Placeholder.component("entity_egg", getSprite(Atlases.ITEMS.key, "item/egg")),
                    Placeholder.unparsed("seconds_left", String.valueOf(Math.abs(age) / 20)));
            }
        }
        return Component.empty();
    }

    public static Component defaultGetEntityOwner(Entity entity) {
        if (!(entity instanceof Tameable tameable) || !tameable.isTamed()) return Component.empty();
        AnimalTamer owner = tameable.getOwner();
        if (owner == null) return Component.empty();

        return mm.deserialize(STR_OWNER, Placeholder.unparsed("entity_owner", owner.getName()));
    }

    public static Component defaultGetIsLeashed(Entity entity) {
        if (entity instanceof LivingEntity lEntity && lEntity.isLeashed()) {
            return mm.deserialize(STR_LEASH, Placeholder.component("leash_icon", getSprite(Atlases.ITEMS.key, "item/lead")));
        }
        return Component.empty();
    }

    public static Component defaultGetEntityHealth(Entity entity) {
        if (entity instanceof LivingEntity data) {
            AttributeInstance maxAttr = data.getAttribute(Attribute.MAX_HEALTH);
            if (maxAttr != null) {
                return mm.deserialize(STR_HEALTH,
                    Placeholder.component("heart_icon", getSprite(Atlases.GUI.key, "hud/heart/full")),
                    Placeholder.unparsed("entity_health", String.valueOf((int) data.getHealth())),
                    Placeholder.unparsed("entity_max_health", String.valueOf((int) maxAttr.getValue()))
                );
            }
        }
        return Component.empty();
    }

    public static Component defaultGetVillagerProfession(Entity entity) {
        if (entity instanceof Villager villager) {
            return mm.deserialize(STR_PROFESSION, Placeholder.unparsed("profession", PROFESSIONS.getOrDefault(villager.getProfession(), "")));
        }
        return Component.empty();
    }

    public static Component defaultGetTNTFuseTime(Entity entity) {
        if (entity instanceof TNTPrimed tnt) {
            return mm.deserialize(STR_TNT, Placeholder.unparsed("fuse_time", String.valueOf(tnt.getFuseTicks() / 20.0f)));
        }
        return Component.empty();
    }

    public static Component defaultGetHorseSpeed(Entity entity) {
        if (!(entity instanceof AbstractHorse horse)) return Component.empty();
        AttributeInstance speed = horse.getAttribute(Attribute.MOVEMENT_SPEED);
        if (speed == null) return Component.empty();
        return mm.deserialize(STR_HORSE_SPD,
            Placeholder.component("speed_icon", getEffectSprite(PotionEffectType.SPEED.key())),
            Placeholder.unparsed("horse_speed", String.format("%.3f", speed.getBaseValue() * 43.17))
        );
    }

    public static Component defaultGetHorseJumpStrength(Entity entity) {
        if (!(entity instanceof AbstractHorse horse)) return Component.empty();
        AttributeInstance jumpStrength = horse.getAttribute(Attribute.JUMP_STRENGTH);
        if (jumpStrength == null) return Component.empty();

        double str = jumpStrength.getBaseValue();
        double height = 4.53680079 * str * str + 1.61431730 * str - 0.22656224;

        return mm.deserialize(STR_HORSE_JMP,
            Placeholder.component("jump_icon", getEffectSprite(PotionEffectType.JUMP_BOOST.key())),
            Placeholder.unparsed("horse_jump_strength", String.format("%.3f", height))
        );
    }

    public static String getColorForPercent(float percent) {
        if (percent >= 0 && percent <= 25) return CLR_25;
        if (percent > 25 && percent <= 50) return CLR_50;
        if (percent > 50 && percent <= 75) return CLR_75;
        if (percent > 75) return CLR_100;
        return CLR_DEF;
    }

    public static String getColorForPercentEnd(float percent) {
        if (percent >= 0 && percent <= 25) return CLR_E_25;
        if (percent > 25 && percent <= 50) return CLR_E_50;
        if (percent > 50 && percent <= 75) return CLR_E_75;
        if (percent > 75) return CLR_E_100;
        return CLR_E_DEF;
    }

    public static Component getSprite(Key atlas, Key value) {
        return Component.object(ObjectContents.sprite(atlas, value));
    }

    public static Component getSprite(Key atlas, String value) {
        return getSprite(atlas, Key.key("minecraft", value));
    }

    public static Component getEffectSprite(Key effect) {
        return getSprite(Atlases.GUI.key, Key.key("minecraft", "mob_effect/" + effect.value()));
    }

    public static ToolTier getTier(ItemStack stack) {
        for (Function<ItemStack, ToolTier> getter : TIER_GETTERS) {
            ToolTier tier = getter.apply(stack);
            if (tier != null) return tier;
        }
        return null;
    }

    public static ToolTier getRequiredTier(Block block) {
        for (Function<Block, ToolTier> getter : BLOCK_TIER_GETTERS) {
            ToolTier tier = getter.apply(block);
            if (tier != null) return tier;
        }
        return null;
    }

    private static ToolTier getTierByType(String type, String material) {
        if (type == null || material == null) return null;
        String match = material + "_" + type;
        for (ToolTier tier : TIERS) {
            if (tier.id.getKey().equals(match)) return tier;
        }
        return null;
    }

    public static void addRequiredTierGetter(Function<Block, ToolTier> getter) {
        BLOCK_TIER_GETTERS.add(getter);
    }

    public static void addTierGetter(Function<ItemStack, ToolTier> getter) {
        TIER_GETTERS.add(getter);
    }

    public static void addTier(ToolTier tier) {
        TIERS.add(tier);
    }

    public static void reloadValuesFile() {
        valuesFile = ConfigUtils.loadValuesFIle();

        STR_RS_ON = valuesFile.getString("minecraft.redstone_power_on", " <red>●</red>");
        STR_RS_OFF = valuesFile.getString("minecraft.redstone_power_off", " <gray>●</gray>");
        STR_RS_LVL = valuesFile.getString("minecraft.redstone_power_level", " <red>●</red> <power>");

        STR_CROP_AGE_1 = valuesFile.getString("minecraft.crop_age_1", " <color_by_percent>\uD83C\uDF31 <age>/<max_age><color_by_percent_end>");
        STR_CROP_AGE_2 = valuesFile.getString("minecraft.crop_age_2", " <color_by_percent>\uD83C\uDF31 <age>/<max_age><color_by_percent_end>");
        STR_CROP_AGE_3 = valuesFile.getString("minecraft.crop_age_3", " <color_by_percent>\uD83C\uDF31 <age>/<max_age><color_by_percent_end>");
        STR_CROP_AGE_4 = valuesFile.getString("minecraft.crop_age_4", " <color_by_percent>\uD83C\uDF31 <age>/<max_age><color_by_percent_end>");

        STR_HONEY_LVL = valuesFile.getString("minecraft.honey_level", " <honey_icon> <color_by_percent><honey_level>/<honey_max_level><color_by_percent_end>");
        STR_SMELT_TIME = valuesFile.getString("minecraft.smelt_time", " <color_by_percent>⌛ <seconds_remaining>s<color_by_percent_end>");
        STR_CONTAINER = valuesFile.getString("minecraft.items_in_container", "<gold>📦 <items></gold> ");
        STR_BEACON_1 = valuesFile.getString("minecraft.beacon_1_effect", " <effect_icon> <primary_amplifier>");
        STR_BEACON_2 = valuesFile.getString("minecraft.beacon_2_effect", " <effect_icon_1> <primary_amplifier> <effect_icon_2> <secondary_amplifier>");
        STR_SPAWNER = valuesFile.getString("minecraft.spawner_info", " <egg_icon> <green><spawning_entity></green>");
        STR_NOTEBLOCK = valuesFile.getString("minecraft.noteblock_info", " <gold>\uD83C\uDFB9 <instrument>: <tone> <octave></gold>");
        STR_FARM_HYD = valuesFile.getString("minecraft.farmland_hydrated", " <blue>\uD83D\uDCA7</blue>");
        STR_FARM_DRY = valuesFile.getString("minecraft.farmland_not_hydrated", " <gray>\uD83D\uDCA7</gray>");

        STR_AGE = valuesFile.getString("minecraft.entity_time_to_grow", " <entity_egg> <seconds_left>s");
        STR_OWNER = valuesFile.getString("minecraft.entity_owner", "<gray><entity_owner></gray> ");
        STR_LEASH = valuesFile.getString("minecraft.entity_is_leashed", "<leash_icon> ");
        STR_HEALTH = valuesFile.getString("minecraft.entity_health", " <heart_icon> <red><entity_health>/<entity_max_health></red>");
        STR_PROFESSION = valuesFile.getString("minecraft.villager_profession", " <gray><profession></gray>");
        STR_TNT = valuesFile.getString("minecraft.tnt_fuse_time", " <red>\\uD83D\\uDCA3<fuse_time></red>");
        STR_HORSE_SPD = valuesFile.getString("minecraft.horse_speed", " <speed_icon> <horse_speed>");
        STR_HORSE_JMP = valuesFile.getString("minecraft.horse_jump_strength", " <jump_icon> <horse_jump_strength>");

        CLR_25 = valuesFile.getString("percent_color.25", "<red>");
        CLR_50 = valuesFile.getString("percent_color.50", "<yellow>");
        CLR_75 = valuesFile.getString("percent_color.75", "<15ed15>");
        CLR_100 = valuesFile.getString("percent_color.100", "<2b662b>");
        CLR_DEF = valuesFile.getString("percent_color.default", "<gray>");

        CLR_E_25 = valuesFile.getString("percent_color.25", "</red>");
        CLR_E_50 = valuesFile.getString("percent_color.50", "</yellow>");
        CLR_E_75 = valuesFile.getString("percent_color.75", "</15ed15>");
        CLR_E_100 = valuesFile.getString("percent_color.100", "</2b662b>");
        CLR_E_DEF = valuesFile.getString("percent_color.default", "</gray>");

        PROFESSIONS.put(Profession.ARMORER, valuesFile.getString("profession.ARMORER", "Armorer"));
        PROFESSIONS.put(Profession.BUTCHER, valuesFile.getString("profession.BUTCHER", "Butcher"));
        PROFESSIONS.put(Profession.CARTOGRAPHER, valuesFile.getString("profession.CARTOGRAPHER", "Cartographer"));
        PROFESSIONS.put(Profession.CLERIC, valuesFile.getString("profession.CLERIC", "Cleric"));
        PROFESSIONS.put(Profession.FARMER, valuesFile.getString("profession.FARMER", "Farmer"));
        PROFESSIONS.put(Profession.FISHERMAN, valuesFile.getString("profession.FISHERMAN", "Fisherman"));
        PROFESSIONS.put(Profession.FLETCHER, valuesFile.getString("profession.FLETCHER", "Fletcher"));
        PROFESSIONS.put(Profession.LEATHERWORKER, valuesFile.getString("profession.LEATHERWORKER", "Leather Worker"));
        PROFESSIONS.put(Profession.LIBRARIAN, valuesFile.getString("profession.LIBRARIAN", "Librarian"));
        PROFESSIONS.put(Profession.MASON, valuesFile.getString("profession.MASON", "Mason"));
        PROFESSIONS.put(Profession.NITWIT, valuesFile.getString("profession.NITWIT", "Nitwit"));
        PROFESSIONS.put(Profession.SHEPHERD, valuesFile.getString("profession.SHEPHERD", "Shepherd"));
        PROFESSIONS.put(Profession.WEAPONSMITH, valuesFile.getString("profession.WEAPONSMITH", "Weaponsmith"));
        PROFESSIONS.put(Profession.NONE, valuesFile.getString("profession.JOBLESS", "Jobless"));
    }

    public static YamlConfiguration getValuesFile() {
        return valuesFile;
    }

    public enum Atlases {
        ITEMS(Key.key("minecraft", "items")),
        BLOCKS(Key.key("minecraft", "blocks")),
        GUI(Key.key("minecraft", "gui"));

        public final Key key;
        Atlases(Key key) {
            this.key = key;
        }
    }
}