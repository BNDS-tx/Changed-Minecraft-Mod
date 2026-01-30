package net.ltxprogrammer.changed.datagen;

import net.ltxprogrammer.changed.Changed;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GatherData {

//    @SubscribeEvent
//    public static void onGatherData(GatherDataEvent event) {
//        DataGenerator generator = event.getGenerator();
//        ExistingFileHelper helper = event.getExistingFileHelper();
//        PackOutput packOutput = generator.getPackOutput();
//        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
//        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
//        // All the Variables upwards of this comment may become needed by a specific kind of datagen
//
//        generator.addProvider(true, new AccessoryEntityProvider(generator));
//        generator.addProvider(true, new TFTagsProvider(packOutput, lookupProvider, helper));
//
//        CompletableFuture<HolderLookup.Provider> lookup0 =
//                generator.addProvider(event.includeServer(), new DatapackEntriesProvider(packOutput, lookupProvider)).getRegistryProvider();
//
//        generator.addProvider(event.includeServer(), new DamageTypeTagProvider(packOutput, lookup0, helper));
//
////        BlockTagsProvider blocks = new BlockTagsProvider(packOutput, lookupProvider, helper);
////        generator.addProvider(true, blocks);
////        generator.addProvider(true, new ItemTagsProvider(generator, lookupProvider, blocks.contentsGetter(), existingFileHelper));
////        generator.addProvider(true, new FluidTagsProvider(packOutput, lookupProvider, helper));
////
////        generator.addProvider(true, new EntityTypeTagsProvider(packOutput, lookupProvider, helper));
////        generator.addProvider(true, new CAPaintingVariantTagsProvider(packOutput, lookupProvider, helper));
////
////        generator.addProvider(event.includeServer(), new BiomeTagProvider(packOutput, lookup0, helper));
////
////        generator.addProvider(true, new RecipeProvider(packOutput));
////
////        generator.addProvider(true, new LootTableProvider(packOutput));
////
////        generator.addProvider(true, new BlockStateProvider(packOutput, helper));
////        generator.addProvider(true, new ItemModelProvider(packOutput, helper));
////        generator.addProvider(new AdvancementProvider(generator, helper));
//
//    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        // =========================================
        // 服务端数据 (Server Data) - Tag, Recipe, Loot, Advancements
        // =========================================
        if (event.includeServer()) {
            // 1. 自定义 Provider (按照之前的迁移修改构造函数)
            // AccessoryEntityProvider 只需传入 generator
            generator.addProvider(new AccessoryEntityProvider(generator));

            // TFTagsProvider 传入 generator 和 helper (无需 lookupProvider)
            generator.addProvider(new TFTagsProvider(generator, helper));

            // 2. 核心 Tag (方块 & 物品)
            // 1.18.2 的 BlockTagsProvider 构造函数: (Generator, ModId, Helper)
            BlockTagsProvider blockTags = new BlockTagsProvider(generator, Changed.MODID, helper);
            generator.addProvider(blockTags);

            // ItemTagsProvider 需要依赖 BlockTagsProvider
            // 构造函数: (Generator, BlockTagsProvider, ModId, Helper)
            generator.addProvider(new ItemTagsProvider(generator, blockTags, Changed.MODID, helper));

            // 3. 其他 Tags
            // FluidTagsProvider, EntityTypeTagsProvider 等通常类似
            generator.addProvider(new FluidTagsProvider(generator, Changed.MODID, helper));
            generator.addProvider(new EntityTypeTagsProvider(generator, Changed.MODID, helper));

            // 4. 配方 (Recipe)
            // 你需要自己有一个类继承自 net.minecraft.data.recipes.RecipeProvider
            // generator.addProvider(new MyRecipeProvider(generator));

            // 5. 战利品表 (LootTable)
            // 1.18.2 的 LootTableProvider 写法比较复杂，通常需要自定义一个子类
            // generator.addProvider(new MyLootTableProvider(generator));

            // =========================================
            // 已移除/不可用的 1.20 特性
            // =========================================
            // [移除] DatapackEntriesProvider:
            // 1.18.2 不支持通过代码动态生成 Biome/Structure 的注册表数据。
            // 解决方案：直接编写 JSON 文件放入 resources/data/changed/worldgen/ 目录下。

            // [移除] DamageTypeTagProvider:
            // 1.18.2 没有伤害类型注册表，伤害源是硬编码的字符串 (DamageSource.IN_FIRE)。
            // 解决方案：删除该 Provider，代码逻辑改为检查 source.msgId 等于特定字符串。
        }

        // =========================================
        // 客户端资源 (Client Assets) - Models, BlockStates, Lang
        // =========================================
        if (event.includeClient()) {
            // 1. BlockState & Models
            // 构造函数: (Generator, ModId, Helper)
            generator.addProvider(new net.minecraftforge.client.model.generators.BlockStateProvider(generator, Changed.MODID, helper) {
                @Override
                protected void registerStatesAndModels() {
                    // 你的方块状态注册逻辑，或者指向你单独的类
                }
            });

            generator.addProvider(new net.minecraftforge.client.model.generators.ItemModelProvider(generator, Changed.MODID, helper) {
                @Override
                protected void registerModels() {
                    // 你的物品模型注册逻辑
                }
            });

            // 2. 语言文件 (Lang)
            // generator.addProvider(new LanguageProvider(generator, Changed.MODID, "en_us") { ... });
        }
    }
}
