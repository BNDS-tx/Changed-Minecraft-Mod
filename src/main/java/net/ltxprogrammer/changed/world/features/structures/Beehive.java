package net.ltxprogrammer.changed.world.features.structures;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

import java.util.Optional;

//public class Beehive extends StructureFeature<NoneFeatureConfiguration> {
//    public static final Codec<Beehive> CODEC = RecordCodecBuilder.create((instance) -> {
//        return instance.group(
//                settingsCodec(instance),
//                ResourceLocation.CODEC.fieldOf("piece").forGetter(Beehive::getPiece)
//        ).apply(instance, Beehive::new);
//    });
//
//    private final ResourceLocation piece;
//    private final GenerationStep.Decoration step;
//
//    public Beehive(Codec<NoneFeatureConfiguration> codec, GenerationStep.Decoration step, ResourceLocation piece) {
//        super(codec, PieceGeneratorSupplier.simple(Beehive::checkLocation, Beehive.generatePieces(piece)));
//        this.piece = piece;
//        this.step = step;
//    }
//
//    private static <C extends FeatureConfiguration> boolean checkLocation(PieceGeneratorSupplier.Context<C> context) {
//        if (!context.validBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG)) {
//            return false;
//        } else {
//            return context.getLowestY(12, 15) >= context.chunkGenerator().getSeaLevel();
//        }
//    }
//
//    public ResourceLocation getPiece() {
//        return piece;
//    }
//
//    private static PieceGenerator<NoneFeatureConfiguration> generatePieces(ResourceLocation builder) {
//        return (bd, context) -> {
//            bd.addPiece(new SurfaceNBTPiece(builder, null, context));
//        };
//    }
//
//    @Override
//    public GenerationStep.Decoration step() {
//        return step;
//    }
//}

public class Beehive extends StructureFeature<NoneFeatureConfiguration> {

    // 保存必要的配置数据
    private final ResourceLocation piece;
    private final GenerationStep.Decoration step;
    private final TagKey<Biome> validBiomes;

    // 构造函数：与 ChangedStructures 中的调用匹配
    public Beehive(Codec<NoneFeatureConfiguration> codec,
                   GenerationStep.Decoration step,
                   ResourceLocation piece,
                   TagKey<Biome> validBiomes) {
        // 使用 Lambda 捕获参数传递给静态的 supplier
        super(codec, context -> pieceGeneratorSupplier(context, piece, validBiomes));
        this.step = step;
        this.piece = piece;
        this.validBiomes = validBiomes;
    }

    @Override
    public GenerationStep.Decoration step() {
        return step;
    }

    // 核心生成逻辑
    private static Optional<PieceGenerator<NoneFeatureConfiguration>> pieceGeneratorSupplier(
            PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context,
            ResourceLocation piece,
            TagKey<Biome> validBiomes) {

        // 1. [生物群系检查]
        // 获取区块中心
        BlockPos centerPos = context.chunkPos().getMiddleBlockPosition(0);

        // 获取生物群系
        var biome = context.chunkGenerator().getBiomeSource().getNoiseBiome(
                QuartPos.fromBlock(centerPos.getX()),
                QuartPos.fromBlock(0),
                QuartPos.fromBlock(centerPos.getZ()),
                context.chunkGenerator().climateSampler()
        );

        // 如果不在指定标签的群系内，停止生成
        if (!biome.is(validBiomes)) {
            return Optional.empty();
        }

        // 2. [位置有效性检查] (还原你原代码中的 checkLocation 逻辑)
        // 检查中心点的高度是否高于海平面
        // 原逻辑: context.getLowestY(12, 15) >= context.chunkGenerator().getSeaLevel()
        // 这里简化为检查中心点高度，通常足够有效
        int landHeight = context.chunkGenerator().getBaseHeight(
                centerPos.getX(), centerPos.getZ(),
                Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor()
        );

        if (landHeight < context.chunkGenerator().getSeaLevel()) {
            return Optional.empty(); // 如果在水下，不生成
        }

        // 3. [生成拼图]
        // 计算生成坐标
        BlockPos blockPos = new BlockPos(centerPos.getX(), landHeight, centerPos.getZ());

        return Optional.of((builder, generatorContext) -> {
            // 使用传入的 piece ID 生成 SurfaceNBTPiece
            builder.addPiece(new SurfaceNBTPiece(piece, null, generatorContext));
        });
    }

    // Getter 方法 (如果其他地方需要用到)
    public ResourceLocation getPiece() {
        return piece;
    }
}