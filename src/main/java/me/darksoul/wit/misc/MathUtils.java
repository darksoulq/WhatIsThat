package me.darksoul.wit.misc;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Collection;

public class MathUtils {

    /**
     * Get the entity the player is looking at, within the specified distance.
     *
     * @param player   The player.
     * @param distance The maximum distance to check.
     * @return The entity the player is looking at, or null if none.
     */
    public static Entity isLookingAtEntity(Player player, double distance) {
        RayTraceResult result = player.rayTraceEntities((int) distance, false);
        if (result != null && result.getHitEntity() != null) {
            return result.getHitEntity();
        }
        return null;
    }

    /**
     * Get the block the player is looking at, within the specified distance.
     *
     * @param player   The player.
     * @param distance The maximum distance to check.
     * @return The block the player is looking at, or null if none.
     */
    public static Block getLookingAtBlock(Player player, double distance) {
        RayTraceResult result =  player.rayTraceBlocks(distance);
        if (result != null && result.getHitBlock() != null) {
            return result.getHitBlock();
        }
        return null;
    }

}
