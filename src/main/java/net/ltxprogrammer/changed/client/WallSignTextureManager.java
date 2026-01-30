package net.ltxprogrammer.changed.client;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.decoration.WallSignVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.stream.Stream;

public class WallSignTextureManager extends TextureAtlasHolder {
    private static final ResourceLocation BACK_SPRITE_LOCATION = Changed.modResource("back");

    public WallSignTextureManager(TextureManager textureManager) {
        super(textureManager, Changed.modResource("textures/atlas/wall_signs.png"), "wall_sign");
    }

    public TextureAtlasSprite get(WallSignVariant variant) {
        return this.getSprite(ChangedRegistry.WALL_SIGN_VARIANT.getKey(variant));
    }

    public TextureAtlasSprite getBackSprite() {
        return this.getSprite(BACK_SPRITE_LOCATION);
    }

    @Override
    protected Stream<ResourceLocation> getResourcesToLoad() {
        // 1. 获取所有注册的变体纹理 ID
        Stream<ResourceLocation> variantTextures = ChangedRegistry.WALL_SIGN_VARIANT.getKeys().stream();

        // 2. 将 "back" 纹理 ID 合并进去
        return Stream.concat(variantTextures, Stream.of(BACK_SPRITE_LOCATION));
    }
}
