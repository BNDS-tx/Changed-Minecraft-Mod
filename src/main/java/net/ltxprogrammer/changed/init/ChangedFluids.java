//package net.ltxprogrammer.changed.init;
//
//import net.ltxprogrammer.changed.Changed;
//import net.ltxprogrammer.changed.fluid.*;
//import net.minecraft.client.renderer.ItemBlockRenderTypes;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.fluids.ForgeFlowingFluid;
//import net.minecraftforge.fml.DistExecutor;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;
//
//import javax.annotation.Nullable;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.function.Consumer;
//import java.util.function.Supplier;
//
//public class ChangedFluids {
//    public static final DeferredRegister<Fluid> REGISTRY_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUIDS, Changed.MODID);
//    public static final DeferredRegister<Fluid> REGISTRY_FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Changed.MODID);
//
//    public static final Collection<Runnable> APPLY_RENDER_LAYERS = new ArrayList<>();
//
//    public static final RegistryObject<ForgeFlowingFluid> DARK_LATEX_FLUID = REGISTRY_TYPES.register("dark_latex", () -> new ForgeFlowingFluid.Source(DarkLatexFluid.PROPERTIES));
//    public static final RegistryObject<ForgeFlowingFluid> WHITE_LATEX_FLUID = REGISTRY_TYPES.register("white_latex", () -> new ForgeFlowingFluid.Source(WhiteLatexFluid.PROPERTIES));
//
//    public static final RegistryObject<ForgeFlowingFluid> WOLF_TRANSFUR_GAS = REGISTRY_TYPES.register("wolf_transfur_gas", () -> new ForgeFlowingFluid.Source(WolfGas.PROPERTIES));
//    public static final RegistryObject<ForgeFlowingFluid> TIGER_TRANSFUR_GAS = REGISTRY_TYPES.register("tiger_transfur_gas", () -> new ForgeFlowingFluid.Source(TigerGas.PROPERTIES));
//    public static final RegistryObject<ForgeFlowingFluid> SKUNK_TRANSFUR_GAS = REGISTRY_TYPES.register("skunk_gas", () -> new ForgeFlowingFluid.Source(SkunkGas.PROPERTIES));
//
//    public static final RegistryObject<AbstractLatexFluid> DARK_LATEX = REGISTRY_FLUIDS.register("dark_latex", DarkLatexFluid.Source::new);
//    public static final RegistryObject<AbstractLatexFluid> DARK_LATEX_FLOWING = REGISTRY_FLUIDS.register("dark_latex_flowing", DarkLatexFluid.Flowing::new);
//    public static final RegistryObject<AbstractLatexFluid> WHITE_LATEX = REGISTRY_FLUIDS.register("white_latex", WhiteLatexFluid.Source::new);
//    public static final RegistryObject<AbstractLatexFluid> WHITE_LATEX_FLOWING = REGISTRY_FLUIDS.register("white_latex_flowing", WhiteLatexFluid.Flowing::new);
//
//    public static final RegistryObject<TransfurGas> WOLF_GAS = register("wolf_gas", WolfGas.Source::new, ChangedFluids::translucentRenderer);
//    public static final RegistryObject<TransfurGas> WOLF_GAS_FLOWING = register("wolf_gas_flowing", WolfGas.Flowing::new, ChangedFluids::translucentRenderer);
//
//    public static final RegistryObject<TransfurGas> TIGER_GAS = register("tiger_gas", TigerGas.Source::new, ChangedFluids::translucentRenderer);
//    public static final RegistryObject<TransfurGas> TIGER_GAS_FLOWING = register("tiger_gas_flowing", TigerGas.Flowing::new, ChangedFluids::translucentRenderer);
//
//    public static final RegistryObject<TransfurGas> SKUNK_GAS = register("skunk_gas", SkunkGas.Source::new, ChangedFluids::translucentRenderer);
//    public static final RegistryObject<TransfurGas> SKUNK_GAS_FLOWING = register("skunk_gas_flowing", SkunkGas.Flowing::new, ChangedFluids::translucentRenderer);
//
//    public static void cutoutRenderer(Fluid fluid) {
//        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
//                ItemBlockRenderTypes.setRenderLayer(fluid, net.minecraft.client.renderer.RenderType.cutout()));
//    }
//
//    public static void translucentRenderer(Fluid fluid) {
//        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
//                ItemBlockRenderTypes.setRenderLayer(fluid, net.minecraft.client.renderer.RenderType.translucent()));
//    }
//
//    public static <T extends Fluid, F extends T> RegistryObject<T> register(String name, Supplier<F> fluid) {
//        return register(name, fluid, null);
//    }
//
//    public static <T extends Fluid, F extends T> RegistryObject<T> register(String name, Supplier<F> fluid, @Nullable Consumer<T> renderLayer) {
//        var obj = REGISTRY_FLUIDS.<T>register(name, fluid);
//        if (renderLayer != null)
//            APPLY_RENDER_LAYERS.add(() -> renderLayer.accept(obj.get()));
//        return obj;
//    }
//}

