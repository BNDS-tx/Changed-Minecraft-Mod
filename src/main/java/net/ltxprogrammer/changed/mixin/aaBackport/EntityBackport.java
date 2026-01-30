package net.ltxprogrammer.changed.mixin.aaBackport;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public abstract class EntityBackport {

    @Shadow
    protected BlockPos portalEntrancePos;

    @Unique
    public void setPortalEntrancePos(BlockPos pos) {
        this.portalEntrancePos = pos;
    }

    @Unique
    public BlockPos getPortalEntrancePos() {
        return this.portalEntrancePos;
    }
}

