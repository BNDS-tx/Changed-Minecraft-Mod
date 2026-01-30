package net.ltxprogrammer.changed.ability.tree.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ltxprogrammer.changed.aaBackport.CodecWrapperAC;
import net.ltxprogrammer.changed.aaBackport.EntityBackportHelper;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.data.RegistryElementPredicate;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilityTreeCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.function.BiPredicate;

public class InFluidCondition extends AbstractCondition {
    public enum Qualification implements StringRepresentable, BiPredicate<IAbstractChangedEntity, RegistryElementPredicate<Fluid>> {
        TOUCHING("touching", (entity, fluidType) -> {
            return EntityBackportHelper.isEntityInFluid(entity.getEntity(), (testFluid, height) -> fluidType.test(testFluid));
        }),
        SUBMERGED("submerged", (entity, fluidType) -> {
            return fluidType.test(EntityBackportHelper.getEyeInFluid(entity.getEntity()).orElse(null));
        });

        public static Codec<Qualification> CODEC = Codec.STRING.comapFlatMap(Qualification::fromSerial, Qualification::getSerializedName);

        public final String serialName;
        public final BiPredicate<IAbstractChangedEntity, RegistryElementPredicate<Fluid>> predicate;

        Qualification(String serialName, BiPredicate<IAbstractChangedEntity, RegistryElementPredicate<Fluid>> predicate) {
            this.serialName = serialName;
            this.predicate = predicate;
        }

        @Override
        public boolean test(IAbstractChangedEntity entity, RegistryElementPredicate<Fluid> fluidType) {
            return predicate.test(entity, fluidType);
        }

        @Override
        public String getSerializedName() {
            return serialName;
        }

        public static DataResult<Qualification> fromSerial(String name) {
            return Arrays.stream(values()).filter(type -> type.serialName.equals(name))
                    .findFirst().map(DataResult::success).orElseGet(() -> DataResult.error(name + " is not a valid Qualification"));
        }
    }

    public final RegistryElementPredicate<Fluid> fluid;
    public final Qualification qualification;

    public static final Codec<InFluidCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryElementPredicate.codec(ForgeRegistries.FLUIDS).fieldOf("fluid").forGetter(condition -> condition.fluid),
            Qualification.CODEC.fieldOf("qualification").forGetter(condition -> condition.qualification)
    ).apply(instance, InFluidCondition::new));

    public InFluidCondition(RegistryElementPredicate<Fluid> fluid, Qualification qualification) {
        this.fluid = fluid;
        this.qualification = qualification;
    }

    @Override
    public boolean test(IAbstractChangedEntity entity) {
        return qualification.test(entity, fluid);
    }

    @Override
    public Codec<? extends AbstractCondition> getCodec() {
        return CODEC;
    }

    @Override
    public CodecWrapperAC<? extends AbstractCondition> getCodecWrapper() {
        return ChangedAbilityTreeCodecs.IN_FLUID_CONDITION.get();
    }
}
