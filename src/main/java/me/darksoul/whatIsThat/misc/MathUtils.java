package me.darksoul.whatIsThat.misc;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
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
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection().normalize();

        for (double i = 0; i <= distance; i += 0.1) {
            Location checkLocation = eyeLocation.clone().add(direction.clone().multiply(i));
            Collection<Entity> nearbyEntities = checkLocation.getWorld().getNearbyEntities(checkLocation, 0.5, 0.5, 0.5);

            for (Entity entity : nearbyEntities) {
                if (entity != player && entity.getBoundingBox().contains(checkLocation.toVector())) {
                    return entity;
                }
            }
        }

        return null;
    }

    public static Block getLookingAtBlock(Player player, double distance) {
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection().normalize();

        for (double i = 0; i <= distance; i += 0.1) {
            Location checkLocation = eyeLocation.clone().add(direction.clone().multiply(i));
            Block block = checkLocation.getBlock();

            if (block.isEmpty()) continue;

            BoundingBox blockBoundingBox = getBlockBoundingBox(block);

            if (blockBoundingBox != null && blockBoundingBox.contains(checkLocation.toVector())) {
                return block;
            }
        }

        return null;
    }

    /**
     * Get the precise bounding box of a block.
     *
     * @param block The block.
     * @return The block's bounding box, or null if it has no collision.
     */
    private static BoundingBox getBlockBoundingBox(Block block) {
        BlockData blockData = block.getBlockData();
        BoundingBox box = block.getBoundingBox();
        if (box != null) {
            return box;
        }

        return BoundingBox.of(block);
    }

}
