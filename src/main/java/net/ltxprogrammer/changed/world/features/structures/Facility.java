package net.ltxprogrammer.changed.world.features.structures;

import com.google.common.base.Stopwatch;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityKeystone;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//public class Facility extends Structure {
//    public static final int GENERATION_CHUNK_RADIUS = 8;
//
//    public static final Codec<Facility> CODEC = simpleCodec(Facility::new);
//
//    public Facility(Structure.StructureSettings settings) {
//        super(settings);
//    }
//
//    public Optional<Structure.GenerationStub> findGenerationPoint(GenerationContext context) {
//        Rotation rotation = Rotation.getRandom(context.random());
//        BlockPos blockpos = this.getLowestYIn5by5BoxOffset7Blocks(context, rotation);
//        return blockpos.getY() < 60 ? Optional.empty() : Optional.of(new Structure.GenerationStub(blockpos, (builder) -> {
//            this.generatePieces(builder, context, blockpos, rotation);
//        }));
//    }
//
//    private static final int REROLL_FOR_SIZE_COUNT = 1;
//
//    private void generatePieces(StructurePiecesBuilder builder, GenerationContext context, BlockPos blockPos, Rotation rotation) {
//        ChunkPos center = context.chunkPos();
//        ChunkPos min = new ChunkPos(center.x - GENERATION_CHUNK_RADIUS, center.z - GENERATION_CHUNK_RADIUS);
//        ChunkPos max = new ChunkPos(center.x + GENERATION_CHUNK_RADIUS, center.z + GENERATION_CHUNK_RADIUS);
//        BlockPos minPos = new BlockPos(min.getMinBlockX(), context.heightAccessor().getMinBuildHeight(), min.getMinBlockZ());
//        BlockPos maxPos = new BlockPos(max.getMaxBlockX(), context.heightAccessor().getMaxBuildHeight(), max.getMaxBlockZ());
//
//        BoundingBox generationRegion = BoundingBox.fromCorners(minPos, maxPos);
//
//        List<Integer> sizes = new ArrayList<>(REROLL_FOR_SIZE_COUNT);
//        List<StructurePiece> largestSet = List.of();
//        FacilityKeystone largestKeystone = null;
//
//        for (int reroll = 0; reroll < REROLL_FOR_SIZE_COUNT; reroll++) {
//            builder.clear();
//
//            FacilityKeystone keystone = FacilityPieces.generateFacility(builder, context, 5, 20, generationRegion);
//            builder.addPiece(keystone);
//
//            int size = ((StructurePiecesBuilderExtender)builder).pieceCount();
//            sizes.add(size);
//            if (((StructurePiecesBuilderExtender)builder).pieceCount() > largestSet.size()) {
//                largestSet = new ArrayList<>(((StructurePiecesBuilderExtender)builder).getPieces());
//                largestKeystone = keystone;
//            }
//        }
//
//        builder.clear();
//        largestSet.forEach(builder::addPiece);
//
//        Changed.LOGGER.info("Generated facility \"{}\" with {} pieces (best of {}), at ChunkPos {}",
//                largestKeystone,
//                largestSet.size(),
//                sizes,
//                center);
//    }
//
//    @Override
//    public StructureType<?> type() {
//        return ChangedStructureTypes.FACILITY.get();
//    }
//}

// 1. 继承 StructureFeature<NoneFeatureConfiguration>
public class Facility extends StructureFeature<NoneFeatureConfiguration> {
    public static final int GENERATION_CHUNK_RADIUS = 6;
    private static final int REROLL_FOR_SIZE_COUNT = 5;

    private final GenerationStep.Decoration step;
    private final TagKey<Biome> validBiomes; // 新增：群系限制

    // 构造函数：接收 Codec, Step 和 BiomeTag
    public Facility(Codec<NoneFeatureConfiguration> codec, GenerationStep.Decoration step, TagKey<Biome> validBiomes) {
        super(codec, context -> pieceGeneratorSupplier(context, validBiomes));
        this.step = step;
        this.validBiomes = validBiomes;
    }

    @Override
    public GenerationStep.Decoration step() {
        return step;
    }

    private static Optional<PieceGenerator<NoneFeatureConfiguration>> pieceGeneratorSupplier(
            PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context,
            TagKey<Biome> validBiomes) {

        // 1. 初始化随机数
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);

