package net.ltxprogrammer.changed.mixin.compatibility.Rubidium;

import me.jellysquid.mods.sodium.client.world.WorldSlice;
import me.jellysquid.mods.sodium.client.world.cloned.ChunkRenderContext;
import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSection;
import net.ltxprogrammer.changed.extension.RequiredMods;
import net.ltxprogrammer.changed.extension.rubidium.ClonedChunkSectionExtension;
import net.ltxprogrammer.changed.extension.rubidium.WorldSliceExtension;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldSlice.class, remap = false)
@RequiredMods("rubidium")
public abstract class WorldSliceMixin implements WorldSliceExtension {

    @Shadow @Final private static int SECTION_TABLE_ARRAY_SIZE;

    @Shadow private int baseX;
    @Shadow private int baseY;
    @Shadow private int baseZ;

    @Unique
    private final PalettedContainer<LatexCoverState>[] changed$latexSections =
            (PalettedContainer<LatexCoverState>[]) new PalettedContainer[SECTION_TABLE_ARRAY_SIZE];

    @Override
    public LatexCoverState getLatexCoverState(int x, int y, int z) {
        int relX = x - baseX;
        int relY = y - baseY;
        int relZ = z - baseZ;

        int secX = relX >> 4;
        int secY = relY >> 4;
        int secZ = relZ >> 4;

        int sectionIndex = WorldSlice.getLocalSectionIndex(secX, secY, secZ);
        if (sectionIndex < 0 || sectionIndex >= SECTION_TABLE_ARRAY_SIZE) {
            return ChangedLatexTypes.NONE.get().defaultCoverState();
        }

        PalettedContainer<LatexCoverState> container = changed$latexSections[sectionIndex];
        if (container == null) {
            return ChangedLatexTypes.NONE.get().defaultCoverState();
        }

        return container.get(relX & 15, relY & 15, relZ & 15);
    }

    @Inject(method = "copyData", at = @At("TAIL"))
    private void changed$copyLatexSections(ChunkRenderContext context, CallbackInfo ci) {
        ClonedChunkSection[] sections = context.getSections();
        int n = Math.min(sections.length, SECTION_TABLE_ARRAY_SIZE);

        for (int i = 0; i < n; i++) {
            ClonedChunkSection sec = sections[i];
            if (sec instanceof ClonedChunkSectionExtension ext) {
                changed$latexSections[i] = ext.getLatexCoverData();
            } else {
                changed$latexSections[i] = null;
            }
        }
    }
}
