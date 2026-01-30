package net.ltxprogrammer.changed.mixin.network;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.world.LatexCoverHitResult;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.BiConsumer; // 必须导入
import java.util.function.Function;   // 必须导入

@Mixin(FriendlyByteBuf.class)
public abstract class FriendlyByteBufMixin {

    // 1.18.2: 参数改为 BiConsumer
    @Shadow public abstract <T> void writeOptional(Optional<T> p_130076_, BiConsumer<FriendlyByteBuf, T> p_130077_);

    // 1.18.2: 参数改为 Function
    @Shadow public abstract <T> Optional<T> readOptional(Function<FriendlyByteBuf, T> p_130089_);

    @Unique
    private static final ResourceLocation EXTENDED_HIT_RESULT = Changed.modResource("latex_cover_hit_result");

    @Inject(method = "writeBlockHitResult", at = @At("RETURN"))
    public void writeExtendedHitResult(BlockHitResult hitResult, CallbackInfo ci) {
        Optional<LatexCoverHitResult> opt = hitResult instanceof LatexCoverHitResult coverHitResult
                ? Optional.of(coverHitResult)
                : Optional.empty();

        // 这里的 buffer 类型现在会被正确推断为 FriendlyByteBuf
        this.writeOptional(opt, (buffer, coverHitResult) -> {
            buffer.writeResourceLocation(EXTENDED_HIT_RESULT);
        });
    }

    @Inject(method = "readBlockHitResult", at = @At("RETURN"), cancellable = true)
    public void readExtendedHitResult(CallbackInfoReturnable<BlockHitResult> cir) {
        BlockHitResult originalHit = cir.getReturnValue();

        // 这里的 buffer 类型现在会被正确推断为 FriendlyByteBuf
        Optional<BlockHitResult> extended = this.readOptional((buffer) -> {
            // 1.18.2 确切拥有 readResourceLocation() 方法
            ResourceLocation id = buffer.readResourceLocation();
            if (id.equals(EXTENDED_HIT_RESULT)) {
                return LatexCoverHitResult.wrap(originalHit);
            }
            return originalHit;
        });

        extended.ifPresent(cir::setReturnValue);
    }
}