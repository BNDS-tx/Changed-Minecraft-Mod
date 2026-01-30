package net.ltxprogrammer.changed.mixin.server;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.network.packet.LatexCoverUpdatePacket;
import net.ltxprogrammer.changed.network.packet.SectionLatexCoversUpdatePacket;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.network.NetworkDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkHolder.class)
public abstract class ChunkHolderMixin {

    // 1.18.2 的 broadcast 签名不同，通常是这个
    @Shadow protected abstract void broadcast(Packet<?> p_140084_, boolean p_140085_);

    // 1. 单方块更新注入
    // 1.18.2 中没有 broadcastBlockEntityIfNeeded 这个便捷方法，逻辑通常直接写在循环里
    // 你需要找到创建 ClientboundBlockUpdatePacket 的位置
    // 目标通常是: new ClientboundBlockUpdatePacket(level, blockPos)
    @Inject(method = "broadcastChanges", at = @At(value = "NEW", target = "net/minecraft/network/protocol/game/ClientboundBlockUpdatePacket"))
    public void broadcastChangedChangesSingle(LevelChunk chunk, CallbackInfo ci,
                                              @Local Level level,
                                              @Local BlockPos blockPos) { // 注意：blockPos 可能是通过局部变量计算出来的
        // 发送自定义包
        Packet<?> packet = Changed.PACKET_HANDLER.toVanillaPacket(
                new LatexCoverUpdatePacket(blockPos, LatexCoverState.getAt(chunk, blockPos)), NetworkDirection.PLAY_TO_CLIENT);

        this.broadcast(packet, false);
    }

    // 2. 多方块(Section)更新注入
    // 目标: new ClientboundSectionBlocksUpdatePacket(sectionPos, shortSet, section, boolean)
    @Inject(method = "broadcastChanges", at = @At(value = "NEW", target = "net/minecraft/network/protocol/game/ClientboundSectionBlocksUpdatePacket"))
    public void broadcastChangedChangesMulti(LevelChunk chunk, CallbackInfo ci,
                                             @Local LevelChunkSection section,
                                             @Local SectionPos sectionPos,
                                             @Local ShortSet shortSet) {
        // 发送自定义包
        Packet<?> packet = Changed.PACKET_HANDLER.toVanillaPacket(
                new SectionLatexCoversUpdatePacket(sectionPos, shortSet, section), NetworkDirection.PLAY_TO_CLIENT);

        this.broadcast(packet, false);
    }
}
