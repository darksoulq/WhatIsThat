package me.darksoul.wit;

import me.darksoul.wit.misc.ConfigUtils;
import me.darksoul.wit.misc.ItemGroups;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Tag;
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
import org.bukkit.entity.*;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.spawner.Spawner;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Information {
    private static YamlConfiguration valuesFile = ConfigUtils.loadValuesFIle();
    public static MiniMessage mm = MiniMessage.miniMessage();
    private static final List<ItemStack> usableHoes = new LinkedList<>();
    private static final List<ItemStack> usableShovels = new LinkedList<>();
    private static final List<ItemStack> usableAxes = new LinkedList<>();
    private static final List<ItemStack> usablePickaxes = new LinkedList<>();
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
        usableHoes.clear();
        usableShovels.clear();
        usableAxes.clear();
        usablePickaxes.clear();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        ItemStack canBreak = null;
        new LinkedList<>(ItemGroups.HOES)
                .stream()
                .filter(item -> canToolBreakBlock(item.getType(), block.getType()))
                .forEach(usableHoes::add);
        new LinkedList<>(ItemGroups.SHOVELS)
                .stream()
                .filter(item -> canToolBreakBlock(item.getType(), block.getType()))
                .forEach(usableShovels::add);
        new LinkedList<>(ItemGroups.AXES)
                .stream()
                .filter(item -> canToolBreakBlock(item.getType(), block.getType()))
                .forEach(usableAxes::add);
        new LinkedList<>(ItemGroups.PICKAXES)
                .stream()
                .filter(item -> canToolBreakBlock(item.getType(), block.getType()))
                .forEach(usablePickaxes::add);

        for (ItemStack hoe : usableHoes) {
                if (heldItem == hoe) {
                    canBreak = heldItem;
                    break;
                }
            }
        if (canBreak == null) {
            for (ItemStack shovel : usableShovels) {
                if (heldItem == shovel) {
                    canBreak = heldItem;
                    break;
                }
            }
        }
        if (canBreak == null) {
            for (ItemStack axe : usableAxes) {
                if (heldItem == axe) {
                    canBreak = heldItem;
                    break;
                }
            }
        }
        if (canBreak == null) {
            for (ItemStack pickaxe : usablePickaxes) {
                if (heldItem == pickaxe) {
                    canBreak = heldItem;
                    break;
                }
            }
        }

        Map<String, Boolean> emojiMap = new LinkedHashMap<>();
        if (!usableHoes.isEmpty()) {
            emojiMap.put(getEmojiForTool(Material.WOODEN_HOE), canToolBreakBlock(heldItem.getType(), block.getType()));
        }
        if (!usableShovels.isEmpty()) {
            emojiMap.put(getEmojiForTool(Material.WOODEN_SHOVEL), canToolBreakBlock(heldItem.getType(), block.getType()));
        }
        if (!usableAxes.isEmpty()) {
            emojiMap.put(getEmojiForTool(Material.WOODEN_AXE), canToolBreakBlock(heldItem.getType(), block.getType()));
        }
        if (!usablePickaxes.isEmpty()) {
            emojiMap.put(getEmojiForTool(Material.WOODEN_PICKAXE), canToolBreakBlock(heldItem.getType(), block.getType()));
        }

        StringBuilder toReturn = new StringBuilder();
        for (String emoji : emojiMap.keySet()) {
            if (emojiMap.get(emoji)) {
                toReturn.append("<green>").append(emoji).append("</green>");
            } else {
                toReturn.append("<red>").append(emoji).append("</red>");
            }
        }

        return mm.deserialize(toReturn.toString());
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
    public static ToolType getPrefferedTool(Material mat) {
        if (Tag.MINEABLE_AXE.isTagged(mat)) {
            return ToolType.AXE;
        } else if (Tag.MINEABLE_PICKAXE.isTagged(mat)) {
            return ToolType.PICKAXE;
        } else if (Tag.MINEABLE_SHOVEL.isTagged(mat)) {
            return ToolType.SHOVEL;
        } else if (Tag.MINEABLE_HOE.isTagged(mat)) {
            return ToolType.HOE;
        } else {
            return null;
        }
    }
    public static boolean canToolBreakBlock(Material iMat, Material bMat) {
        ToolType prefferedTool = getPrefferedTool(bMat);

        switch (prefferedTool) {
            case AXE -> {
                return iMat.name().contains("AXE");
            }
            case PICKAXE -> {
                return iMat.name().contains("PICKAXE");
            }
            case SHOVEL -> {
                return iMat.name().contains("SHOVEL");
            }
            case HOE -> {
                return iMat.name().contains("HOE");
            }
            case null, default -> {
                return false;
            }
        }
    }
    public static String getEmojiForTool(@Nullable Material mat) {
        if (mat != null) {
            if (mat.name().endsWith("_AXE")) {
                return valuesFile.getString("tool_emoji.AXE", "\uD83E\uDE93");
            } else if (mat.name().endsWith("_PICKAXE")) {
                return valuesFile.getString("tool_emoji.PICKAXE", "⛏");
            } else if (mat.name().endsWith("_SHOVEL")) {
                return valuesFile.getString("tool_emoji.SHOVEL", "\uD83E\uDD44");
            } else if (mat.name().endsWith("_HOE")) {
                return valuesFile.getString("tool_emoji.HOE", "\uD83D\uDD2A");
            }
        }
        return "";
    }

    public enum ToolType {
        AXE,
        PICKAXE,
        SHOVEL,
        HOE,
    }

    public static void reloadValuesFile() {
        valuesFile = ConfigUtils.loadValuesFIle();
    }
    public static YamlConfiguration getValuesFile() {
        return valuesFile;
    }
}
