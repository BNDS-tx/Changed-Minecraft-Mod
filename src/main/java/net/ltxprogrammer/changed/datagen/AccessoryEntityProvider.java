package net.ltxprogrammer.changed.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.data.AccessorySlotType;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static net.ltxprogrammer.changed.init.ChangedAccessorySlots.*;


public class AccessoryEntityProvider implements DataProvider {

//    private static final Logger LOGGER = LogUtils.getLogger();
//    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
//
//    // The Changed Mod Objects are registered too late soo is need to make static supplier a method
//    // private static Supplier<AccessorySlotType[]> humanoidSlots = () -> new AccessorySlotType[]{BODY.get(), FULL_BODY.get(), LEGS.get(), HANDS.get()};
//
//    protected final String modId;
//    private final DataGenerator generator;
//    private final Map<String, Appender> appenders = new HashMap<>();
//
//    public AccessoryEntityProvider(DataGenerator generator) {
//        this(generator, Changed.MODID);
//    }
//
//    public AccessoryEntityProvider(DataGenerator generator, String modId) {
//        this.generator = generator;
//        this.modId = modId;
//    }
//
//    @Override
//    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
//        registerEntityAccessories();
//
//        List<CompletableFuture<?>> list = new ArrayList<>();
//        Path basePath = this.generator.getPackOutput().getOutputFolder();
//        Appender appender;
//        for (Map.Entry<String, Appender> entry : appenders.entrySet()) {
//            appender = entry.getValue();
//
//            if (appender.isInvalid()) {
//                LOGGER.error("{} Provider: Appender for file {} is missing entities or slots!", getName(), entry.getKey());
//                continue;
//            }
//
//            Path path = createPath(basePath, entry.getKey());
//            list.add(DataProvider.saveStable(cache, appender.toJson(), path));
//        }
//
//        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
//    }
//
//    public Appender add(@NotNull String fileName) {
//        return appenders.computeIfAbsent(fileName, f -> new Appender());
//    }
//
//    protected void registerEntityAccessories() {
//    }
//
//    private Path createPath(Path base, String fileName) {
//        return base.resolve("data/" + modId + "/accessories/entities/" + fileName + ".json");
//    }
//
//    @Override
//    public @NotNull String getName() {
//        return "Accessory Entities";
//    }
//
//    @ParametersAreNonnullByDefault
//    public static class Appender {
//
//        private final Set<EntityType<?>> entities = new HashSet<>();
//        private final Set<TagKey<EntityType<?>>> entityTypesTags = new HashSet<>();
//        private final Set<AccessorySlotType> slots = new ObjectArraySet<>();
//
//        private Appender() {
//        }
//
//        public Appender entity(EntityType<?> entity) {
//            entities.add(entity);
//            return this;
//        }
//
//        public Appender entities(EntityType<?>... entities) {
//            Collections.addAll(this.entities, entities);
//            return this;
//        }
//
//        public Appender entityTypesTag(TagKey<EntityType<?>> entityTypeTagKey) {
//            this.entityTypesTags.add(entityTypeTagKey);
//            return this;
//        }
//
//        @SafeVarargs
//        public final Appender entityTypesTags(TagKey<EntityType<?>>... entityTypeTagKey) {
//            Collections.addAll(this.entityTypesTags, entityTypeTagKey);
//            return this;
//        }
//
//        public Appender slot(AccessorySlotType slot) {
//            slots.add(slot);
//            return this;
//        }
//
//        public Appender slots(AccessorySlotType... slots) {
//            Collections.addAll(this.slots, slots);
//            return this;
//        }
//
//        private boolean isInvalid() {
//            return (entities.isEmpty() && entityTypesTags.isEmpty()) || slots.isEmpty();
//        }
//
//        private JsonObject toJson() {
//            JsonObject root = new JsonObject();
//            JsonArray entityAr = new JsonArray();
//            JsonArray slotAr = new JsonArray();
//            root.add("entities", entityAr);
//            root.add("slots", slotAr);
//
//            for (EntityType<?> type : entities) {
//                entityAr.add(ForgeRegistries.ENTITY_TYPES.getKey(type).toString());
//            }
//
//            for (TagKey<EntityType<?>> typeTagKey : entityTypesTags) {
//                entityAr.add("#" + typeTagKey.location());
//            }
//
//            for (AccessorySlotType slot : slots) {
//                slotAr.add(ChangedRegistry.ACCESSORY_SLOTS.getKey(slot).toString());
//            }
//
//            return root;
//        }
//    }

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected final String modId;
    private final DataGenerator generator;
    private final Map<String, Appender> appenders = new HashMap<>();

