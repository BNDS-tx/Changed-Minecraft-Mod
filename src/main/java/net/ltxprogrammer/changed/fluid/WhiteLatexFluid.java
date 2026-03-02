package net.ltxprogrammer.changed.fluid;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.init.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.List;

public abstract class WhiteLatexFluid extends AbstractLatexFluid {
    public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(
            ChangedFluids.WHITE_LATEX,           // 静态流体源 (Source)
            ChangedFluids.WHITE_LATEX_FLOWING,   // 流动流体 (Flowing)

            // --- 核心融合点：这里对应 1.20.1 的 FluidType 和 initializeClient ---
            FluidAttributes.builder(
                            Changed.modResource("block/white_latex_block_top"), // 对应 1.20.1 的 getStillTexture
                            Changed.modResource("block/white_latex_fluid_flow")  // 对应 1.20.1 的 getFlowingTexture
                    )
                    .overlay(Changed.modResource("block/white_latex_fluid_overlay"))
                    // 物理属性 (来自 1.20.1 的 createProperties)
                    .viscosity(6000)        // 粘度
                    .density(6000)          // 密度

                    // 声音 (1.20.1 代码没展示，但气体通常需要，否则无声)
                    .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)
                    .translationKey("block." + Changed.MODID + ".white_latex")
    )
            // --- 逻辑属性 (来自 1.20.1 的 ForgeFlowingFluid.Properties) ---
            .tickRate(50)                   // 扩散速度
            .levelDecreasePerBlock(9999)      // 流动衰减
            .explosionResistance(100f)     // 抗爆性
            .bucket(ChangedItems.WHITE_LATEX_BUCKET)
            .block(ChangedBlocks.WHITE_LATEX_FLUID); // 对应的方块

    public WhiteLatexFluid() {
        super(PROPERTIES, ChangedLatexTypes.WHITE_LATEX, List.of(ChangedTransfurVariants.PURE_WHITE_LATEX_WOLF));
    }

    public BlockState createLegacyBlock(FluidState p_76466_) {
        return ChangedBlocks.WHITE_LATEX_FLUID.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(p_76466_));
    }

    @Override
    public boolean canEntityStandOn(LivingEntity entity) {
        return this.getLatexType().isFriendlyTo(LatexType.getEntityLatexType(entity));
    }

    public static class Source extends WhiteLatexFluid {
        public Source() {
            super();
        }

        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }

    public static class Flowing extends WhiteLatexFluid {
        public Flowing() {
            super();
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
}
