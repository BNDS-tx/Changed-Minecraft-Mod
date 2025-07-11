package net.ltxprogrammer.changed.world.features.structures;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.block.GluBlock;
import net.ltxprogrammer.changed.world.features.structures.facility.*;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.*;

public class FacilityPieces { // TODO extend facility pieces to be data-oriented
    private static void registerEntrances(FacilityPieceCollectionBuilder builder) {
        builder.register(new FacilityEntrance(Changed.modResource("facility/entrance_blue")));
    }

    private static void registerStaircaseStarts(FacilityPieceCollectionBuilder builder) {
        builder.register(new FacilityStaircaseStart(Changed.modResource("facility/staircase_start_red")))
            .register(new FacilityStaircaseStart(Changed.modResource("facility/staircase_start_blue")));
    }
    private static void registerStaircaseSections(FacilityPieceCollectionBuilder builder) {
        builder.register(new FacilityStaircaseSection(Changed.modResource("facility/staircase_section_red")))
            .register(new FacilityStaircaseSection(Changed.modResource("facility/staircase_section_blue")));
    }
    private static void registerStaircaseEnds(FacilityPieceCollectionBuilder builder) {
        builder.register(new FacilityStaircaseEnd(Changed.modResource("facility/staircase_end_red")))
            .register(new FacilityStaircaseEnd(Changed.modResource("facility/staircase_end_blue")));
    }

