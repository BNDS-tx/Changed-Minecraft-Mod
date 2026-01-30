//package net.ltxprogrammer.changed.fluid;
//
//import net.ltxprogrammer.changed.Changed;
//import net.ltxprogrammer.changed.entity.latex.LatexType;
//import net.ltxprogrammer.changed.init.*;
//import net.minecraft.core.BlockPos;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.block.LiquidBlock;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.StateDefinition;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.level.material.FluidState;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.fluids.FluidAttributes;
//import net.minecraftforge.fluids.ForgeFlowingFluid;
//
//import java.util.List;
//import java.util.function.Consumer;
//
//public abstract class DarkLatexFluid extends AbstractLatexFluid {
////    public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(
////            ChangedFluids.DARK_LATEX_FLUID, ChangedFluids.DARK_LATEX, ChangedFluids.DARK_LATEX_FLOWING)
////            .tickRate(50)
////            .levelDecreasePerBlock(9999)
////            .explosionResistance(100f)
////            .bucket(ChangedItems.DARK_LATEX_BUCKET)
////            .block(ChangedBlocks.DARK_LATEX_FLUID);
////
////    public static FluidType createFluidType() {
////        return new FluidType(AbstractLatexFluid.createProperties().descriptionId("dark_latex")) {
////            @Override
////            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
////                consumer.accept(new IClientFluidTypeExtensions() {
////                    private static final ResourceLocation DARK_LATEX_STILL = Changed.modResource("block/dark_latex_block_top");
////                    private static final ResourceLocation DARK_LATEX_FLOW = Changed.modResource("block/dark_latex_block_top");
////
////                    public ResourceLocation getStillTexture() {
////                        return DARK_LATEX_STILL;
////                    }
////
////                    public ResourceLocation getFlowingTexture() {
////                        return DARK_LATEX_FLOW;
////                    }
////                });
////            }
////        };
////    }
//
//    public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(
//            ChangedFluids.DARK_LATEX,           // 静态流体源 (Source)
//            ChangedFluids.DARK_LATEX_FLOWING,   // 流动流体 (Flowing)
//
//            // --- 核心融合点：这里对应 1.20.1 的 FluidType 和 initializeClient ---
//            FluidAttributes.builder(
//                            Changed.modResource("block/dark_latex_block_top"), // 对应 1.20.1 的 getStillTexture
//                            Changed.modResource("block/dark_latex_block_top")  // 对应 1.20.1 的 getFlowingTexture
//                    )
//                    // 物理属性 (来自 1.20.1 的 createProperties)
//                    .viscosity(6000)        // 粘度
//                    .density(6000)          // 密度
//
//                    // 声音 (1.20.1 代码没展示，但气体通常需要，否则无声)
//                    .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)
//    )
//            // --- 逻辑属性 (来自 1.20.1 的 ForgeFlowingFluid.Properties) ---
//            .tickRate(50)                   // 扩散速度
//            .levelDecreasePerBlock(9999)      // 流动衰减
//            .explosionResistance(100f)     // 抗爆性
//            .bucket(ChangedItems.DARK_LATEX_BUCKET)
//            .block(ChangedBlocks.DARK_LATEX_FLUID); // 对应的方块
//
//    protected DarkLatexFluid() {
//        super(PROPERTIES, ChangedLatexTypes.DARK_LATEX, List.of(ChangedTransfurVariants.DARK_LATEX_WOLF_MALE, ChangedTransfurVariants.DARK_LATEX_WOLF_FEMALE, ChangedTransfurVariants.DARK_LATEX_YUFENG));
//    }
//
//    public BlockState createLegacyBlock(FluidState p_76466_) {
//        return ChangedBlocks.DARK_LATEX_FLUID.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(p_76466_));
//    }
//
//    @Override
//    public boolean canEntityStandOn(LivingEntity entity) {
//        return this.getLatexType().isFriendlyTo(LatexType.getEntityLatexType(entity));
//    }
//
//    public static class Source extends DarkLatexFluid {
//        public Source() {
//            super();
//        }
//
//        public int getAmount(FluidState state) {
//            return 8;
//        }
//
//        public boolean isSource(FluidState state) {
//            return true;
//        }
//    }
//
//    public static class Flowing extends DarkLatexFluid {
//        public Flowing() {
//            super();
//        }
//
//        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
//            super.createFluidStateDefinition(builder);
//            builder.add(LEVEL);
//        }
//
//        public int getAmount(FluidState state) {
//            return state.getValue(LEVEL);
//        }
//
//        public boolean isSource(FluidState state) {
//            return false;
//        }
//    }
//}

package net.ltxprogrammer.changed.fluid;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.init.*;
import net.minecraft.resources.ResourceLocation;
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

public abstract class DarkLatexFluid extends AbstractLatexFluid {
    // 定义材质路径 (从 1.20.1 的 createFluidType 中提取)
    private static final ResourceLocation STILL_TEXTURE = Changed.modResource("block/dark_latex_block_top");
    private static final ResourceLocation FLOWING_TEXTURE = Changed.modResource("block/dark_latex_block_top");
    // 如果有 Overlay 贴图，可以在这里定义

    public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(
            ChangedFluids.DARK_LATEX,           // Source Fluid (Supplier)
            ChangedFluids.DARK_LATEX_FLOWING,   // Flowing Fluid (Supplier)
            // 1.18.2 核心差异：使用 FluidAttributes.builder 定义材质和声音
            FluidAttributes.builder(STILL_TEXTURE, FLOWING_TEXTURE)
                    .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY) // 默认声音，可根据需要修改
                    .translationKey("block." + Changed.MODID + ".dark_latex_fluid") // 对应 1.20 的 descriptionId
    )
            .tickRate(50)
            .levelDecreasePerBlock(9999)
            .explosionResistance(100f)
            .bucket(ChangedItems.DARK_LATEX_BUCKET)
            .block(ChangedBlocks.DARK_LATEX_FLUID);

    protected DarkLatexFluid() {
        super(PROPERTIES, ChangedLatexTypes.DARK_LATEX, List.of(ChangedTransfurVariants.DARK_LATEX_WOLF_MALE, ChangedTransfurVariants.DARK_LATEX_WOLF_FEMALE, ChangedTransfurVariants.DARK_LATEX_YUFENG));
    }

    @Override
    public BlockState createLegacyBlock(FluidState state) {
        return ChangedBlocks.DARK_LATEX_FLUID.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public boolean canEntityStandOn(LivingEntity entity) {
        return this.getLatexType().isFriendlyTo(LatexType.getEntityLatexType(entity));
    }

    public static class Source extends DarkLatexFluid {
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

    public static class Flowing extends DarkLatexFluid {
        public Flowing() {
            super();
        }

        @Override
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