package me.darksoul.whatIsThat;

import me.darksoul.whatIsThat.compatibility.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Handlers {
    private static List<BiFunction<Block, Player, Boolean>> blockHandlers = new ArrayList<>();
    private static List<BiFunction<Entity, Player, Boolean>> entityHandlers = new ArrayList<>();
    private static YamlConfiguration config = WAILAListener.getConfig();

    public static void setup() {
        // Entities Handlers
        if (config.getBoolean("valhallammo.enabled", true) && ValhallaMMOCompat.getIsVMMOInstalled()) {
            entityHandlers.add(ValhallaMMOCompat::handleVMMOEntity);
        }
        if (config.getBoolean("itemsadder.entities.enabled", true) && ItemsAdderCompat.getIsIAInstalled()) {
            entityHandlers.add(ItemsAdderCompat::handleIAEntity);
        }
        if (config.getBoolean("elitemobs.enabled", true) && EliteMobsCompat.isEMInstalled()) {
            entityHandlers.add(EliteMobsCompat::handleEMEntity);
        }
        if (config.getBoolean("auramobs.enabled", true) && AuraMobsCompat.getIsAuraMobsInstalled()) {
            entityHandlers.add(AuraMobsCompat::handleAuraMobs);
        }
        // Block Handlers
        if (config.getBoolean("minetorio.enabled", true) && MinetorioCompat.getIsMTInstalled()) {
            blockHandlers.add(MinetorioCompat::handleMTDisplay);
        }
        if (config.getBoolean("itemsadder.blocks.enabled", true) && ItemsAdderCompat.getIsIAInstalled()) {
            blockHandlers.add(ItemsAdderCompat::handleIABlocks);
        }
    }

    public static List<BiFunction<Block, Player, Boolean>> getBlockHandlers() {
        return blockHandlers;
    }
    public static List<BiFunction<Entity, Player, Boolean>> getEntityHandlers() {
        return entityHandlers;
    }
}
