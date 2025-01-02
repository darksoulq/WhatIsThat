package me.darksoul.whatIsThat.api;

import me.darksoul.whatIsThat.Handlers;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WAILAManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

/**
 * The WITAPI class provides methods to extend the functionality of the "What Is That" plugin.
 * It allows adding custom handlers for entities and blocks and updating the boss bar text for players.
 */
public class WITAPI {

    /**
     * Adds a custom handler for entities.
     * The handler is a {@link BiFunction} that takes an {@link Entity} and a {@link Player},
     * and returns a {@code Boolean} to indicate whether the handling was successful.
     *
     * @param function the handler function to process entities.
     */
    public static void addEntityHandler(BiFunction<Entity, Player, Boolean> function) {
        Handlers.getEntityHandlers().add(function);
    }

    /**
     * Adds a custom handler for blocks.
     * The handler is a {@link BiFunction} that takes a {@link Block} and a {@link Player},
     * and returns a {@code Boolean} to indicate whether the handling was successful.
     *
     * @param function the handler function to process blocks.
     */
    public static void addBlockHandler(BiFunction<Block, Player, Boolean> function) {
        Handlers.getBlockHandlers().add(function);
    }

    /**
     * Removes a custom handler for blocks.
     * The handler is a {@link BiFunction} that takes a {@link Block} and a {@link Player},
     * and returns a {@code Boolean} to indicate whether the handling was successful.
     *
     * @param function the handler function to remove from processing blocks.
     */
    public static void removeBlockHandler(BiFunction<Block, Player, Boolean> function) {
        Handlers.removeBlockHandler(function);
    }

    /**
     * Removes a custom handler for entities.
     * The handler is a {@link BiFunction} that takes an {@link Entity} and a {@link Player},
     * and returns a {@code Boolean} to indicate whether the handling was successful.
     *
     * @param function the handler function to remove from processing entities.
     */
    public static void removeEntityHandler(BiFunction<Entity, Player, Boolean> function) {
        Handlers.removeEntityHandler(function);
    }

    /**
     * Updates the boss bar text displayed to a specific player.
     *
     * @param text   the new text to display on the boss bar.
     * @param player the {@link Player} for whom the boss bar text is updated.
     */
    public static void updateBar(String text, Player player) {
        WAILAManager.setBar(player, WAILAListener.getPlayerConfig(player).getString("type"), text);
    }
}
