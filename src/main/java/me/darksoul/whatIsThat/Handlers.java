package me.darksoul.whatIsThat;

import me.darksoul.whatIsThat.compatibility.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class Handlers {
    private static List<BiFunction<Block, Player, Boolean>> blockHandlers = new ArrayList<>();
    private static List<BiFunction<Entity, Player, Boolean>> entityHandlers = new ArrayList<>();
    private static YamlConfiguration config = WAILAListener.getConfig();

    public static void setup() {
        blockHandlers.clear();
        entityHandlers.clear();
        //Litefarm
        if (config.getBoolean("litefarm.enabled", true) && LiteFarmCompat.getIsInstalled()) {
            blockHandlers.add(LiteFarmCompat::handleBlock);
        }
        // Entities Handlers
        if (config.getBoolean("valhallammo.enabled", true) && ValhallaMMOCompat.getIsInstalled()) {
            entityHandlers.add(ValhallaMMOCompat::handleEntity);
        }
        if (config.getBoolean("itemsadder.entities.enabled", true) && ItemsAdderCompat.getIsInstalled()) {
            entityHandlers.add(ItemsAdderCompat::handleEntity);
        }
        if (config.getBoolean("elitemobs.enabled", true) && EliteMobsCompat.getIsInstalled()) {
            entityHandlers.add(EliteMobsCompat::handleEntity);
        }
        if (config.getBoolean("auramobs.enabled", true) && AuraMobsCompat.getIsInstalled()) {
            entityHandlers.add(AuraMobsCompat::handleEntity);
        }
        if (config.getBoolean("nexo.entities.enabled", true) && NexoCompat.getIsInstalled()) {
            entityHandlers.add(NexoCompat::handleEntity);
        }
        // Block Handlers
        if (config.getBoolean("minetorio.enabled", true) && MinetorioCompat.getIsInstalled()) {
            blockHandlers.add(MinetorioCompat::handleBlock);
        }
        if (config.getBoolean("itemsadder.blocks.enabled", true) && ItemsAdderCompat.getIsInstalled()) {
            blockHandlers.add(ItemsAdderCompat::handleBlock);
        }
        if (config.getBoolean("slimefun.enabled", true) && SlimefunCompat.getIsInstalled()) {
            blockHandlers.add(SlimefunCompat::handleBlock);
        }
        if (config.getBoolean("nexo.blocks.enabled", true) && NexoCompat.getIsInstalled()) {
            blockHandlers.add(NexoCompat::handleBlock);
        }
    }

    public static void removeBlockHandler(BiFunction<Block, Player, Boolean> handler) {
        blockHandlers.remove(handler);
    }
    public static void removeEntityHandler(BiFunction<Entity, Player, Boolean> handler) {
        entityHandlers.remove(handler);
    }
    public static List<BiFunction<Block, Player, Boolean>> getBlockHandlers() {
        return blockHandlers;
    }
    public static List<BiFunction<Entity, Player, Boolean>> getEntityHandlers() {
        return entityHandlers;
    }
}