package net.ltxprogrammer.changed.init;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.fluid.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ChangedFluids {
    // 1.18.2 不需要 FluidType 注册表
    public static final DeferredRegister<Fluid> REGISTRY_FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Changed.MODID);

    // 用于在客户端初始化时应用渲染层（需要在 ClientSetup 中调用 run()）
    public static final Collection<Runnable> APPLY_RENDER_LAYERS = new ArrayList<>();

    // ------------------------------------------------------------------------------------------------
    // 注册流体
    // 注意：这里的构造函数引用 (DarkLatexFluid.Source::new) 会在流体类加载时自动读取 Properties
    // ------------------------------------------------------------------------------------------------

    // Dark Latex
    public static final RegistryObject<AbstractLatexFluid> DARK_LATEX = REGISTRY_FLUIDS.register("dark_latex", DarkLatexFluid.Source::new);
    public static final RegistryObject<AbstractLatexFluid> DARK_LATEX_FLOWING = REGISTRY_FLUIDS.register("dark_latex_flowing", DarkLatexFluid.Flowing::new);

    // White Latex
    public static final RegistryObject<AbstractLatexFluid> WHITE_LATEX = REGISTRY_FLUIDS.register("white_latex", WhiteLatexFluid.Source::new);
    public static final RegistryObject<AbstractLatexFluid> WHITE_LATEX_FLOWING = REGISTRY_FLUIDS.register("white_latex_flowing", WhiteLatexFluid.Flowing::new);

    // Gases (带半透明渲染)
    public static final RegistryObject<TransfurGas> WOLF_GAS = register("wolf_gas", WolfGas.Source::new, ChangedFluids::translucentRenderer);
    public static final RegistryObject<TransfurGas> WOLF_GAS_FLOWING = register("wolf_gas_flowing", WolfGas.Flowing::new, ChangedFluids::translucentRenderer);

    public static final RegistryObject<TransfurGas> TIGER_GAS = register("tiger_gas", TigerGas.Source::new, ChangedFluids::translucentRenderer);
    public static final RegistryObject<TransfurGas> TIGER_GAS_FLOWING = register("tiger_gas_flowing", TigerGas.Flowing::new, ChangedFluids::translucentRenderer);

    public static final RegistryObject<TransfurGas> SKUNK_GAS = register("skunk_gas", SkunkGas.Source::new, ChangedFluids::translucentRenderer);
    public static final RegistryObject<TransfurGas> SKUNK_GAS_FLOWING = register("skunk_gas_flowing", SkunkGas.Flowing::new, ChangedFluids::translucentRenderer);

    // ------------------------------------------------------------------------------------------------
    // 渲染辅助方法
    // ------------------------------------------------------------------------------------------------

    public static void cutoutRenderer(Fluid fluid) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                ItemBlockRenderTypes.setRenderLayer(fluid, RenderType.cutout()));
    }

    public static void translucentRenderer(Fluid fluid) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                ItemBlockRenderTypes.setRenderLayer(fluid, RenderType.translucent()));
    }

    public static <T extends Fluid, F extends T> RegistryObject<T> register(String name, Supplier<F> fluid) {
        return register(name, fluid, null);
    }

    public static <T extends Fluid, F extends T> RegistryObject<T> register(String name, Supplier<F> fluid, @Nullable Consumer<T> renderLayer) {
        RegistryObject<T> obj = REGISTRY_FLUIDS.register(name, fluid);
        if (renderLayer != null) {
            APPLY_RENDER_LAYERS.add(() -> renderLayer.accept(obj.get()));
        }
        return obj;
    }
}