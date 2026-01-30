package net.ltxprogrammer.changed.init;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.decoration.WallSignVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.item.WallSignItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Predicate;

//public class ChangedTabs {
//    private static CreativeModeTab.Builder makeTab(String id, CreativeModeTab.DisplayItemsGenerator generator) {
//        return CreativeModeTab.builder()
//                .title(new TranslatableComponent("itemGroup." + id))
//                .displayItems(generator);
//    }
//
//    public static DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Changed.MODID);
//
//    private static final Predicate<TransfurVariant<?>> CHANGED_ONLY_TRANSFURS = variant -> variant.getFormId().getNamespace().equals(Changed.MODID);
//    private static final Predicate<WallSignVariant> CHANGED_ONLY_WALL_SIGNS = variant -> ChangedRegistry.WALL_SIGN_VARIANT.getKey(variant).getNamespace().equals(Changed.MODID);
//
//    private static RegistryObject<CreativeModeTab> register(String id, Function<CreativeModeTab.Builder, CreativeModeTab> finalizer) {
//        return REGISTRY.register(id, () -> finalizer.apply(
//                CreativeModeTab.builder().title(new TranslatableComponent("itemGroup.tab_changed_" + id))
//        ));
//    }
//
//    public static RegistryObject<CreativeModeTab> TAB_CHANGED_BLOCKS = register("blocks", builder ->
//            builder.icon(() -> new ItemStack(ChangedBlocks.WALL_LIGHTRED_STRIPED.get()))
//                    .displayItems((params, output) -> {
//                        output.accept(ChangedBlocks.INFUSER.get());
//                        output.accept(ChangedBlocks.PURIFIER.get());
//                        output.accept(ChangedBlocks.STASIS_CHAMBER.get());
//
//                        output.accept(ChangedBlocks.AIR_CONDITIONER.get());
//                        output.accept(ChangedBlocks.BAR_STOOL.get());
//                        output.accept(ChangedBlocks.BAR_TOP.get());
//                        output.accept(ChangedBlocks.BEAKER.get());
//                        output.accept(ChangedBlocks.BEDSIDE_IV_RACK.get());
//                        output.accept(ChangedBlocks.BLACK_RAILING.get());
//                        output.accept(ChangedBlocks.CLIPBOARD.get());
//                        output.accept(ChangedBlocks.NOTE.get());
//                        output.accept(ChangedBlocks.COMPUTER.get());
//                        output.accept(ChangedBlocks.CANNED_PEACHES.get());
//                        output.accept(ChangedBlocks.CANNED_SOUP.get());
//                        output.accept(ChangedBlocks.CARDBOARD_BOX.get());
//                        output.accept(ChangedBlocks.CARDBOARD_BOX_SMALL.get());
//                        output.accept(ChangedBlocks.CARDBOARD_BOX_TALL.get());
//                        output.accept(ChangedBlocks.BOX_PILE.get());
//                        output.accept(ChangedBlocks.PEN_BOX.get());
//                        output.accept(ChangedBlocks.DUCT.get());
//                        output.accept(ChangedBlocks.ERLENMEYER_FLASK.get());
//                        output.accept(ChangedBlocks.EXOSKELETON_CHARGER.get());
//                        output.accept(ChangedBlocks.FLOOR_SIGN_WET.get());
//                        output.accept(ChangedBlocks.FLOOR_SIGN_EXIT.get());
//                        output.accept(ChangedBlocks.FLOOR_SIGN_ELECTRICAL.get());
//                        output.accept(ChangedBlocks.GENERATOR.get());
//                        output.accept(ChangedBlocks.IRON_CRATE.get());
//                        output.accept(ChangedBlocks.KEYPAD.get());
//                        output.accept(ChangedBlocks.LAB_LIGHT.get());
//                        output.accept(ChangedBlocks.LAB_LIGHT_SMALL.get());
//                        output.accept(ChangedBlocks.LAB_TABLE.get());
//                        output.accept(ChangedBlocks.LASER_EMITTER.get());
//                        output.accept(ChangedBlocks.LATEX_CRYSTAL.get());
//                        output.accept(ChangedBlocks.LATEX_PUP_CRYSTAL.get());
//                        output.accept(ChangedBlocks.LATEX_CONTAINER.get());
//                        output.accept(ChangedBlocks.LATEX_TRAFFIC_CONE.get());
//                        output.accept(ChangedBlocks.BEIFENG_CRYSTAL.get());
//                        output.accept(ChangedBlocks.BEIFENG_CRYSTAL_SMALL.get());
//                        output.accept(ChangedBlocks.DARK_DRAGON_CRYSTAL.get());
//                        output.accept(ChangedBlocks.WOLF_CRYSTAL.get());
//                        output.accept(ChangedBlocks.WOLF_CRYSTAL_SMALL.get());
//                        output.accept(ChangedBlocks.DARK_LATEX_CRYSTAL_LARGE.get());
//                        output.accept(ChangedBlocks.DARK_LATEX_PUDDLE.get());
//                        output.accept(ChangedBlocks.WHITE_LATEX_PUDDLE_MALE.get());
//                        output.accept(ChangedBlocks.WHITE_LATEX_PUDDLE_FEMALE.get());
//                        output.accept(ChangedBlocks.PIPE.get());
//                        output.accept(ChangedBlocks.PETRI_DISH.get());
//                        output.accept(ChangedBlocks.RETINAL_SCANNER.get());
//                        output.accept(ChangedBlocks.ROOMBA_CHARGER.get());
//                        output.accept(ChangedBlocks.SHIPPING_CONTAINER_BLUE.get());
//                        output.accept(ChangedBlocks.SHIPPING_CONTAINER_ORANGE.get());
//                        output.accept(ChangedBlocks.SPEAKER.get());
//                        output.accept(ChangedBlocks.MICROPHONE.get());
//                        output.accept(ChangedBlocks.MICROSCOPE.get());
//                        output.accept(ChangedBlocks.OFFICE_CHAIR.get());
//                        output.accept(ChangedBlocks.ORANGE_TREE_LEAVES.get());
//                        output.accept(ChangedBlocks.ORANGE_TREE_SAPLING.get());
//                        output.accept(ChangedBlocks.TAPE_RECORDER.get());
//                        output.accept(ChangedBlocks.TILES_BLUE.get());
//                        output.accept(ChangedBlocks.TILES_BLUE_SMALL.get());
//                        output.accept(ChangedBlocks.TILES_CAUTION.get());
//                        output.accept(ChangedBlocks.TILES_CAUTION_SLAB.get());
//                        output.accept(ChangedBlocks.TILES_CAUTION_STAIRS.get());
//                        output.accept(ChangedBlocks.TILES_GREENHOUSE.get());
//                        output.accept(ChangedBlocks.WALL_GREENHOUSE.get());
//                        output.accept(ChangedBlocks.TILES_GRAYBLUE.get());
//                        output.accept(ChangedBlocks.TILES_GRAYBLUE_SLAB.get());
//                        output.accept(ChangedBlocks.TILES_GRAYBLUE_STAIRS.get());
//                        output.accept(ChangedBlocks.TILES_GRAY.get());
//                        output.accept(ChangedBlocks.TILES_GRAY_SLAB.get());
//                        output.accept(ChangedBlocks.TILES_GRAY_STAIRS.get());
//                        output.accept(ChangedBlocks.TILES_GRAYBLUE_BOLTED.get());
//                        output.accept(ChangedBlocks.TILES_GRAYBLUE_BOLTED_CONNECTED.get());
//                        output.accept(ChangedBlocks.TILES_GRAYBLUE_BOLTED_SLAB.get());
//                        output.accept(ChangedBlocks.TILES_GRAYBLUE_BOLTED_STAIRS.get());
//                        output.accept(ChangedBlocks.TILES_LIBRARY_BROWN.get());
//                        output.accept(ChangedBlocks.TILES_LIBRARY_BROWN_SLAB.get());
//                        output.accept(ChangedBlocks.TILES_LIBRARY_BROWN_STAIRS.get());
//                        output.accept(ChangedBlocks.TILES_LIBRARY_TAN.get());
//                        output.accept(ChangedBlocks.TILES_LIBRARY_TAN_SLAB.get());
//                        output.accept(ChangedBlocks.TILES_LIBRARY_TAN_STAIRS.get());
//                        output.accept(ChangedBlocks.TILES_TEAL.get());
//                        output.accept(ChangedBlocks.TILES_WHITE.get());
//                        output.accept(ChangedBlocks.TILES_WHITE_SLAB.get());
//                        output.accept(ChangedBlocks.TILES_WHITE_STAIRS.get());
//                        output.accept(ChangedBlocks.ORANGE_LAB_CARPETING.get());
//                        output.accept(ChangedBlocks.VENT_FAN.get());
//                        output.accept(ChangedBlocks.VENT_HATCH.get());
//                        output.accept(ChangedBlocks.WALL_BLUE_STRIPED.get());
//                        output.accept(ChangedBlocks.WALL_BLUE_TILED.get());
//                        output.accept(ChangedBlocks.WALL_CAUTION.get());
//                        output.accept(ChangedBlocks.WALL_LIBRARY_UPPER.get());
//                        output.accept(ChangedBlocks.WALL_LIBRARY_LOWER.get());
//                        output.accept(ChangedBlocks.WALL_LIGHTRED.get());
//                        output.accept(ChangedBlocks.WALL_LIGHTRED_SLAB.get());
//                        output.accept(ChangedBlocks.WALL_LIGHTRED_STAIRS.get());
//                        output.accept(ChangedBlocks.WALL_LIGHTRED_STRIPED.get());
//                        output.accept(ChangedBlocks.WALL_GRAY.get());
//                        output.accept(ChangedBlocks.WALL_GRAY_SLAB.get());
//                        output.accept(ChangedBlocks.WALL_GRAY_STAIRS.get());
//                        output.accept(ChangedBlocks.WALL_GRAY_STRIPED.get());
//                        output.accept(ChangedBlocks.WALL_GREEN.get());
//                        output.accept(ChangedBlocks.WALL_GREEN_SLAB.get());
//                        output.accept(ChangedBlocks.WALL_GREEN_STAIRS.get());
//                        output.accept(ChangedBlocks.WALL_GREEN_STRIPED.get());
//                        output.accept(ChangedBlocks.WALL_VENT.get());
//                        output.accept(ChangedBlocks.WALL_WHITE.get());
//                        output.accept(ChangedBlocks.WALL_WHITE_GREEN_STRIPED.get());
//                        output.accept(ChangedBlocks.WALL_WHITE_GREEN_TILED.get());
//                        output.accept(ChangedBlocks.WALL_WHITE_SLAB.get());
//                        output.accept(ChangedBlocks.WALL_WHITE_STAIRS.get());
//                        output.accept(ChangedBlocks.WHITE_LAB_TABLE.get());
//
//                        output.accept(ChangedBlocks.BEEHIVE_BED.get());
//                        output.accept(ChangedBlocks.BEEHIVE_WALL.get());
//                        output.accept(ChangedBlocks.BEEHIVE_CORNER.get());
//                        output.accept(ChangedBlocks.BEEHIVE_FLOOR.get());
//                        output.accept(ChangedBlocks.BEEHIVE_ROOF.get());
//
//                        output.accept(ChangedBlocks.LARGE_LAB_DOOR.get());
//                        output.accept(ChangedBlocks.LARGE_LIBRARY_DOOR.get());
//                        output.accept(ChangedBlocks.LARGE_MAINTENANCE_DOOR.get());
//                        output.accept(ChangedBlocks.LARGE_BLUE_LAB_DOOR.get());
//                        output.accept(ChangedBlocks.LAB_DOOR.get());
//                        output.accept(ChangedBlocks.LIBRARY_DOOR.get());
//                        output.accept(ChangedBlocks.MAINTENANCE_DOOR.get());
//                        output.accept(ChangedBlocks.BLUE_LAB_DOOR.get());
//
//                        output.accept(ChangedBlocks.EMPTY_CANISTER.get());
//                        output.accept(ChangedBlocks.OXYGENATED_WATER_CANISTER.get());
//                        output.accept(ChangedBlocks.WOLF_GAS_CANISTER.get());
//                        output.accept(ChangedBlocks.TIGER_GAS_CANISTER.get());
//                        output.accept(ChangedBlocks.SKUNK_GAS_CANISTER.get());
//
//                        output.accept(ChangedBlocks.DARK_LATEX_BLOCK.get());
//                        output.accept(ChangedBlocks.DARK_LATEX_ICE.get());
//                        output.accept(ChangedBlocks.WOLF_CRYSTAL_BLOCK.get());
//                        output.accept(ChangedBlocks.WHITE_LATEX_BLOCK.get());
//                        output.accept(ChangedBlocks.WHITE_LATEX_PILLAR.get());
//
//                        output.accept(ChangedItems.BIPED_ARMOR_STAND.get());
//                        output.accept(ChangedItems.CENTAUR_ARMOR_STAND.get());
//                        output.accept(ChangedItems.LEGLESS_ARMOR_STAND.get());
//
//                        WallSignItem.fillItemList(CHANGED_ONLY_WALL_SIGNS, params, output);
//
//                        ChangedBlocks.PILLOWS.values().stream().map(RegistryObject::get).forEach(output::accept);
//
//                        ForgeRegistries.PAINTING_VARIANTS.getKeys().stream()
//                                .filter(name -> name.getNamespace().equals(Changed.MODID))
//                                .map(ForgeRegistries.PAINTING_VARIANTS::getHolder).filter(Optional::isPresent).map(Optional::get)
//                                .forEach(variant -> {
//                                    ItemStack stack = new ItemStack(Items.PAINTING);
//                                    CompoundTag compoundtag = stack.getOrCreateTagElement("EntityTag");
//                                    Painting.storeVariant(compoundtag, variant);
//                                    output.accept(stack);
//                                });
//                    }).build());
//
//    public static RegistryObject<CreativeModeTab> TAB_CHANGED_ITEMS = register("items", builder ->
//            builder.icon(() -> new ItemStack(ChangedItems.LATEX_BASE.get()))
//                    .displayItems((params, output) -> {
//                        output.accept(ChangedItems.GAS_MASK.get());
//                        output.accept(ChangedItems.COMPACT_DISC.get());
//                        output.accept(ChangedItems.LAB_BOOK.get());
//                        output.accept(ChangedItems.LATEX_BASE.get());
//                        output.accept(ChangedBlocks.MUG.get());
//                        output.accept(ChangedItems.MUG_WITH_WATER.get());
//                        output.accept(ChangedItems.MUG_WITH_MILK.get());
//                        output.accept(ChangedItems.MUG_WITH_COFFEE.get());
//                        output.accept(ChangedItems.MUG_WITH_DARK_LATEX.get());
//                        output.accept(ChangedItems.MUG_WITH_WHITE_LATEX.get());
//                        output.accept(ChangedItems.ORANGE.get());
//                        output.accept(ChangedItems.SYRINGE.get());
//                        output.accept(ChangedItems.BLOOD_SYRINGE.get());
//                        output.accept(ChangedItems.DARK_LATEX_GOO.get());
//                        output.accept(ChangedItems.DARK_LATEX_BUCKET.get());
//                        output.accept(ChangedItems.WHITE_LATEX_GOO.get());
//                        output.accept(ChangedItems.WHITE_LATEX_BUCKET.get());
//                        output.accept(ChangedItems.DARK_LATEX_CRYSTAL_FRAGMENT.get());
//                        output.accept(ChangedItems.BEIFENG_CRYSTAL_FRAGMENT.get());
//                        output.accept(ChangedItems.WOLF_CRYSTAL_FRAGMENT.get());
//                        output.accept(ChangedItems.DARK_DRAGON_CRYSTAL_FRAGMENT.get());
//                        output.accept(ChangedItems.LATEX_INKBALL.get());
//                        output.accept(ChangedItems.ROOMBA.get());
//                        output.accept(ChangedItems.EXOSKELETON.get());
//
//                        ChangedItems.DARK_LATEX_MASK.get().fillItemList(CHANGED_ONLY_TRANSFURS, params, output);
//                        ChangedItems.LATEX_SYRINGE.get().fillItemList(CHANGED_ONLY_TRANSFURS, params, output);
//                        ChangedItems.LATEX_FLASK.get().fillItemList(CHANGED_ONLY_TRANSFURS, params, output);
//                        ChangedItems.LATEX_TIPPED_ARROW.get().fillItemList(CHANGED_ONLY_TRANSFURS, params, output);
//                    })
//                    .build());
//
//    public static RegistryObject<CreativeModeTab> TAB_CHANGED_ENTITIES = register("entities", builder ->
//            builder.icon(() -> new ItemStack(ChangedItems.DARK_LATEX_MASK.get()))
//                    .displayItems((params, output) -> {
//                        ChangedEntities.SPAWN_EGGS.values().stream()
//                                .map(RegistryObject::get)
//                                .forEach(output::accept);
//                    })
//                    .build());
//
//    public static RegistryObject<CreativeModeTab> TAB_CHANGED_COMBAT = register("combat", builder ->
//            builder.icon(() -> new ItemStack(ChangedItems.TSC_BATON.get()))
//                    .displayItems((params, output) -> {
//                        output.accept(ChangedItems.TSC_BATON.get());
//                        output.accept(ChangedItems.TSC_STAFF.get());
//                        output.accept(ChangedItems.TSC_SHIELD.get());
//
//                        output.accept(ChangedItems.LEATHER_UPPER_ABDOMEN_ARMOR.get());
//                        output.accept(ChangedItems.LEATHER_LOWER_ABDOMEN_ARMOR.get());
//                        output.accept(ChangedItems.LEATHER_QUADRUPEDAL_LEGGINGS.get());
//                        output.accept(ChangedItems.LEATHER_QUADRUPEDAL_BOOTS.get());
//
//                        output.accept(ChangedItems.CHAINMAIL_UPPER_ABDOMEN_ARMOR.get());
//                        output.accept(ChangedItems.CHAINMAIL_LOWER_ABDOMEN_ARMOR.get());
//                        output.accept(ChangedItems.CHAINMAIL_QUADRUPEDAL_LEGGINGS.get());
//                        output.accept(ChangedItems.CHAINMAIL_QUADRUPEDAL_BOOTS.get());
//
//                        output.accept(ChangedItems.IRON_UPPER_ABDOMEN_ARMOR.get());
//                        output.accept(ChangedItems.IRON_LOWER_ABDOMEN_ARMOR.get());
//                        output.accept(ChangedItems.IRON_QUADRUPEDAL_LEGGINGS.get());
//                        output.accept(ChangedItems.IRON_QUADRUPEDAL_BOOTS.get());
//
//                        output.accept(ChangedItems.GOLDEN_UPPER_ABDOMEN_ARMOR.get());
//                        output.accept(ChangedItems.GOLDEN_LOWER_ABDOMEN_ARMOR.get());
//                        output.accept(ChangedItems.GOLDEN_QUADRUPEDAL_LEGGINGS.get());
//                        output.accept(ChangedItems.GOLDEN_QUADRUPEDAL_BOOTS.get());
//
//                        output.accept(ChangedItems.DIAMOND_UPPER_ABDOMEN_ARMOR.get());
//                        output.accept(ChangedItems.DIAMOND_LOWER_ABDOMEN_ARMOR.get());
//                        output.accept(ChangedItems.DIAMOND_QUADRUPEDAL_LEGGINGS.get());
//                        output.accept(ChangedItems.DIAMOND_QUADRUPEDAL_BOOTS.get());
//
//                        output.accept(ChangedItems.NETHERITE_UPPER_ABDOMEN_ARMOR.get());
//                        output.accept(ChangedItems.NETHERITE_LOWER_ABDOMEN_ARMOR.get());
//                        output.accept(ChangedItems.NETHERITE_QUADRUPEDAL_LEGGINGS.get());
//                        output.accept(ChangedItems.NETHERITE_QUADRUPEDAL_BOOTS.get());
//
//                        output.accept(ChangedItems.ABDOMEN_ARMOR_CONVERSION.get());
//                        output.accept(ChangedItems.QUADRUPEDAL_ARMOR_CONVERSION.get());
//                    })
//                    .build());
//
//    public static RegistryObject<CreativeModeTab> TAB_CHANGED_CLOTHING = register("clothing", builder ->
//            builder.icon(() -> new ItemStack(ChangedItems.BENIGN_SHORTS.get()))
//                    .displayItems((params, output) -> {
//                        output.accept(ChangedItems.BENIGN_SHORTS.get());
//                        output.accept(ChangedItems.PINK_SHORTS.get());
//                        output.accept(ChangedItems.BLACK_TSHIRT.get());
//                        output.accept(ChangedItems.WHITE_TSHIRT.get());
//                        output.accept(ChangedItems.SPORTS_BRA.get());
//                        output.accept(ChangedItems.LAB_COAT.get());
//                        output.accept(ChangedItems.WETSUIT.get());
//                        output.accept(ChangedItems.NITRILE_GLOVES.get());
//                        output.accept(ChangedItems.ORANGE_NECK_TIE.get());
//                        output.accept(ChangedItems.RED_NECK_TIE.get());
//                        output.accept(ChangedItems.BLUE_NECK_TIE.get());
//                    })
//                    .build());
//
//    public static RegistryObject<CreativeModeTab> TAB_CHANGED_MUSIC = register("music", builder ->
//            builder.icon(() -> new ItemStack(ChangedItems.PURO_THE_BLACK_GOO_RECORD.get()))
//                    .displayItems((params, output) -> {
//                        output.accept(ChangedItems.BLACK_GOO_ZONE_RECORD.get());
//                        output.accept(ChangedItems.CRYSTAL_ZONE_RECORD.get());
//                        output.accept(ChangedItems.GAS_ROOM_RECORD.get());
//                        output.accept(ChangedItems.LABORATORY_RECORD.get());
//                        output.accept(ChangedItems.OUTSIDE_THE_TOWER_RECORD.get());
//                        output.accept(ChangedItems.PURO_THE_BLACK_GOO_RECORD.get());
//                        output.accept(ChangedItems.PUROS_HOME_RECORD.get());
//                        output.accept(ChangedItems.THE_LIBRARY_RECORD.get());
//                        output.accept(ChangedItems.THE_LION_CHASE_RECORD.get());
//                        output.accept(ChangedItems.THE_SCARLET_CRYSTAL_MINE_RECORD.get());
//                        output.accept(ChangedItems.THE_SHARK_RECORD.get());
//                        output.accept(ChangedItems.THE_SQUID_DOG_RECORD.get());
//                        output.accept(ChangedItems.THE_WHITE_GOO_JUNGLE_RECORD.get());
//                        output.accept(ChangedItems.THE_WHITE_TAIL_CHASE_PART_1.get());
//                        output.accept(ChangedItems.THE_WHITE_TAIL_CHASE_PART_2.get());
//                        output.accept(ChangedItems.VENT_PIPE_RECORD.get());
//                    })
//                    .build());
//}

