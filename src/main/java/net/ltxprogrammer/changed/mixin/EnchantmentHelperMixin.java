package net.ltxprogrammer.changed.mixin;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    @Inject(method = "getDepthStrider", at = @At("RETURN"), cancellable = true)
    private static void getDepthStrider(LivingEntity le, CallbackInfoReturnable<Integer> callback) {
        if (le instanceof ChangedEntity entity) {
            callback.setReturnValue(callback.getReturnValue() + entity.getDepthStriderLevel());
            return;
        }

        ProcessTransfur.ifPlayerTransfurred(EntityUtil.playerOrNull(le), variant -> {
            callback.setReturnValue(callback.getReturnValue() + variant.getChangedEntity().getDepthStriderLevel());
        });
    }

    @Inject(method = "hasAquaAffinity", at = @At("HEAD"), cancellable = true)
    private static void hasAquaAffinity(LivingEntity le, CallbackInfoReturnable<Boolean> callback) {
        ProcessTransfur.ifPlayerTransfurred(EntityUtil.playerOrNull(le), variant -> {
            if (variant.breatheMode.hasAquaAffinity())
                callback.setReturnValue(true);
        });
    }

    @Inject(method = "getRespiration", at = @At("RETURN"), cancellable = true)
    private static void getRespirationOrIfStrong(LivingEntity le, CallbackInfoReturnable<Integer> callback) {
        ProcessTransfur.ifPlayerTransfurred(EntityUtil.playerOrNull(le), variant -> {
            if (variant.breatheMode == TransfurVariant.BreatheMode.STRONG)
                callback.setReturnValue(Math.max(callback.getReturnValue(), 4));
        });
    }
}
