package net.ltxprogrammer.changed.mixin.compatibility.Rubidium;

import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSection;
import com.mojang.logging.LogUtils;
import net.ltxprogrammer.changed.extension.RequiredMods;
import net.ltxprogrammer.changed.extension.rubidium.ClonedChunkSectionExtension;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.ltxprogrammer.changed.world.LevelChunkSectionExtension;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.slf4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClonedChunkSection.class, remap = false)
@RequiredMods("rubidium")
public abstract class ClonedChunkSectionMixin implements ClonedChunkSectionExtension {

    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Unique
    private PalettedContainer<LatexCoverState> changed$latexCoverStateData;

    @Inject(method = "init(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/SectionPos;)V", at = @At("TAIL"))
    private void changed$initLatexCover(Level level, SectionPos pos, CallbackInfo ci) {
        // ⚠️ 这里千万别 throw。出错会让 Rubidium 整个 chunk rebuild 失败 -> 世界方块都不渲染。
        PalettedContainer<LatexCoverState> latexCoverData = null;

        try {
            LevelChunk chunk = level.getChunk(pos.getX(), pos.getZ());

            LevelChunkSection section = changed$getChunkSection(level, chunk, pos);
            if (section != null) {
                latexCoverData = (((LevelChunkSectionExtension) section).getLatexStates()).copy();
            }
        } catch (Throwable t) {
            LOGGER.warn("[Changed] Failed to clone latex cover section data at {}", pos, t);
        }

        this.changed$latexCoverStateData = latexCoverData;
    }

    @Unique
    private static @Nullable LevelChunkSection changed$getChunkSection(Level level, ChunkAccess chunk, SectionPos pos) {
        // 这段逻辑是照着 Rubidium 自己的 getChunkSection(...) bytecode 复刻的
        if (level.isOutsideBuildHeight(SectionPos.sectionToBlockCoord(pos.getY()))) {
            return null;
        }
        return chunk.getSections()[level.getSectionIndexFromSectionY(pos.getY())];
    }

    @Override
    public PalettedContainer<LatexCoverState> getLatexCoverData() {
        return this.changed$latexCoverStateData;
    }
}
