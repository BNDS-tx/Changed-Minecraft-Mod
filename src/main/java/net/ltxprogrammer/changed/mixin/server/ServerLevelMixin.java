package net.ltxprogrammer.changed.mixin.server;

import com.llamalad7.mixinextras.sugar.Local;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.network.packet.TransfurEntityEventPacket;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.ltxprogrammer.changed.world.data.ChangedGameData;
import net.ltxprogrammer.changed.world.data.ChangedGameDataAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements ChangedGameDataAccessor {
    private final ChangedGameData changedGameData = new ChangedGameData((ServerLevel)(Object)this);

    @Override
    public ChangedGameData getChangedGameData() {
        return changedGameData;
    }

    private ServerLevelMixin(WritableLevelData p_204149_, ResourceKey<Level> p_204150_, Holder<DimensionType> p_204151_, Supplier<ProfilerFiller> p_204152_, boolean p_204153_, boolean p_204154_, long p_204155_) {
        super(p_204149_, p_204150_, p_204151_, p_204152_, p_204153_, p_204154_, p_204155_);
    }

    @Inject(method = "broadcastEntityEvent", at = @At("HEAD"), cancellable = true)
    public void maybeBroadcastForVariant(Entity entity, byte id, CallbackInfo ci) {
        if (entity instanceof ChangedEntity changedEntity && changedEntity.getUnderlyingPlayer() != null) {
            Changed.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(changedEntity::getUnderlyingPlayer),
                    new TransfurEntityEventPacket(changedEntity.getUnderlyingPlayer(), id));
            ci.cancel();
        }
    }

    @Inject(method = "tickChunk", at = @At("TAIL"))
    public void doChangedChunkTicks(LevelChunk chunk, int tickCount, CallbackInfo ci,
                                    @Local ProfilerFiller profilerFiller) {
        profilerFiller.push("changed:latexCoverTick");

        ChunkPos chunkpos = chunk.getPos();
        int i = chunkpos.getMinBlockX();
        int j = chunkpos.getMinBlockZ();

        if (tickCount > 0) {
            LevelChunkSection[] alevelchunksection = chunk.getSections();

            for(int l = 0; l < alevelchunksection.length; ++l) {
                LevelChunkSection levelchunksection = alevelchunksection[l];
                if (levelchunksection.isRandomlyTicking()) {
                    int j1 = chunk.getSectionYFromSectionIndex(l);
                    int k1 = SectionPos.sectionToBlockCoord(j1);

                    for(int l1 = 0; l1 < tickCount; ++l1) {
                        BlockPos blockPos = this.getBlockRandomPos(i, k1, j, 15);
                        profilerFiller.push("randomTick");
                        LatexCoverState coverState = LatexCoverState.getAt(levelchunksection, blockPos.getX() - i, blockPos.getY() - k1, blockPos.getZ() - j);

                        if (coverState.isRandomlyTicking())
                            coverState.randomTick((ServerLevel)(Object)this, blockPos, this.random);

                        profilerFiller.pop();
                    }
                }
            }
        }

        profilerFiller.pop();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void doChangedTicks(BooleanSupplier hasTimeSupplier, CallbackInfo ci,
                               @Local ProfilerFiller profilerFiller) {
        profilerFiller.push("changed:gamedata");

        changedGameData.tick(hasTimeSupplier);

        profilerFiller.pop();
    }
}
