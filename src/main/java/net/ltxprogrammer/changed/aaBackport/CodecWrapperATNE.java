package net.ltxprogrammer.changed.aaBackport;

import com.mojang.serialization.Codec;
import net.ltxprogrammer.changed.ability.tree.AbilityTree;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class CodecWrapper<T extends AbilityTree.NodeEffect> implements IForgeRegistryEntry<CodecWrapper<? extends AbilityTree.NodeEffect>> {
    // 关键点1：这里用 ? extends T，允许存储子类的 Codec
    private final Codec<? extends AbilityTree.NodeEffect> codec;
    private ResourceLocation registryName;

    // 关键点2：构造函数接收 ? extends T
    public CodecWrapper(Codec<? extends AbilityTree.NodeEffect> codec) {
        this.codec = codec;
    }

    // 关键点3：Getter 返回 ? extends T
    public Codec<? extends AbilityTree.NodeEffect> getCodec() {
        return codec;
    }

    @Override
    public CodecWrapper<? extends AbilityTree.NodeEffect> setRegistryName(ResourceLocation name) {
        this.registryName = name;
        return this;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return this.registryName;
    }

    @Override
    public Class<CodecWrapper<? extends AbilityTree.NodeEffect>> getRegistryType() {
        return (Class<CodecWrapper<? extends AbilityTree.NodeEffect>>) (Class<?>) CodecWrapper.class;
    }
}
