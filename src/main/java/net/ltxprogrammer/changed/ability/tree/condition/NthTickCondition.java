package net.ltxprogrammer.changed.ability.tree.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ltxprogrammer.changed.aaBackport.CodecWrapperAC;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.data.RegistryElementPredicate;
import net.ltxprogrammer.changed.init.ChangedAbilityTreeCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class NthTickCondition extends AbstractCondition {
    public final int tickRate;

    public static final Codec<NthTickCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("tickRate").forGetter(condition -> condition.tickRate)
    ).apply(instance, NthTickCondition::new));

    public NthTickCondition(int tickRate) {
        this.tickRate = tickRate;
    }

    @Override
    public boolean test(IAbstractChangedEntity entity) {
        return entity.getEntity().tickCount % tickRate == 0;
    }

    @Override
    public Codec<? extends AbstractCondition> getCodec() {
        return CODEC;
    }

    @Override
    public CodecWrapperAC<? extends AbstractCondition> getCodecWrapper() {
        return ChangedAbilityTreeCodecs.NTH_TICK_CONDITION.get();
    }
}