        // 2. 检查群系 (新增)
        BlockPos centerPos = context.chunkPos().getMiddleBlockPosition(0);
        var biome = context.chunkGenerator().getBiomeSource().getNoiseBiome(
                QuartPos.fromBlock(centerPos.getX()),
                QuartPos.fromBlock(0),
                QuartPos.fromBlock(centerPos.getZ()),
                context.chunkGenerator().climateSampler()
        );

        if (validBiomes != null && !biome.is(validBiomes)) {
            return Optional.empty();
        }

        // 3. 高度与生成逻辑
        // ... (保持之前的 generatePieces 调用逻辑不变) ...
        // ... (记得把之前重写好的 Facility 逻辑复制过来，这里为了省篇幅只写关键改动) ...

        // 简略示意：
        int y = context.chunkGenerator().getBaseHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
        if (y < 60) return Optional.empty();

        BlockPos blockPos = new BlockPos(centerPos.getX(), y, centerPos.getZ());
        net.minecraft.world.level.block.Rotation rotation = net.minecraft.world.level.block.Rotation.getRandom(worldgenrandom);

        return Optional.of((builder, generatorContext) -> {
            tryGeneratePieces(builder, generatorContext, blockPos, rotation);
        });
    }

    // ... generatePieces 方法保持之前修改好的版本 (使用 PieceGenerator.Context) ...
    // 注意：generatePieces 方法体需要包含我们之前修复的 Mixin 调用和 Reroll 逻辑

    // 阶段 2：拼图生成 (Generator)
    // 这里的 context 是 PieceGenerator.Context<NoneFeatureConfiguration>，它拥有你报错缺失的所有方法
    private static void tryGeneratePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context, BlockPos blockPos, Rotation rotation) {
    private void tryGeneratePieces(StructurePiecesBuilder builder, GenerationContext context, BlockPos blockPos, Rotation rotation) {
        ChunkPos center = context.chunkPos();
        Changed.LOGGER.info("Started facility generation at ChunkPos {}",
                center);

        Stopwatch stopwatch = Stopwatch.createStarted();

        ChunkPos min = new ChunkPos(center.x - GENERATION_CHUNK_RADIUS, center.z - GENERATION_CHUNK_RADIUS);
        ChunkPos max = new ChunkPos(center.x + GENERATION_CHUNK_RADIUS, center.z + GENERATION_CHUNK_RADIUS);
        BlockPos minPos = new BlockPos(min.getMinBlockX(), context.heightAccessor().getMinBuildHeight(), min.getMinBlockZ());
        BlockPos maxPos = new BlockPos(max.getMaxBlockX(), context.heightAccessor().getMaxBuildHeight(), max.getMaxBlockZ());

        BoundingBox generationRegion = BoundingBox.fromCorners(minPos, maxPos);

        final int rerollForSizeCount = Changed.config.server.facilityRollForSizeAttempts.get();
        final int genDepth = Changed.config.server.facilityGenerateDepth.get();

        List<Integer> sizes = new ArrayList<>(rerollForSizeCount);
        List<StructurePiece> largestSet = List.of();
        FacilityKeystone largestKeystone = null;

        for (int reroll = 0; reroll < rerollForSizeCount; reroll++) {
            builder.clear();

            Optional<FacilityKeystone> keystoneOpt = FacilityPieces.generateFacility(builder, context, genDepth, generationRegion);
            if (keystoneOpt.isEmpty()) continue;
            FacilityKeystone keystone = keystoneOpt.get();

            builder.addPiece(keystone);

            int size = ((StructurePiecesBuilderExtender) builder).pieceCount();
            sizes.add(size);

            if (size > largestSet.size()) {
                largestSet = new ArrayList<>(((StructurePiecesBuilderExtender) builder).getPieces());
                largestKeystone = keystone;
            }
        }

        // 恢复最大的那一组
        ((StructurePiecesBuilderExtender) builder).clear();
        if (largestKeystone == null) {
            Changed.LOGGER.info("Failed generating facility at ChunkPos {}",
                    center);
            return;
        }

        largestSet.forEach(builder::addPiece);

        var duration = stopwatch.elapsed().toMillis();

        Changed.LOGGER.info("Generated facility \"{}\" with {} pieces (best of {}), at ChunkPos {} after {} ms",
                largestKeystone,
                largestSet.size(),
                sizes,
                center,
                duration);
    }
}