public class ChangedTabs {
    // 1.18.2 不需要 DeferredRegister，直接定义静态常量即可

    // 辅助 Predicate (保持不变)
    private static final Predicate<TransfurVariant<?>> CHANGED_ONLY_TRANSFURS = variant -> variant.getFormId().getNamespace().equals(Changed.MODID);
    private static final Predicate<WallSignVariant> CHANGED_ONLY_WALL_SIGNS = variant -> ChangedRegistry.WALL_SIGN_VARIANT.getKey(variant).getNamespace().equals(Changed.MODID);

    // -------------------------------------------------------------------------------------------
    // Blocks Tab
    // -------------------------------------------------------------------------------------------
    public static final CreativeModeTab TAB_CHANGED_BLOCKS = new CreativeModeTab("tab_changed_blocks") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ChangedBlocks.WALL_LIGHTRED_STRIPED.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            // 使用辅助方法 accept 来简化代码，模拟 1.20.1 的 output.accept
            accept(items, ChangedBlocks.INFUSER.get());
            accept(items, ChangedBlocks.PURIFIER.get());
            accept(items, ChangedBlocks.STASIS_CHAMBER.get());
            // ... (这里你需要把原代码里长长的列表搬过来，用 accept 包裹)
            accept(items, ChangedBlocks.AIR_CONDITIONER.get());
            accept(items, ChangedBlocks.BAR_STOOL.get());
            accept(items, ChangedBlocks.BAR_TOP.get());
            accept(items, ChangedBlocks.BEAKER.get());
            accept(items, ChangedBlocks.BEDSIDE_IV_RACK.get());
            accept(items, ChangedBlocks.BLACK_RAILING.get());
            accept(items, ChangedBlocks.CLIPBOARD.get());
            accept(items, ChangedBlocks.NOTE.get());
            accept(items, ChangedBlocks.COMPUTER.get());
            accept(items, ChangedBlocks.CANNED_PEACHES.get());
            accept(items, ChangedBlocks.CANNED_SOUP.get());
            accept(items, ChangedBlocks.CARDBOARD_BOX.get());
            accept(items, ChangedBlocks.CARDBOARD_BOX_SMALL.get());
            accept(items, ChangedBlocks.CARDBOARD_BOX_TALL.get());
            accept(items, ChangedBlocks.BOX_PILE.get());
            accept(items, ChangedBlocks.PEN_BOX.get());
            accept(items, ChangedBlocks.DUCT.get());
            accept(items, ChangedBlocks.ERLENMEYER_FLASK.get());
            accept(items, ChangedBlocks.EXOSKELETON_CHARGER.get());
            accept(items, ChangedBlocks.FLOOR_SIGN_WET.get());
            accept(items, ChangedBlocks.FLOOR_SIGN_EXIT.get());
            accept(items, ChangedBlocks.FLOOR_SIGN_ELECTRICAL.get());
            accept(items, ChangedBlocks.GENERATOR.get());
            accept(items, ChangedBlocks.IRON_CRATE.get());
            accept(items, ChangedBlocks.KEYPAD.get());
            accept(items, ChangedBlocks.LAB_LIGHT.get());
            accept(items, ChangedBlocks.LAB_LIGHT_SMALL.get());
            accept(items, ChangedBlocks.LAB_TABLE.get());
            accept(items, ChangedBlocks.LASER_EMITTER.get());
            accept(items, ChangedBlocks.LATEX_CRYSTAL.get());
            accept(items, ChangedBlocks.LATEX_PUP_CRYSTAL.get());
            accept(items, ChangedBlocks.LATEX_CONTAINER.get());
            accept(items, ChangedBlocks.LATEX_TRAFFIC_CONE.get());
            accept(items, ChangedBlocks.BEIFENG_CRYSTAL.get());
            accept(items, ChangedBlocks.BEIFENG_CRYSTAL_SMALL.get());
            accept(items, ChangedBlocks.DARK_DRAGON_CRYSTAL.get());
            accept(items, ChangedBlocks.WOLF_CRYSTAL.get());
            accept(items, ChangedBlocks.WOLF_CRYSTAL_SMALL.get());
            accept(items, ChangedBlocks.DARK_LATEX_CRYSTAL_LARGE.get());
            accept(items, ChangedBlocks.DARK_LATEX_PUDDLE.get());
            accept(items, ChangedBlocks.WHITE_LATEX_PUDDLE_MALE.get());
            accept(items, ChangedBlocks.WHITE_LATEX_PUDDLE_FEMALE.get());
            accept(items, ChangedBlocks.PIPE.get());
            accept(items, ChangedBlocks.PETRI_DISH.get());
            accept(items, ChangedBlocks.RETINAL_SCANNER.get());
            accept(items, ChangedBlocks.ROOMBA_CHARGER.get());
            accept(items, ChangedBlocks.SHIPPING_CONTAINER_BLUE.get());
            accept(items, ChangedBlocks.SHIPPING_CONTAINER_ORANGE.get());
            accept(items, ChangedBlocks.SPEAKER.get());
            accept(items, ChangedBlocks.MICROPHONE.get());
            accept(items, ChangedBlocks.MICROSCOPE.get());
            accept(items, ChangedBlocks.OFFICE_CHAIR.get());
            accept(items, ChangedBlocks.ORANGE_TREE_LEAVES.get());
            accept(items, ChangedBlocks.ORANGE_TREE_SAPLING.get());
            accept(items, ChangedBlocks.TAPE_RECORDER.get());
            accept(items, ChangedBlocks.TILES_BLUE.get());
            accept(items, ChangedBlocks.TILES_BLUE_SMALL.get());
            accept(items, ChangedBlocks.TILES_CAUTION.get());
            accept(items, ChangedBlocks.TILES_CAUTION_SLAB.get());
            accept(items, ChangedBlocks.TILES_CAUTION_STAIRS.get());
            accept(items, ChangedBlocks.TILES_GREENHOUSE.get());
            accept(items, ChangedBlocks.WALL_GREENHOUSE.get());
            accept(items, ChangedBlocks.TILES_GRAYBLUE.get());
            accept(items, ChangedBlocks.TILES_GRAYBLUE_SLAB.get());
            accept(items, ChangedBlocks.TILES_GRAYBLUE_STAIRS.get());
            accept(items, ChangedBlocks.TILES_GRAY.get());
            accept(items, ChangedBlocks.TILES_GRAY_SLAB.get());
            accept(items, ChangedBlocks.TILES_GRAY_STAIRS.get());
            accept(items, ChangedBlocks.TILES_GRAYBLUE_BOLTED.get());
            accept(items, ChangedBlocks.TILES_GRAYBLUE_BOLTED_CONNECTED.get());
            accept(items, ChangedBlocks.TILES_GRAYBLUE_BOLTED_SLAB.get());
            accept(items, ChangedBlocks.TILES_GRAYBLUE_BOLTED_STAIRS.get());
            accept(items, ChangedBlocks.TILES_LIBRARY_BROWN.get());
            accept(items, ChangedBlocks.TILES_LIBRARY_BROWN_SLAB.get());
            accept(items, ChangedBlocks.TILES_LIBRARY_BROWN_STAIRS.get());
            accept(items, ChangedBlocks.TILES_LIBRARY_TAN.get());
            accept(items, ChangedBlocks.TILES_LIBRARY_TAN_SLAB.get());
            accept(items, ChangedBlocks.TILES_LIBRARY_TAN_STAIRS.get());
            accept(items, ChangedBlocks.TILES_TEAL.get());
            accept(items, ChangedBlocks.TILES_WHITE.get());
            accept(items, ChangedBlocks.TILES_WHITE_SLAB.get());
            accept(items, ChangedBlocks.TILES_WHITE_STAIRS.get());
            accept(items, ChangedBlocks.ORANGE_LAB_CARPETING.get());
            accept(items, ChangedBlocks.VENT_FAN.get());
            accept(items, ChangedBlocks.VENT_HATCH.get());
            accept(items, ChangedBlocks.WALL_BLUE_STRIPED.get());
            accept(items, ChangedBlocks.WALL_BLUE_TILED.get());
            accept(items, ChangedBlocks.WALL_CAUTION.get());
            accept(items, ChangedBlocks.WALL_LIBRARY_UPPER.get());
            accept(items, ChangedBlocks.WALL_LIBRARY_LOWER.get());
            accept(items, ChangedBlocks.WALL_LIGHTRED.get());
            accept(items, ChangedBlocks.WALL_LIGHTRED_SLAB.get());
            accept(items, ChangedBlocks.WALL_LIGHTRED_STAIRS.get());
            accept(items, ChangedBlocks.WALL_LIGHTRED_STRIPED.get());
            accept(items, ChangedBlocks.WALL_GRAY.get());
            accept(items, ChangedBlocks.WALL_GRAY_SLAB.get());
            accept(items, ChangedBlocks.WALL_GRAY_STAIRS.get());
            accept(items, ChangedBlocks.WALL_GRAY_STRIPED.get());
            accept(items, ChangedBlocks.WALL_GREEN.get());
            accept(items, ChangedBlocks.WALL_GREEN_SLAB.get());
            accept(items, ChangedBlocks.WALL_GREEN_STAIRS.get());
            accept(items, ChangedBlocks.WALL_GREEN_STRIPED.get());
            accept(items, ChangedBlocks.WALL_VENT.get());
            accept(items, ChangedBlocks.WALL_WHITE.get());
            accept(items, ChangedBlocks.WALL_WHITE_GREEN_STRIPED.get());
            accept(items, ChangedBlocks.WALL_WHITE_GREEN_TILED.get());
            accept(items, ChangedBlocks.WALL_WHITE_SLAB.get());
            accept(items, ChangedBlocks.WALL_WHITE_STAIRS.get());
            accept(items, ChangedBlocks.WHITE_LAB_TABLE.get());

