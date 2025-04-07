package me.darksoul.wit;

import me.darksoul.wit.compatibility.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class Handlers {
    private static List<BiFunction<Block, Player, Boolean>> blockHandlers = new ArrayList<>();
    private static List<BiFunction<Entity, Player, Boolean>> entityHandlers = new ArrayList<>();
    private static YamlConfiguration config = WITListener.getConfig();

    public static void setup() {
        blockHandlers.clear();
        entityHandlers.clear();
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
