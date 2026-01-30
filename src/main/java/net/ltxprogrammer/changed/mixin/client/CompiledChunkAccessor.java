package net.ltxprogrammer.changed.mixin.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ChunkRenderDispatcher.CompiledChunk.class)
public interface CompiledChunkAccessor {
    // 1.18.2 Official Mapping 中，这个字段叫 hasBlocks
    // 它的作用就是存储"该区块包含哪些渲染层"
    @Accessor("hasBlocks")
    Set<RenderType> getHasBlocks();
}