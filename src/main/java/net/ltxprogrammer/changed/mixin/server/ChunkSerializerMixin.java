package net.ltxprogrammer.changed.mixin.server;

import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.ltxprogrammer.changed.world.LevelChunkSectionExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkSerializer.class)
public abstract class ChunkSerializerMixin {
    @Unique private static final String CHANGED_LATEX_COVERS_TAG = "ChangedLatexCovers";

    @Unique
    private static int toIndex(int x, int y, int z) {
        // vanilla section index order: (y << 8) | (z << 4) | x
        return (y << 8) | (z << 4) | x;
    }

    @Unique
    private static void writeLatexCoversToSectionTag(LevelChunkSection section, CompoundTag sectionTag) {
        if (!(section instanceof LevelChunkSectionExtension ext))
            return;

        int[] data = new int[LevelChunkSection.SECTION_SIZE]; // 4096
        boolean anyNonAir = false;

        for (int y = 0; y < 16; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    LatexCoverState state = ext.getLatexCoverState(x, y, z);
                    // 假设 ChangedLatexTypes.getLatexCoverStateIDMap() 返回的是 IdMapper<LatexCoverState>
                    int id = ChangedLatexTypes.getLatexCoverStateIDMap().getId(state);
                    data[toIndex(x, y, z)] = id;
                    if (!state.isAir())
                        anyNonAir = true;
                }
            }
        }

        if (anyNonAir) {
            sectionTag.put(CHANGED_LATEX_COVERS_TAG, new IntArrayTag(data));
        }
    }

    @Unique
    private static void readLatexCoversFromSectionTag(LevelChunkSection section, CompoundTag sectionTag) {
        if (!(section instanceof LevelChunkSectionExtension ext))
            return;

        if (!sectionTag.contains(CHANGED_LATEX_COVERS_TAG, 11)) // 11 = int array
            return;

        int[] data = sectionTag.getIntArray(CHANGED_LATEX_COVERS_TAG);
        if (data.length != LevelChunkSection.SECTION_SIZE)
            return;

        PalettedContainer<LatexCoverState> container = new PalettedContainer<>(
                ChangedLatexTypes.getLatexCoverStateIDMap(),
                ChangedLatexTypes.NONE.get().defaultCoverState(),
                PalettedContainer.Strategy.SECTION_STATES
        );

        for (int y = 0; y < 16; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    int id = data[toIndex(x, y, z)];
                    LatexCoverState state = ChangedLatexTypes.getLatexCoverStateIDMap().byId(id);
                    if (state == null) state = ChangedLatexTypes.NONE.get().defaultCoverState();
                    container.getAndSetUnchecked(x, y, z, state);
                }
            }
        }

        ext.acceptLatexStates(container);
        ext.recalcLatexCoverCounts();
    }

    @Inject(method = "write", at = @At("RETURN"))
    private static void changed$writeLatexCovers(net.minecraft.server.level.ServerLevel level, net.minecraft.world.level.chunk.ChunkAccess chunk, CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag root = cir.getReturnValue();
        if (root == null) return;
        var sections = root.getList("sections", 10);

        for (int i = 0; i < sections.size(); i++) {
            CompoundTag sectionTag = sections.getCompound(i);
            if (!sectionTag.contains("block_states", 10))
                continue;

            int sectionY = sectionTag.getByte("Y");
            int idx = chunk.getSectionIndexFromSectionY(sectionY);
            if (idx >= 0 && idx < chunk.getSections().length) {
                LevelChunkSection section = chunk.getSections()[idx];
                if (section != null)
                    writeLatexCoversToSectionTag(section, sectionTag);
            }
        }
    }

    @Inject(method = "read", at = @At("RETURN"))
    private static void changed$readLatexCovers(net.minecraft.server.level.ServerLevel level,
                                                net.minecraft.world.entity.ai.village.poi.PoiManager poi,
                                                net.minecraft.world.level.ChunkPos expectedPos,
                                                CompoundTag root,
                                                CallbackInfoReturnable<net.minecraft.world.level.chunk.ProtoChunk> cir) {
        var chunk = cir.getReturnValue();
        if (chunk == null) return;
        var sections = root.getList("sections", 10);

        for (int i = 0; i < sections.size(); i++) {
            CompoundTag sectionTag = sections.getCompound(i);
            if (!sectionTag.contains(CHANGED_LATEX_COVERS_TAG, 11))
                continue;

            int sectionY = sectionTag.getByte("Y");
            int idx = chunk.getSectionIndexFromSectionY(sectionY);
            if (idx >= 0 && idx < chunk.getSections().length) {
                LevelChunkSection section = chunk.getSections()[idx];
                if (section != null)
                    readLatexCoversFromSectionTag(section, sectionTag);
            }
        }
    }
}