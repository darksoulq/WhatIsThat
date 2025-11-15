package com.github.darksoulq.wit;

import com.mojang.datafixers.util.Either;
import io.papermc.paper.datacomponent.DataComponentTypes;
import com.github.darksoulq.wit.misc.ConfigUtils;
import com.github.darksoulq.wit.misc.ItemGroups;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.object.ObjectContents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
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
import org.bukkit.spawner.Spawner;
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
    public static Component default_getRedstoneInfo(Block block) {
        boolean isPowerSource = block.isBlockIndirectlyPowered();
        int power = block.getBlockPower();
        //RedStone components
        if (ItemGroups.getRedstoneComponents().contains(block.getType())) {
            if (power > 0 || isPowerSource) {
                return mm.deserialize(valuesFile.getString("minecraft.redstone_power_level",
                                " <red>§c●</red> {power}")
                        .replace("{power}", String.valueOf(power)));
            } else if (power == 0) {
                return mm.deserialize(valuesFile.getString("minecraft.redstone_power_off", " <gray>●</gray>"));
            }
        }
        // Redstone Block
        if (block.getType() == Material.REDSTONE_BLOCK) {
            return mm.deserialize(valuesFile.getString("minecraft.redstone_power_on", " <red>●</red>"));
        }
        // Redstone Providers
        if (ItemGroups.getRedstoneProviders().contains(block.getType())) {
            if (power > 0 || isPowerSource) {
                return mm.deserialize(valuesFile.getString("minecraft.redstone_power_on", " <red>●</red>"));
            } else if (power == 0) {
                return mm.deserialize(valuesFile.getString("minecraft.redstone_power_on", " <gray>●</gray>"));
            }
        }
        return Component.text("");
    }
    public static Component default_getCropAge(Block block) {
        if (ItemGroups.getCrops().contains(block.getType())) {
            BlockData data = block.getBlockData();
            int age = ((Ageable) data).getAge();
            int maxAge = ((Ageable) data).getMaximumAge();

            int percentage = (age / maxAge) * 100;

            if (percentage >= 0 && percentage <= 25) {
                return mm.deserialize(valuesFile.getString("minecraft.crop_age_1", " {colorByPercent}\uD83C\uDF31 {age}/{maxAge}{colorByPercentEnd}")
                        .replace("{colorByPercent}", getColorForPercent((float) percentage))
                        .replace("{age}", String.valueOf(age))
                        .replace("{maxAge}", String.valueOf(maxAge))
                        .replace("{colorByPercentEnd}", getColorForPercentEnd(percentage)));
            } else if (percentage > 25 && percentage <= 50) {
                return mm.deserialize(valuesFile.getString("minecraft.crop_age_2", " {colorByPercent}\uD83C\uDF3F {age}/{maxAge}{colorByPercentEnd}")
                        .replace("{colorByPercent}", getColorForPercent((float) percentage))
                        .replace("{age}", String.valueOf(age))
                        .replace("{maxAge}", String.valueOf(maxAge))
                        .replace("{colorByPercentEnd}", getColorForPercentEnd(percentage)));
            } else if (percentage > 50 && percentage <= 75) {
                return mm.deserialize(valuesFile.getString("minecraft.crop_age_3", " {colorByPercent}\uD83C\uDF3D {age}/{maxAge}{colorByPercentEnd}")
                        .replace("{colorByPercent}", getColorForPercent((float) percentage))
                        .replace("{age}", String.valueOf(age))
                        .replace("{maxAge}", String.valueOf(maxAge))
                        .replace("{colorByPercentEnd}", getColorForPercentEnd(percentage)));
            } else if (percentage > 75) {
                return mm.deserialize(valuesFile.getString("minecraft.crop_age_4", " {colorByPercent}\uD83C\uDF3D {age}/{maxAge}{colorByPercentEnd}")
                        .replace("{colorByPercent}", getColorForPercent((float) percentage))
                        .replace("{age}", String.valueOf(age))
                        .replace("{maxAge}", String.valueOf(maxAge))
                        .replace("{colorByPercentEnd}", getColorForPercentEnd(percentage)));
            }
        }
        return Component.text("");
    }
    public static Component default_getHoneyLevel(Block block) {
        if (ItemGroups.getHoneyProducers().contains(block.getType())) {
            BlockData data = block.getBlockData();
            if (data instanceof Beehive) {
                int honeyLevel = ((Beehive) data).getHoneyLevel();
                int maxHoneyLevel = ((Beehive) data).getMaximumHoneyLevel();

                double percentage = (honeyLevel / (double) maxHoneyLevel) * 100;

                return mm.deserialize(valuesFile.getString("minecraft.honey_level",
                                " {colorByPercent}\uD83D\uDC1D {honeyLevel}/{honeyMaxLevel}{colorByPercentEnd}")
                        .replace("{colorByPercent}", getColorForPercent((float) percentage))
                        .replace("{honeyLevel}", String.valueOf(honeyLevel))
                        .replace("{honeyMaxLevel}", String.valueOf(maxHoneyLevel))
                        .replace("{colorByPercentEnd}", getColorForPercentEnd((float) percentage)));
            }
        }
        return Component.text("");
    }
    public static Component default_getRemainingSmeltTime(Block block) {
        if (ItemGroups.getFurnaces().contains(block.getType())) {
            BlockState state = block.getState();
            InventoryHolder ih = (InventoryHolder) state;
            FurnaceInventory inventory = (FurnaceInventory) ih.getInventory();

            int cookTime = ((Furnace) state).getCookTime();
            int cookTimeTotal = ((Furnace) state).getCookTimeTotal();

            int ticksRemaining = cookTimeTotal - cookTime;
            int secondsRemaining = ticksRemaining / 20;
            float percentage = ((float) cookTime / cookTimeTotal) * 100;
            if (inventory.getSmelting() != null && cookTime != 0) {
                return mm.deserialize(valuesFile.getString("minecraft.smelt_time", " {colorByPercent}⌛ {secondsRemaining}s{colorByPercentEnd}")
                        .replace("{colorByPercent}", getColorForPercent(percentage))
                        .replace("{secondsRemaining}", String.valueOf(secondsRemaining))
                        .replace("{colorByPercentEnd}", getColorForPercentEnd(percentage)));
                }
        }
        return Component.text("");
    }
    public static Component default_getTotalItemsInContainer(Block block) {
        if (ItemGroups.getContainers().contains(block.getType())) {
            BlockState state = block.getState();
            InventoryHolder ih = (InventoryHolder) state;
            Inventory inventory = ih.getInventory();

            int totalItems = 0;

            for (ItemStack item : inventory.getContents()) {
                if (item != null) {
                        totalItems += item.getAmount();
                }
            }

            return mm.deserialize(valuesFile.getString("minecraft.items_in_container", "§6\uD83D\uDCE6 {items}§f ")
                    .replace("{items}", String.valueOf(totalItems)));
        }
        return Component.text("");
    }
    public static Component default_getBeaconEffect(Block block) {
        if (block.getType() == Material.BEACON) {
            Beacon state = (Beacon) block.getState();
            PotionEffect primaryEffect = state.getPrimaryEffect();
            PotionEffect secondaryEffect = state.getSecondaryEffect();
            if (primaryEffect != null) {
                if (secondaryEffect != null) {
                    return mm.deserialize(valuesFile.getString("minecraft.beacon_2_effect",
                            " {emojiByEffect1}:{primaryAmplifier} {emojiByEffect2}:{secondaryAmplifier}")
                            .replace("{emojiByEffect1}", getEmojiForEffect(primaryEffect.getType().getName()))
                            .replace("{primaryAmplifier}", String.valueOf(primaryEffect.getAmplifier() + 1))
                            .replace("{emojiByEffect2}", getEmojiForEffect(secondaryEffect.getType().getName()))
                            .replace("{secondaryAmplifier}", String.valueOf(secondaryEffect.getAmplifier() + 1)));
                }
                return mm.deserialize(valuesFile.getString("minecraft.beacon_1_effect", " {emojiByEffect1}:{primaryAmplifier}")
                        .replace("{emojiByEffect1}", getEmojiForEffect(primaryEffect.getType().getName()))
                        .replace("{primaryAmplifier}", String.valueOf(primaryEffect.getAmplifier() + 1)));
            }
        }
        return Component.text("");
    }
    public static Component default_getSpawnerInfo(Block block) {
        if (block.getType() == Material.SPAWNER) {
            Spawner state = (Spawner) block.getState();
            EntityType entity = state.getSpawnedType();
            if (entity != null) {
                return mm.deserialize(valuesFile.getString("minecraft.spawner_info", " §a\uD83E\uDDDF {spawningEntity}")
                        .replace("{spawningEntity}", entity.name()));
            }
        }
        return Component.text("");
    }
    public static Component default_getNoteblockInfo(Block block) {
        if (block.getType() == Material.NOTE_BLOCK) {
            NoteBlock data = (NoteBlock) block.getBlockData();
            Note note = data.getNote();
            Instrument instrument = data.getInstrument();

            return mm.deserialize(valuesFile.getString("minecraft.noteblock_info", " §6🎹 {instrument}: {tone} {octave}")
                    .replace("{instrument}", instrument.name())
                    .replace("{tone}", note.getTone().name())
                    .replace("{octave}", String.valueOf(note.getOctave())));
        }
        return Component.text("");
    }
    public static Component default_getFarmlandHydration(Block block) {
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
    public static Component default_getToolToBreak(Block block, Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        ToolTier tier = getTier(heldItem);
        ToolTier reqTier = getRequiredTier(block);

        if (reqTier == null) {
            return Component.empty();
        }

        Key baseKey = reqTier.stack.getDataOrDefault(DataComponentTypes.ITEM_MODEL,
                new NamespacedKey("minecraft", reqTier.stack.getType().name().toLowerCase(Locale.ROOT)));
        NamespacedKey key = new NamespacedKey(baseKey.namespace(), "item/" + baseKey.value());

        return Component.object(ObjectContents.sprite(new NamespacedKey("minecraft", "blocks"), key));
    }
    // Entities
    public static Component default_getEntityAgeLeft(Entity entity) {
        if (entity instanceof org.bukkit.entity.Ageable data) {
            int age = data.getAge();
            if (age < 0) {
                int secondsLeft = Math.abs(age) / 20;
                return mm.deserialize(valuesFile.getString("minecraft.entity_time_to_grow", " §e\uD83D\uDC25 {secondsLeft}s")
                        .replace("{secondsLeft}", String.valueOf(secondsLeft)));
            }
        }
        return Component.text("");
    }
    public static Component default_getEntityOwner(Entity entity) {
        if (ItemGroups.getPets().contains(entity.getType())) {
            Tameable data = (Tameable) entity;
            AnimalTamer owner = data.getOwner();
            if (data.isTamed() && owner != null) {
                return mm.deserialize(valuesFile.getString("minecraft.entity_owner", "§8{entityOwner} ")
                        .replace("{entityOwner}", owner.getName()));
            }
        }
        return Component.text("");
    }
    public static Component default_getIsLeashed(Entity entity) {
        if (entity instanceof LivingEntity lEntity) {
            if (lEntity.isLeashed()) {
                return mm.deserialize(valuesFile.getString("minecraft.entity_is_leashed", " §2\uD83D\uDD17"));
            }
        }
        return Component.text("");
    }
    public static Component default_getEntityHealth(Entity entity) {
        if (entity instanceof LivingEntity data) {
            int health = (int) data.getHealth();
            int maxHealth = (int) Objects.requireNonNull(data.getAttribute(Attribute.MAX_HEALTH)).getValue();

            return mm.deserialize(valuesFile.getString("minecraft.entity_health", " §c❤ {entityHealth}/{entityMaxHealth}")
                    .replace("{entityHealth}", String.valueOf(health))
                    .replace("{entityMaxHealth}", String.valueOf(maxHealth)));
        }
        return Component.text("");
    }
    public static Component default_getVillagerProfession(Entity entity) {
        if (entity.getType() == EntityType.VILLAGER) {
            Villager villager = (Villager) entity;
            Profession profession = villager.getProfession();

            return mm.deserialize(valuesFile.getString("minecraft.villager_profession", "§8{profession}")
                    .replace("{profession}", getProfessionString(profession)));
        }
        return Component.text("");
    }
    public static Component default_getTNTFuseTime(Entity entity) {
        if (entity.getType() == EntityType.TNT) {
            float tntFuseTime = ((TNTPrimed) entity).getFuseTicks() / 20.0f;
            return mm.deserialize(valuesFile.getString("minecraft.tnt_fuse_time", " §4\uD83D\uDCA3{fuseTime}")
                    .replace("{fuseTime}", String.valueOf(tntFuseTime)));
        }
        return Component.text("");
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
    public static String getEmojiForEffect(String effect) {
        return switch (effect) {
            case "SPEED" -> valuesFile.getString("effect_emoji.SPEED", "§b💨");
            case "FAST_DIGGING" -> valuesFile.getString("effect_emoji.HASTE", "§e⛏");
            case "DAMAGE_RESISTANCE" -> valuesFile.getString("effect_emoji.RESISTANCE", "§6🛡");
            case "JUMP" -> valuesFile.getString("effect_emoji.JUMP", "§a🐇");
            case "INCREASE_DAMAGE" -> valuesFile.getString("effect_emoji.STRENGTH", "§c💪");
            case "REGENERATION" -> valuesFile.getString("effect_emoji.REGENERATION", "§d❤");
            default -> "";
        };
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
}
