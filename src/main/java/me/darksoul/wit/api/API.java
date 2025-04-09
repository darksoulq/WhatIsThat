package me.darksoul.wit.api;

import me.darksoul.wit.Handlers;
import me.darksoul.wit.display.InfoDisplay;
import me.darksoul.wit.display.WAILAManager;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * The WITAPI class provides methods to extend the functionality of the "What Is That" plugin.
 * It allows adding custom handlers for entities and blocks and updating the boss bar text for players.
 */
public class API {
    private static final List<WITAddon> reloadListeners = new ArrayList<>();

    /**
     * Registers a {@link WITAddon} to receive callbacks when the WIT plugin is reloaded.
     * This allows addons to re-register their handlers or reset internal state after a reload.
     *
     * @param addon the {@link WITAddon} instance to register.
     */
    public static void registerAddon(WITAddon addon) {
        reloadListeners.add(addon);
    }

    /**
     * Unregisters a previously registered {@link WITAddon}, stopping it from receiving reload callbacks.
     * This should be called during addon shutdown or disable.
     *
     * @param addon the {@link WITAddon} instance to unregister.
     */
    public static void unregisterAddon(WITAddon addon) {
        reloadListeners.remove(addon);
    }

    /**
     * Fires the reload event, notifying all registered {@link WITAddon} instances
     * that the WIT plugin has been reloaded. This is  called internally by
     * the plugin's reload command.
     */
    public static void fireReload() {
        for (WITAddon addon : reloadListeners) {
            addon.onWITReload();
        }
    }

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
    public static void updateBar(Component text, Player player) {
        WAILAManager.setBar(player, text);
    }

    /**
     * Adds a custom display to the plugin.
     * The display is an instance of {@link InfoDisplay} that provides information to be displayed.
     *
     * @param display the custom display to add to the plugin.
     */
    public static void addDisplay(InfoDisplay display) {
        WAILAManager.addDisplay(display);
    }
}
