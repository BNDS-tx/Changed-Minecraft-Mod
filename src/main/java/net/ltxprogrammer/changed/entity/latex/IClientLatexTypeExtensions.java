package net.ltxprogrammer.changed.entity.latex;

import net.ltxprogrammer.changed.util.Color3;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public interface IClientLatexTypeExtensions {
    IClientLatexTypeExtensions DEFAULT = new IClientLatexTypeExtensions() { };

    enum CoverRenderLayer {
        AUTO,
        CUTOUT,
        TRANSLUCENT
    }

    ResourceLocation RENDER_TYPE_SOLID = new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "solid");
    ResourceLocation RENDER_TYPE_CUTOUT = new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "cutout");
    ResourceLocation RENDER_TYPE_TRANSLUCENT = new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "translucent");

    static IClientLatexTypeExtensions of(LatexCoverState state) {
        return of(state.getType());
    }

    static IClientLatexTypeExtensions of(LatexType type) {
        return type.getRenderPropertiesInternal() instanceof IClientLatexTypeExtensions props ? props : DEFAULT;
    }

    default Color3 getColor() {
        return Color3.WHITE;
    }

    default CoverRenderLayer getCoverRenderLayer() {
        return CoverRenderLayer.AUTO;
    }

    default ResourceLocation getTextureForFace(Direction face) {
        return null;
    }

    default ResourceLocation getTextureForParticle() {
        return getTextureForFace(Direction.UP);
    }

    default ResourceLocation getNamedRenderType() {
        return RENDER_TYPE_SOLID;
    }
}
