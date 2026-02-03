package net.ltxprogrammer.changed.mixin.item;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.item.SpecializedItemRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ItemLayerModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin implements ResourceManagerReloadListener {
    @Unique private static final String TAGGED_SPECIAL_RENDER = "__tagged_special_render";
    @Unique private static final Map<ItemStack, LivingEntity> ENTITY_CACHE = new HashMap<>();
    @Unique private static final Map<ItemStack, ItemStack> ORIGINAL_STACK_CACHE = new HashMap<>();

    @Shadow public abstract ItemModelShaper getItemModelShaper();

    @Final private Minecraft minecraft;

    @Unique private ItemStack cachedStack;
    @Unique private Level cachedLevel;
    @Unique private LivingEntity cachedEntity;
    @Unique private int cachedSeed;

    @WrapMethod(method = "getModel")
    public BakedModel cacheOverrideParams(ItemStack stack, Level level, LivingEntity entity, int seed, Operation<BakedModel> original) {
        cachedStack = stack;
        cachedLevel = level;
        cachedEntity = entity;
        cachedSeed = seed;
        return original.call(stack, level, entity, seed);
    }

    @Inject(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V",
            at = @At("HEAD"))
    public void renderStaticPre(@Nullable LivingEntity entity, ItemStack itemStack, ItemTransforms.TransformType type, boolean p_174246_, PoseStack pose, MultiBufferSource buffers, @Nullable Level level, int p_174250_, int p_174251_, int p_174252_, CallbackInfo callback) {
        if (entity == null) return;
        if (ENTITY_CACHE.size() > 32) {
            ENTITY_CACHE.clear();
            Changed.LOGGER.error("Memory leak detected in ItemRendererMixin");
        }
        ENTITY_CACHE.put(itemStack, entity); // Cache entity for item holder to catch overrides later
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderPre(ItemStack itemStack, ItemTransforms.TransformType type, boolean leftHand, PoseStack pose,
                       MultiBufferSource buffers, int packedLight, int packedOverlay, BakedModel model, CallbackInfo callback) {
        if (itemStack.getTag() != null && itemStack.getTag().contains(TAGGED_SPECIAL_RENDER))
            return;
        if (!(itemStack.getItem() instanceof SpecializedItemRendering special))
            return; // Don't override

        // 关键：GUI / 地面 / 展示框 这些场景必须走 vanilla item model（models/item/*.json）
        if (SpecializedItemRendering.isGUI(type))
            return;

        ResourceLocation modelLocation = special.getModelLocation(itemStack, type);
        if (modelLocation == null)
            return;

        ItemRenderer self = (ItemRenderer)(Object)this;
        model = self.getItemModelShaper().getModelManager().getModel(special.getModelLocation(itemStack, type));

        // Fetch model overrides
        LivingEntity holder = ENTITY_CACHE.remove(itemStack);
        model = model.getOverrides().resolve(model, itemStack, Minecraft.getInstance().level, holder, 0);

        // Recursion lock
        ItemStack nStack = itemStack.copy();
        nStack.getOrCreateTag().putBoolean(TAGGED_SPECIAL_RENDER, true);
        ENTITY_CACHE.put(nStack, holder);
        if (ORIGINAL_STACK_CACHE.size() > 32) {
            ORIGINAL_STACK_CACHE.clear();
            Changed.LOGGER.error("Memory leak detected in ItemRendererMixin");
        }
        ORIGINAL_STACK_CACHE.put(nStack, itemStack);
        if (model != null)
            self.render(nStack, type, leftHand, pose, buffers, packedLight, packedOverlay, model);
        callback.cancel();
    }

//    @WrapOperation(method = "render", at = @At(
//            value = "INVOKE",
//            target = "Lnet/minecraftforge/client/ForgeHooksClient;handleCameraTransforms(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemDisplayContext;Z)Lnet/minecraft/client/resources/model/BakedModel;",
//            remap = false))
//    public BakedModel getOverriddenModel(PoseStack poseStack, BakedModel model, ItemDisplayContext cameraTransformType, boolean applyLeftHandTransform, Operation<BakedModel> original,
//                                         @Local(argsOnly = true) ItemStack stack) {
//        if (stack.getItem() instanceof SpecializedItemRendering special) {
//            var modelName = special.getModelLocation(stack, cameraTransformType);
//            if (modelName != null) {
//                var newModel = this.getItemModelShaper().getModelManager().getModel(modelName);
//                if (newModel != model && cachedStack == stack) {
//                    ClientLevel clientlevel = cachedLevel instanceof ClientLevel ? (ClientLevel) cachedLevel : null;
//                    newModel = newModel.getOverrides().resolve(newModel, stack, clientlevel, cachedEntity, cachedSeed);
//                }
//
//                model = newModel;
//            }
//        }
//
//        return original.call(poseStack, model, cameraTransformType, applyLeftHandTransform);
//    }

    @WrapOperation(method = "render", at = @At(
            value = "INVOKE",
            // 注意：1.18.2 的 TransformType 是 ItemTransforms 的内部类，签名里要用 $ 符号
            target = "Lnet/minecraftforge/client/ForgeHooksClient;handleCameraTransforms(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;Z)Lnet/minecraft/client/resources/model/BakedModel;",
            remap = false))
    public BakedModel getOverriddenModel(PoseStack poseStack, BakedModel model,
                                         ItemTransforms.TransformType cameraTransformType, // 类型变了
                                         boolean applyLeftHandTransform,
                                         Operation<BakedModel> original,
                                         @Local(argsOnly = true) ItemStack stack) {

        // 你需要确保你的接口 SpecializedItemRendering 也迁移到了 TransformType
        if (stack.getItem() instanceof SpecializedItemRendering special) {
            var modelLocation = special.getModelLocation(stack, cameraTransformType);

            if (modelLocation != null) {
                // 1.18.2 itemModelShaper 通常是 protected，可能需要 Shadow 或者用 Accessor
                // 或者直接用 getItemModelShaper() 如果它是 public 的
                var newModel = this.getItemModelShaper().getModelManager().getModel(modelLocation);

                if (newModel != model) {
                    // --- 1.18.2 难点：上下文缺失 ---
                    // 1.18 的 ItemRenderer 没有 cachedLevel/cachedEntity 字段。
                    // 我们只能尝试获取当前客户端的主世界和主玩家。
                    // 警告：这会导致其他实体拿着这个物品时，Override 动画（如拉弓）可能失效或显示为主玩家的状态。
                    ClientLevel level = Minecraft.getInstance().level;
                    LivingEntity entity = Minecraft.getInstance().player;
                    int seed = 0; // 无法获取随机种子

                    // 如果你想完美解决，你需要 Mixin ItemRenderer.render 的上游调用者来捕获实体，但这非常复杂。
                    // 对于大多数 GUI/第一人称渲染，用 MC.player 足够了。
                    newModel = newModel.getOverrides().resolve(newModel, stack, level, entity, seed);
                }

                model = newModel;
            }
        }

        return original.call(poseStack, model, cameraTransformType, applyLeftHandTransform);
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void renderPost(ItemStack itemStack, ItemTransforms.TransformType type, boolean leftHand, PoseStack pose,
                       MultiBufferSource buffers, int packedLight, int packedOverlay, BakedModel model, CallbackInfo callback) {
        LivingEntity holder = ENTITY_CACHE.remove(itemStack);
        ItemStack original = ORIGINAL_STACK_CACHE.remove(itemStack);
        if (itemStack.getTag() == null || !itemStack.getTag().contains(TAGGED_SPECIAL_RENDER))
            return;
        if (!(itemStack.getItem() instanceof SpecializedItemRendering special))
            return;

        // GUI 场景不要叠任何层（避免图标出现奇怪叠加）
        if (SpecializedItemRendering.isGUI(type))
            return;

        ModelResourceLocation location = special.getModelLocation(itemStack, type);
        if (location == null)
            return;
        ItemRenderer self = (ItemRenderer)(Object)this;
        model = self.getItemModelShaper().getModelManager().getModel(location);
        if (original != null)
            model = model.getOverrides().resolve(model, original, Minecraft.getInstance().level, holder, 0);

        pose.pushPose();
        model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(pose, model, type, leftHand);

        pose.translate(-0.5D, -0.5D, -0.5D);

        RenderType renderType = ItemLayerModel.getLayerRenderType(true);
        ForgeHooksClient.setRenderType(renderType); // needed for compatibility with MultiLayerModels
        VertexConsumer vertexBuilder = ItemRenderer.getFoilBufferDirect(buffers, renderType, true, itemStack.hasFoil());
        self.renderModelLists(model, itemStack, LightTexture.FULL_BRIGHT, packedOverlay, pose, vertexBuilder);
        ForgeHooksClient.setRenderType(null);

        pose.popPose();
    }
}
