package net.ltxprogrammer.changed.world.features.structures.facility;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedStructurePieceTypes;
import net.ltxprogrammer.changed.util.TagUtil;
import net.ltxprogrammer.changed.world.data.ActiveFacilityInstance;
import net.ltxprogrammer.changed.world.data.ChangedGameDataAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

///**
// * Meta piece that holds all the properties of the facility
// * Does not place any blocks
// * Is used to create a facility data object to handle spawns and facility operations
// */
//public class FacilityKeystone extends StructurePiece {
//    private ActiveFacilityInstance.Header header;
//    private Map<Zone, List<Pair<ResourceLocation, BoundingBox>>> piecesByZone;
//
//    private static final Codec<Pair<ResourceLocation, BoundingBox>> PIECE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
//            ResourceLocation.CODEC.fieldOf("name").forGetter(Pair::getFirst),
//            BoundingBox.CODEC.fieldOf("region").forGetter(Pair::getSecond)
//    ).apply(instance, Pair::of));
//
//    private static final Codec<Map<Zone, List<Pair<ResourceLocation, BoundingBox>>>> PIECES_BY_ZONE_CODEC = Codec.unboundedMap(
//            ChangedRegistry.FACILITY_ZONES.get().getCodec(),
//            Codec.list(PIECE_CODEC)
//    );
//
//    public FacilityKeystone(int genDepth, Map<Zone, List<Pair<ResourceLocation, BoundingBox>>> piecesByZone, BoundingBox entrance, Random random) {
//        super(ChangedStructurePieceTypes.FACILITY_KEYSTONE.get(), genDepth, entrance);
//
//        this.piecesByZone = piecesByZone;
//        header = new ActiveFacilityInstance.Header();
//        header.initialize(BoundingBox.encapsulatingBoxes(piecesByZone.values().stream().flatMap(List::stream).map(Pair::getSecond)::iterator).orElseThrow(() -> {
//            return new IllegalStateException("Unable to calculate boundingbox without pieces");
//        }), random);
//    }
//
//    public FacilityKeystone(StructureTemplateManager manager, CompoundTag tag) {
//        super(ChangedStructurePieceTypes.FACILITY_KEYSTONE.get(), tag);
//
//        this.header = ActiveFacilityInstance.Header.CODEC.decode(NbtOps.INSTANCE, tag.get("header")).getOrThrow(false, onError -> {}).getFirst();
//        this.piecesByZone = PIECES_BY_ZONE_CODEC.decode(NbtOps.INSTANCE, tag.get("piecesByZone")).getOrThrow(true, onError -> {}).getFirst();
//    }
//
//    @Override
//    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
//        tag.put("header",
//                ActiveFacilityInstance.Header.CODEC.encodeStart(NbtOps.INSTANCE, header).getOrThrow(false, onError -> {})
//        );
//        tag.put("piecesByZone",
//                PIECES_BY_ZONE_CODEC.encodeStart(NbtOps.INSTANCE, piecesByZone).getOrThrow(false, onError -> {})
//        );
//    }
//
//    @Override
//    public void postProcess(WorldGenLevel level, StructureManager p_226770_, ChunkGenerator p_226771_, Random p_226772_, BoundingBox p_226773_, ChunkPos p_226774_, BlockPos p_226775_) {
//        if (level.getLevel() instanceof ChangedGameDataAccessor gameDataAccessor) {
//            CompletableFuture.runAsync(() -> {
//                String resourceName = this.header.getResourceName();
//                if (gameDataAccessor.getChangedGameData().facilities.stream().anyMatch(facility -> facility.getHeader().getResourceName().equals(resourceName)))
//                    return; // Already tracked; don't duplicate
//                gameDataAccessor.getChangedGameData().trackNewFacility(this.createActiveFacilityInstance());
//            }, level.getServer());
//        }
//    }
//
//    public ActiveFacilityInstance createActiveFacilityInstance() {
//        try {
//            var zoneInfoBuilder = ImmutableMap.<Zone, ActiveFacilityInstance.ZoneInfo>builder();
//
//            piecesByZone.forEach((zone, boundingBox) -> {
//                zoneInfoBuilder.put(zone, new ActiveFacilityInstance.ZoneInfo(
//                        FacilityZoneEntities.INSTANCE.getSpawns(zone).stream().map(ActiveFacilityInstance.SpawnInfo::new).toList(),
//                        boundingBox.stream().map(pair -> new ActiveFacilityInstance.PieceInfo(pair.getFirst(), pair.getSecond())).toList(),
//                        Optional.empty()));
//            });
//
//            var facilityInstance = new ActiveFacilityInstance(zoneInfoBuilder.build(), Optional.empty());
//            facilityInstance.setHeader(header);
//            return facilityInstance;
//        } catch (Exception e) {
//            Changed.LOGGER.error("Exception while creating ActiveFacilityInstance", e);
//            throw e;
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "Site " + header.name;
//    }
//}

/**
 * Meta piece that holds all the properties of the facility
 * Does not place any blocks
 * Is used to create a facility data object to handle spawns and facility operations
 */
public class FacilityKeystone extends StructurePiece {
    private ActiveFacilityInstance.Header header;
    private Map<Zone, List<Pair<ResourceLocation, BoundingBox>>> piecesByZone;

