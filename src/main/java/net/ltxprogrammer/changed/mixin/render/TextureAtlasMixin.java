package net.ltxprogrammer.changed.mixin.render;

import net.ltxprogrammer.changed.client.TextureAtlasExtender;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Mixin(TextureAtlas.class)
public abstract class TextureAtlasMixin implements TextureAtlasExtender {
    @Shadow @Final private Map<ResourceLocation, TextureAtlasSprite> texturesByName;

    @Override
    public Stream<TextureAtlasSprite> getSprites() {
        return texturesByName.values().stream();
    }

    @Override
    public int getWidth() {
        return getSprites().findAny().map(sprite -> {
            return (int)((float)(sprite.getX() + sprite.getWidth()) / sprite.getU1());
        }).orElseThrow();
    }

    @Override
    public int getHeight() {
        return getSprites().findAny().map(sprite -> {
            return (int)((float)(sprite.getY() + sprite.getHeight()) / sprite.getV1());
        }).orElseThrow();
    }
}
