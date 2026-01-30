package net.ltxprogrammer.changed.aaBackport;

import net.ltxprogrammer.changed.mixin.aaBackport.EntityBackport;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BiPredicate;

public class EntityBackportHelper {

    public static void setEntityPortalEntrancePos(Entity entity, BlockPos pos) {
        try {
            Method setter = entity.getClass().getMethod("setPortalEntrancePos", BlockPos.class);
            setter.invoke(entity, pos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static BlockPos getEntityPortalEntrancePos(Entity entity) {
        try {
            return (BlockPos) entity.getClass().getMethod("getPortalEntrancePos").invoke(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isEntityInFluid(Entity entity, BiPredicate<Fluid, Double> predicate) {
        AABB bounds = entity.getBoundingBox().deflate(0.001D);
        BlockPos pos = new BlockPos(Mth.floor(bounds.minX), Mth.floor(bounds.minY), Mth.floor(bounds.minZ));
        FluidState state = entity.level.getFluidState(pos);
        return predicate.test(state.getType(), (double) state.getHeight(entity.level, pos));
    }

    public static Optional<Fluid> getEyeInFluid(Entity entity) {
        Vec3 eyePos = entity.getEyePosition(1.0F);
        BlockPos pos = new BlockPos(eyePos);
        if (!entity.level.isLoaded(pos)) return Optional.empty();

        FluidState state = entity.level.getFluidState(pos);
        double fluidHeight = state.getHeight(entity.level, pos) + pos.getY();

        if (eyePos.y <= fluidHeight) {
            return Optional.of(state.getType());
        } else {
            return Optional.empty();
        }
    }
}
