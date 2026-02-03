package net.ltxprogrammer.changed.world;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.network.packet.CustomLevelEventPacket;
import net.ltxprogrammer.changed.network.packet.LatexCoverUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class ServerLevelExtension extends LevelExtension {
    public static final ServerLevelExtension INSTANCE = new ServerLevelExtension();

    @Override
    public void sendCoverUpdated(LevelAccessor level, BlockPos blockPos, LatexCoverState oldState, LatexCoverState newState, int flags) {
        ((ServerLevel)level).getChunkSource().blockChanged(blockPos);
        Changed.LOGGER.info("Sending LatexCover update at " + blockPos + ": " + newState.getType());
        Changed.PACKET_HANDLER.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 64.0, ((ServerLevel)level).dimension())), new LatexCoverUpdatePacket(blockPos, newState));
    }

    @Override
    public void customLevelEvent(LevelAccessor level, @Nullable Player source, int id, BlockPos blockPos, int param) {
        ((ServerLevel)level).getServer().getPlayerList().broadcast(source,
                (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 64.0D,
                ((ServerLevel)level).dimension(),
                Changed.PACKET_HANDLER.toVanillaPacket(new CustomLevelEventPacket(id, blockPos, param, false), NetworkDirection.PLAY_TO_CLIENT));
    }
}
