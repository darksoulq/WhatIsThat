package com.github.darksoulq.wit.misc;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

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

        if (result != null) {
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

    public static float getToolMultiplier(ItemStack item, Block block) {
        Material type = item.getType();
        Material blockType = block.getType();
        String name = type.name();

        if (name.endsWith("_SWORD")) return blockType == Material.COBWEB ? 15f : 1.5f;
        if (name.endsWith("_SHEARS")) return (blockType.name().contains("LEAVES") || blockType.name().contains("WOOL")) ? 5f : 15f;
        if (name.contains("NETHERITE")) return 9f;
        if (name.contains("DIAMOND")) return 8f;
        if (name.contains("IRON")) return 6f;
        if (name.contains("STONE")) return 4f;
        if (name.contains("WOODEN") || name.contains("GOLDEN")) return 2f;

        return 1f;
    }

    public static long calculateBreakTimeMs(Block block, Player player) {
        float hardness = block.getType().getHardness();
        if (hardness < 0) return -1;
        if (hardness == 0) return 0;

        ItemStack tool = player.getInventory().getItemInMainHand();
        boolean canHarvest = block.isPreferredTool(tool);

        float speedMultiplier = 1f;
        if (canHarvest || tool.getType().name().contains("SHEARS") || tool.getType().name().contains("SWORD")) {
            speedMultiplier = getToolMultiplier(tool, block);
        }

        if (canHarvest && tool.containsEnchantment(Enchantment.EFFICIENCY)) {
            int effLevel = tool.getEnchantmentLevel(Enchantment.EFFICIENCY);
            speedMultiplier += (effLevel * effLevel + 1);
        }

        if (player.hasPotionEffect(PotionEffectType.HASTE)) {
            speedMultiplier *= (1f + (0.2f * (player.getPotionEffect(PotionEffectType.HASTE).getAmplifier() + 1)));
        }

        if (player.hasPotionEffect(PotionEffectType.CONDUIT_POWER)) {
            speedMultiplier *= (1f + (0.2f * (player.getPotionEffect(PotionEffectType.CONDUIT_POWER).getAmplifier() + 1)));
        }

        if (player.hasPotionEffect(PotionEffectType.MINING_FATIGUE)) {
            int fatigueLevel = player.getPotionEffect(PotionEffectType.MINING_FATIGUE).getAmplifier();
            speedMultiplier *= (float) Math.pow(0.3, fatigueLevel + 1);
        }

        if (player.isInWater() && !tool.containsEnchantment(Enchantment.AQUA_AFFINITY)) {
            ItemStack helmet = player.getInventory().getHelmet();
            if (helmet == null || !helmet.containsEnchantment(Enchantment.AQUA_AFFINITY)) {
                speedMultiplier /= 5f;
            }
        }

        if (!player.isOnGround()) {
            speedMultiplier /= 5f;
        }

        float damage = speedMultiplier / hardness / (canHarvest ? 30f : 100f);
        if (damage > 1f) return 0;

        int ticks = (int) Math.ceil(1f / damage);
        return ticks * 50L;
    }
}