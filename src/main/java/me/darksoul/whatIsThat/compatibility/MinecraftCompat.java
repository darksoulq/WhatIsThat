package me.darksoul.whatIsThat.compatibility;

import me.darksoul.whatIsThat.Information;
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
        for (Function<Block, String> func : Information.getSuffixVBlocks()) {
            vBlockSInfo.append(func.apply(block));
        }
        for (Function<Block, String> func : Information.getPrefixVBlocks()) {
            vBlockPInfo.append(func.apply(block));
        }
        info.append(vBlockPInfo).append(blockName).append(vBlockSInfo);
        WAILAManager.updateBossBar(player, info.toString());
    }

    public static void handleMinecraftEntityDisplay(Entity entity, Player player) {
        for (EntityType not : ItemGroups.getNotRender()) {
            if (entity.getType() != not) {
                String entityName = vanillaEntitiesLang.getString("entity." + entity.getType(), entity.getName());
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
                info.append(vEntityPInfo).append(entityName).append(vEntitySInfo);
                WAILAManager.updateBossBar(player, info.toString());
                return;
            }
        }
    }

    public static YamlConfiguration getVanillaEntitiesLang() {
        return vanillaEntitiesLang;
    }
}