            accept(items, ChangedBlocks.BEEHIVE_BED.get());
            accept(items, ChangedBlocks.BEEHIVE_WALL.get());
            accept(items, ChangedBlocks.BEEHIVE_CORNER.get());
            accept(items, ChangedBlocks.BEEHIVE_FLOOR.get());
            accept(items, ChangedBlocks.BEEHIVE_ROOF.get());

            accept(items, ChangedBlocks.LARGE_LAB_DOOR.get());
            accept(items, ChangedBlocks.LARGE_LIBRARY_DOOR.get());
            accept(items, ChangedBlocks.LARGE_MAINTENANCE_DOOR.get());
            accept(items, ChangedBlocks.LARGE_BLUE_LAB_DOOR.get());
            accept(items, ChangedBlocks.LAB_DOOR.get());
            accept(items, ChangedBlocks.LIBRARY_DOOR.get());
            accept(items, ChangedBlocks.MAINTENANCE_DOOR.get());
            accept(items, ChangedBlocks.BLUE_LAB_DOOR.get());

            accept(items, ChangedBlocks.EMPTY_CANISTER.get());
            accept(items, ChangedBlocks.OXYGENATED_WATER_CANISTER.get());
            accept(items, ChangedBlocks.WOLF_GAS_CANISTER.get());
            accept(items, ChangedBlocks.TIGER_GAS_CANISTER.get());
            accept(items, ChangedBlocks.SKUNK_GAS_CANISTER.get());

