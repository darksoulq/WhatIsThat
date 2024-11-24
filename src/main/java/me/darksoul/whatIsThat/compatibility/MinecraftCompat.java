package me.darksoul.whatIsThat.compatibility;

import me.darksoul.whatIsThat.Informations;
import me.darksoul.whatIsThat.WAILAManager;
import me.darksoul.whatIsThat.misc.ConfigUtils;
import me.darksoul.whatIsThat.misc.ItemGroups;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class MinecraftCompat {
    private static final YamlConfiguration vanillaLang = ConfigUtils.loadVanillaBlocksLang();
    private static final YamlConfiguration vanillaEntitiesLang = ConfigUtils.loadVanillaEntitiesLang();

    public static void handleMinecraftBlockDisplay(Block block, Player player) {
        String blockName = vanillaLang.getString("block." + block.getType().name(), block.getType().name());
        StringBuilder vBlockSInfo = new StringBuilder();
        StringBuilder vBlockPInfo = new StringBuilder();
        StringBuilder info = new StringBuilder();
        for (Function<Block, String> func : Informations.getSuffixVBlocks()) {
            vBlockSInfo.append(func.apply(block));
        }
        for (Function<Block, String> func : Informations.getPrefixVBlocks()) {
            vBlockPInfo.append(func.apply(block));
        }
        info.append(vBlockPInfo).append(blockName).append(vBlockSInfo);
        WAILAManager.updateBossBar(player, info.toString());
    }
    public static void handleMinecraftEntityDisplay(Entity entity, EntityType type, Player player) {
        for (EntityType not : ItemGroups.getNotRender()) {
            if (type != not) {
                String entityName = "";
                if (entity.customName() != null) {
                    entityName = entity.customName().toString();
                } else {
                    entityName = vanillaEntitiesLang.getString("entity." + entity.getType(), entity.name().toString());
                }
                StringBuilder vEntitySInfo = new StringBuilder();
                StringBuilder vEntityPInfo = new StringBuilder();
                StringBuilder info = new StringBuilder();
                for (Function<Entity, String> func : Informations.getSuffixVEntity()) {
                    vEntitySInfo.append(func.apply(entity));
                }
                for (Function<Entity, String> func : Informations.getPrefixVEntity()) {
                    vEntityPInfo.append(func.apply(entity));
                }
                info.append(vEntityPInfo).append(entityName).append(vEntitySInfo);
                WAILAManager.updateBossBar(player, info.toString());
                return;
            }
        }
    }
}
