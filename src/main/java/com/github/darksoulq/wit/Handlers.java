package com.github.darksoulq.wit;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public class Handlers {
    private static Set<BiFunction<Block, Player, Boolean>> blockHandlers = new HashSet<>();
    private static Set<BiFunction<Entity, Player, Boolean>> entityHandlers = new HashSet<>();
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
    public static Set<BiFunction<Block, Player, Boolean>> getBlockHandlers() {
        return blockHandlers;
    }
    public static Set<BiFunction<Entity, Player, Boolean>> getEntityHandlers() {
        return entityHandlers;
    }
}
