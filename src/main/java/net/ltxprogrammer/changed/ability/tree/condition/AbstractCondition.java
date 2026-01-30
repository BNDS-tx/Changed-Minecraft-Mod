package net.ltxprogrammer.changed.ability.tree.condition;

import com.mojang.serialization.Codec;
import net.ltxprogrammer.changed.aaBackport.CodecWrapperAC;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedRegistry;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractCondition implements Predicate<IAbstractChangedEntity> {
    public static final Codec<AbstractCondition> CONDITION_CODEC = ChangedRegistry.ABILITY_EFFECT_CONDITIONS.get().getCodec().dispatch("type",
            AbstractCondition::getCodecWrapper, CodecWrapperAC::getCodec);

    public abstract Codec<? extends AbstractCondition> getCodec();

    public abstract CodecWrapperAC<? extends AbstractCondition> getCodecWrapper();
}
