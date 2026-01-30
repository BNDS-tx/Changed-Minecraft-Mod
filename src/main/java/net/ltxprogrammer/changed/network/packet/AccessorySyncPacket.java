package net.ltxprogrammer.changed.network.packet;

import com.mojang.datafixers.util.Either;
import net.ltxprogrammer.changed.data.AccessorySlotType;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.util.UniversalDist;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.ltxprogrammer.changed.world.inventory.AccessoryAccessMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Packet intended to sync accessory slots with clients
 * Client copies all data into given entity
 * Server opens the sender's accessory menu
 */
public class AccessorySyncPacket implements ChangedPacket {
    private final int entityId;
    private final boolean partial;
    private final AccessorySlots slots;
    private ItemStack carryRequest = null;

    public AccessorySyncPacket(int entityId, AccessorySlots slots) {
        this.entityId = entityId;
        this.partial = false;
        this.slots = slots;
    }

    public AccessorySyncPacket(int entityId, ItemStack carryRequest) {
        this.entityId = entityId;
        this.partial = false;
        this.slots = AccessorySlots.DUMMY;

        this.carryRequest = carryRequest;
    }

    public AccessorySyncPacket(int entityId, Map<AccessorySlotType, ItemStack> slots) {
        this.entityId = entityId;
        this.partial = true;
        this.slots = new AccessorySlots(null);
        this.slots.initialize(slots::containsKey, ignored -> {});
        slots.forEach(this.slots::setItem);
    }

    public AccessorySyncPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.partial = buffer.readBoolean();
        this.slots = new AccessorySlots(null);
//        buffer.readEither(
//                leftBuffer -> leftBuffer,
//                rightBuffer -> ItemStack.of(rightBuffer.readAnySizeNbt())
//                ).ifLeft(slots::readNetwork).ifRight(stack -> carryRequest = stack);
        if (!buffer.readBoolean()) {
            // Left 逻辑 (对应 leftBuffer -> leftBuffer)
            // 这里 leftBuffer 其实就是 buffer 本身
            slots.readNetwork(buffer);
        } else {
            // Right 逻辑 (对应 rightBuffer -> ItemStack.of(...))
            // 手动读取 NBT 并转换为 ItemStack
            carryRequest = ItemStack.of(buffer.readAnySizeNbt()); // 1.18.2 通常用 readNbt() 而不是 readAnySizeNbt()，除非有特殊需求
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(entityId);
        buffer.writeBoolean(partial);
//        buffer.writeEither(carryRequest == null ? Either.left(slots) : Either.right(carryRequest),
//                (leftBuffer, slots) -> slots.writeNetwork(leftBuffer),
//                (rightBuffer, stack) -> rightBuffer.writeNbt(stack.serializeNBT()));
        // 1. 判断我们要发的是哪种数据（Left 还是 Right）
        // 根据原代码：carryRequest != null 时发 Right (Item)，否则发 Left (Slots)
        boolean isItem = (carryRequest != null);

        // 2. 写入标记位 (这对应了 readEither 里的 readBoolean)
        //通常约定：false = Left, true = Right
        buffer.writeBoolean(isItem);

        // 3. 根据情况写入具体数据
        if (isItem) {
            // === Right 分支 (发送 ItemStack) ===
            // 原代码: rightBuffer.writeNbt(stack.serializeNBT())
            // 1.18.2 建议写法:
            buffer.writeNbt(carryRequest.serializeNBT());

            // 注意：如果 serializeNBT() 报错（虽然 Forge 通常都有），
            // 可以改用原版写法: buffer.writeNbt(carryRequest.save(new CompoundTag()));
        } else {
            // === Left 分支 (发送 Slots) ===
            // 原代码: slots.writeNetwork(leftBuffer)
            slots.writeNetwork(buffer);
        }

        slots.writeNetwork(buffer);
    }

    @Override
    public CompletableFuture<Void> handle(NetworkEvent.Context context, CompletableFuture<Level> levelFuture, Executor sidedExecutor) {
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.setPacketHandled(true);
            return levelFuture.thenAccept(level -> {
                if (!(level.getEntity(entityId) instanceof LivingEntity livingEntity))
                    throw new IllegalStateException("Entity is not a living entity");

                AccessorySlots.getForEntity(livingEntity).ifPresent(accessorySlots -> accessorySlots.setAll(this.slots, !partial));
            });
        }

        else {
            var sender = context.getSender();
            if (sender == null)
                return CompletableFuture.failedFuture(new IllegalStateException("Sender is null (Shouldn't be possible)"));

            context.setPacketHandled(true);
            AccessoryAccessMenu.openForPlayer(sender, carryRequest == null ? ItemStack.EMPTY : carryRequest);

            return CompletableFuture.completedFuture(null);
        }
    }
}
