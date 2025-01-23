package me.darksoul.whatIsThat;

import com.MT.xxxtrigger50xxx.Devices.Battery2;
import com.MT.xxxtrigger50xxx.Devices.Device;
import com.magmaguy.elitemobs.mobconstructor.EliteEntity;
import dev.aurelium.auramobs.api.AuraMobsAPI;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.user.SkillsUser;
import dev.lone.itemsadder.api.CustomCrop;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.SolarGenerator;
import io.github.thebusybiscuit.slimefun4.implementation.operations.FuelOperation;
import me.athlaeos.valhallammo.playerstats.profiles.ProfileCache;
import me.athlaeos.valhallammo.playerstats.profiles.implementations.PowerProfile;
import me.athlaeos.valhallaraces.Race;
import me.athlaeos.valhallaraces.RaceManager;
import me.darksoul.whatIsThat.compatibility.AuraSkillsCompat;
import me.darksoul.whatIsThat.misc.ConfigUtils;
import me.darksoul.whatIsThat.misc.ItemGroups;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
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

import java.util.Objects;

public class Information {
    private static YamlConfiguration valuesFile = ConfigUtils.loadValuesFIle();
    // Blocks
    public static String minetorio_getPower(Block block) {
        Device device = Device.getDevice(block);
        if (device != null) {
            if (ItemGroups.isGenerator(device)) {
                if (device.isProducingPower()) {
                    return valuesFile.getString("minetorio.generator_power_gen_on", " §a⚡ {powerGen}")
                            .replace("{powerGen}", String.valueOf(device.getPowerGen()));
                } else {
                    return valuesFile.getString("minetorio.generator_power_gen_off", " §8⚡ 0");
                }
            } else if (device instanceof Battery2) {
                float fillPercentage = ((float) ((Battery2) device).getStored() / ((Battery2) device).getCapacity()) * 100;
                String color = getColorForPercent(fillPercentage);
                return valuesFile.getString("minetorio.battery_stored_power", "{colorByPercent} ⚡ {storedPower}")
                        .replace("{colorByPercent}", color)
                        .replace("{storedPower}", String.valueOf(((Battery2) device).getStored()));
            } else if (device.isNeedsPower()){
                if (device.isPowered()) {
                    if (device.getIdlePower() == 0) {
                        return valuesFile.getString("minetorio.device_action_power_noidle", " §c⚡ {needsPower}")
                                .replace("{needsPower}", String.valueOf(device.getActionPower()));
                    }
                    return valuesFile.getString("minetorio.device_action_power", " §c⚡ {needsPowerIdle}")
                            .replace("{needsIdlePower}",
                                    String.valueOf(device.getActionPower()/device.getIdlePower()));
                }
            } else {
                return "";
            }
        }
        return "";
    }
    public static String default_getRedstoneInfo(Block block) {
        boolean isPowerSource = block.isBlockIndirectlyPowered();
        int power = block.getBlockPower();
        //RedStone components
        if (ItemGroups.getRedstoneComponents().contains(block.getType())) {
            if (power > 0 || isPowerSource) {
                return valuesFile.getString("minecraft.redstone_power_level", " §c● {power}")
                        .replace("{power}", String.valueOf(power));
            } else if (power == 0) {
                return valuesFile.getString("minecraft.redstone_power_off", " §8●");
            }
        }
        // Redstone Block
        if (block.getType() == Material.REDSTONE_BLOCK) {
            return valuesFile.getString("minecraft.redstone_power_on", " §c●");
        }
        // Redstone Providers
        if (ItemGroups.getRedstoneProviders().contains(block.getType())) {
            if (power > 0 || isPowerSource) {
                return valuesFile.getString("minecraft.redstone_power_on", " §c●");
            } else if (power == 0) {
                return valuesFile.getString("minecraft.redstone_power_off", " §8●");
            }
        }
        return "";
    }
    public static String default_getCropAge(Block block) {
        if (ItemGroups.getCrops().contains(block.getType())) {
            BlockData data = block.getBlockData();
            int age = ((Ageable) data).getAge();
            int maxAge = ((Ageable) data).getMaximumAge();

            int percentage = (age / maxAge) * 100;

            if (percentage >= 0 && percentage <= 25) {
                return valuesFile.getString("minecraft.crop_age_1", " {colorByPercent}\uD83C\uDF31 {age}/{maxAge}")
                        .replace("{colorByPercent}", getColorForPercent((float) percentage))
                        .replace("{age}", String.valueOf(age))
                        .replace("{maxAge}", String.valueOf(maxAge));
            } else if (percentage > 25 && percentage <= 50) {
                return valuesFile.getString("minecraft.crop_age_2", " {colorByPercent}\uD83C\uDF3F {age}/{maxAge}")
                        .replace("{colorByPercent}", getColorForPercent((float) percentage))
                        .replace("{age}", String.valueOf(age))
                        .replace("{maxAge}", String.valueOf(maxAge));
            } else if (percentage > 50 && percentage <= 75) {
                return valuesFile.getString("minecraft.crop_age_3", " {colorByPercent}\uD83C\uDF3D {age}/{maxAge}")
                        .replace("{colorByPercent}", getColorForPercent((float) percentage))
                        .replace("{age}", String.valueOf(age))
                        .replace("{maxAge}", String.valueOf(maxAge));
            } else if (percentage > 75) {
                return valuesFile.getString("minecraft.crop_age_4", " {colorByPercent}\uD83C\uDF3D {age}/{maxAge}")
                        .replace("{colorByPercent}", getColorForPercent((float) percentage))
                        .replace("{age}", String.valueOf(age))
                        .replace("{maxAge}", String.valueOf(maxAge));
            }
        }
        return "";
    }
    public static String itemsAdder_getCropAge(CustomCrop crop) {
        int age = crop.getAge();
        int maxAge = crop.getMaxAge();
        int percentage = (age / maxAge) * 100;

        if (percentage >= 0 && percentage <= 25) {
            return valuesFile.getString("minecraft.crop_age_1", " {colorByPercent}\uD83C\uDF31 {age}/{maxAge}")
                    .replace("{colorByPercent}", getColorForPercent((float) percentage))
                    .replace("{age}", String.valueOf(age))
                    .replace("{maxAge}", String.valueOf(maxAge));
        } else if (percentage > 25 && percentage <= 50) {
            return valuesFile.getString("minecraft.crop_age_2", " {colorByPercent}\uD83C\uDF3F {age}/{maxAge}")
                    .replace("{colorByPercent}", getColorForPercent((float) percentage))
                    .replace("{age}", String.valueOf(age))
                    .replace("{maxAge}", String.valueOf(maxAge));
        } else if (percentage > 50 && percentage <= 75) {
            return valuesFile.getString("minecraft.crop_age_3", " {colorByPercent}\uD83C\uDF3D {age}/{maxAge}")
                    .replace("{colorByPercent}", getColorForPercent((float) percentage))
                    .replace("{age}", String.valueOf(age))
                    .replace("{maxAge}", String.valueOf(maxAge));
        } else if (percentage > 75) {
            return valuesFile.getString("minecraft.crop_age_4", " {colorByPercent}\uD83C\uDF3D {age}/{maxAge}")
                    .replace("{colorByPercent}", getColorForPercent((float) percentage))
                    .replace("{age}", String.valueOf(age))
                    .replace("{maxAge}", String.valueOf(maxAge));
        }
        return "";
    }
    public static String default_getHoneyLevel(Block block) {
        if (ItemGroups.getHoneyProducers().contains(block.getType())) {
            BlockData data = block.getBlockData();
            if (data instanceof Beehive) {
                int honeyLevel = ((Beehive) data).getHoneyLevel();
                int maxHoneyLevel = ((Beehive) data).getMaximumHoneyLevel();

                double percentage = (honeyLevel / (double) maxHoneyLevel) * 100;

                return valuesFile.getString("minecraft.honey_level",
                                " {colorByPercent}\uD83D\uDC1D {honeyLevel}/{honeyMaxLevel}")
                        .replace("{colorByPercent}", getColorForPercent((float) percentage))
                        .replace("{honeyLevel}", String.valueOf(honeyLevel))
                        .replace("{honeyMaxLevel}", String.valueOf(maxHoneyLevel));
            }
        }
        return "";
    }
    public static String default_getRemainingSmeltTime(Block block) {
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
                    return valuesFile.getString("minecraft.smelt_time", " {colorByPercent}⌛ {secondsRemaining}s")
                            .replace("{colorByPercent}", getColorForPercent(percentage))
                            .replace("{secondsRemaining}", String.valueOf(secondsRemaining));
                }
        }
        return "";
    }
    public static String default_getTotalItemsInContainer(Block block) {
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

            return valuesFile.getString("minecraft:items_in_container", "§6\uD83D\uDCE6 {items}§f ")
                    .replace("{items}", String.valueOf(totalItems));
        }
        return "";
    }
    public static String default_getBeaconEffect(Block block) {
        if (block.getType() == Material.BEACON) {
            Beacon state = (Beacon) block.getState();
            PotionEffect primaryEffect = state.getPrimaryEffect();
            PotionEffect secondaryEffect = state.getSecondaryEffect();
            if (primaryEffect != null) {
                if (secondaryEffect != null) {
                    return valuesFile.getString("minecraft.beacon_2_effect",
                            " {emojiByEffect1}:{primaryAmplifier} {emojiByEffect2}:{secondaryAmplifier}")
                            .replace("{emojiByEffect1}", getEmojiForEffect(primaryEffect.getType().getName()))
                            .replace("{primaryAmplifier}", String.valueOf(primaryEffect.getAmplifier() + 1))
                            .replace("{emojiByEffect2}", getEmojiForEffect(secondaryEffect.getType().getName()))
                            .replace("{secondaryAmplifier}", String.valueOf(secondaryEffect.getAmplifier() + 1));
                }
                return valuesFile.getString("minecraft.beacon_1_effect", " {emojiByEffect1}:{primaryAmplifier}")
                        .replace("{emojiByEffect1}", getEmojiForEffect(primaryEffect.getType().getName()))
                        .replace("{primaryAmplifier}", String.valueOf(primaryEffect.getAmplifier() + 1));
            }
        }
        return "";
    }
    public static String default_getSpawnerInfo(Block block) {
        if (block.getType() == Material.SPAWNER) {
            Spawner state = (Spawner) block.getState();
            EntityType entity = state.getSpawnedType();
            if (entity != null) {
                return valuesFile.getString("minecraft.spawner_info", " §a\uD83E\uDDDF {spawningEntity}")
                        .replace("{spawningEntity}", entity.name());
            }
        }
        return "";
    }
    public static String default_getNoteblockInfo(Block block) {
        if (block.getType() == Material.NOTE_BLOCK) {
            NoteBlock data = (NoteBlock) block.getBlockData();
            Note note = data.getNote();
            Instrument instrument = data.getInstrument();

            return valuesFile.getString("minecraft.noteblock_info", " §6🎹 {instrument}: {tone} {octave}")
                    .replace("{instrument}", instrument.name())
                    .replace("{tone}", note.getTone().name())
                    .replace("{octave}", String.valueOf(note.getOctave()));
        }
        return "";
    }
    public static String default_getFarmlandHydration(Block block) {
        if (block.getType() == Material.FARMLAND) {
            Farmland data = (Farmland) block.getBlockData();
            int moisture = data.getMoisture();

            if (moisture > 0) {
                return valuesFile.getString("minecraft.farmland_hydrated", " §9\uD83D\uDCA7");
            } else {
                return valuesFile.getString("minecraft.farmland_not_hydrated", " §8\uD83D\uDCA7");
            }
        }
        return "";
    }
    public static String default_getToolToBreak(Block block, Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        ToolType prefTool = getPrefferedTool(block.getType());
        Material prefMat;
        switch (prefTool) {
            case HOE -> prefMat = Material.WOODEN_HOE;
            case AXE -> prefMat = Material.WOODEN_AXE;
            case PICKAXE -> prefMat = Material.WOODEN_PICKAXE;
            case SHOVEL -> prefMat = Material.WOODEN_SHOVEL;
            case null, default -> prefMat = Material.AIR;
        }
        if (prefMat == Material.AIR) {
            return "";
        }
        if (ItemGroups.getContainers().contains(block.getType())) {
            if (heldItem.getType().isAir()) {
                return valuesFile.getString("minecraft.preferred_tool_not_has", "§c {emojiByTool} ")
                        .replace("{emojiByTool}", getEmojiForTool(prefMat));
            }
            if (canToolBreakBlock(heldItem.getType(), block.getType())) {
                return valuesFile.getString("minecraft.preferred_tool_has", "§a {emojiByTool} ")
                        .replace("{emojiByTool}", getEmojiForTool(prefMat));
            } else {
                return valuesFile.getString("minecraft.preferred_tool_not_has", "§c {emojiByTool} ")
                        .replace("{emojiByTool}", getEmojiForTool(prefMat));
            }
        } else {
            if (heldItem.getType().isAir()) {
                return valuesFile.getString("minecraft.preferred_tool_not_has", "§c {emojiByTool} ")
                        .replace("{emojiByTool}", getEmojiForTool(prefMat));
            }
            if (canToolBreakBlock(heldItem.getType(), block.getType())) {
                return valuesFile.getString("minecraft.preferred_tool_has", "§a {emojiByTool} ")
                        .replace("{emojiByTool}", getEmojiForTool(prefMat));
            } else {
                return valuesFile.getString("minecraft.preferred_tool_not_has", "§c {emojiByTool} ")
                        .replace("{emojiByTool}", getEmojiForTool(prefMat));
            }
        }
    }
    // Entities
    public static String default_getEntityAgeLeft(Entity entity) {
        if (entity instanceof org.bukkit.entity.Ageable data) {
            int age = data.getAge();
            if (age < 0) {
                int secondsLeft = Math.abs(age) / 20;
                return valuesFile.getString("minecraft.entity_time_to_grow", " §e\uD83D\uDC25 {secondsLeft}s")
                        .replace("{secondsLeft}", String.valueOf(secondsLeft));
            }
        }
        return "";
    }
    public static String default_getEntityOwner(Entity entity) {
        if (ItemGroups.getPets().contains(entity.getType())) {
            Tameable data = (Tameable) entity;
            AnimalTamer owner = data.getOwner();
            if (data.isTamed() && owner != null) {
                return valuesFile.getString("minecraft.entity_owner", "§8{entityOwner} ")
                        .replace("{entityOwner}", owner.getName());
            }
        }
        return "";
    }
    public static String default_getIsLeashed(Entity entity) {
        if (entity instanceof LivingEntity lEntity) {
            if (lEntity.isLeashed()) {
                return valuesFile.getString("minecraft.entity_is_leashed", " §2\uD83D\uDD17");
            }
        }
        return "";
    }
    public static String default_getEntityHealth(Entity entity) {
        if (entity instanceof LivingEntity data) {
            int health = (int) data.getHealth();
            int maxHealth = (int) Objects.requireNonNull(data.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();

            return valuesFile.getString("minecraft.entity_health", " §c❤ {entityHealth}/{entityMaxHealth}")
                    .replace("{entityHealth}", String.valueOf(health))
                    .replace("{entityMaxHealth}", String.valueOf(maxHealth));
        }
        return "";
    }
    public static String default_getVillagerProfession(Entity entity) {
        if (entity.getType() == EntityType.VILLAGER) {
            Villager villager = (Villager) entity;
            Profession profession = villager.getProfession();

            return valuesFile.getString("minecraft.villager_profession", "§8{profession}")
                    .replace("{profession}", getProfessionString(profession));
        }
        return "";
    }
    public static String default_getTNTFuseTime(Entity entity) {
        if (entity.getType() == EntityType.TNT) {
            float tntFuseTime = ((TNTPrimed) entity).getFuseTicks() / 20.0f;
            return valuesFile.getString("minecraft.tnt_fuse_time", " §4\uD83D\uDCA3{fuseTime}")
                    .replace("{fuseTime}", String.valueOf(tntFuseTime));
        }
        return "";
    }
    public static String auraMobs_getEntityLevel(Entity entity) {
        int level = AuraMobsAPI.getMobLevel(entity);
        return valuesFile.getString("auramobs.level", "§8Lv: {entityLevel}")
                .replace("{entityLevel}", String.valueOf(level));
    }
    public static String auraSkills_getPowerLevel(Entity entity) {
        if (entity instanceof Player player) {
            SkillsUser user = AuraSkillsCompat.getSkillsApi().getUser(player.getUniqueId());
            int level = user.getPowerLevel();
            return valuesFile.getString("auraskills.level", "§c💪 {playerLevel} ")
                    .replace("{playerLevel}", String.valueOf(level));
        }
        return "";
    }
    public static String eliteMobs_getHealth(EliteEntity entity) {
        int health = (int) entity.getHealth();
        int maxHealth = (int) entity.getMaxHealth();

        return valuesFile.getString("minecraft.entity_health", " §c❤ {entityHealth}/{entityMaxHealth}")
                .replace("{entityHealth}", String.valueOf(health))
                .replace("{entityMaxHealth}", String.valueOf(maxHealth));
    }
    public static String slimefun_getStoredEnergy(SlimefunItem item, Block block) {
        if (item instanceof EnergyNetComponent energyNetComponent) {
            EnergyNetComponentType energyNetComponentType = energyNetComponent.getEnergyComponentType();
            if (energyNetComponentType == EnergyNetComponentType.CAPACITOR
                    || energyNetComponentType == EnergyNetComponentType.GENERATOR
                    || energyNetComponentType == EnergyNetComponentType.CONSUMER) {
                if (energyNetComponent.getCapacity() != 0) {
                    if (energyNetComponent.getCharge(block.getLocation()) == 0) {
                        return valuesFile.getString("slimefun.stored_energy_0", " §8⚡ {deviceEnergy}/{deviceCapacity}")
                                .replace("{deviceEnergy}", String.valueOf(energyNetComponent.getCharge(block.getLocation())))
                                .replace("{deviceCapacity}", String.valueOf(energyNetComponent.getCapacity()));
                    } else {
                        return valuesFile.getString("slimefun.stored_energy", " §2⚡ {deviceEnergy}/{deviceCapacity}")
                                .replace("{deviceEnergy}", String.valueOf(energyNetComponent.getCharge(block.getLocation())))
                                .replace("{deviceCapacity}", String.valueOf(energyNetComponent.getCapacity()));
                    }
                }
            }
        }
        return "";
    }
    public static String slimefun_getEnergyGen(SlimefunItem item, Block block) {
        if (item instanceof  AGenerator gen) {
            int generation = gen.getEnergyProduction();
            FuelOperation operation = gen.getMachineProcessor().getOperation(block.getLocation());
            StringBuilder info = new StringBuilder();
            if (operation != null) {
                info.append(valuesFile.getString("slimefun.energy_gen_on", " §2⬆ {deviceGen}")
                        .replace("{deviceGen}", String.valueOf(generation)));
            } else {
                info.append(valuesFile.getString("slimefun.energy_gen_off", " §8➖"));
            }
            return info.toString();
        }
        if (item instanceof SolarGenerator gen) {
            int dayEnergy = gen.getDayEnergy();
            int nightEnergy = gen.getNightEnergy();
            return valuesFile.getString("slimefun.solar_energy_gen", " §6☀ {dayEnergy} §7\uD83C\uDF12 {nightEnergy}")
                    .replace("{dayEnergy}", String.valueOf(dayEnergy))
                    .replace("{nightEnergy}", String.valueOf(nightEnergy));
        }
        return "";
    }
    public static String vmmo_getLevel(Player player) {
        PowerProfile profile = ProfileCache.getOrCache(player, PowerProfile.class);
        int level = profile.getLevel();
        return valuesFile.getString("valhallammo.level", "§c💪 {playerLevel}§8 ")
                .replace("{playerLevel}", String.valueOf(level));
    }
    public static String vmmo_getRace(Player player) {
        Race race = RaceManager.getRace(player);
        if (race != null) {
            return valuesFile.getString("valhallammo.race", "{race}")
                    .replace("{race}", race.getDisplayName());
        }
        return "";
    }
    // Utility
    public static String getColorForPercent(float percent) {
        if (percent >= 0 && percent <= 25) {
            return valuesFile.getString("percent_color.25", "§c");
        } else if (percent > 25 && percent <= 50) {
            return valuesFile.getString("percent_color.50", "§e");
        } else if (percent > 50 && percent <= 75) {
            return valuesFile.getString("percent_color.75", "§a");
        } else if (percent > 75) {
            return valuesFile.getString("percent_color.100", "§2");
        }
        return valuesFile.getString("percent_color.default", "§8");
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
}
