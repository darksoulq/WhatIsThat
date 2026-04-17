package com.github.darksoulq.wit.compatibility;

import com.github.darksoulq.wit.Information;
import com.github.darksoulq.wit.WITListener;
import com.github.darksoulq.wit.api.API;
import com.github.darksoulq.wit.api.Info;
import com.github.darksoulq.wit.misc.Events;
import com.github.darksoulq.wit.misc.ItemGroups;
import com.github.darksoulq.wit.misc.MathUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MinecraftCompat {
    private static final List<Function<Block, Component>> blockSuffix = new ArrayList<>();
    private static final List<Function<Block, Component>> blockPrefix = new ArrayList<>();
    private static final List<Function<Entity, Component>> entityPrefix = new ArrayList<>();
    private static final List<Function<Entity, Component>> entitySuffix = new ArrayList<>();

    private static boolean BREAK_PROGRESS;
    private static boolean TOOL_INFO;
    private static boolean HEALTH_PROGRESS;

    public static void setup() {
        entityPrefix.clear();
        blockPrefix.clear();
        entitySuffix.clear();
        blockSuffix.clear();

        YamlConfiguration config = WITListener.getConfig();
        BREAK_PROGRESS = config.getBoolean("break-progress", true);
        TOOL_INFO = config.getBoolean("block.toolinfo", true);
        HEALTH_PROGRESS = config.getBoolean("health-progress", true);

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

    public static boolean handleBlock(Block block, Player player) {
        if (!ItemGroups.getBlacklistedBlocks().contains(block.getType())) {
            Component key = Component.translatable("block.minecraft." + block.getType().toString().toLowerCase());
            Info info = new Info();
            float progress = 0;

            if (BREAK_PROGRESS) {
                Float p = Events.BREAK_PROGRESS.get(MathUtils.getBlockKey(block.getX(), block.getY(), block.getZ()));
                if (p != null) progress = p;
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
            API.updateBar(info, 1 - progress, player);
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
        API.updateBar(info, 1 - health, player);
        return true;
    }
}