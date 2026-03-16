package com.github.darksoulq.wit;

import com.mojang.datafixers.util.Either;
import io.papermc.paper.datacomponent.DataComponentTypes;
import com.github.darksoulq.wit.misc.ConfigUtils;
import com.github.darksoulq.wit.misc.ItemGroups;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.object.ObjectContents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import org.bukkit.*;
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
import org.bukkit.entity.*;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.spawner.Spawner;
import org.checkerframework.checker.units.qual.K;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class Information {
    private static YamlConfiguration valuesFile = ConfigUtils.loadValuesFIle();
    public static MiniMessage mm = MiniMessage.miniMessage();
    private static final List<Function<ItemStack, ToolTier>> TIER_GETTERS = new ArrayList<>();
    private static final List<Function<Block, ToolTier>> BLOCK_TIER_GETTERS = new ArrayList<>();
    private static final List<ToolTier> TIERS = new ArrayList<>();

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

    public static final Function<ItemStack, ToolTier> VANILLA_TIER_GETTER = item -> {
        String type = "";
        String material = "";

        Material mat = item.getType();
        String name = mat.name().toLowerCase();

        if (name.startsWith("wooden_")) material = "wooden";
        else if (name.startsWith("stone_")) material = "stone";
        else if (name.startsWith("iron_")) material = "iron";
        else if (name.startsWith("golden_")) material = "gold";
        else if (name.startsWith("diamond_")) material = "diamond";
        else if (name.startsWith("netherite_")) material = "netherite";

        if (name.endsWith("_sword")) type = "sword";
        else if (name.endsWith("_pickaxe")) type = "pickaxe";
        else if (name.endsWith("_axe")) type = "axe";
        else if (name.endsWith("_shovel")) type = "shovel";
        else if (name.endsWith("_hoe")) type = "hoe";

        if (!type.isEmpty() && !material.isEmpty()) {
            NamespacedKey key = new NamespacedKey("default", material + "_" + type);
            for (ToolTier tier : TIERS) {
                if (tier.id.equals(key)) {
                    return tier;
                }
            }
        }

        return null;
    };
    public static final Function<Block, ToolTier> VANILLA_BLOCK_TIER_GETTER = block -> {

        Either<ResourceKey<net.minecraft.world.level.block.Block>, net.minecraft.world.level.block.Block> either =
                ((CraftBlockState) block.getState()).getHandle().getBlockHolder().unwrap();

        net.minecraft.world.level.block.Block nmsBlock = null;
        Optional<ResourceKey<net.minecraft.world.level.block.Block>> nmsKey = either.left();
        Optional<net.minecraft.world.level.block.Block> nmsOptionalBlock = either.right();

        if (nmsKey.isPresent()) {
            nmsBlock = BuiltInRegistries.BLOCK.getValue(nmsKey.get());
        } else if (nmsOptionalBlock.isPresent()) {
            nmsBlock = nmsOptionalBlock.get();
        }

        if (nmsBlock == null) return null;

        List<TagKey<net.minecraft.world.level.block.Block>> tags = nmsBlock.defaultBlockState().getTags().toList();

        return getToolTier(tags);
    };
    private static @Nullable ToolTier getToolTier(List<TagKey<net.minecraft.world.level.block.Block>> tags) {
        ToolTier baseTier = null;
        String preferredType = null;

        if (tags.contains(BlockTags.MINEABLE_WITH_AXE)) preferredType = "axe";
        else if (tags.contains(BlockTags.MINEABLE_WITH_PICKAXE)) preferredType = "pickaxe";
        else if (tags.contains(BlockTags.MINEABLE_WITH_SHOVEL)) preferredType = "shovel";
        else if (tags.contains(BlockTags.MINEABLE_WITH_HOE)) preferredType = "hoe";


        if (tags.contains(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)) baseTier = getTierByType(preferredType, "diamond");
        else if (tags.contains(BlockTags.INCORRECT_FOR_IRON_TOOL)) baseTier = getTierByType(preferredType, "diamond");
        else if (tags.contains(BlockTags.INCORRECT_FOR_STONE_TOOL)) baseTier = getTierByType(preferredType, "iron");
        else if (tags.contains(BlockTags.INCORRECT_FOR_COPPER_TOOL)) baseTier = getTierByType(preferredType, "stone");
        else if (tags.contains(BlockTags.INCORRECT_FOR_GOLD_TOOL)) baseTier = getTierByType(preferredType, "copper");
        else if (tags.contains(BlockTags.INCORRECT_FOR_WOODEN_TOOL)) baseTier = getTierByType(preferredType, "gold");

        if (tags.contains(BlockTags.NEEDS_DIAMOND_TOOL)) baseTier = getTierByType(preferredType, "diamond");
        else if (tags.contains(BlockTags.NEEDS_IRON_TOOL)) baseTier = getTierByType(preferredType, "iron");
        else if (tags.contains(BlockTags.NEEDS_STONE_TOOL)) baseTier = getTierByType(preferredType, "stone");

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
            return Objects.equals(id, toolTier.id);
        }
        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
    }
    public static class StartTierRegistrationEvent extends Event {
        public static HandlerList HANDLERS = new HandlerList();

        @Override
        public @NotNull HandlerList getHandlers() {
            return HANDLERS;
        }

        public static HandlerList getHandlerList() {
            return HANDLERS;
        }
    }

    // Blocks
    public static Component defaultGetRedstoneInfo(Block block) {
        if (block.getType() == Material.REDSTONE_BLOCK) {
            return mm.deserialize(valuesFile.getString("minecraft.redstone_power_on", " <red>●</red>"));
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
            return mm.deserialize(
                valuesFile.getString("minecraft.redstone_power_level", " <red>●</red> <power>"),
                Placeholder.unparsed("power", String.valueOf(power))
            );
        }

        return mm.deserialize(valuesFile.getString(
            powered ? "minecraft.redstone_power_on" : "minecraft.redstone_power_off",
            powered ? " <red>●</red>" : " <gray>●</gray>"
        ));
    }
    public static Component defaultGetCropAge(Block block) {
        BlockData data = block.getBlockData();
        if (!(data instanceof Ageable ageable)) return Component.empty();

        int age = ageable.getAge();
        int maxAge = ageable.getMaximumAge();
        int percent = (int) ((age / (double) maxAge) * 100);

        int stage = Math.min(3, percent / 25) + 1;

        return mm.deserialize(
            valuesFile.getString("minecraft.crop_age_" + stage,
                " <color_by_percent>\uD83C\uDF31 <age>/<max_age><color_by_percent_end>"
            ),
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

        return mm.deserialize(
            valuesFile.getString("minecraft.honey_level",
                " <honey_icon> <color_by_percent><honey_level>/<honey_max_level><color_by_percent_end>"
            ),
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

        return mm.deserialize(
            valuesFile.getString("minecraft.smelt_time", " <color_by_percent>⌛ <seconds_remaining>s<color_by_percent_end>"),
            Placeholder.parsed("color_by_percent", getColorForPercent(percent)),
            Placeholder.parsed("color_by_percent_end", getColorForPercentEnd(percent)),
            Placeholder.unparsed("seconds_remaining", String.valueOf(secondsRemaining))
        );
    }
    public static Component defaultGetTotalItemsInContainer(Block block) {
        if (!ItemGroups.getContainers().contains(block.getType())) {
            return Component.text("");
        }

        BlockState state = block.getState();
        if (!(state instanceof InventoryHolder holder)) {
            return Component.text("g");
        }

        int totalItems = 0;

        for (ItemStack item : holder.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                totalItems += item.getAmount();
            }
        }

        return mm.deserialize(
            valuesFile.getString("minecraft.items_in_container", "<gold>📦 <items></gold> "),
            Placeholder.parsed("items", String.valueOf(totalItems))
        );
    }
    public static Component defaultGetBeaconEffect(Block block) {
        if (block.getType() == Material.BEACON) {
            Beacon state = (Beacon) block.getState();
            PotionEffect primaryEffect = state.getPrimaryEffect();
            PotionEffect secondaryEffect = state.getSecondaryEffect();
            if (primaryEffect != null) {
                if (secondaryEffect != null) {
                    return mm.deserialize(valuesFile.getString("minecraft.beacon_2_effect",
                            " <effect_icon_1> <primary_amplifier> <effect_icon_2> <secondary_amplifier>"),
                            Placeholder.component("effect_icon_1", getEffectSprite(primaryEffect.getType().key())),
                            Placeholder.unparsed("primary_amplifier", String.valueOf(primaryEffect.getAmplifier() + 1)),
                            Placeholder.component("effect_icon_2", getEffectSprite(secondaryEffect.getType().key())),
                            Placeholder.unparsed("secondary_amplifier", String.valueOf(secondaryEffect.getAmplifier() + 1))
                    );
                }
                return mm.deserialize(valuesFile.getString("minecraft.beacon_1_effect", " <effect_icon> <primary_amplifier>"),
                        Placeholder.component("effect_icon", getEffectSprite(primaryEffect.getType().key())),
                        Placeholder.unparsed("primary_amplifier", String.valueOf(primaryEffect.getAmplifier() + 1))
                );
            }
        }
        return Component.text("");
    }
    public static Component defaultGetSpawnerInfo(Block block) {
        if (block.getType() == Material.SPAWNER) {
            Spawner state = (Spawner) block.getState();
            EntitySnapshot snapshot = state.getSpawnedEntity();
            if (snapshot != null) {
                Entity entity = snapshot.createEntity(new Location(Bukkit.getWorlds().getFirst(), 0, 0, 0));
                Component key = Component.translatable("entity.minecraft." + entity.getType().toString().toLowerCase());
                if (entity.customName() != null) {
                    key = entity.customName();
                }
                entity.remove();
                assert key != null;
                return mm.deserialize(valuesFile.getString("minecraft.spawner_info", " <egg_icon> <green><spawning_entity></green>"),
                        Placeholder.component("egg_icon", getSprite(Atlases.ITEMS.key, "item/egg")),
                        Placeholder.component("spawning_entity", key)
                );
            }
        }
        return Component.text("");
    }
    public static Component defaultGetNoteblockInfo(Block block) {
        if (block.getType() == Material.NOTE_BLOCK) {
            NoteBlock data = (NoteBlock) block.getBlockData();
            Note note = data.getNote();
            Instrument instrument = data.getInstrument();

            return mm.deserialize(valuesFile.getString("minecraft.noteblock_info", " <gold>\uD83C\uDFB9 <instrument>: <tone> <octave></gold>"),
                    Placeholder.unparsed("instrument", ConfigUtils.toProperCase(instrument.name())),
                    Placeholder.unparsed("tone", ConfigUtils.toProperCase(note.getTone().name())),
                    Placeholder.unparsed("octave", String.valueOf(note.getOctave()))
            );
        }
        return Component.text("");
    }
    public static Component defaultGetFarmlandHydration(Block block) {
        if (block.getType() == Material.FARMLAND) {
            Farmland data = (Farmland) block.getBlockData();
            int moisture = data.getMoisture();

            if (moisture > 0) {
                return mm.deserialize(valuesFile.getString("minecraft.farmland_hydrated", " <blue>\uD83D\uDCA7</blue>"));
            } else {
                return mm.deserialize(valuesFile.getString("minecraft.farmland_not_hydrated", " <gray>\uD83D\uDCA7</gray>"));
            }
        }
        return Component.text("");
    }
    public static Component defaultGetToolToBreak(Block block, Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        ToolTier tier = getTier(heldItem);
        ToolTier reqTier = getRequiredTier(block);

        if (reqTier == null) {
            return Component.empty();
        }

        Key baseKey = reqTier.stack.getDataOrDefault(DataComponentTypes.ITEM_MODEL,
                new NamespacedKey("minecraft", reqTier.stack.getType().name().toLowerCase(Locale.ROOT)));
        NamespacedKey key = new NamespacedKey(baseKey.namespace(), "item/" + baseKey.value());

        return getSprite(Atlases.ITEMS.key, key).append(Component.text(" "));
    }
    // Entities
    public static Component defaultGetEntityAgeLeft(Entity entity) {
        if (entity instanceof org.bukkit.entity.Ageable data) {
            int age = data.getAge();
            if (age < 0) {
                int secondsLeft = Math.abs(age) / 20;
                return mm.deserialize(valuesFile.getString("minecraft.entity_time_to_grow", " <entity_egg> <seconds_left>s"),
                    Placeholder.component("entity_egg", getSprite(Atlases.ITEMS.key, "item/egg")),
                    Placeholder.unparsed("seconds_left", String.valueOf(secondsLeft)));
            }
        }
        return Component.text("");
    }
    public static Component defaultGetEntityOwner(Entity entity) {
        if (!(entity instanceof Tameable tameable)) return Component.empty();

        AnimalTamer owner = tameable.getOwner();
        if (!tameable.isTamed() || owner == null) return Component.empty();

        return mm.deserialize(
            valuesFile.getString("minecraft.entity_owner", "<gray><entity_owner></gray> "),
            Placeholder.unparsed("entity_owner", owner.getName())
        );
    }
    public static Component defaultGetIsLeashed(Entity entity) {
        if (entity instanceof LivingEntity lEntity) {
            if (lEntity.isLeashed()) {
                return mm.deserialize(valuesFile.getString("minecraft.entity_is_leashed", "<leash_icon> "),
                    Placeholder.component("leash_icon", getSprite(Atlases.ITEMS.key, "item/lead")));
            }
        }
        return Component.text("");
    }
    public static Component defaultGetEntityHealth(Entity entity) {
        if (entity instanceof LivingEntity data) {
            int health = (int) data.getHealth();
            int maxHealth = (int) Objects.requireNonNull(data.getAttribute(Attribute.MAX_HEALTH)).getValue();

            return mm.deserialize(valuesFile.getString("minecraft.entity_health", " <heart_icon> <red><entity_health>/<entity_max_health></red>"),
                    Placeholder.component("heart_icon", getSprite(Atlases.GUI.key, "hud/heart/full")),
                    Placeholder.unparsed("entity_health", String.valueOf(health)),
                    Placeholder.unparsed("entity_max_health", String.valueOf(maxHealth))
            );
        }
        return Component.text("");
    }
    public static Component defaultGetVillagerProfession(Entity entity) {
        if (entity.getType() == EntityType.VILLAGER) {
            Villager villager = (Villager) entity;
            Profession profession = villager.getProfession();

            return mm.deserialize(valuesFile.getString("minecraft.villager_profession", " <gray><profession></gray>"),
                    Placeholder.unparsed("profession", getProfessionString(profession))
            );
        }
        return Component.text("");
    }
    public static Component defaultGetTNTFuseTime(Entity entity) {
        if (entity.getType() == EntityType.TNT) {
            float tntFuseTime = ((TNTPrimed) entity).getFuseTicks() / 20.0f;
            return mm.deserialize(valuesFile.getString("minecraft.tnt_fuse_time", " <red>\\uD83D\\uDCA3<fuse_time></red>"),
                    Placeholder.unparsed("fuse_time", String.valueOf(tntFuseTime))
            );
        }
        return Component.text("");
    }
    public static Component defaultGetHorseSpeed(Entity entity) {
        if (!(entity instanceof AbstractHorse horse)) return Component.text("");
        AttributeInstance speed = horse.getAttribute(Attribute.MOVEMENT_SPEED);
        if (speed == null) return Component.text("");
        return mm.deserialize(valuesFile.getString("minecraft.horse_speed", " <speed_icon> <horse_speed>"),
            Placeholder.component("speed_icon", getEffectSprite(PotionEffectType.SPEED.key())),
            Placeholder.unparsed("horse_speed", String.format("%.3f", speed.getBaseValue() * 43.17))
        );
    }
    public static Component defaultGetHorseJumpStrength(Entity entity) {
        if (!(entity instanceof AbstractHorse horse)) return Component.text("");
        AttributeInstance jumpStrength = horse.getAttribute(Attribute.JUMP_STRENGTH);
        if (jumpStrength == null) return Component.text("");
        return mm.deserialize(valuesFile.getString("minecraft.horse_jump_strength", " <jump_icon> <horse_jump_strength>"),
            Placeholder.component("jump_icon", getEffectSprite(PotionEffectType.JUMP_BOOST.key())),
            Placeholder.unparsed("horse_jump_strength", String.format("%.3f", getJumpHeight(jumpStrength.getBaseValue())))
        );
    }
    private static double getJumpHeight(double jumpStrength) {
        return 4.53680079 * jumpStrength * jumpStrength + 1.61431730 * jumpStrength - 0.22656224;
    }

    // Utility
    public static String getColorForPercent(float percent) {
        if (percent >= 0 && percent <= 25) {
            return valuesFile.getString("percent_color.25", "<red>");
        } else if (percent > 25 && percent <= 50) {
            return valuesFile.getString("percent_color.50", "<yellow>");
        } else if (percent > 50 && percent <= 75) {
            return valuesFile.getString("percent_color.75", "<15ed15>");
        } else if (percent > 75) {
            return valuesFile.getString("percent_color.100", "<2b662b>");
        }
        return valuesFile.getString("percent_color.default", "<gray>");
    }
    public static String getColorForPercentEnd(float percent) {
        if (percent >= 0 && percent <= 25) {
            return valuesFile.getString("percent_color.25", "</red>");
        } else if (percent > 25 && percent <= 50) {
            return valuesFile.getString("percent_color.50", "</yellow>");
        } else if (percent > 50 && percent <= 75) {
            return valuesFile.getString("percent_color.75", "</15ed15>");
        } else if (percent > 75) {
            return valuesFile.getString("percent_color.100", "</2b662b>");
        }
        return valuesFile.getString("percent_color.default", "/<gray>");
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
    public static String getProfessionString(Profession profession) {
        if (profession == Profession.ARMORER) {
            return valuesFile.getString("profession.ARMORER", "Armorer");
        } else if (profession == Profession.BUTCHER) {
            return valuesFile.getString("profession.BUTCHER", "Butcher");
        } else if (profession == Profession.CARTOGRAPHER) {
            return valuesFile.getString("profession.CARTOGRAPHER", "Cartographer");
        } else if (profession == Profession.CLERIC) {
            return valuesFile.getString("profession.CLERIC", "Cleric");
        } else if (profession == Profession.FARMER) {
            return valuesFile.getString("profession.FARMER", "Farmer");
        } else if (profession == Profession.FISHERMAN) {
            return valuesFile.getString("profession.FISHERMAN", "Fisherman");
        } else if (profession == Profession.FLETCHER) {
            return valuesFile.getString("profession.FLETCHER", "Fletcher");
        } else if (profession == Profession.LEATHERWORKER) {
            return valuesFile.getString("profession.LEATHERWORKER", "Leather Worker");
        } else if (profession == Profession.LIBRARIAN) {
            return valuesFile.getString("profession.LIBRARIAN", "Librarian");
        } else if (profession == Profession.MASON) {
            return valuesFile.getString("profession.MASON", "Mason");
        } else if (profession == Profession.NITWIT) {
            return valuesFile.getString("profession.NITWIT", "Nitwit");
        } else if (profession == Profession.SHEPHERD) {
            return valuesFile.getString("profession.SHEPHERD", "Shepherd");
        } else if (profession == Profession.WEAPONSMITH) {
            return valuesFile.getString("profession.WEAPONSMITH", "Weaponsmith");
        } else if (profession == Profession.NONE) {
            return valuesFile.getString("profession.JOBLESS", "Jobless");
        }
        return "";
    }

    public static ToolTier getTier(ItemStack stack) {
        for (Function<ItemStack, ToolTier> getter : TIER_GETTERS) {
            ToolTier tier = getter.apply(stack);
            if (tier != null) {
                return tier;
            }
        }

        return null;
    }
    public static ToolTier getRequiredTier(Block block) {
        for (Function<Block, ToolTier> getter : BLOCK_TIER_GETTERS) {
            ToolTier tier = getter.apply(block);
            if (tier != null) {
                return tier;
            }
        }

        return null;
    }
    private static ToolTier getTierByType(String type, String material) {
        if (type == null || material == null) {
            return null;
        }

        for (ToolTier tier : TIERS) {
            String key = tier.id.getKey();
            if (key.equals(material + "_" + type)) {
                return tier;
            }
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
