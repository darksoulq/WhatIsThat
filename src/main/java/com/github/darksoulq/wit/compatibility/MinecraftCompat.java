package com.github.darksoulq.wit.compatibility;

import com.github.darksoulq.wit.Information;
import com.github.darksoulq.wit.WITListener;
import com.github.darksoulq.wit.api.API;
import com.github.darksoulq.wit.api.Info;
import com.github.darksoulq.wit.api.ProgressProviders;
import com.github.darksoulq.wit.misc.ItemGroups;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class MinecraftCompat {
    private static final List<Function<Block, Component>> blockSuffix = new ArrayList<>();
    private static final List<Function<Block, Component>> blockPrefix = new ArrayList<>();
    private static final List<Function<Entity, Component>> entityPrefix = new ArrayList<>();
    private static final List<Function<Entity, Component>> entitySuffix = new ArrayList<>();

    private static boolean BREAK_PROGRESS;
    private static boolean TOOL_INFO;
    private static boolean HEALTH_PROGRESS;
    private static boolean DYNAMIC_BLOCK_COLORS;
    private static boolean DYNAMIC_ENTITY_COLORS;
    private static TextColor DEFAULT_BLOCK_COLOR;
    private static TextColor DEFAULT_ENTITY_COLOR;

    private static TreeMap<Integer, TextColor> blockProgressColors = new TreeMap<>();
    private static TreeMap<Integer, TextColor> entityProgressColors = new TreeMap<>();

    public static void setup() {
        entityPrefix.clear();
        blockPrefix.clear();
        entitySuffix.clear();
        blockSuffix.clear();

        YamlConfiguration config = WITListener.getConfig();
        YamlConfiguration values = Information.getValuesFile();

        BREAK_PROGRESS = config.getBoolean("blocks.break-progress", true);
        TOOL_INFO = config.getBoolean("blocks.toolinfo", true);
        HEALTH_PROGRESS = config.getBoolean("entities.health-progress", true);
        DYNAMIC_BLOCK_COLORS = config.getBoolean("blocks.dynamic_progress_colors", false);
        DYNAMIC_ENTITY_COLORS = config.getBoolean("entities.dynamic_progress_colors", true);

        DEFAULT_BLOCK_COLOR = parseColor(values.getString("block_color", "green"));
        DEFAULT_ENTITY_COLOR = parseColor(values.getString("entity_color", "red"));

        TreeMap<Integer, TextColor> newBlockColors = new TreeMap<>();
        ConfigurationSection blockColorsSec = values.getConfigurationSection("block_progress_colors");
        if (blockColorsSec != null) {
            for (String keyStr : blockColorsSec.getKeys(false)) {
                try {
                    newBlockColors.put(Integer.parseInt(keyStr), parseColor(blockColorsSec.getString(keyStr)));
                } catch (NumberFormatException ignored) {}
            }
        }
        blockProgressColors = newBlockColors;

        TreeMap<Integer, TextColor> newEntityColors = new TreeMap<>();
        ConfigurationSection entityColorsSec = values.getConfigurationSection("entity_progress_colors");
        if (entityColorsSec != null) {
            for (String keyStr : entityColorsSec.getKeys(false)) {
                try {
                    newEntityColors.put(Integer.parseInt(keyStr), parseColor(entityColorsSec.getString(keyStr)));
                } catch (NumberFormatException ignored) {}
            }
        }
        entityProgressColors = newEntityColors;

        if (config.getBoolean("blocks.containerinfo", true)) blockPrefix.add(Information::defaultGetTotalItemsInContainer);
        if (config.getBoolean("blocks.redstoneinfo", true)) blockSuffix.add(Information::defaultGetRedstoneInfo);
        if (config.getBoolean("blocks.cropinfo", true)) blockSuffix.add(Information::defaultGetCropAge);
        if (config.getBoolean("blocks.beehiveinfo", true)) blockSuffix.add(Information::defaultGetHoneyLevel);
        if (config.getBoolean("blocks.smeltinfo", true)) blockSuffix.add(Information::defaultGetRemainingSmeltTime);
        if (config.getBoolean("blocks.beaconinfo", true)) blockSuffix.add(Information::defaultGetBeaconEffect);
        if (config.getBoolean("blocks.spawnerinfo", true)) blockSuffix.add(Information::defaultGetSpawnerInfo);
        if (config.getBoolean("blocks.noteblockinfo", true)) blockSuffix.add(Information::defaultGetNoteblockInfo);
        if (config.getBoolean("blocks.farmlandinfo", true)) blockSuffix.add(Information::defaultGetFarmlandHydration);

        if (config.getBoolean("entities.ownerinfo", true)) entityPrefix.add(Information::defaultGetEntityOwner);
        if (config.getBoolean("entities.leashinfo", true)) entityPrefix.add(Information::defaultGetIsLeashed);
        if (config.getBoolean("entities.ageinfo", true)) entitySuffix.add(Information::defaultGetEntityAgeLeft);
        if (config.getBoolean("entities.healthinfo", true)) entitySuffix.add(Information::defaultGetEntityHealth);
        if (config.getBoolean("entities.professioninfo", true)) entitySuffix.add(Information::defaultGetVillagerProfession);
        if (config.getBoolean("entities.tntinfo", true)) entitySuffix.add(Information::defaultGetTNTFuseTime);
        if (config.getBoolean("entities.horseinfo", true)) {
            entitySuffix.add(Information::defaultGetHorseSpeed);
            entitySuffix.add(Information::defaultGetHorseJumpStrength);
        }
    }

    static {
        setup();
    }

    private static TextColor parseColor(String str) {
        if (str == null || str.isEmpty()) return NamedTextColor.WHITE;
        if (str.startsWith("#")) {
            TextColor c = TextColor.fromHexString(str);
            return c != null ? c : NamedTextColor.WHITE;
        }
        TextColor named = NamedTextColor.NAMES.value(str.toLowerCase());
        if (named != null) return named;
        TextColor c = TextColor.fromHexString("#" + str);
        return c != null ? c : NamedTextColor.WHITE;
    }

    private static TextColor getMatchingColor(TreeMap<Integer, TextColor> colorMap, int percentage, TextColor fallback) {
        Map.Entry<Integer, TextColor> entry = colorMap.ceilingEntry(percentage);
        return entry != null ? entry.getValue() : fallback;
    }

    public static boolean handleBlock(Block block, Player player) {
        if (!ItemGroups.getBlacklistedBlocks().contains(block.getType())) {
            Component key = Component.translatable("block.minecraft." + block.getType().toString().toLowerCase());
            Info info = new Info();
            float progress = 0f;

            if (BREAK_PROGRESS) {
                progress = ProgressProviders.getProgress(block, player);
            }

            for (Function<Block, Component> func : blockSuffix) {
                info.addSuffix(func.apply(block));
            }
            if (TOOL_INFO) {
                info.addPrefix(Information.defaultGetToolToBreak(block, player));
            }
            for (Function<Block, Component> func : blockPrefix) {
                info.addPrefix(func.apply(block));
            }
            info.setName(key);

            float displayProgress = 1f - progress;
            TextColor activeColor = DEFAULT_BLOCK_COLOR;
            if (DYNAMIC_BLOCK_COLORS) {
                int pct = Math.round(displayProgress * 100);
                activeColor = getMatchingColor(blockProgressColors, pct, DEFAULT_BLOCK_COLOR);
            }

            API.updateBar(info, displayProgress, activeColor, player);
            return true;
        }
        return false;
    }

    public static boolean handleEntity(Entity entity, Player player) {
        float health = 0;
        if (HEALTH_PROGRESS && entity instanceof LivingEntity le) {
            double maxHealth = le.getAttribute(Attribute.MAX_HEALTH).getValue();
            health = 1 - (float) Math.max(0d, Math.min(1d, le.getHealth() / maxHealth));
        }

        Component key;
        if (entity instanceof Player pl) {
            key = pl.displayName();
        } else if (entity.customName() != null) {
            key = entity.customName();
        } else {
            key = Component.translatable("entity.minecraft." + entity.getType().toString().toLowerCase());
        }

        Info info = new Info();
        for (Function<Entity, Component> func : entitySuffix) {
            info.addSuffix(func.apply(entity));
        }
        for (Function<Entity, Component> func : entityPrefix) {
            info.addPrefix(func.apply(entity));
        }
        info.setName(key);

        float displayProgress = 1f - health;
        TextColor activeColor = DEFAULT_ENTITY_COLOR;
        if (DYNAMIC_ENTITY_COLORS) {
            int pct = Math.round(displayProgress * 100);
            activeColor = getMatchingColor(entityProgressColors, pct, DEFAULT_ENTITY_COLOR);
        }

        API.updateBar(info, displayProgress, activeColor, player);
        return true;
    }
}