    private static void registerCorridors(FacilityPieceCollectionBuilder builder) {
        builder.register(new FacilityCorridorSection(Changed.modResource("facility/corridor_blue_v1")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/corridor_blue_v2")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/corridor_blue_v3")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/corridor_blue_turn_v1")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/corridor_red_v1")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/corridor_red_v2")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/corridor_red_v3")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/corridor_red_turn_v1")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/corridor_gray_v1")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/corridor_gray_v2")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/corridor_gray_v3")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/corridor_gray_turn_v1")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/corridor_maintenance_v1")))

            .register(new FacilityCorridorSection(Changed.modResource("facility/longhallway1_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/longhallway1_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/longhallway1_red")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/longhallway2_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/longhallway2_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/longhallway2_red")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/longhallway3_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/longhallway3_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/longhallway3_red")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/longhallway4_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/longhallway4_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/longhallway4_red")))

            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway1_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway1_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway1_red")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway2_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway2_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway2_red")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway3_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway3_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway3_red")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway4_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway4_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway4_red")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway5_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway5_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway5_red")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway6_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway6_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway6_red")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway7_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway7_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway7_red")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway8_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway8_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/shorthallway8_red")))

            .register(new FacilityCorridorSection(Changed.modResource("facility/stairs1_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/stairs1_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/stairs1_red")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/stairs2_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/stairs2_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/stairs2_red")))

            .register(new FacilityCorridorSection(Changed.modResource("facility/hallwayturn1_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/hallwayturn1_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/hallwayturn1_red")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/hallwayturn2_blue")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/hallwayturn2_gray")))
            .register(new FacilityCorridorSection(Changed.modResource("facility/hallwayturn2_red")))

            .register(new FacilityCorridorSection(Changed.modResource("facility/laser_hall")));
    }

    private static void registerTransitions(FacilityPieceCollectionBuilder builder) {
        builder.register(new FacilityTransitionSection(Changed.modResource("facility/corridor_blue_stairs_to_red")))
            .register(new FacilityTransitionSection(Changed.modResource("facility/corridor_blue_stairs_to_gray")))
            .register(new FacilityTransitionSection(Changed.modResource("facility/transition_red_to_gray")));
    }

    private static void registerSplits(FacilityPieceCollectionBuilder builder) {
        builder.register(new FacilitySplitSection(Changed.modResource("facility/corridor_red_t_v1")))
            .register(new FacilitySplitSection(Changed.modResource("facility/corridor_blue_t_v1")))
            .register(new FacilitySplitSection(Changed.modResource("facility/corridor_gray_t_v1")))
            .register(new FacilitySplitSection(Changed.modResource("facility/corridor_gray_circle")))
            .register(new FacilitySplitSection(Changed.modResource("facility/intersection1_blue")))
            .register(new FacilitySplitSection(Changed.modResource("facility/intersection1_gray")))
            .register(new FacilitySplitSection(Changed.modResource("facility/intersection1_red")))
            .register(new FacilitySplitSection(Changed.modResource("facility/intersection2_blue")))
            .register(new FacilitySplitSection(Changed.modResource("facility/intersection2_gray")))
            .register(new FacilitySplitSection(Changed.modResource("facility/intersection2_red")));
    }

    private static void registerRooms(FacilityPieceCollectionBuilder builder) {
        builder.register(new FacilityRoomPiece(Changed.modResource("facility/room_blue_wl_test"), LootTables.DECAYED_LAB_WL))
            .register(new FacilityRoomPiece(Changed.modResource("facility/room_red_dl_test"), LootTables.DECAYED_LAB_DL))
            .register(new FacilityRoomPiece(Changed.modResource("facility/room_gray_origin"), LootTables.DECAYED_LAB_ORIGIN))
            .register(new FacilityRoomPiece(Changed.modResource("facility/room_blue_storage"), LootTables.LAB_WHITE_LATEX))
            .register(new FacilityRoomPiece(Changed.modResource("facility/room_red_storage"), LootTables.LAB_DARK_LATEX))
            .register(new FacilityRoomPiece(Changed.modResource("facility/room_gray_storage"), LootTables.DECAYED_LAB_ORIGIN))
            .register(new FacilityRoomPiece(Changed.modResource("facility/room_blue_wl_risk"), LootTables.HIGH_TIER_LAB))
            .register(new FacilityRoomPiece(Changed.modResource("facility/room_red_dl_risk"), LootTables.HIGH_TIER_LAB));
    }

    private static void registerSeals(FacilityPieceCollectionBuilder builder) {
        builder.register(new FacilitySealPiece(Changed.modResource("facility/seal_blue")))
            .register(new FacilitySealPiece(Changed.modResource("facility/seal_red")))
            .register(new FacilitySealPiece(Changed.modResource("facility/seal_gray")));
    }

    private static final Map<PieceType, FacilityPieceCollection> BY_PIECE_TYPE = new HashMap<>();

    public static FacilityPieceCollection getPiecesOfType(PieceType pieceType) {
        return BY_PIECE_TYPE.get(pieceType);
    }

    public static void gatherFacilityPieces() {
        BY_PIECE_TYPE.clear();

        for (PieceType pieceType : PieceType.values()) {
            var builder = new FacilityPieceCollectionBuilder();
            switch (pieceType) {
                case ENTRANCE -> registerEntrances(builder);
                case STAIRCASE_START -> registerStaircaseStarts(builder);
                case STAIRCASE_SECTION -> registerStaircaseSections(builder);
                case STAIRCASE_END -> registerStaircaseEnds(builder);
                case CORRIDOR -> registerCorridors(builder);
                case SPLIT -> registerSplits(builder);
                case TRANSITION -> registerTransitions(builder);
                case SEAL -> registerSeals(builder);
                case ROOM -> registerRooms(builder);
            }
            Changed.postModLoadingEvent(new GatherFacilityPiecesEvent(pieceType, builder));

            BY_PIECE_TYPE.put(pieceType, builder.build());
        }
    }

    private static BlockPos gluNeighbor(BlockPos gluPos, BlockState gluState) {
        return gluPos.relative(gluState.getValue(GluBlock.ORIENTATION).front());
    }
    
    public static boolean isNotCompletelyInsideRegion(BoundingBox boundingBox, BoundingBox region) {
        return boundingBox.minX() < region.minX() || boundingBox.minY() < region.minY() || boundingBox.minZ() < region.minZ() ||
                boundingBox.maxX() > region.maxX() || boundingBox.maxY() > region.maxY() || boundingBox.maxZ() > region.maxZ();
    }

    private static void treeGenerate(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context,
                                     Stack<FacilityPiece> stack, StructurePiece parentStructure,
                                     GenStep start, int genDepth, int span, BoundingBox allowedRegion) {
        var parent = stack.peek();

        int reroll = 10;
        while (reroll > 0) {
            PieceType pieceType;
            BoundingBox allowedRegionForPiece;
            if (parent.type == PieceType.SPLIT && reroll == 1) { // Split pieces will dead-end if it's too close to the gen region
                pieceType = PieceType.SEAL;
                allowedRegionForPiece = BoundingBox.infinite();
            } else if (span == 0) {
                pieceType = PieceType.ROOM;
                allowedRegionForPiece = allowedRegion;
            } else {
                var type = start.validTypes().getRandom(context.random());
                if (type.isEmpty())
                    break;
                pieceType = type.get().getData();
                allowedRegionForPiece = allowedRegion;
            }

            boolean placed = BY_PIECE_TYPE.get(pieceType).shuffledStream(context.random()).anyMatch(nextPiece -> {
                var nextStructure = nextPiece.createStructurePiece(context.structureManager(), genDepth);
                if (!nextStructure.setupBoundingBox(builder, start.blockInfo(), context.random(), allowedRegionForPiece))
                    return false;

                var startPos = gluNeighbor(start.blockInfo().pos, start.blockInfo().state);
                builder.addPiece(nextStructure);

                if (span <= 0)
                    return false;

                int nextSpan = pieceType.shouldConsumeSpan() ? span - 1 : span;
                stack.push(nextPiece);

                var genStack = new FacilityGenerationStack(stack, nextStructure.getBoundingBox(), context, nextSpan);
                List<GenStep> starts = new ArrayList<>();
                nextStructure.addSteps(genStack, starts);

                int piecesBefore = ((StructurePiecesBuilderExtender)builder).pieceCount();
                starts.stream().filter(next -> !next.blockInfo().pos.equals(startPos)).forEach(next -> {
                    treeGenerate(builder, context, stack, nextStructure, next, genDepth, nextSpan, allowedRegion);
                });
                int piecesAfter = ((StructurePiecesBuilderExtender)builder).pieceCount();

                stack.pop();

                if (piecesAfter > piecesBefore) // Successfully generated pieces that attach to this one
                    return true;

                // No piece was generated to attach to this one
                if (pieceType == PieceType.ROOM || pieceType == PieceType.SEAL)
                    return true; // This behaviour is expected for a room

                // Attempt to regenerate this piece as a room, to prevent a dead end
                ((StructurePiecesBuilderExtender)builder).removePiece(nextStructure);

                StructurePiece pieceToPut = BY_PIECE_TYPE.get(PieceType.ROOM).shuffledStream(context.random()).map(nextRoom -> {
                    var nextRoomStructure = nextRoom.createStructurePiece(context.structureManager(), genDepth);
                    if (!nextRoomStructure.setupBoundingBox(builder, start.blockInfo(), context.random(), allowedRegion))
                        return null;

                    // Success
                    return nextRoomStructure;
                }).filter(Objects::nonNull).findFirst().orElse(nextStructure);

                if (pieceToPut == nextStructure) {
                    pieceToPut = BY_PIECE_TYPE.get(PieceType.SEAL).stream().map(nextSeal -> {
                        var nextRoomStructure = nextSeal.createStructurePiece(context.structureManager(), genDepth);
                        if (!nextRoomStructure.setupBoundingBox(builder, start.blockInfo(), context.random(), allowedRegion))
                            return null;

                        // Success
                        return nextRoomStructure;
                    }).filter(Objects::nonNull).findFirst().orElse(nextStructure);
                }

                if (pieceToPut == nextStructure)
                    Changed.LOGGER.debug("Failed to seal dead end in facility, startPos {}", startPos);
                else
                    Changed.LOGGER.debug("Sealed dead end in facility, startPos {}", startPos);
                builder.addPiece(pieceToPut);

                return true;
            });

            if (placed)
                break;

            reroll--;
        }

        return;
    }

    public static void generateFacility(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context, int genDepth, int span, BoundingBox allowedRegion) {
        BlockPos blockPos = new BlockPos(
                context.chunkPos().getBlockX(8), 0,
                context.chunkPos().getBlockZ(8));
        blockPos = blockPos.atY(context.chunkGenerator().getBaseHeight(blockPos.getX(), blockPos.getZ(),
                Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor()));

        Stack<FacilityPiece> stack = new Stack<>();
        List<GenStep> starts = new ArrayList<>();
        FacilityPiece entranceNew = BY_PIECE_TYPE.get(PieceType.ENTRANCE).findNextPiece(context.random()).orElseThrow();
        FacilityPieceInstance entrancePiece = entranceNew.createStructurePiece(context.structureManager(), genDepth);

        var directions = new ArrayList<>(Direction.Plane.HORIZONTAL.stream().toList());
        Collections.shuffle(directions, context.random());

        for (Direction dir : directions) {
            entrancePiece.setRotation(dir);
            entrancePiece.setupBoundingBoxOnBottomCenter(blockPos);
            BoundingBox entranceBB = entrancePiece.getBoundingBox();

            int minXminZ = context.chunkGenerator().getBaseHeight(entranceBB.minX() + 1, entranceBB.minZ() + 1, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
            int minXmaxZ = context.chunkGenerator().getBaseHeight(entranceBB.minX() + 1, entranceBB.maxZ() - 1, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
            int maxXminZ = context.chunkGenerator().getBaseHeight(entranceBB.maxX() - 1, entranceBB.minZ() + 1, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
            int maxXmaxZ = context.chunkGenerator().getBaseHeight(entranceBB.maxX() - 1, entranceBB.maxZ() - 1, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
            int min = Math.min(Math.min(minXminZ, minXmaxZ), Math.min(maxXminZ, maxXmaxZ));
            int max = Math.max(Math.max(minXminZ, minXmaxZ), Math.max(maxXminZ, maxXmaxZ));

            entrancePiece.setupBoundingBoxOnBottomCenter(new BlockPos(blockPos.getX(), min, blockPos.getZ()));

            if (max - min < 3) break; // Surface is flat enough to not worry about rotating the entrance

            BlockPos testPos = entrancePiece.getRandomStart(context.random());
            double minX = Mth.lerp((double)(testPos.getZ() - entranceBB.minZ()) / (double)entranceBB.getZSpan(), (double)minXminZ, (double)minXmaxZ);
            double maxX = Mth.lerp((double)(testPos.getZ() - entranceBB.minZ()) / (double)entranceBB.getZSpan(), (double)maxXminZ, (double)maxXmaxZ);
            double height = Mth.lerp((double)(testPos.getX() - entranceBB.minX()) / (double)entranceBB.getXSpan(), minX, maxX);

            if (testPos.getY() < height) break; // Next structure piece is in the surface
        }

        stack.push(entranceNew);
        builder.addPiece(entrancePiece);

        entrancePiece.addSteps(new FacilityGenerationStack(stack, entrancePiece.getBoundingBox(), context, span), starts);

        if (span > 0) {
            starts.forEach(start -> {
                treeGenerate(builder, context, stack, entrancePiece, start, genDepth, span - 1, allowedRegion);
            });
        }

        stack.pop();
    }
}
