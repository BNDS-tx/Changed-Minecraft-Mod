package net.ltxprogrammer.changed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.DataFixer;
import net.ltxprogrammer.changed.Changed;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(NbtUtils.class)
public abstract class NbtUtilsMixin {
    @WrapOperation(method = "setValueHelper", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/properties/Property;getValue(Ljava/lang/String;)Ljava/util/Optional;"))
    private static <T extends Comparable<T>> Optional<T> getValueAndUpdate(Property<T> instance, String s, Operation<Optional<T>> original) {
        if (Changed.dataFixer != null)
            return original.call(instance, s).or(() -> Changed.dataFixer.updateBlockState(instance, s));
        else
            return original.call(instance, s);
    }

    // 2. 注入目标是 NbtUtils 的静态 update 方法
    // 注意：1.18.2 的 update 方法多了一个 DataFixTypes 参数，因为它不是实例方法而是静态方法
    @Inject(method = "update(Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/util/datafix/DataFixTypes;Lnet/minecraft/nbt/CompoundTag;II)Lnet/minecraft/nbt/CompoundTag;",
            at = @At("RETURN"))
    private static void updateChanged(DataFixer fixer, DataFixTypes type, CompoundTag tag, int version, int newVersion, CallbackInfoReturnable<CompoundTag> cir) {
        if (Changed.dataFixer != null) {
            // 3. 这里直接使用传入的 'type' 参数，而不需要 (DataFixTypes)(Object)this
            // cir.getReturnValue() 获取的是原版 update 刚刚处理完的 NBT
            Changed.dataFixer.updateCompoundTag(type, cir.getReturnValue());
        }
    }
}
