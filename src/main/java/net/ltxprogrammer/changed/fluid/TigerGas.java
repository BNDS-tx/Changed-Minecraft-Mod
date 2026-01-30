package net.ltxprogrammer.changed.fluid;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.init.ChangedFluids;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Consumer;

public abstract class TigerGas extends TransfurGas {
//    public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(
//            ChangedFluids.TIGER_TRANSFUR_GAS, ChangedFluids.TIGER_GAS, ChangedFluids.TIGER_GAS_FLOWING)
//            .tickRate(4)
//            .levelDecreasePerBlock(1)
//            .explosionResistance(100f)
//            .block(ChangedBlocks.TIGER_GAS);
//
//    public static FluidType createFluidType() {
//        return new GasFluidType(Gas.createProperties().descriptionId("tiger_transfur_gas")) {
//            @Override
//            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
//                consumer.accept(new IClientFluidTypeExtensions() {
//                    private static final ResourceLocation GAS_STILL = Changed.modResource("block/tiger_gas");
//                    private static final ResourceLocation GAS_FLOW = Changed.modResource("block/tiger_gas");
//
//                    public ResourceLocation getStillTexture() {
//                        return GAS_STILL;
//                    }
//
//                    public ResourceLocation getFlowingTexture() {
//                        return GAS_FLOW;
//                    }
//
//                    public int getTintColor() {
//                        return 0x7FFFFFFF;
//                    }
//                });
//            }
//        };
//    }

    public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(
            ChangedFluids.TIGER_GAS,           // 静态流体源 (Source)
            ChangedFluids.TIGER_GAS_FLOWING,   // 流动流体 (Flowing)

            // --- 核心融合点：这里对应 1.20.1 的 FluidType 和 initializeClient ---
            FluidAttributes.builder(
                            Changed.modResource("block/tiger_gas"), // 对应 1.20.1 的 getStillTexture
                            Changed.modResource("block/tiger_gas")  // 对应 1.20.1 的 getFlowingTexture
                    )
                    // 物理属性 (来自 1.20.1 的 createProperties)
                    .viscosity(200)        // 粘度
                    .density(200)          // 密度
                    .gaseous()             // 对应 Gas 类型 (1.18.2 中通常用这个来标记气体，使其向上流动或表现为气体)

                    // 视觉属性 (来自 1.20.1 的 initializeClient)
                    .color(0x7FFFFFFF)     // 对应 getTintColor (ARGB)

                    // 声音 (1.20.1 代码没展示，但气体通常需要，否则无声)
                    .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)
    )
            // --- 逻辑属性 (来自 1.20.1 的 ForgeFlowingFluid.Properties) ---
            .tickRate(4)                   // 扩散速度
            .levelDecreasePerBlock(1)      // 流动衰减
            .explosionResistance(100f)     // 抗爆性
            .block(ChangedBlocks.TIGER_GAS); // 对应的方块

    protected TigerGas() {
        super(PROPERTIES, ChangedTransfurVariants.GAS_TIGER);
    }

    @Override
    public Color3 getColor() {
        return Color3.fromInt(0xffba44);
    }

    @Override
    public BlockState createLegacyBlock(FluidState fluidState) {
        return ChangedBlocks.TIGER_GAS.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
    }

    public static class Flowing extends TigerGas {
        public Flowing() {
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends TigerGas {
        public Source() {}

        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
