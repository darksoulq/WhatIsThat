package me.darksoul.whatIsThat.compatibility;

import me.darksoul.whatIsThat.Information;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.display.WAILAManager;
import me.darksoul.whatIsThat.misc.ConfigUtils;
import me.darksoul.whatIsThat.misc.ItemGroups;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MinecraftCompat {
    // Blocks
    private static final List<Function<Block, String>> suffixVBlocks = new ArrayList<>();
    private static final List<Function<Block, String>> prefixVBlocks = new ArrayList<>();
    // Entities
    private static final List<Function<Entity, String>> prefixVEntity = new ArrayList<>();
    private static final List<Function<Entity, String>> suffixVEntity = new ArrayList<>();

    public static void setup() {
        // Clean Up
        prefixVEntity.clear();
        prefixVBlocks.clear();
        suffixVEntity.clear();
        suffixVBlocks.clear();
        // Blocks

        if (WAILAListener.getConfig().getBoolean("blocks.containerinfo", true)) {
            prefixVBlocks.add(Information::default_getTotalItemsInContainer);
        }
        if (WAILAListener.getConfig().getBoolean("blocks.redstoneinfo", true)) {
            suffixVBlocks.add(Information::default_getRedstoneInfo);
        }
        if (WAILAListener.getConfig().getBoolean("blocks.cropinfo", true)) {
            suffixVBlocks.add(Information::default_getCropAge);
        }
        if (WAILAListener.getConfig().getBoolean("blocks.beehiveinfo", true)) {
            suffixVBlocks.add(Information::default_getHoneyLevel);
        }
        if (WAILAListener.getConfig().getBoolean("blocks.smeltinfo", true)) {
            suffixVBlocks.add(Information::default_getRemainingSmeltTime);
        }
        if (WAILAListener.getConfig().getBoolean("blocks.beaconinfo", true)) {
            suffixVBlocks.add(Information::default_getBeaconEffect);
        }
        if (WAILAListener.getConfig().getBoolean("blocks.spawnerinfo", true)) {
            suffixVBlocks.add(Information::default_getSpawnerInfo);
        }
        if (WAILAListener.getConfig().getBoolean("blocks.noteblockinfo", true)) {
            suffixVBlocks.add(Information::default_getNoteblockInfo);
        }
        if (WAILAListener.getConfig().getBoolean("blocks.farmlandinfo", true)) {
            suffixVBlocks.add(Information::default_getFarmlandHydration);
        }
        // Entities
        if (WAILAListener.getConfig().getBoolean("entities.ownerinfo", true)) {
            prefixVEntity.add(Information::default_getEntityOwner);
        }
        if (WAILAListener.getConfig().getBoolean("entities.leashinfo", true)) {
            prefixVEntity.add(Information::default_getIsLeashed);
        }
        if (WAILAListener.getConfig().getBoolean("entities.ageinfo", true)) {
            suffixVEntity.add(Information::default_getEntityAgeLeft);
        }
        if (WAILAListener.getConfig().getBoolean("entities.healthinfo", true)) {
            suffixVEntity.add(Information::default_getEntityHealth);
        }
        if (WAILAListener.getConfig().getBoolean("entities.professioninfo", true)) {
            suffixVEntity.add(Information::default_getVillagerProfession);
        }
        if (WAILAListener.getConfig().getBoolean("entities.tntinfo", true)) {
            suffixVEntity.add(Information::default_getTNTFuseTime);
        }
    }

    static {
        setup();
    }

    public static boolean handleBlock(Block block, Player player) {
        if (!ItemGroups.getOperatorBlocks().contains(block.getType())) {
            String key = "block.minecraft." + block.getType().toString().toLowerCase();
            String blockName = ConfigUtils.getTranslatedName(key);
            StringBuilder vBlockSInfo = new StringBuilder();
            StringBuilder vBlockPInfo = new StringBuilder();
            StringBuilder info = new StringBuilder();
            for (Function<Block, String> func : suffixVBlocks) {
                vBlockSInfo.append(func.apply(block));
            }
            if (WAILAListener.getConfig().getBoolean("block.toolinfo", true)) {
                vBlockPInfo.append(Information.default_getToolToBreak(block, player));
            }
            for (Function<Block, String> func : prefixVBlocks) {
                vBlockPInfo.append(func.apply(block));
            }
            if (!vBlockPInfo.isEmpty()) {
                info.append(vBlockPInfo).append(Information.getValuesFile().getString("SPLITTER", " §f| "));
            }
            info.append(blockName);
            if (!vBlockSInfo.isEmpty()) {
                info.append(Information.getValuesFile().getString("SPLITTER", " §f| ")).append(vBlockSInfo);
            }
            WAILAListener.setLookingAt(player, blockName);
            WAILAListener.setLookingAtPrefix(player, vBlockPInfo.toString());
            WAILAListener.setLookingAtSuffix(player, vBlockSInfo.toString());
            WAILAListener.setLookingAtInfo(player, info.toString());
            WAILAManager.setBar(player, info.toString());
            return true;
        }
        return false;
    }
    public static boolean handleEntity(Entity entity, Player player) {
        for (EntityType not : ItemGroups.getNotRenderEntities()) {
            if (entity.getType() != not) {
                String key = "entity.minecraft." + entity.getType().toString().toLowerCase();
                String entityName = ConfigUtils.getTranslatedName(key);
                if (entity.getCustomName() != null) {
                    entityName = entity.getCustomName();
                }
                StringBuilder vEntitySInfo = new StringBuilder();
                StringBuilder vEntityPInfo = new StringBuilder();
                StringBuilder info = new StringBuilder();
                for (Function<Entity, String> func : suffixVEntity) {
                    vEntitySInfo.append(func.apply(entity));
                }
                for (Function<Entity, String> func : prefixVEntity) {
                    vEntityPInfo.append(func.apply(entity));
                }
                if (!vEntityPInfo.isEmpty()) {
                    info.append(vEntityPInfo).append(Information.getValuesFile().getString("SPLITTER", " §f| "));
                }
                info.append(entityName);
                if (!vEntitySInfo.isEmpty()) {
                    info.append(Information.getValuesFile().getString("SPLITTER", " §f| ")).append(vEntitySInfo);
                }
                WAILAListener.setLookingAt(player, entityName);
                WAILAListener.setLookingAtPrefix(player, vEntityPInfo.toString());
                WAILAListener.setLookingAtSuffix(player, vEntitySInfo.toString());
                WAILAManager.setBar(player, info.toString());
                return true;
            }
        }
        return false;
    }

    public static List<Function<Entity, String>> getPrefixVEntity() {
        return prefixVEntity;
    }
}
