package net.ltxprogrammer.changed.extension.rubidium;

import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.world.level.chunk.PalettedContainer;

public interface ClonedChunkSectionExtension {
    PalettedContainer<LatexCoverState> getLatexCoverData();
}
