package net.ltxprogrammer.changed.mixin.compatibility.PresenceFootsteps;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eu.ha3.presencefootsteps.world.PFSolver;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.extension.RequiredMods;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PFSolver.class, remap = false)
@RequiredMods("presencefootsteps")
public abstract class PFSolverMixin {
//    @WrapOperation(method = "findAssociation(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/AABB;)Leu/ha3/presencefootsteps/world/Association;",
//        at = @At(value = "INVOKE",
//                target = "Leu/ha3/presencefootsteps/world/PFSolver;getBlockStateAt(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
//                ordinal = 0))
//    public BlockState overrideLatexCover(PFSolver instance, Entity entity, BlockPos pos, Operation<BlockState> original) {
//        final Block effectBlock = AbstractLatexBlock.getSurfaceType(entity.level, pos.above(), Direction.DOWN).getBlock();
//        if (effectBlock != null)
//            return effectBlock.defaultBlockState();
//        else
//            return original.call(instance, entity, pos);
//    }

    @Redirect(
            // 2. 严格匹配你刚刚反编译看到的 1.18.2 私有方法签名
            method = "findAssociation(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/AABB;)Leu/ha3/presencefootsteps/world/Association;",

            // 3. 拦截 Minecraft 原版的 getBlockState 方法
            // 注意：这里的 remap = true 是必须的，因为我们要拦截的是原版混淆方法
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    remap = true
            )
    )
    public BlockState redirectGetBlockState(Level world, BlockPos pos) {
        // --- 插入你的 Latex 逻辑 ---

        // 你的原版逻辑是检查 pos.above() 也就是脚下的方块表面有没有覆盖层
        // 1.18.2 的 findAssociation 传进来了 world 参数，直接用即可，不需要 entity
        Block effectBlock = AbstractLatexBlock.getSurfaceType(world, pos.above(), Direction.DOWN).getBlock();

        if (effectBlock != null) {
            // 如果有覆盖层，欺骗 Solver 说这里的方块就是 Latex 方块
            return effectBlock.defaultBlockState();
        }

        // --- 如果没有覆盖层，执行原版逻辑 ---
        return world.getBlockState(pos);
    }
}
