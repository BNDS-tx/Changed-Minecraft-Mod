//package net.ltxprogrammer.changed.mixin.render;
//
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
//import net.ltxprogrammer.changed.entity.beast.AzurebyssEntity;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.entity.LivingEntityRenderer;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//@Mixin(LivingEntityRenderer.class)
//public abstract class HurtBackMixin {
//    // 拦截 setupRotations 中获取 hurtTime 的操作
//    // 在 1.20.1 中，渲染旋转是根据 entity.hurtTime > 0 来判断是否应用旋转矩阵的
//
//    @WrapOperation(
//            // 【关键修复】使用完整的方法描述符 (Descriptor)
//            // 格式：方法名(参数类型1;参数类型2;...)返回值类型
//            // 这样 Mixin 绝对不会找错方法，即使它是泛型的
//            method = "setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V",
//
//            at = @At(
//                    value = "FIELD",
//                    // 明确指定我们要拦截的是 LivingEntity 类下的 hurtTime 字段
//                    target = "Lnet/minecraft/world/entity/LivingEntity;hurtTime:I",
//                    opcode = org.objectweb.asm.Opcodes.GETFIELD
//            )
//    )
//    private int wrapHurtTime(LivingEntity instance, Operation<Integer> original) {
//        // 1. 获取原始的 hurtTime
//        int originalHurtTime = original.call(instance);
//
//        // 2. 判断是否为玩家且正在吞噬
//        if (instance instanceof Player player) {
//            // 注意：这里调用你自己写的检查逻辑
//            if (IAbstractChangedEntity.forEitherSafe(player).isPresent() &&
//                    IAbstractChangedEntity.forEitherSafe(player).get().getChangedEntity() instanceof AzurebyssEntity) {
//                return 0; // 返回 0，渲染器就会认为没受伤，从而不应用旋转（抽搐）
//            }
//        }
//        return originalHurtTime; // 正常情况返回原始值
//    }
//}