    public AccessoryEntityProvider(DataGenerator generator) {
        this(generator, Changed.MODID);
    }

    public AccessoryEntityProvider(DataGenerator generator, String modId) {
        this.generator = generator;
        this.modId = modId;
    }

    @Override
    public void run(@NotNull HashCache cache) throws IOException { // 1.18.2: void run(HashCache) throws IOException
        registerEntityAccessories();

        // 1.18.2: 直接从 generator 获取路径，没有 getPackOutput()
        Path basePath = this.generator.getOutputFolder();
        Appender appender;

        for (Map.Entry<String, Appender> entry : appenders.entrySet()) {
            appender = entry.getValue();

            if (appender.isInvalid()) {
                LOGGER.error("{} Provider: Appender for file {} is missing entities or slots!", getName(), entry.getKey());
                continue;
            }

            Path path = createPath(basePath, entry.getKey());

            // 1.18.2: 同步保存，必须传入 GSON 实例
            DataProvider.save(GSON, cache, appender.toJson(), path);
        }
        // 不需要 return CompletableFuture
    }

    public Appender add(@NotNull String fileName) {
        return appenders.computeIfAbsent(fileName, f -> new Appender());
    }

    protected void registerEntityAccessories() {
    }

    private Path createPath(Path base, String fileName) {
        return base.resolve("data/" + modId + "/accessories/entities/" + fileName + ".json");
    }

    @Override
    public @NotNull String getName() {
        return "Accessory Entities";
    }

    @ParametersAreNonnullByDefault
    public static class Appender {

        private final Set<EntityType<?>> entities = new HashSet<>();
        private final Set<TagKey<EntityType<?>>> entityTypesTags = new HashSet<>();
        private final Set<AccessorySlotType> slots = new ObjectArraySet<>();

        private Appender() {
        }

        public Appender entity(EntityType<?> entity) {
            entities.add(entity);
            return this;
        }

        public Appender entities(EntityType<?>... entities) {
            Collections.addAll(this.entities, entities);
            return this;
        }

        public Appender entityTypesTag(TagKey<EntityType<?>> entityTypeTagKey) {
            this.entityTypesTags.add(entityTypeTagKey);
            return this;
        }

        @SafeVarargs
        public final Appender entityTypesTags(TagKey<EntityType<?>>... entityTypeTagKey) {
            Collections.addAll(this.entityTypesTags, entityTypeTagKey);
            return this;
        }

        public Appender slot(AccessorySlotType slot) {
            slots.add(slot);
            return this;
        }

        public Appender slots(AccessorySlotType... slots) {
            Collections.addAll(this.slots, slots);
            return this;
        }

        private boolean isInvalid() {
            return (entities.isEmpty() && entityTypesTags.isEmpty()) || slots.isEmpty();
        }

        private JsonObject toJson() {
            JsonObject root = new JsonObject();
            JsonArray entityAr = new JsonArray();
            JsonArray slotAr = new JsonArray();
            root.add("entities", entityAr);
            root.add("slots", slotAr);

            for (EntityType<?> type : entities) {
                // 确保 ForgeRegistries 在 1.18.2 可用，通常是没问题的
                entityAr.add(Objects.requireNonNull(ForgeRegistries.ENTITIES.getKey(type)).toString());
            }

            for (TagKey<EntityType<?>> typeTagKey : entityTypesTags) {
                entityAr.add("#" + typeTagKey.location());
            }

            for (AccessorySlotType slot : slots) {
                // 如果 ChangedRegistry.ACCESSORY_SLOTS 是 DeferredRegister，这里可能需要改成 getKey(slot) 的正确调用方式
                // 假设 1.20 代码能跑，这里的逻辑在 1.18 应该也是通用的（只要 ACCESSORY_SLOTS 类型没变）
                slotAr.add(ChangedRegistry.ACCESSORY_SLOTS.getKey(slot).toString());
            }

            return root;
        }
    }
}
