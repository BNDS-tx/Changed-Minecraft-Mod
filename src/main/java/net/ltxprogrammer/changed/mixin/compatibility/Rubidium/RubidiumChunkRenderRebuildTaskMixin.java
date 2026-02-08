package net.ltxprogrammer.changed.mixin.compatibility.Rubidium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import me.jellysquid.mods.sodium.client.gl.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.pipeline.BlockRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderRebuildTask;
import me.jellysquid.mods.sodium.client.util.task.CancellationSource;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.ltxprogrammer.changed.client.ChangedClient;
import net.ltxprogrammer.changed.client.LatexCoveredBlocksRenderer;
import net.ltxprogrammer.changed.extension.RequiredMods;
import net.ltxprogrammer.changed.extension.rubidium.WorldSliceExtension;
import net.ltxprogrammer.changed.entity.latex.SpreadingLatexType;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.world.LatexCoverGetter;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(value = ChunkRenderRebuildTask.class, remap = false)
@RequiredMods("rubidium")
public abstract class RubidiumChunkRenderRebuildTaskMixin {

    @Unique private static final Logger LOGGER = LogUtils.getLogger();
    @Unique private static final AtomicBoolean LOGGED_RUBIDIUM_COVER = new AtomicBoolean(false);
    @Unique private final BlockPos.MutableBlockPos changed$worldPos = new BlockPos.MutableBlockPos();
    @Unique private final BlockPos.MutableBlockPos changed$localPos = new BlockPos.MutableBlockPos();
    @Unique private ChunkBuildContext changed$buildContext;
    @Unique private CancellationSource changed$cancellation;
    @Unique private WorldSlice changed$slice;
    @Unique private LatexCoverGetter changed$coverGetter;
    @Unique private VisGraph changed$occluder;

    @Unique
    public LatexCoverState changed$getLatexCover(WorldSlice slice, BlockPos pos) {
        if ((Object) slice instanceof WorldSliceExtension ext) {
            return ext.getLatexCoverState(pos.getX(), pos.getY(), pos.getZ());
        }
        return ChangedLatexTypes.NONE.get().defaultCoverState();
    }

    @Inject(
            method = "performBuild(Lme/jellysquid/mods/sodium/client/gl/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationSource;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildResult;",
            at = @At("HEAD")
    )
    private void changed$beginBuild(ChunkBuildContext buildContext, CancellationSource cancellationSource, CallbackInfoReturnable<?> cir) {
        this.changed$buildContext = buildContext;
        this.changed$cancellation = cancellationSource;
        this.changed$slice = buildContext.cache.getWorldSlice();
        this.changed$coverGetter = LatexCoverGetter.extend(this.changed$slice, fetchPos -> changed$getLatexCover(this.changed$slice, fetchPos));
        this.changed$occluder = null;
    }

    @Redirect(
            method = "performBuild(Lme/jellysquid/mods/sodium/client/gl/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationSource;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildResult;",
            at = @At(value = "NEW", target = "net/minecraft/client/renderer/chunk/VisGraph")
    )
    private VisGraph changed$captureVisGraph() {
        VisGraph occluder = new VisGraph();
        this.changed$occluder = occluder;
        return occluder;
    }

