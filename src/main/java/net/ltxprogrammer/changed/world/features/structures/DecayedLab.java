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
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

//public class DecayedLab extends Structure {
//    public static final Codec<DecayedLab> CODEC = RecordCodecBuilder.create((instance) -> {
//        return instance.group(
//                settingsCodec(instance),
//                ResourceLocation.CODEC.fieldOf("piece").forGetter(DecayedLab::getPiece),
//                ResourceLocation.CODEC.fieldOf("loot_table").forGetter(DecayedLab::getLootTable)
//        ).apply(instance, DecayedLab::new);
//    });
//
//    private final ResourceLocation piece;
//    private final ResourceLocation lootTable;
//
//    public DecayedLab(Structure.StructureSettings settings, ResourceLocation piece, ResourceLocation lootTable) {
//        super(settings);
//        this.piece = piece;
//        this.lootTable = lootTable;
//    }
//
//    public ResourceLocation getPiece() {
//        return piece;
//    }
//
//    public ResourceLocation getLootTable() {
//        return lootTable;
//    }
//
//    private void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
//        builder.addPiece(new SurfaceNBTPiece(this.getPiece(), null, context));
//    }
//
//    @Override
//    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
//        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (builder) -> {
//            generatePieces(builder, context);
//        });
//    }
//
//    @Override
//    public StructureType<?> type() {
//        return ChangedStructureTypes.DECAYED_LAB.get();
//    }
//}

// 1. 继承 StructureFeature，泛型使用我们自定义的配置类
public class DecayedLab extends StructureFeature<NoneFeatureConfiguration> {
    // 1. 定义成员变量来存储数据
    private final ResourceLocation piece;
    private final ResourceLocation lootTable;
    private final TagKey<Biome> validBiomes;

    // 2. 构造函数直接接收数据
    public DecayedLab(Codec<NoneFeatureConfiguration> codec,
                      ResourceLocation piece,
                      ResourceLocation lootTable,
                      TagKey<Biome> validBiomes) {
        // 传递 Lambda，捕获成员变量
        super(codec, context -> pieceGeneratorSupplier(context, piece, lootTable, validBiomes));
        this.piece = piece;
        this.lootTable = lootTable;
        this.validBiomes = validBiomes;
    }

    public ResourceLocation getPiece() {
        return piece;
    }

    public ResourceLocation getLootTable() {
        return lootTable;
    }

    private void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<?> context) {
        builder.addPiece(new SurfaceNBTPiece(this.getPiece(), lootTable, context));
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    // 3. 生成逻辑现在接收具体参数
    private static Optional<PieceGenerator<NoneFeatureConfiguration>> pieceGeneratorSupplier(
            PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context,
            ResourceLocation piece,
            ResourceLocation lootTable,
            TagKey<Biome> validBiomes) {

        // --- 生物群系检查 ---
        BlockPos centerPos = context.chunkPos().getMiddleBlockPosition(0);
        var biome = context.chunkGenerator().getBiomeSource().getNoiseBiome(
                QuartPos.fromBlock(centerPos.getX()),
                QuartPos.fromBlock(0),
                QuartPos.fromBlock(centerPos.getZ()),
                context.chunkGenerator().climateSampler()
        );

        if (!biome.is(validBiomes)) {
            return Optional.empty();
        }

        // --- 高度计算 ---
        int y = context.chunkGenerator().getBaseHeight(
                centerPos.getX(), centerPos.getZ(),
                Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor()
        );
        BlockPos blockPos = new BlockPos(centerPos.getX(), y, centerPos.getZ());

        // --- 生成拼图 ---
        return Optional.of((builder, generatorContext) -> {
            // 直接使用传入的 piece 和 lootTable
            builder.addPiece(new SurfaceNBTPiece(piece, lootTable, generatorContext));
        });
    }
}
