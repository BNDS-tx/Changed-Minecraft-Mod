package net.ltxprogrammer.changed.network.packet;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.ltxprogrammer.changed.util.UniversalDist;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class SetTransfurVariantDataPacket implements ChangedPacket {
    private final int id;
    @Nullable
    private final List<SynchedEntityData.DataItem<?>> packedItems;

    public SetTransfurVariantDataPacket(int id, List<SynchedEntityData.DataItem<?>> data) {
        this.id = id;
        this.packedItems = data;
    }

    public SetTransfurVariantDataPacket(FriendlyByteBuf buffer) {
        this.id = buffer.readVarInt();
        this.packedItems = SynchedEntityData.unpack(buffer);
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.id);
        SynchedEntityData.pack(this.packedItems, buffer);
    }

    @Override
    public CompletableFuture<Void> handle(NetworkEvent.Context context, CompletableFuture<Level> levelFuture, Executor sidedExecutor) {
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.setPacketHandled(true);
            return levelFuture.thenAccept(level -> {
                var player = level.getEntity(this.id);

                ProcessTransfur.ifPlayerTransfurred(EntityUtil.playerOrNull(player), variant -> {
                    if (packedItems == null)
                        return;
                    variant.getChangedEntity().getEntityData().assignValues(packedItems);
                });
            });
        }

        return CompletableFuture.failedFuture(makeIllegalSideException(context.getDirection().getReceptionSide(), LogicalSide.CLIENT));
    }

    public int getId() {
        return this.id;
    }
}
