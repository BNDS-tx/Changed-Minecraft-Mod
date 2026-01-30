package net.ltxprogrammer.changed.aaBackport;

import com.mojang.serialization.Codec;
import net.ltxprogrammer.changed.ability.tree.condition.AbstractCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class CodecWrapperAC<T extends AbstractCondition> implements IForgeRegistryEntry<CodecWrapperAC<? extends AbstractCondition>> {
    // 关键点1：这里用 ? extends T，允许存储子类的 Codec
    private final Codec<? extends AbstractCondition> codec;
    private ResourceLocation registryName;

    // 关键点2：构造函数接收 ? extends T
    public CodecWrapperAC(Codec<? extends AbstractCondition> codec) {
        this.codec = codec;
    }

    // 关键点3：Getter 返回 ? extends T
    public Codec<? extends AbstractCondition> getCodec() {
        return codec;
    }

    @Override
    public CodecWrapperAC<? extends AbstractCondition> setRegistryName(ResourceLocation name) {
        this.registryName = name;
        return this;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return this.registryName;
    }

    @Override
    public Class<CodecWrapperAC<? extends AbstractCondition>> getRegistryType() {
        return (Class<CodecWrapperAC<? extends AbstractCondition>>) (Class<?>) CodecWrapperAC.class;
    }
}
