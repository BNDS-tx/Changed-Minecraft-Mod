package net.ltxprogrammer.changed.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class UndeathPacket implements ChangedPacket {
    private final int playerId;
    private final ItemStack itemStack;

    public UndeathPacket(Player target, ItemStack itemStack) {
        this.playerId = target.getId();
        this.itemStack = itemStack;
    }

    public UndeathPacket(FriendlyByteBuf buffer) {
        this.playerId = buffer.readInt();
        this.itemStack = buffer.readItem();
    }


    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(playerId);
        buffer.writeItem(itemStack);
    }

    @Override
    public CompletableFuture<Void> handle(NetworkEvent.Context context, CompletableFuture<Level> levelFuture, Executor sidedExecutor) {
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.setPacketHandled(true);
            return levelFuture.thenAccept(level -> {
                // ✅ 关键：只让“目标玩家”的客户端播放
                var mc = net.minecraft.client.Minecraft.getInstance();
                if (mc.player == null || mc.player.getId() != this.playerId) return;

                mc.gameRenderer.displayItemActivation(this.itemStack);

                mc.player.playSound(net.minecraft.sounds.SoundEvents.TOTEM_USE, 1.0F, 1.0F);
            });
        }

        return CompletableFuture.failedFuture(makeIllegalSideException(
                context.getDirection().getReceptionSide(), LogicalSide.CLIENT));
    }
}
