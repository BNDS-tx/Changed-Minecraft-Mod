package net.ltxprogrammer.changed.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.client.gui.AbilityOverlay;
import net.ltxprogrammer.changed.client.gui.GrabOverlay;
import net.ltxprogrammer.changed.client.gui.TransfurProgressOverlay;
import net.ltxprogrammer.changed.client.gui.VariantBlindnessOverlay;
import net.ltxprogrammer.changed.entity.LivingEntityDataExtension;
import net.ltxprogrammer.changed.fluid.TransfurGas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Changed.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedOverlays {
    protected static final ResourceLocation VIGNETTE_LOCATION = new ResourceLocation("textures/misc/vignette.png");

    public static final ResourceLocation DANGER_OVERLAY = Changed.modResource("danger");
    public static final ResourceLocation ABILITY_OVERLAY = Changed.modResource("ability");
    public static final ResourceLocation GRABBED_OVERLAY = Changed.modResource("grabbed");
    public static final ResourceLocation GAS_VFX_OVERLAY = Changed.modResource("gas_vfx");
    public static final ResourceLocation VARIANT_BLINDNESS_OVERLAY = Changed.modResource("variant_blindness");

//    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
//        event.registerAboveAll(DANGER_OVERLAY.getPath(), (gui, graphics, partialTick, screenWidth, screenHeight) -> {
//            gui.setupOverlayRenderState(true, false);
//            TransfurProgressOverlay.renderDangerOverlay(gui, graphics, partialTick, screenWidth, screenHeight);
//        });
//        event.registerAbove(DANGER_OVERLAY, ABILITY_OVERLAY.getPath(), (gui, graphics, partialTick, screenWidth, screenHeight) -> {
//            gui.setupOverlayRenderState(true, false);
//            AbilityOverlay.renderSelectedAbility(gui, graphics, partialTick, screenWidth, screenHeight);
//        });
//        event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), GRABBED_OVERLAY.getPath(), GrabOverlay::renderProgressBars);
//        event.registerAbove(VanillaGuiOverlay.VIGNETTE.id(), GAS_VFX_OVERLAY.getPath(), (gui, graphics, partialTick, screenWidth, screenHeight) -> {
//            var cameraEntity = Minecraft.getInstance().cameraEntity;
//
//            if (cameraEntity instanceof LivingEntityDataExtension ext) {
//                ext.isEyeInGas(TransfurGas.class).map(TransfurGas::getColor).ifPresent(color -> {
//                    RenderSystem.disableDepthTest();
//                    RenderSystem.depthMask(false);
//                    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
//                    RenderSystem.setShaderColor(color.red(), color.green(), color.blue(), 1.0F);
//
//                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
//                    RenderSystem.setShaderTexture(0, VIGNETTE_LOCATION);
//                    Tesselator tesselator = Tesselator.getInstance();
//                    BufferBuilder bufferbuilder = tesselator.getBuilder();
//                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//                    bufferbuilder.vertex(0.0D, screenHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
//                    bufferbuilder.vertex(screenWidth, screenHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
//                    bufferbuilder.vertex(screenWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
//                    bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
//                    tesselator.end();
//                    RenderSystem.depthMask(true);
//                    RenderSystem.enableDepthTest();
//                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//                    RenderSystem.defaultBlendFunc();
//                });
//            }
//        });
//        event.registerBelowAll(VARIANT_BLINDNESS_OVERLAY.getPath(), VariantBlindnessOverlay::render);
//    }

    public static void registerOverlays(FMLClientSetupEvent event) {
        // 1. DANGER_OVERLAY (Top)
        // 1.20: event.registerAboveAll(...)
        // 1.18: OverlayRegistry.registerOverlayTop(...)
        var DANGER_ELEMENT = OverlayRegistry.registerOverlayTop(DANGER_OVERLAY.getPath(), (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            // 注意：你需要修改 TransfurProgressOverlay.renderDangerOverlay 的定义，将 GuiGraphics 参数改为 PoseStack
            TransfurProgressOverlay.renderDangerOverlay(gui, poseStack, partialTick, screenWidth, screenHeight);
        });

        // 2. ABILITY_OVERLAY (Above DANGER)
        // 1.18 中，我们需要通过 ResourceLocation 找到上一个 Overlay 的引用，或者直接持有引用
        // 这里假设 DANGER_OVERLAY 是一个 ResourceLocation，我们用它的 path 作为 ID 查找
        // 如果 DANGER_OVERLAY 刚刚被注册，这种查找是安全的
        OverlayRegistry.registerOverlayAbove(DANGER_ELEMENT, ABILITY_OVERLAY.getPath(), (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            // 同样，修改 renderSelectedAbility 的参数为 PoseStack
            AbilityOverlay.renderSelectedAbility(gui, poseStack, partialTick, screenWidth, screenHeight);
        });

        // 3. GRABBED_OVERLAY (Above Experience Bar)
        // 1.20: VanillaGuiOverlay.EXPERIENCE_BAR.id()
        // 1.18: ForgeIngameGui.EXPERIENCE_BAR_ELEMENT
        // GrabOverlay::renderProgressBars 也需要适配 PoseStack
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.EXPERIENCE_BAR_ELEMENT, GRABBED_OVERLAY.getPath(), GrabOverlay::renderProgressBars);

        // 4. GAS_VFX_OVERLAY (Above Vignette)
        // 1.20: VanillaGuiOverlay.VIGNETTE.id()
        // 1.18: ForgeIngameGui.VIGNETTE_ELEMENT
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.VIGNETTE_ELEMENT, GAS_VFX_OVERLAY.getPath(), (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            var cameraEntity = Minecraft.getInstance().cameraEntity;

            if (cameraEntity instanceof LivingEntityDataExtension ext) {
                ext.isEyeInGas(TransfurGas.class).map(TransfurGas::getColor).ifPresent(color -> {
                    // 这部分 Tesselator 渲染代码是纯 OpenGL 操作，与版本无关，直接保留即可
                    RenderSystem.disableDepthTest();
                    RenderSystem.depthMask(false);
                    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                    RenderSystem.setShaderColor(color.red(), color.green(), color.blue(), 1.0F);

                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderTexture(0, VIGNETTE_LOCATION);
                    Tesselator tesselator = Tesselator.getInstance();
                    BufferBuilder bufferbuilder = tesselator.getBuilder();
                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                    bufferbuilder.vertex(0.0D, screenHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
                    bufferbuilder.vertex(screenWidth, screenHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
                    bufferbuilder.vertex(screenWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
                    bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
                    tesselator.end();

                    RenderSystem.depthMask(true);
                    RenderSystem.enableDepthTest();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.defaultBlendFunc();
                });
            }
        });

        // 5. VARIANT_BLINDNESS_OVERLAY (Bottom)
        // 1.20: event.registerBelowAll(...)
        // 1.18: OverlayRegistry.registerOverlayBottom(...)
        OverlayRegistry.registerOverlayBottom(VARIANT_BLINDNESS_OVERLAY.getPath(), VariantBlindnessOverlay::render);
    }
}
