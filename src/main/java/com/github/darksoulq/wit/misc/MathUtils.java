package com.github.darksoulq.wit.misc;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MathUtils {

    public static long getBlockKey(int x, int y, int z) {
        return (((long) x & 0x3FFFFFF) << 38) | (((long) y & 0xFFF)) | (((long) z & 0x3FFFFFF) << 12);
    }

    public static Entity isLookingAtEntity(Player player, double distance) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        Vec3 eye = sp.getEyePosition(1.0F);
        Vec3 view = sp.getViewVector(1.0F);
        Vec3 end = eye.add(view.x * distance, view.y * distance, view.z * distance);

        BlockHitResult blockHit = sp.level().clip(new ClipContext(eye, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, sp));
        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
        }

        AABB aabb = sp.getBoundingBox().expandTowards(end.subtract(eye)).inflate(1.0D, 1.0D, 1.0D);

        EntityHitResult result = ProjectileUtil.getEntityHitResult(sp.level(), sp, eye, end, aabb, e -> !e.isSpectator() && e.isPickable(), 0.3F);

        if (result != null && result.getEntity() != null) {
            Entity hit = result.getEntity().getBukkitEntity();
            return hit.equals(player.getVehicle()) ? null : hit;
        }
        return null;
    }

    public static Block getLookingAtBlock(Player player, double distance) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        Vec3 eye = sp.getEyePosition(1.0F);
        Vec3 view = sp.getViewVector(1.0F);
        Vec3 end = eye.add(view.x * distance, view.y * distance, view.z * distance);

        BlockHitResult result = sp.level().clip(new ClipContext(eye, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, sp));
        if (result.getType() == HitResult.Type.BLOCK) {
            return player.getWorld().getBlockAt(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ());
        }
        return null;
    }
}