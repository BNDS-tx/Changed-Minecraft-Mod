package net.ltxprogrammer.changed.init;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.world.features.structures.ChestLootTableProcessor;
import net.ltxprogrammer.changed.world.features.structures.GluReplacementProcessor;
import net.ltxprogrammer.changed.world.features.structures.HangingBlockFixerProcessor;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber
public class ChangedFeatures {
    public static final DeferredRegister<Feature<?>> REGISTRY_FEATURE = DeferredRegister.create(ForgeRegistries.FEATURES, Changed.MODID);

    public static final DeferredRegister<StructureProcessorType<?>> REGISTRY_PROCESSOR = DeferredRegister.create(Registry.STRUCTURE_PROCESSOR_REGISTRY, Changed.MODID);
    public static RegistryObject<StructureProcessorType<ChestLootTableProcessor>> CHEST_LOOT_TABLE_PROCESSOR = REGISTRY_PROCESSOR.register("chest_loot_table_processor",
            () -> () -> ChestLootTableProcessor.CODEC);
    public static RegistryObject<StructureProcessorType<GluReplacementProcessor>> GLU_REPLACEMENT_PROCESSOR = REGISTRY_PROCESSOR.register("glu_replacement_processor",
            () -> () -> GluReplacementProcessor.CODEC);
    public static RegistryObject<StructureProcessorType<HangingBlockFixerProcessor>> HANGING_BLOCK_FIXER_PROCESSOR = REGISTRY_PROCESSOR.register("hanging_block_fixer_processor",
            () -> () -> HangingBlockFixerProcessor.CODEC);

    // Defined in data/changed/worldgen/configured_feature/orange_tree.json (1.18.2 has no FeatureUtils.createKey)
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORANGE_TREE =
            ResourceKey.create(BuiltinRegistries.CONFIGURED_FEATURE.key(), Changed.modResource("orange_tree"));
    // TODO: replace orange tree and tree feature mixin with an orange bush.
}