    private static final Codec<Pair<ResourceLocation, BoundingBox>> PIECE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("name").forGetter(Pair::getFirst),
            BoundingBox.CODEC.fieldOf("region").forGetter(Pair::getSecond)
    ).apply(instance, Pair::of));

    private static final Codec<Map<Zone, List<Pair<ResourceLocation, BoundingBox>>>> PIECES_BY_ZONE_CODEC = Codec.unboundedMap(
            ChangedRegistry.FACILITY_ZONES.get().getCodec(),
            Codec.list(PIECE_CODEC)
    );

    // 生成时使用的构造函数
    public FacilityKeystone(int genDepth, Map<Zone, List<Pair<ResourceLocation, BoundingBox>>> piecesByZone, BoundingBox entrance, Random random) {
        super(ChangedStructurePieceTypes.FACILITY_KEYSTONE.get(), genDepth, entrance);

        this.piecesByZone = piecesByZone;
        header = new ActiveFacilityInstance.Header();
        header.initialize(BoundingBox.encapsulatingBoxes(piecesByZone.values().stream().flatMap(List::stream).map(Pair::getSecond)::iterator).orElseThrow(() -> {
            return new IllegalStateException("Unable to calculate BoundingBox without pieces");
        }), random);
    }

    // 加载时使用的构造函数 (1.18.2 通常只需要 CompoundTag)
    // 如果你在注册 PieceType 时指定了 Contextless，则用此签名
    public FacilityKeystone(StructureManager structureManager, CompoundTag tag) {
        super(ChangedStructurePieceTypes.FACILITY_KEYSTONE.get(), tag);

        // 1.18.2 的 getOrThrow 参数签名通常也是兼容的
        this.header = ActiveFacilityInstance.Header.CODEC.decode(NbtOps.INSTANCE, tag.get("header"))
                .getOrThrow(false, onError -> {}).getFirst();
        this.piecesByZone = PIECES_BY_ZONE_CODEC.decode(NbtOps.INSTANCE, tag.get("piecesByZone"))
                .getOrThrow(true, onError -> {}).getFirst();
    }

    // 保存数据的方法 (1.18.2 参数通常包含 Context，但在旧版映射或部分 Forge 版本中可能只有 CompoundTag)
    // 标准 1.18.2 Forge 映射下: protected void addAdditionalSaveData(CompoundTag tag)
    // 如果你的开发环境提示需要 StructurePieceSerializationContext，说明你混用了版本，但纯 1.18.2 是不需要 Context 的
    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.put("header",
                ActiveFacilityInstance.Header.CODEC.encodeStart(NbtOps.INSTANCE, header).getOrThrow(false, onError -> {})
        );
        tag.put("piecesByZone",
                PIECES_BY_ZONE_CODEC.encodeStart(NbtOps.INSTANCE, piecesByZone).getOrThrow(false, onError -> {})
        );
    }

    // 核心生成/后处理方法
    @Override
    public void postProcess(WorldGenLevel level, StructureFeatureManager structureManager, ChunkGenerator generator, Random random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        // 逻辑保持不变，除了参数类型变化
        if (level.getLevel() instanceof ChangedGameDataAccessor gameDataAccessor) {
            CompletableFuture.runAsync(() -> {
                String resourceName = this.header.getResourceName();
                if (gameDataAccessor.getChangedGameData().facilities.stream().anyMatch(facility -> facility.getHeader().getResourceName().equals(resourceName)))
                    return; // Already tracked; don't duplicate
                gameDataAccessor.getChangedGameData().trackNewFacility(this.createActiveFacilityInstance());
            }, level.getServer());
        }
    }

    public ActiveFacilityInstance createActiveFacilityInstance() {
        try {
            var zoneInfoBuilder = ImmutableMap.<Zone, ActiveFacilityInstance.ZoneInfo>builder();

            piecesByZone.forEach((zone, boundingBox) -> {
                zoneInfoBuilder.put(zone, new ActiveFacilityInstance.ZoneInfo(
                        FacilityZoneEntities.INSTANCE.getSpawns(zone).stream().map(ActiveFacilityInstance.SpawnInfo::new).toList(),
                        boundingBox.stream().map(pair -> new ActiveFacilityInstance.PieceInfo(pair.getFirst(), pair.getSecond())).toList(),
                        Optional.empty()));
            });

            var facilityInstance = new ActiveFacilityInstance(zoneInfoBuilder.build(), Optional.empty());
            facilityInstance.setHeader(header);
            return facilityInstance;
        } catch (Exception e) {
            Changed.LOGGER.error("Exception while creating ActiveFacilityInstance", e);
            throw e;
        }
    }

    @Override
    public String toString() {
        return "Site " + header.name;
    }

    // 1.18.2 辅助方法：手动计算包围盒
    private static BoundingBox calculateEncapsulatingBox(Map<Zone, List<Pair<ResourceLocation, BoundingBox>>> pieces) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        boolean hasPieces = false;

        for (List<Pair<ResourceLocation, BoundingBox>> list : pieces.values()) {
            for (Pair<ResourceLocation, BoundingBox> pair : list) {
                BoundingBox box = pair.getSecond();
                if (box.minX() < minX) minX = box.minX();
                if (box.minY() < minY) minY = box.minY();
                if (box.minZ() < minZ) minZ = box.minZ();
                if (box.maxX() > maxX) maxX = box.maxX();
                if (box.maxY() > maxY) maxY = box.maxY();
                if (box.maxZ() > maxZ) maxZ = box.maxZ();
                hasPieces = true;
            }
        }

        if (!hasPieces) {
            throw new IllegalStateException("Unable to calculate boundingbox without pieces");
        }

        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
