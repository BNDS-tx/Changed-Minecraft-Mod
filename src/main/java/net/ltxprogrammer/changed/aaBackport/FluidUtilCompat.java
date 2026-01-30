package net.ltxprogrammer.changed.aaBackport;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

import java.util.function.BiPredicate;

public class FluidUtilCompat {
    /**
     * 模拟 getFluidTypeHeight
     * @param entity 实体
     * @param fluid 目标流体（例如 Fluids.WATER）
     * @return 流体覆盖高度
     */
    public static double getFluidHeight(Entity entity, Fluid fluid) {
        AABB box = entity.getBoundingBox();
        Level world = entity.level;
        double maxHeight = 0.0;

        for (double y = box.minY; y <= box.maxY; y += 0.25) { // 粗略采样
            BlockPos pos = new BlockPos(entity.getX(), y, entity.getZ());
            FluidState state = world.getFluidState(pos);
            if (state.getType() == fluid) {
                maxHeight = y - box.minY;
            }
        }
        return maxHeight;
    }

    /**
     * 模拟 1.20.1 isInFluidType
     * @param entity 目标实体
     * @param predicate 流体 + 覆盖高度 -> boolean
     * @param allowPartial 是否允许部分被覆盖（true = 检查部分覆盖，false = 整体覆盖）
     */
    public static boolean isInFluidType(Entity entity, BiPredicate<Fluid, Double> predicate, boolean allowPartial) {
        AABB box = entity.getBoundingBox();
        Level world = entity.level;

        double step = 0.25; // 采样间隔，可调整精度
        double maxHeight = 0.0;

        for (double y = box.minY; y <= box.maxY; y += step) {
            BlockPos pos = new BlockPos(entity.getX(), y, entity.getZ());
            FluidState state = world.getFluidState(pos);

            if (state.getType() != Fluids.EMPTY) { // 任意流体
                double height = y - box.minY;
                if (predicate.test(state.getType(), height)) {
                    if (allowPartial) return true; // 部分即可
                    maxHeight = Math.max(maxHeight, height);
                }
            }
        }

        return !allowPartial && maxHeight >= (box.getYsize()); // 全覆盖要求
    }
}