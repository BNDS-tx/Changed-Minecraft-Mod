package net.ltxprogrammer.changed.util;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityUtil {
    public static Player playerOrNull(Entity entity) {
        if (entity instanceof Player player)
            return player;
        return null;
    }

    /**
     * Checks if the entity is a transfurred player's form, and returns the underlying entity if so. Otherwise, returns the entity.
     * @param entity Entity to check
     * @return Underlying entity or original entity
     */
    public static LivingEntity maybeGetUnderlying(LivingEntity entity) {
        if (entity instanceof ChangedEntity changedEntity)
            return changedEntity.maybeGetUnderlying();
        return entity;
    }

    /**
     * Checks if the entity is a transfurred player, and returns the changed entity if so. Otherwise, returns the entity.
     * @param entity Entity to check
     * @return Changed entity or original entity
     */
    public static LivingEntity maybeGetOverlaying(LivingEntity entity) {
        return ProcessTransfur.getPlayerTransfurVariantSafe(playerOrNull(entity))
                .map(instance -> (LivingEntity)instance.getChangedEntity())
                .orElse(entity);
    }

    public static float getFrictionOnBlock(@NotNull Entity entity) {
        return getFrictionOnBlock(entity.level, new BlockPos(entity.getX(), entity.getBoundingBox().minY - 0.5000001D, entity.getZ()), entity);
    }

    public static float getFrictionOnBlock(LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return getFrictionOnBlock(level.getBlockState(pos), level, pos, entity);
    }

    public static float getFrictionOnBlock(BlockState instance, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        final float originalFriction = instance.getFriction(level, pos, entity);

        if (instance.is(BlockTags.ICE) && entity instanceof LivingEntity livingEntity) {
            return ProcessTransfur.getEntityVariant(livingEntity).map(variant -> {
                if (livingEntity.getAttributeValue(Attributes.MOVEMENT_SPEED) > 0.1f) // TODO replace with variant builder property for affected by friction
                    return 0.6f;
                else
                    return originalFriction;
            }).orElse(originalFriction);
        }

        return originalFriction;
    }
}
