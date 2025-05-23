package me.darksoul.wit.compatibility;

import me.darksoul.wit.Information;
import me.darksoul.wit.WITListener;
import me.darksoul.wit.api.API;
import me.darksoul.wit.api.Info;
import me.darksoul.wit.misc.Events;
import me.darksoul.wit.misc.ItemGroups;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static me.darksoul.wit.Information.mm;

public class MinecraftCompat {
    // Blocks
    private static final List<Function<Block, Component>> blockSuffix = new ArrayList<>();
    private static final List<Function<Block, Component>> blockPrefix = new ArrayList<>();
    // Entities
    private static final List<Function<Entity, Component>> entityPrefix = new ArrayList<>();
    private static final List<Function<Entity, Component>> entitySuffix = new ArrayList<>();

    public static void setup() {
        // Clean Up
        entityPrefix.clear();
        blockPrefix.clear();
        entitySuffix.clear();
        blockSuffix.clear();
        // Blocks

        if (WITListener.getConfig().getBoolean("blocks.containerinfo", true)) {
            blockPrefix.add(Information::default_getTotalItemsInContainer);
        }
        if (WITListener.getConfig().getBoolean("blocks.redstoneinfo", true)) {
            blockSuffix.add(Information::default_getRedstoneInfo);
        }
        if (WITListener.getConfig().getBoolean("blocks.cropinfo", true)) {
            blockSuffix.add(Information::default_getCropAge);
        }
        if (WITListener.getConfig().getBoolean("blocks.beehiveinfo", true)) {
            blockSuffix.add(Information::default_getHoneyLevel);
        }
        if (WITListener.getConfig().getBoolean("blocks.smeltinfo", true)) {
            blockSuffix.add(Information::default_getRemainingSmeltTime);
        }
        if (WITListener.getConfig().getBoolean("blocks.beaconinfo", true)) {
            blockSuffix.add(Information::default_getBeaconEffect);
        }
        if (WITListener.getConfig().getBoolean("blocks.spawnerinfo", true)) {
            blockSuffix.add(Information::default_getSpawnerInfo);
        }
        if (WITListener.getConfig().getBoolean("blocks.noteblockinfo", true)) {
            blockSuffix.add(Information::default_getNoteblockInfo);
        }
        if (WITListener.getConfig().getBoolean("blocks.farmlandinfo", true)) {
            blockSuffix.add(Information::default_getFarmlandHydration);
        }
        // Entities
        if (WITListener.getConfig().getBoolean("entities.ownerinfo", true)) {
            entityPrefix.add(Information::default_getEntityOwner);
        }
        if (WITListener.getConfig().getBoolean("entities.leashinfo", true)) {
            entityPrefix.add(Information::default_getIsLeashed);
        }
        if (WITListener.getConfig().getBoolean("entities.ageinfo", true)) {
            entitySuffix.add(Information::default_getEntityAgeLeft);
        }
        if (WITListener.getConfig().getBoolean("entities.healthinfo", true)) {
            entitySuffix.add(Information::default_getEntityHealth);
        }
        if (WITListener.getConfig().getBoolean("entities.professioninfo", true)) {
            entitySuffix.add(Information::default_getVillagerProfession);
        }
        if (WITListener.getConfig().getBoolean("entities.tntinfo", true)) {
            entitySuffix.add(Information::default_getTNTFuseTime);
        }
    }

    static {
        setup();
    }

    public static boolean handleBlock(Block block, Player player) {
        if (!ItemGroups.getBlACKLISTED_BLOCKS().contains(block.getType())) {
            Component key = Component.translatable("block.minecraft." + block.getType().toString().toLowerCase());
            Info info = new Info();
            float progress = 0;
            if (WITListener.getConfig().getBoolean("break-progress", true)) {
                if (Events.progressMap.containsKey(block)) {
                    progress = Events.progressMap.get(block);
                }
            }

            for (Function<Block, Component> func : blockSuffix) {
                 info.addSuffix(func.apply(block));
            }
            if (WITListener.getConfig().getBoolean("block.toolinfo", true)) {
                 info.addPrefix(Information.default_getToolToBreak(block, player));
            }
            for (Function<Block, Component> func : blockPrefix) {
                info.addPrefix(func.apply(block));
            }
            if (!((TextComponent) info.getPrefix()).content().isEmpty()) {
                info.addPrefix(mm.deserialize(Information.getValuesFile().getString("SPLITTER", " §f| ")));
            }
            info.setName(key);
            if (!((TextComponent) info.getSuffix()).content().isEmpty()) {
                info.suffixSplit(mm.deserialize(Information.getValuesFile().getString("SPLITTER", " §f| ")));
            }
            API.updateBar(info, 1 - progress, player);
            return true;
        }
        return false;
    }
    public static boolean handleEntity(Entity entity, Player player) {
        for (EntityType not : ItemGroups.getBlacklistedEntities()) {
            if (entity.getType() != not) {
                float health = 0;
                if (WITListener.getConfig().getBoolean("health-progress", true)) {
                    if (entity instanceof LivingEntity) {
                        health = (float) Math.max(0f, Math.min(1f, ((LivingEntity) entity).getHealth() /
                                ((LivingEntity) entity).getAttribute(Attribute.MAX_HEALTH).getValue()));
                    }
                }
                Component key = Component.translatable("entity.minecraft." + entity.getType().toString().toLowerCase());
                if (entity.customName() != null) {
                    key = entity.customName();
                }
                Info info = new Info();
                for (Function<Entity, Component> func : entitySuffix) {
                    info.addSuffix(func.apply(entity));
                }
                for (Function<Entity, Component> func : entityPrefix) {
                    info.addPrefix(func.apply(entity));
                }
                if (!((TextComponent) info.getPrefix()).content().isEmpty()) {
                    info.addPrefix(Component.text(Information.getValuesFile().getString("SPLITTER", " §f| ")));
                }
                info.setName(key);
                if (!((TextComponent) info.getSuffix()).content().isEmpty()) {
                    info.suffixSplit(Component.text(Information.getValuesFile().getString("SPLITTER", " §f| ")));
                }
                API.updateBar(info, 1 - health, player);
                return true;
            }
        }
        return false;
    }
}
