package me.darksoul.whatIsThat;

import com.MT.xxxtrigger50xxx.Devices.Battery2;
import com.MT.xxxtrigger50xxx.Devices.Device;
import dev.aurelium.auramobs.api.AuraMobsAPI;
import me.darksoul.whatIsThat.misc.ItemGroups;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Information {
    private static final YamlConfiguration config = WAILAListener.getConfig();
    private static final List<Function<Block, String>> suffixMTBlocks = new ArrayList<>();
    private static final List<Function<Block, String>> prefixMTBlocks = new ArrayList<>();
    private static final List<Function<Block, String>> suffixVBlocks = new ArrayList<>();
    private static final List<Function<Block, String>> prefixVBlocks = new ArrayList<>();
    private static final List<Function<Entity, String>> prefixVEntity = new ArrayList<>();
    private static final List<Function<Entity, String>> suffixVEntity = new ArrayList<>();
    private static final List<Function<Entity, String>> prefixIAEntity = new ArrayList<>();
    private static final List<Function<Entity, String>> suffixIAEntity = new ArrayList<>();
    private static final List<Function<Entity, String>> prefixASEntity = new ArrayList<>();

    static {
        // Blocks
        if (config.getBoolean("minetorio.powerinfo", true)) {
            suffixMTBlocks.add(Information::getPower);
        }
        if (config.getBoolean("blocks.redstoneinfo", true)) {
            suffixVBlocks.add(Information::getRedstoneInfo);
        }
        if (config.getBoolean("blocks.cropinfo", true)) {
            suffixVBlocks.add(Information::getHarvestInfo);
        }
        if (config.getBoolean("blocks.beehiveinfo", true)) {
            suffixVBlocks.add(Information::getHoneyInfo);
        }
        if (config.getBoolean("blocks.smeltinfo", true)) {
            suffixVBlocks.add(Information::getRemainingSmeltTime);
        }
        if (config.getBoolean("minetorio.smeltinfo", true)) {
            suffixMTBlocks.add(Information::getRemainingSmeltTime);
        }
        if (config.getBoolean("blocks.containerinfo", true)) {
            prefixVBlocks.add(Information::getTotalItemsInContainer);
        }
        if (config.getBoolean("minetorio.containerinfo", true)) {
            prefixMTBlocks.add(Information::getTotalItemsInContainer);
        }
        if (config.getBoolean("blocks.beaconinfo", true)) {
            suffixVBlocks.add(Information::getBeaconEffect);
        }
        if (config.getBoolean("blocks.spawnerinfo", true)) {
            suffixVBlocks.add(Information::getSpawnerInfo);
        }
        if (config.getBoolean("blocks.noteblockinfo", true)) {
            suffixVBlocks.add(Information::getNoteblockInfo);
        }
        if (config.getBoolean("blocks.farmlandinfo", true)) {
            suffixVBlocks.add(Information::getFarmlandHydration);
        }
        // Entities
        if (config.getBoolean("entities.ageinfo", true)) {
            suffixVEntity.add(Information::getEntityAgeLeft);
        }
        if (config.getBoolean("entities.healthinfo", true)) {
            suffixVEntity.add(Information::getEntityHealth);
        }
        if (config.getBoolean("entities.ownerinfo", true)) {
            prefixVEntity.add(Information::getEntityOwner);
        }
        if (config.getBoolean("entities.leashinfo", true)) {
            prefixVEntity.add(Information::getIsLeashed);
        }
        if (config.getBoolean("entities.professioninfo", true)) {
            suffixVEntity.add(Information::getVillagerProfession);
        }
        if (config.getBoolean("itemsadder.entities.healthinfo", true)) {
            suffixIAEntity.add(Information::getEntityHealth);
        }
    }

    // Blocks
    private static String getPower(Block block) {
        Device device = Device.getDevice(block);
        if (device != null) {
            if (!device.isNeedsPower()) {
                return "";
            }
            if (ItemGroups.isGenerator(device)) {
                if (device.isProducingPower()) {
                    return " | §a⚡ " + device.getPowerGen();
                } else {
                    return " | §8⚡ 0";
                }
            } else if (device instanceof Battery2) {
                float fillPercentage = ((float) ((Battery2) device).getStored() / ((Battery2) device).getCapacity()) * 100;
                String color = getColorForPercent(fillPercentage);
                return color + "⚡ " + ((Battery2) device).getStored();
            } else {
                if (device.isPowered()) {
                    if (device.getIdlePower() == 0) {
                        return " | §c⚡ " + device.getActionPower();
                    }
                    return " | §c⚡ " + device.getActionPower() / device.getIdlePower();
                }
            }
        }
        return "";
    }
    private static String getRedstoneInfo(Block block) {
        boolean isPowerSource = block.isBlockIndirectlyPowered();
        int power = block.getBlockPower();
        //RedStone components
        for (Material type : ItemGroups.getRedstoneComponents()) {
            if (block.getType() == type) {
                if (power > 0 || isPowerSource) {
                    return " | §c● " + power;
                } else if (power == 0) {
                    return " | §8● ";
                }
            }
        }
        // Redstone Providers
        for (Material type : ItemGroups.getRedstoneProviders()) {
            if (block.getType() == type) {
                if (power > 0 || isPowerSource) {
                    return " | §c● ";
                } else if (power == 0) {
                    return " | §8● ";
                }
            }
        }
        return "";
    }
    private static String getHarvestInfo(Block block) {
        for (Material type : ItemGroups.getCrops()) {
            if (block.getType() == type) {
                BlockData data = block.getBlockData();
                if (data instanceof Ageable) {
                    int age = ((Ageable) data).getAge();
                    int maxAge = ((Ageable) data).getMaximumAge();

                    int percentage = (age / (int) maxAge) * 100;

                    if (percentage >= 0 && percentage <= 25) {
                        return " | " + getColorForPercent((float) percentage) + "\uD83C\uDF31 " + age + "/" + maxAge;
                    } else if (percentage > 25 && percentage <= 50) {
                        return " | " + getColorForPercent((float) percentage) + "\uD83C\uDF3F " + age + "/" + maxAge;
                    } else if (percentage > 50 && percentage <= 75) {
                        return " | " + getColorForPercent((float) percentage) + "\uD83C\uDF3D " + age + "/" + maxAge;
                    } else if (percentage > 75) {
                        return " | " + getColorForPercent((float) percentage) + "\uD83C\uDF3D " + age + "/" + maxAge;
                    }
                }
            }
        }
        return "";
    }
    private static String getHoneyInfo(Block block) {
        for (Material type : ItemGroups.getHoneyProducers()) {
            if (block.getType() == type) {
                BlockData data = block.getBlockData();
                if (data instanceof Beehive) {
                    int honeyLevel = ((Beehive) data).getHoneyLevel();
                    int maxHoneyLevel = ((Beehive) data).getMaximumHoneyLevel();

                    double percentage = (honeyLevel / (double) maxHoneyLevel) * 100;

                    return " | " + getColorForPercent((float) percentage) + "\uD83D\uDC1D "
                            + honeyLevel + "/"
                            + maxHoneyLevel;
                }
            }
        }
        return "";
    }
    private static String getRemainingSmeltTime(Block block) {
        for (Material type : ItemGroups.getFurnaces()) {
            if (block.getType() == type) {
                BlockState state = block.getState();
                InventoryHolder ih = (InventoryHolder) state;
                FurnaceInventory inventory = (FurnaceInventory) ih.getInventory();

                int cookTime = ((Furnace) state).getCookTime();
                int cookTimeTotal = ((Furnace) state).getCookTimeTotal();

                int ticksRemaining = cookTimeTotal - cookTime;
                int secondsRemaining = ticksRemaining / 20;
                float percentage = ((float) cookTime / cookTimeTotal) * 100;
                if (inventory.getSmelting() != null && cookTime != 0) {
                    return " | " + getColorForPercent(percentage) + "⌛ " + secondsRemaining + "s";
                }
            }
        }
        return "";
    }
    private static String getTotalItemsInContainer(Block block) {
        for (Material type : ItemGroups.getContainers()) {
            if (block.getType() == type) {
                BlockState state = block.getState();
                InventoryHolder ih = (InventoryHolder) state;
                Inventory inventory = ih.getInventory();

                int totalItems = 0;

                for (ItemStack item : inventory.getContents()) {
                    if (item != null) {
                        totalItems += item.getAmount();
                    }
                }

                return "§6\uD83D\uDCE6 " + totalItems + " §f| ";
            }
        }
        return "";
    }
    private static String getBeaconEffect(Block block) {
        if (block.getType() == Material.BEACON) {
            Beacon state = (Beacon) block.getState();
            PotionEffect primaryEffect = state.getPrimaryEffect();
            PotionEffect secondaryEffect = state.getSecondaryEffect();
            if (primaryEffect != null) {
                if (secondaryEffect != null) {
                    return " | "
                            + getEmojiForEffect(primaryEffect.getType().getName())
                            + ":"
                            + (primaryEffect.getAmplifier() + 1)
                            + " "
                            + getEmojiForEffect(secondaryEffect.getType().getName())
                            + ":"
                            + (secondaryEffect.getAmplifier() + 1);
                }
                return " | "
                        + getEmojiForEffect(primaryEffect.getType().getName())
                        + ":"
                        + primaryEffect.getAmplifier();
            }
        }
        return "";
    }
    private static String getSpawnerInfo(Block block) {
        if (block.getType() == Material.SPAWNER) {
            Spawner state = (Spawner) block.getState();
            EntityType entity = state.getSpawnedType();
            if (entity != null) {
                return " | §a\uD83E\uDDDF " + entity.name();
            }
        }
        return "";
    }
    private static String getNoteblockInfo(Block block) {
        if (block.getType() == Material.NOTE_BLOCK) {
            NoteBlock data = (NoteBlock) block.getBlockData();
            Note note = data.getNote();
            Instrument instrument = data.getInstrument();

            return " | §6🎹 " + instrument.name()
                    + ": "
                    + note.getTone().name()
                    + " "
                    + note.getOctave();
        }
        return "";
    }
    private static String getFarmlandHydration(Block block) {
        if (block.getType() == Material.FARMLAND) {
            Farmland data = (Farmland) block.getBlockData();
            int moisture = data.getMoisture();

            if (moisture > 0) {
                return " | §9\uD83D\uDCA7 ";
            } else {
                return " | §8\uD83D\uDCA7";
            }
        }
        return "";
    }
    // Entities
    private static String getEntityAgeLeft(Entity entity) {
        if (entity instanceof org.bukkit.entity.Ageable data) {
            int age = data.getAge();
            if (age < 0) {
                int secondsLeft = Math.abs(age) / 20;
                return " | §e\uD83D\uDC25 " + secondsLeft + "s";
            }
        }
        return "";
    }
    private static String getEntityOwner(Entity entity) {
        for (EntityType type : ItemGroups.getPets()) {
            if (entity.getType() == type) {
                Tameable data = (Tameable) entity;
                if (data.isTamed() && data.getOwner() != null) {
                    return "§8" + data.getOwner().getName() + " §f| ";
                }
            }
        }
        return "";
    }
    private static String getIsLeashed(Entity entity) {
        if (entity instanceof LivingEntity lEntity) {
            if (lEntity.isLeashed()) {
                return " | §2\uD83D\uDD17";
            }
        }
        return "";
    }
    public static String getEntityHealth(Entity entity) {
        if (entity instanceof LivingEntity data) {
            int health = (int) data.getHealth();
            int maxHealth = (int) Objects.requireNonNull(data.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();

            return " §c❤ " + health + "/" + maxHealth;
        }
        return "";
    }
    private static String getVillagerProfession(Entity entity) {
        if (entity.getType() == EntityType.VILLAGER) {
            Villager villager = (Villager) entity;
            Profession profession = villager.getProfession();

            return "§8" + getProfessionString(profession) + " §f| ";
        }
        return "";
    }
    // Utility
    public static String getColorForPercent(float percent) {
        if (percent >= 0 && percent <= 25) {
            return "§c";
        } else if (percent > 25 && percent <= 50) {
            return "§e";
        } else if (percent > 50 && percent <= 75) {
            return "§a";
        } else if (percent > 75) {
            return "§2";
        }
        return "§8";
    }
    private static String getEmojiForEffect(String effect) {
        return switch (effect) {
            case "SPEED" -> "§b💨";
            case "FAST_DIGGING" -> "§e⛏";
            case "DAMAGE_RESISTANCE" -> "§6🛡";
            case "JUMP" -> "§a🐇";
            case "INCREASE_DAMAGE" -> "§c💪";
            case "REGENERATION" -> "§d❤";
            default -> "";
        };
    }
    private static String getProfessionString(Profession profession) {
        if (profession == Profession.ARMORER) {
            return "Armorer";
        } else if (profession == Profession.BUTCHER) {
            return "Butcher";
        } else if (profession == Profession.CARTOGRAPHER) {
            return "Cartographer";
        } else if (profession == Profession.CLERIC) {
            return "Cleric";
        } else if (profession == Profession.FARMER) {
            return "Farmer";
        } else if (profession == Profession.FISHERMAN) {
            return "Fisherman";
        } else if (profession == Profession.FLETCHER) {
            return "Fletcher";
        } else if (profession == Profession.LEATHERWORKER) {
            return "Leather Worker";
        } else if (profession == Profession.LIBRARIAN) {
            return "Librarian";
        } else if (profession == Profession.MASON) {
            return "Mason";
        } else if (profession == Profession.NITWIT) {
            return "Nitwit";
        } else if (profession == Profession.SHEPHERD) {
            return "Shepherd";
        } else if (profession == Profession.WEAPONSMITH) {
            return "Weaponsmith";
        } else if (profession == Profession.NONE) {
            return "Jobless";
        }
        return "";
    }
    // Getters
    public static List<Function<Block, String>> getPrefixMTBlocks() {
        return prefixMTBlocks;
    }
    public static List<Function<Block, String>> getSuffixMTBlocks() {
        return suffixMTBlocks;
    }
    public static List<Function<Block, String>> getPrefixVBlocks() {
        return prefixVBlocks;
    }
    public static List<Function<Block, String>> getSuffixVBlocks() {
        return suffixVBlocks;
    }
    public static List<Function<Entity, String>> getPrefixVEntity() {
        return prefixVEntity;
    }
    public static List<Function<Entity, String>> getSuffixVEntity() {
        return suffixVEntity;
    }
    public static List<Function<Entity, String>> getPrefixIAEntity() {
        return prefixIAEntity;
    }
    public static List<Function<Entity, String>> getSuffixIAEntity() {
        return suffixIAEntity;
    }
    public static List<Function<Entity, String>> getPrefixASEntity() {
        return prefixASEntity;
    }
}
