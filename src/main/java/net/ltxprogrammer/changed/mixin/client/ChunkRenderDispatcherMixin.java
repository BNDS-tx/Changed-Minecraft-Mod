package net.ltxprogrammer.changed.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.ltxprogrammer.changed.client.ChangedClient;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.world.LatexCoverGetter;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.client.renderer.ChunkBufferBuilderPack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.Set;

@Mixin(ChunkRenderDispatcher.RenderChunk.RebuildTask.class)
public abstract class ChunkRenderDispatcherMixin {

    @Unique
    private static void changed$beginLayer(BufferBuilder builder) {
        // 这等价于原版 RenderChunk.beginLayer 做的最核心一件事：
        // 让 BufferBuilder 进入 QUADS + BLOCK format 模式
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
    }

    @Unique
    private LatexCoverState changed$getLatexCoverState(RenderChunkRegion region, BlockPos blockPos) {
        int i = SectionPos.blockToSectionCoord(blockPos.getX()) - region.centerX;
        int j = SectionPos.blockToSectionCoord(blockPos.getZ()) - region.centerZ;
        if (i < 0 || i >= region.chunks.length)
            return ChangedLatexTypes.NONE.get().defaultCoverState();
        if (j < 0 || j >= region.chunks[i].length)
            return ChangedLatexTypes.NONE.get().defaultCoverState();
        return LatexCoverState.getAt(region.chunks[i][j].wrapped, blockPos);
    }

    @Inject(
            method = "compile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;getBlockRenderer()Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;"
            )
    )
    private void changed$addCompileSteps(
            float cameraX, float cameraY, float cameraZ,
            ChunkRenderDispatcher.CompiledChunk compiledChunk,
            ChunkBufferBuilderPack bufferBuilderPack,
            CallbackInfoReturnable<Set<BlockEntity>> cir,
            @Local RenderChunkRegion renderchunkregion,
            @Local(ordinal = 0) BlockPos blockpos,
            @Local java.util.Random random
    ) {
        BlockPos blockpos1 = blockpos.offset(15, 15, 15);

        // 保险：如果 region 为空，直接不做任何事
        if (renderchunkregion == null) {
            return;
        }

        // 遍历该 section 的 16³ 方块
        for (BlockPos pos : BlockPos.betweenClosed(blockpos, blockpos1)) {
            BlockState baseState = renderchunkregion.getBlockState(pos);
            LatexCoverState coverState = changed$getLatexCoverState(renderchunkregion, pos);
            if (!coverState.isPresent()) continue;

            // 覆盖层期望的渲染层
            RenderType coverLayer = ChangedClient.latexCoveredBlocksRenderer.get().getBuildRenderType(coverState);

            // ✅ 嵌入原版 per-layer 逻辑（方法 2）
            for (RenderType layer : RenderType.chunkBufferLayers()) {
                if (layer != coverLayer) continue;

                net.minecraftforge.client.ForgeHooksClient.setRenderType(layer);

                BufferBuilder buffer = bufferBuilderPack.builder(layer);

                // 用 compiledChunk.hasLayer 作为“这一层是否已 begin”的状态位
                if (compiledChunk.hasLayer.add(layer)) {
                    changed$beginLayer(buffer);
                }

                ChangedClient.latexCoveredBlocksRenderer.get().tesselate(
                        renderchunkregion,
                        LatexCoverGetter.extend(renderchunkregion, fetchPos -> this.changed$getLatexCoverState(renderchunkregion, fetchPos)),
                        pos,
                        buffer,
                        baseState,
                        coverState,
                        random
                );

                // 标记非空，确保后续 upload / translucent sort 不会把这层当空
                compiledChunk.isCompletelyEmpty = false;
                compiledChunk.hasBlocks.add(layer);
            }
        }

        // 清理 RenderType ThreadLocal，避免污染原版后续逻辑
        net.minecraftforge.client.ForgeHooksClient.setRenderType(null);
    }
}
