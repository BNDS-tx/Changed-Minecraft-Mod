package net.ltxprogrammer.changed.client.renderer.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModelPicker;
import net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.extension.ChangedCompatibility;
import net.ltxprogrammer.changed.world.enchantments.FormFittingEnchantment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class LatexHumanoidArmorLayer<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>, A extends LatexHumanoidArmorModel<T, ?>> extends RenderLayer<T, M> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    final AdvancedHumanoidRenderer<T, M, A> parent;
    public final ArmorModelPicker<? super T> modelPicker;

    public LatexHumanoidArmorLayer(AdvancedHumanoidRenderer<T, M, A> parentModel, ArmorModelPicker<? super T> modelPicker) {
        super(parentModel);
        this.parent = parentModel;
        this.modelPicker = modelPicker;
    }

    public void render(PoseStack pose, MultiBufferSource buffers, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!parent.shouldRenderArmor(entity)) return;

        if (parent.getModel(entity) instanceof AdvancedHumanoidModelInterface advancedModel)
            this.modelPicker.applyAnimatorProperties(entity, advancedModel.getAnimator(entity));
        this.modelPicker.prepareAndSetupModels(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        boolean firstPerson = ChangedCompatibility.isFirstPersonRendering();

        if (!firstPerson || !entity.isVisuallySwimming()) // Don't render chest-plate if swimming in first person
            this.renderArmorPiece(pose, buffers, entity, EquipmentSlot.CHEST, packedLight, this.getArmorModel(entity, EquipmentSlot.CHEST));
        this.renderArmorPiece(pose, buffers, entity, EquipmentSlot.LEGS, packedLight, this.getArmorModel(entity, EquipmentSlot.LEGS));
        this.renderArmorPiece(pose, buffers, entity, EquipmentSlot.FEET, packedLight, this.getArmorModel(entity, EquipmentSlot.FEET));
        if (!firstPerson) // Don't render helmet if first person; only really applies to first person mods
            this.renderArmorPiece(pose, buffers, entity, EquipmentSlot.HEAD, packedLight, this.getArmorModel(entity, EquipmentSlot.HEAD));
    }

    private void renderArmorPiece(PoseStack pose, MultiBufferSource buffers, T entity, EquipmentSlot slot, int packedLight, LatexHumanoidArmorModel<? super T, ?> model) {
        ItemStack itemstack = FormFittingEnchantment.getFormFitted(entity, entity.getItemBySlot(slot), slot);
        if (itemstack.getItem() instanceof ArmorItem) {
            ArmorItem armoritem = (ArmorItem)itemstack.getItem();
            if (armoritem.getSlot() == slot) {
                boolean foil = itemstack.hasFoil();
                var altModel = net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemstack, slot, model);
                if (altModel != model) {
                    if (armoritem instanceof net.minecraft.world.item.DyeableLeatherItem) {
                        int i = ((net.minecraft.world.item.DyeableLeatherItem)armoritem).getColor(itemstack);
                        float red = (float)(i >> 16 & 255) / 255.0F;
                        float green = (float)(i >> 8 & 255) / 255.0F;
                        float blue = (float)(i & 255) / 255.0F;
                        this.renderModel(pose, buffers, packedLight,
                                foil, altModel, red, green, blue, this.getArmorResource(entity, itemstack, slot, null));
                        this.renderModel(pose, buffers, packedLight,
                                foil, altModel, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slot, "overlay"));
                    } else {
                        this.renderModel(pose, buffers, packedLight,
                                foil, altModel, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slot, null));
                    }
                }

                else {
                    if (armoritem instanceof net.minecraft.world.item.DyeableLeatherItem) {
                        int i = ((net.minecraft.world.item.DyeableLeatherItem)armoritem).getColor(itemstack);
                        float red = (float)(i >> 16 & 255) / 255.0F;
                        float green = (float)(i >> 8 & 255) / 255.0F;
                        float blue = (float)(i & 255) / 255.0F;
                        this.renderModel(entity, itemstack, slot, pose, buffers, packedLight,
                                foil, model, red, green, blue, this.getArmorResource(entity, itemstack, slot, null));
                        this.renderModel(entity, itemstack, slot, pose, buffers, packedLight,
                                foil, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slot, "overlay"));
                    } else {
                        this.renderModel(entity, itemstack, slot, pose, buffers, packedLight,
                                foil, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slot, null));
                    }
                }
            }
        }
    }

    private void renderModel(T entity, ItemStack stack, EquipmentSlot slot,
                             PoseStack pose, MultiBufferSource buffers, int packedLight, boolean foil, LatexHumanoidArmorModel<? super T, ?> model,
                             float red, float green, float blue, ResourceLocation armorResource) {
        model.prepareVisibility(slot, stack);
        model.renderForSlot(entity, (RenderLayerParent) this.parent, stack, slot, pose,
                ItemRenderer.getArmorFoilBuffer(buffers, RenderType.armorCutoutNoCull(armorResource), false, foil),
                packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
        model.prepareVisibility(slot, stack);
    }

    private void renderModel(PoseStack pose, MultiBufferSource buffers, int packedLight, boolean foil, net.minecraft.client.model.Model model, float red, float green, float blue, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffers, RenderType.armorCutoutNoCull(armorResource), false, foil);
        model.renderToBuffer(pose, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

    public LatexHumanoidArmorModel<? super T, ?> getArmorModel(T entity, EquipmentSlot slot) {
        return modelPicker.getModelForSlot(entity, slot);
    }

    private boolean usesInnerModel(EquipmentSlot p_117129_) {
        return p_117129_ == EquipmentSlot.LEGS;
    }

    /*=================================== FORGE START =========================================*/

    /**
     * More generic ForgeHook version of the above function, it allows for Items to have more control over what texture they provide.
     *
     * @param entity Entity wearing the armor
     * @param stack ItemStack for the armor
     * @param slot Slot ID that the item is in
     * @param type Subtype, can be null or "overlay"
     * @return ResourceLocation pointing at the armor's texture
     */
    public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
        ArmorItem item = (ArmorItem)stack.getItem();
        String texture = item.getMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format(java.util.Locale.ROOT, "%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (usesInnerModel(slot) ? 2 : 1), type == null ? "" : String.format(java.util.Locale.ROOT, "_%s", type));

        s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);

        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
        }

        return resourcelocation;
    }
    /*=================================== FORGE END ===========================================*/
}