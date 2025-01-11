package me.darksoul.whatIsThat.compatibility;

import me.darksoul.whatIsThat.Information;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.misc.ConfigUtils;
import me.darksoul.whatIsThat.misc.ItemGroups;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class MinecraftCompat {

    public static boolean handleMinecraftBlockDisplay(Block block, Player player) {
        String key = "block.minecraft." + block.getType().toString().toLowerCase();
        String blockName = ConfigUtils.getTranslatedVName(key);
        StringBuilder vBlockSInfo = new StringBuilder();
        StringBuilder vBlockPInfo = new StringBuilder();
        StringBuilder info = new StringBuilder();
        for (Function<Block, String> func : Information.getSuffixVBlocks()) {
            vBlockSInfo.append(func.apply(block));
        }
        if (WAILAListener.getConfig().getBoolean("block.toolinfo", true)) {
            vBlockPInfo.append(Information.getToolToBreak(block, player));
        }
        for (Function<Block, String> func : Information.getPrefixVBlocks()) {
            vBlockPInfo.append(func.apply(block));
        }
        if (!vBlockPInfo.isEmpty()) {
            info.append(vBlockPInfo).append(" §f| ");
        }
        info.append(blockName);
        if (!vBlockSInfo.isEmpty()) {
            info.append(" §f| ").append(vBlockSInfo);
        }
        WAILAListener.setLookingAt(player, blockName);
        WAILAListener.setLookingAtPrefix(player, vBlockPInfo.toString());
        WAILAListener.setLookingAtSuffix(player, vBlockSInfo.toString());
        WAILAListener.setLookingAtInfo(player, info.toString());
        WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                info.toString());
        return true;
    }
    public static boolean handleMinecraftEntityDisplay(Entity entity, Player player) {
        for (EntityType not : ItemGroups.getNotRenderEntities()) {
            if (entity.getType() != not) {
                String key = "entity.minecraft." + entity.getType().toString().toLowerCase();
                String entityName = ConfigUtils.getTranslatedVName(key);
                if (entity.getCustomName() != null) {
                    entityName = entity.getCustomName();
                }
                StringBuilder vEntitySInfo = new StringBuilder();
                StringBuilder vEntityPInfo = new StringBuilder();
                StringBuilder info = new StringBuilder();
                for (Function<Entity, String> func : Information.getSuffixVEntity()) {
                    vEntitySInfo.append(func.apply(entity));
                }
                for (Function<Entity, String> func : Information.getPrefixVEntity()) {
                    vEntityPInfo.append(func.apply(entity));
                }
                if (!vEntityPInfo.isEmpty()) {
                    info.append(vEntityPInfo).append(" §f| ");
                }
                info.append(entityName);
                if (!vEntitySInfo.isEmpty()) {
                    info.append(" §f| ").append(vEntitySInfo);
                }
                WAILAListener.setLookingAt(player, entityName);
                WAILAListener.setLookingAtPrefix(player, vEntityPInfo.toString());
                WAILAListener.setLookingAtSuffix(player, vEntitySInfo.toString());
                WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"),
                        info.toString());
                return true;
            }
        }
        return false;
    }
}