            accept(items, ChangedBlocks.DARK_LATEX_BLOCK.get());
            accept(items, ChangedBlocks.DARK_LATEX_ICE.get());
            accept(items, ChangedBlocks.WOLF_CRYSTAL_BLOCK.get());
            accept(items, ChangedBlocks.WHITE_LATEX_BLOCK.get());
            accept(items, ChangedBlocks.WHITE_LATEX_PILLAR.get());

            accept(items, ChangedItems.BIPED_ARMOR_STAND.get());
            accept(items, ChangedItems.CENTAUR_ARMOR_STAND.get());
            accept(items, ChangedItems.LEGLESS_ARMOR_STAND.get());

            // Wall Sign 特殊逻辑 (需修改 WallSignItem.fillItemList 方法签名以适配)
            // 原本是: WallSignItem.fillItemList(CHANGED_ONLY_WALL_SIGNS, params, output);
            // 这里假设你改成了接收 List<ItemStack>:
            WallSignItem.fillItemList(CHANGED_ONLY_WALL_SIGNS, items);

            ChangedBlocks.PILLOWS.values().stream().map(RegistryObject::get).forEach(block -> accept(items, block));

            // Painting 特殊逻辑 (1.18.2 使用 Motive 和 PAINTING_TYPES)
            ForgeRegistries.PAINTING_TYPES.getValues().stream()
                    .filter(motive -> motive.getRegistryName().getNamespace().equals(Changed.MODID))
                    .forEach(motive -> {
                        ItemStack stack = new ItemStack(Items.PAINTING);
                        CompoundTag compoundtag = stack.getOrCreateTagElement("EntityTag");
                        // 1.18.2 画的 NBT 用 "Motive" 字段，存 ResourceLocation 字符串
                        compoundtag.putString("Motive", motive.getRegistryName().toString());
                        items.add(stack);
                    });
        }
    };

    // -------------------------------------------------------------------------------------------
    // Items Tab
    // -------------------------------------------------------------------------------------------
    public static final CreativeModeTab TAB_CHANGED_ITEMS = new CreativeModeTab("tab_changed_items") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ChangedItems.LATEX_BASE.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            accept(items, ChangedItems.GAS_MASK.get());
            accept(items, ChangedItems.COMPACT_DISC.get());
            accept(items, ChangedItems.LAB_BOOK.get());
            accept(items, ChangedItems.LATEX_BASE.get());
            accept(items, ChangedBlocks.MUG.get());
            accept(items, ChangedItems.MUG_WITH_WATER.get());
            accept(items, ChangedItems.MUG_WITH_MILK.get());
            accept(items, ChangedItems.MUG_WITH_COFFEE.get());
            accept(items, ChangedItems.MUG_WITH_DARK_LATEX.get());
            accept(items, ChangedItems.MUG_WITH_WHITE_LATEX.get());
            accept(items, ChangedItems.ORANGE.get());
            accept(items, ChangedItems.SYRINGE.get());
            accept(items, ChangedItems.BLOOD_SYRINGE.get());
            accept(items, ChangedItems.DARK_LATEX_GOO.get());
            accept(items, ChangedItems.DARK_LATEX_BUCKET.get());
            accept(items, ChangedItems.WHITE_LATEX_GOO.get());
            accept(items, ChangedItems.WHITE_LATEX_BUCKET.get());
            accept(items, ChangedItems.DARK_LATEX_CRYSTAL_FRAGMENT.get());
            accept(items, ChangedItems.BEIFENG_CRYSTAL_FRAGMENT.get());
            accept(items, ChangedItems.WOLF_CRYSTAL_FRAGMENT.get());
            accept(items, ChangedItems.DARK_DRAGON_CRYSTAL_FRAGMENT.get());
            accept(items, ChangedItems.LATEX_INKBALL.get());
            accept(items, ChangedItems.ROOMBA.get());
            accept(items, ChangedItems.EXOSKELETON.get());

            // 同样，假设你适配了这些物品内部的 fillItemList 方法
            // 原本的 params 和 output 在 1.18.2 不需要，直接传 list 即可
            // 如果 fillItemList 是 Item 类原生的方法，你需要创建一个 new ItemStack 传进去
            // 这里假设是你自定义的静态方法或实例方法：

            ChangedItems.DARK_LATEX_MASK.get().fillItemList(CHANGED_ONLY_TRANSFURS, items);
            ChangedItems.LATEX_SYRINGE.get().fillItemList(CHANGED_ONLY_TRANSFURS, items);
            ChangedItems.LATEX_FLASK.get().fillItemList(CHANGED_ONLY_TRANSFURS, items);
            ChangedItems.LATEX_TIPPED_ARROW.get().fillItemList(CHANGED_ONLY_TRANSFURS, items);
        }
    };

    // -------------------------------------------------------------------------------------------
    // Entities Tab
    // -------------------------------------------------------------------------------------------
    public static final CreativeModeTab TAB_CHANGED_ENTITIES = new CreativeModeTab("tab_changed_entities") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ChangedItems.DARK_LATEX_MASK.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            ChangedEntities.SPAWN_EGGS.values().stream()
                    .map(RegistryObject::get)
                    .forEach(item -> accept(items, item));
        }
    };

    // -------------------------------------------------------------------------------------------
    // Combat Tab
    // -------------------------------------------------------------------------------------------
    public static final CreativeModeTab TAB_CHANGED_COMBAT = new CreativeModeTab("tab_changed_combat") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ChangedItems.TSC_BATON.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            accept(items, ChangedItems.TSC_BATON.get());
            accept(items, ChangedItems.TSC_STAFF.get());
            accept(items, ChangedItems.TSC_SHIELD.get());

            accept(items, ChangedItems.LEATHER_UPPER_ABDOMEN_ARMOR.get());
            accept(items, ChangedItems.LEATHER_LOWER_ABDOMEN_ARMOR.get());
            accept(items, ChangedItems.LEATHER_QUADRUPEDAL_LEGGINGS.get());
            accept(items, ChangedItems.LEATHER_QUADRUPEDAL_BOOTS.get());

            accept(items, ChangedItems.CHAINMAIL_UPPER_ABDOMEN_ARMOR.get());
            accept(items, ChangedItems.CHAINMAIL_LOWER_ABDOMEN_ARMOR.get());
            accept(items, ChangedItems.CHAINMAIL_QUADRUPEDAL_LEGGINGS.get());
            accept(items, ChangedItems.CHAINMAIL_QUADRUPEDAL_BOOTS.get());

            accept(items, ChangedItems.IRON_UPPER_ABDOMEN_ARMOR.get());
            accept(items, ChangedItems.IRON_LOWER_ABDOMEN_ARMOR.get());
            accept(items, ChangedItems.IRON_QUADRUPEDAL_LEGGINGS.get());
            accept(items, ChangedItems.IRON_QUADRUPEDAL_BOOTS.get());

            accept(items, ChangedItems.GOLDEN_UPPER_ABDOMEN_ARMOR.get());
            accept(items, ChangedItems.GOLDEN_LOWER_ABDOMEN_ARMOR.get());
            accept(items, ChangedItems.GOLDEN_QUADRUPEDAL_LEGGINGS.get());
            accept(items, ChangedItems.GOLDEN_QUADRUPEDAL_BOOTS.get());

            accept(items, ChangedItems.DIAMOND_UPPER_ABDOMEN_ARMOR.get());
            accept(items, ChangedItems.DIAMOND_LOWER_ABDOMEN_ARMOR.get());
            accept(items, ChangedItems.DIAMOND_QUADRUPEDAL_LEGGINGS.get());
            accept(items, ChangedItems.DIAMOND_QUADRUPEDAL_BOOTS.get());

            accept(items, ChangedItems.NETHERITE_UPPER_ABDOMEN_ARMOR.get());
            accept(items, ChangedItems.NETHERITE_LOWER_ABDOMEN_ARMOR.get());
            accept(items, ChangedItems.NETHERITE_QUADRUPEDAL_LEGGINGS.get());
            accept(items, ChangedItems.NETHERITE_QUADRUPEDAL_BOOTS.get());

            accept(items, ChangedItems.ABDOMEN_ARMOR_CONVERSION.get());
            accept(items, ChangedItems.QUADRUPEDAL_ARMOR_CONVERSION.get());
        }
    };

    // -------------------------------------------------------------------------------------------
    // Clothing Tab
    // -------------------------------------------------------------------------------------------
    public static final CreativeModeTab TAB_CHANGED_CLOTHING = new CreativeModeTab("tab_changed_clothing") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ChangedItems.BENIGN_SHORTS.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            accept(items, ChangedItems.BENIGN_SHORTS.get());
            accept(items, ChangedItems.PINK_SHORTS.get());
            accept(items, ChangedItems.BLACK_TSHIRT.get());
            accept(items, ChangedItems.WHITE_TSHIRT.get());
            accept(items, ChangedItems.SPORTS_BRA.get());
            accept(items, ChangedItems.LAB_COAT.get());
            accept(items, ChangedItems.WETSUIT.get());
            accept(items, ChangedItems.NITRILE_GLOVES.get());
            accept(items, ChangedItems.ORANGE_NECK_TIE.get());
            accept(items, ChangedItems.RED_NECK_TIE.get());
            accept(items, ChangedItems.BLUE_NECK_TIE.get());
        }
    };

    // -------------------------------------------------------------------------------------------
    // Music Tab
    // -------------------------------------------------------------------------------------------
    public static final CreativeModeTab TAB_CHANGED_MUSIC = new CreativeModeTab("tab_changed_music") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ChangedItems.PURO_THE_BLACK_GOO_RECORD.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            accept(items, ChangedItems.BLACK_GOO_ZONE_RECORD.get());
            accept(items, ChangedItems.CRYSTAL_ZONE_RECORD.get());
            accept(items, ChangedItems.GAS_ROOM_RECORD.get());
            accept(items, ChangedItems.LABORATORY_RECORD.get());
            accept(items, ChangedItems.OUTSIDE_THE_TOWER_RECORD.get());
            accept(items, ChangedItems.PURO_THE_BLACK_GOO_RECORD.get());
            accept(items, ChangedItems.PUROS_HOME_RECORD.get());
            accept(items, ChangedItems.THE_LIBRARY_RECORD.get());
            accept(items, ChangedItems.THE_LION_CHASE_RECORD.get());
            accept(items, ChangedItems.THE_SCARLET_CRYSTAL_MINE_RECORD.get());
            accept(items, ChangedItems.THE_SHARK_RECORD.get());
            accept(items, ChangedItems.THE_SQUID_DOG_RECORD.get());
            accept(items, ChangedItems.THE_WHITE_GOO_JUNGLE_RECORD.get());
            accept(items, ChangedItems.THE_WHITE_TAIL_CHASE_PART_1.get());
            accept(items, ChangedItems.THE_WHITE_TAIL_CHASE_PART_2.get());
            accept(items, ChangedItems.VENT_PIPE_RECORD.get());
        }
    };

    // -------------------------------------------------------------------------------------------
    // 辅助方法
    // -------------------------------------------------------------------------------------------
    private static void accept(NonNullList<ItemStack> list, ItemLike item) {
        list.add(new ItemStack(item));
    }
}
