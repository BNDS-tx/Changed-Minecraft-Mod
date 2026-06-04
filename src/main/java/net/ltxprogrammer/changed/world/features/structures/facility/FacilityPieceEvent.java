package net.ltxprogrammer.changed.world.features.structures.facility;

import net.ltxprogrammer.changed.world.data.ActiveFacilityInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class FacilityPieceEvent implements IForgeRegistryEntry<FacilityPieceEvent> {
    public abstract void onPlayerEnterPiece(ServerLevel level, ServerPlayer player, ActiveFacilityInstance.PieceInfo pieceInfo, Zone zone, Runnable markDirty);
    public abstract void onPlayerLeavePiece(ServerLevel level, ServerPlayer player, ActiveFacilityInstance.PieceInfo pieceInfo, Zone zone, Runnable markDirty);

    public abstract void onPieceTick(ServerLevel level, ActiveFacilityInstance.PieceInfo pieceInfo, Zone zone, Runnable markDirty);
}
