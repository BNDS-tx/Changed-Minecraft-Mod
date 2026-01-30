package net.ltxprogrammer.changed.datagen;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeRegistryTagsProvider;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

//public class TFTagsProvider extends TagsProvider<TransfurVariant<?>> {
//
//    public TFTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
//        super(output, ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(), pLookupProvider, Changed.MODID, existingFileHelper);
//    }
//
//    @Override
//    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
//    }
//
//    private static <T extends ChangedEntity> ResourceKey<TransfurVariant<?>> cast(RegistryObject<TransfurVariant<T>> key){
//        return (ResourceKey<TransfurVariant<?>>) (Object) key.getKey();
//    }
//
//    protected TagAppender<TransfurVariant<?>> addAllMatching(TagAppender<TransfurVariant<?>> tag, Predicate<TransfurVariant<?>> predicate) {
//        for (Map.Entry<ResourceKey<TransfurVariant<?>>, TransfurVariant<?>> entry : ChangedRegistry.TRANSFUR_VARIANT.get().getEntries()) {
//            if (predicate.test(entry.getValue())) tag.add(entry.getKey());
//        }
//
//        return tag;
//    }
//
//    @Override
//    public @NotNull String getName() {
//        return "Transfur Type Tags";
//    }
//}

public class TFTagsProvider extends ForgeRegistryTagsProvider<TransfurVariant<?>> {

    // 构造函数：移除 PackOutput 和 CompletableFuture，改为 DataGenerator
    public TFTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        // super 参数：Generator, Registry 实例, ModID, FileHelper
        // 这里假设 ChangedRegistry.TRANSFUR_VARIANT.get() 返回的是 IForgeRegistry 实例
        super(generator, ChangedRegistry.TRANSFUR_VARIANT.get(), Changed.MODID, existingFileHelper);
    }

    // 1.18.2 的 addTags 没有参数
    @Override
    protected void addTags() {
        // 在这里调用 tag(Key).add(...)
    }

    // 辅助方法：RegistryObject 转 ResourceKey (1.18.2 写法)
    // 注意：RegistryObject.getKey() 直接返回 ResourceKey<T>，通常不需要强转
    @SuppressWarnings("unchecked")
    private static <T extends ChangedEntity> ResourceKey<TransfurVariant<?>> cast(RegistryObject<TransfurVariant<T>> key){
        return (ResourceKey<TransfurVariant<?>>) (Object) key.getKey();
    }

    // 移植后的 addAllMatching
    // 1.18.2 的 IForgeRegistry.getEntries() 返回 Set<Map.Entry<ResourceKey<T>, T>>，逻辑与 1.20 几乎一致
    protected TagsProvider.TagAppender<TransfurVariant<?>> addAllMatching(TagsProvider.TagAppender<TransfurVariant<?>> tag, Predicate<TransfurVariant<?>> predicate) {
        for (Map.Entry<ResourceKey<TransfurVariant<?>>, TransfurVariant<?>> entry : ChangedRegistry.TRANSFUR_VARIANT.get().getEntries()) {
            if (predicate.test(entry.getValue())) {
                tag.add(entry.getKey());
            }
        }
        return tag;
    }

    @Override
    public @NotNull String getName() {
        return "Transfur Type Tags";
    }
}

