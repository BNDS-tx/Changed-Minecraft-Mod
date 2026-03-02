package net.ltxprogrammer.changed.fluid;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.init.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

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

    public static FluidType createFluidType() {
        return new FluidType(AbstractLatexFluid.createProperties().descriptionId("dark_latex")) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {
                    private static final ResourceLocation DARK_LATEX_STILL = Changed.modResource("block/dark_latex_block_top");
                    private static final ResourceLocation DARK_LATEX_FLOW = Changed.modResource("block/dark_latex_fluid_flow");
                    private static final ResourceLocation DARK_LATEX_OVERLAY = Changed.modResource("block/dark_latex_fluid_overlay");
                    private static final ResourceLocation DARK_LATEX_RENDER_OVERLAY = Changed.modResource("textures/block/dark_latex_fluid_flow.png");

                    public ResourceLocation getStillTexture() {
                        return DARK_LATEX_STILL;
                    }

                    public ResourceLocation getFlowingTexture() {
                        return DARK_LATEX_FLOW;
                    }

                    @Override
                    public @Nullable ResourceLocation getOverlayTexture() {
                        return DARK_LATEX_OVERLAY;
                    }

                    @Override
                    public @Nullable ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                        return DARK_LATEX_RENDER_OVERLAY;
                    }
                });
            }

            @Override
            public boolean canDrownIn(LivingEntity entity) {
                return !ChangedLatexTypes.DARK_LATEX.get().isFriendlyTo(LatexType.getEntityLatexType(entity));
            }
        };
    }

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