package net.ltxprogrammer.changed.mixin.entity;

import net.ltxprogrammer.changed.aaBackport.FluidUtilCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityBackport {

    @Accessor("portalEntrancePos")
    void setPortalEntrancePos(BlockPos pos);

    @Accessor("portalEntrancePos")
    BlockPos getPortalEntrancePos();

    /**
     * 自定义方法，判断实体是否在某流体中
     */
    @Unique
    default boolean isInFluid(Fluid fluid) {
        Entity self = (Entity) this;

        // 可以调用工具方法获取覆盖高度
        double height = FluidUtilCompat.getFluidHeight(self, fluid);

        // 逻辑：只要覆盖高度大于 0 即认为在流体中
        return height > 0;
    }
}