    @WrapOperation(
            method = "performBuild(Lme/jellysquid/mods/sodium/client/gl/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationSource;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/world/WorldSlice;getBlockState(III)Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private BlockState changed$afterGetBlockState(WorldSlice slice, int x, int y, int z, Operation<BlockState> original) {
        BlockState blockState = original.call(slice, x, y, z);

        if (this.changed$buildContext == null || this.changed$coverGetter == null) {
            return blockState;
        }
        if (this.changed$cancellation != null && this.changed$cancellation.isCancelled()) {
            return blockState;
        }

        this.changed$worldPos.set(x, y, z);
        LatexCoverState cover = changed$getLatexCover(slice, this.changed$worldPos);
        if (!cover.isPresent()) {
            return blockState;
        }

        RenderType coverLayer = ChangedClient.latexCoveredBlocksRenderer.get().getBuildRenderType(cover);
        if (LOGGED_RUBIDIUM_COVER.compareAndSet(false, true)) {
            LOGGER.info("[Changed] Rubidium cover build at {} layer={}", this.changed$worldPos, coverLayer);
        }

        ForgeHooksClient.setRenderType(coverLayer);
        try {
            ChunkModelBuilder modelBuilder = this.changed$buildContext.buffers.get(coverLayer);
            if (modelBuilder == null) {
                return blockState;
            }

            LatexCoveredBlocksRenderer renderer = ChangedClient.latexCoveredBlocksRenderer.get();
            LatexCoveredBlocksRenderer.ModelSet modelSet = renderer.getModelSetForCover(blockState, cover);
            if (modelSet == null) {
                return blockState;
            }

            if (blockState.isCollisionShapeFullBlock(slice, this.changed$worldPos)) {
                return blockState;
            }

            boolean surfaceTop = cover.getProperties().contains(SpreadingLatexType.UP) && cover.getValue(SpreadingLatexType.UP);
            boolean surfaceBottom = cover.getProperties().contains(SpreadingLatexType.DOWN) && cover.getValue(SpreadingLatexType.DOWN);
            boolean surfaceNorth = cover.getProperties().contains(SpreadingLatexType.NORTH) && cover.getValue(SpreadingLatexType.NORTH);
            boolean surfaceSouth = cover.getProperties().contains(SpreadingLatexType.SOUTH) && cover.getValue(SpreadingLatexType.SOUTH);
            boolean surfaceEast = cover.getProperties().contains(SpreadingLatexType.EAST) && cover.getValue(SpreadingLatexType.EAST);
            boolean surfaceWest = cover.getProperties().contains(SpreadingLatexType.WEST) && cover.getValue(SpreadingLatexType.WEST);

            this.changed$localPos.set(x & 15, y & 15, z & 15);
            long seed = cover.getSeed(this.changed$worldPos);
            BlockRenderer blockRenderer = this.changed$buildContext.cache.getBlockRenderer();

            renderer.pushCoverGetter(this.changed$coverGetter);
            boolean rendered = false;
            try {
                if (surfaceTop) {
                    BakedModel model = modelSet.getModel(Direction.UP);
                    if (model != null) {
                        rendered |= blockRenderer.renderModel(slice, blockState, this.changed$worldPos, this.changed$localPos, model, modelBuilder, true, seed, EmptyModelData.INSTANCE);
                    }
                }
                if (surfaceBottom) {
                    BakedModel model = modelSet.getModel(Direction.DOWN);
                    if (model != null) {
                        rendered |= blockRenderer.renderModel(slice, blockState, this.changed$worldPos, this.changed$localPos, model, modelBuilder, true, seed, EmptyModelData.INSTANCE);
                    }
                }
                if (surfaceNorth) {
                    BakedModel model = modelSet.getModel(Direction.NORTH);
                    if (model != null) {
                        rendered |= blockRenderer.renderModel(slice, blockState, this.changed$worldPos, this.changed$localPos, model, modelBuilder, true, seed, EmptyModelData.INSTANCE);
                    }
                }
                if (surfaceSouth) {
                    BakedModel model = modelSet.getModel(Direction.SOUTH);
                    if (model != null) {
                        rendered |= blockRenderer.renderModel(slice, blockState, this.changed$worldPos, this.changed$localPos, model, modelBuilder, true, seed, EmptyModelData.INSTANCE);
                    }
                }
                if (surfaceEast) {
                    BakedModel model = modelSet.getModel(Direction.EAST);
                    if (model != null) {
                        rendered |= blockRenderer.renderModel(slice, blockState, this.changed$worldPos, this.changed$localPos, model, modelBuilder, true, seed, EmptyModelData.INSTANCE);
                    }
                }
                if (surfaceWest) {
                    BakedModel model = modelSet.getModel(Direction.WEST);
                    if (model != null) {
                        rendered |= blockRenderer.renderModel(slice, blockState, this.changed$worldPos, this.changed$localPos, model, modelBuilder, true, seed, EmptyModelData.INSTANCE);
                    }
                }
                BakedModel extra = modelSet.getExtraModel();
                if (extra != null) {
                    rendered |= blockRenderer.renderModel(slice, blockState, this.changed$worldPos, this.changed$localPos, extra, modelBuilder, true, seed, EmptyModelData.INSTANCE);
                }
            } finally {
                renderer.popCoverGetter();
            }

            if (rendered && this.changed$occluder != null) {
                this.changed$occluder.setOpaque(this.changed$localPos);
            }
        } finally {
            ForgeHooksClient.setRenderType(null);
        }

        return blockState;
    }

    @Inject(
            method = "performBuild(Lme/jellysquid/mods/sodium/client/gl/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationSource;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildResult;",
            at = @At("RETURN")
    )
    private void changed$endBuild(ChunkBuildContext buildContext, CancellationSource cancellationSource, CallbackInfoReturnable<?> cir) {
        this.changed$buildContext = null;
        this.changed$cancellation = null;
        this.changed$slice = null;
        this.changed$coverGetter = null;
        this.changed$occluder = null;
    }
}
