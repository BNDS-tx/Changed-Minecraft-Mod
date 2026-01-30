package net.ltxprogrammer.changed.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

//    public RecipeProvider(PackOutput output) {
//        super(output);
//    }
//
//    @Override
//    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
//    }

    // 1. 构造函数参数从 PackOutput 改为 DataGenerator
    public RecipeProvider(DataGenerator generator) {
        super(generator);
    }

    // 2. 方法名从 buildRecipes 改为 buildCraftingRecipes
    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
        // 在这里添加你的配方逻辑
        // 例如:
//         ShapelessRecipeBuilder.shapeless(Items.BOOK)
//             .requires(Items.DIRT)
//             .unlockedBy("has_dirt", has(Items.DIRT))
//             .save(recipeConsumer);
    }
}
