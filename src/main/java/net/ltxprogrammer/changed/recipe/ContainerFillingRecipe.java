package net.ltxprogrammer.changed.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.IntList;
import net.ltxprogrammer.changed.init.ChangedRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry; // 1.18.2 必须引入这个
import org.jetbrains.annotations.NotNull;

import java.util.List;

//public class ContainerFillingRecipe implements CraftingRecipe {
//    private final ResourceLocation id;
//    final String group;
//    final NonNullList<Ingredient> ingredients;
//    final NonNullList<Ingredient> minimumIngredients;
//    final Item container;
//    final int containerCountLimit;
//    final Item result;
//
//    public ContainerFillingRecipe(ResourceLocation id, String group, NonNullList<Ingredient> ingredients, Item container, int containerCountLimit, Item result) {
//        this.id = id;
//        this.group = group;
//        this.ingredients = ingredients;
//        this.minimumIngredients = NonNullList.create();
//        minimumIngredients.addAll(this.ingredients);
//        minimumIngredients.add(Ingredient.of(container));
//
//        this.container = container;
//        this.containerCountLimit = containerCountLimit;
//        this.result = result;
//    }
//
//    @Override
//    public @NotNull ResourceLocation getId() {
//        return id;
//    }
//
//    @Override
//    public @NotNull CraftingBookCategory category() {
//        return CraftingBookCategory.MISC;
//    }
//
//    @Override
//    public @NotNull NonNullList<Ingredient> getIngredients() {
//        return minimumIngredients;
//    }
//
//    @Override
//    public boolean matches(@NotNull CraftingContainer container, @NotNull Level level) {
//        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
//        int nonEmptyStacks = 0;
//        int containerStacks = 0;
//
//        for(int j = 0; j < container.getContainerSize(); ++j) {
//            ItemStack itemstack = container.getItem(j);
//            if (!itemstack.isEmpty()) {
//                if (!itemstack.is(this.container))
//                    ++nonEmptyStacks;
//                else {
//                    ++containerStacks;
//                    continue;
//                }
//
//                inputs.add(itemstack);
//            }
//        }
//
//        return nonEmptyStacks == this.ingredients.size() &&
//                containerStacks > 0 && containerStacks <= containerCountLimit &&
//                net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.ingredients) != null;
//    }
//
//    @Override
//    public @NotNull ItemStack assemble(@NotNull CraftingContainer container, @NotNull RegistryAccess registryAccess) {
//        int containerStacks = 0;
//
//        for(int j = 0; j < container.getContainerSize(); ++j) {
//            ItemStack itemstack = container.getItem(j);
//            if (!itemstack.isEmpty()) {
//                if (itemstack.is(this.container))
//                    ++containerStacks;
//            }
//        }
//
//        return new ItemStack(this.result, Mth.clamp(containerStacks, 1, this.containerCountLimit));
//    }
//
//    @Override
//    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
//        return new ItemStack(this.result);
//    }
//
//    @Override
//    public boolean canCraftInDimensions(int width, int height) {
//        return width * height >= ingredients.size() + 1;
//    }
//
//    @Override
//    public @NotNull RecipeSerializer<?> getSerializer() {
//        return ChangedRecipeSerializers.CONTAINER_FILL_RECIPE.get();
//    }
//
//    @Override
//    public @NotNull String getGroup() {
//        return group;
//    }
//
//    @Override
//    public boolean isSpecial() {
//        return false;
//    }
//
//    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ContainerFillingRecipe> {
//        public ContainerFillingRecipe fromJson(ResourceLocation id, JsonObject json) {
//            String group = GsonHelper.getAsString(json, "group", "");
//            NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"));
//            if (nonnulllist.isEmpty()) {
//                throw new JsonParseException("No ingredients for container filling recipe");
//            } else if (nonnulllist.size() > ShapedRecipe.MAX_WIDTH * ShapedRecipe.MAX_HEIGHT - 1) {
//                throw new JsonParseException("Too many ingredients for container filling recipe. The maximum is " + (ShapedRecipe.MAX_WIDTH * ShapedRecipe.MAX_HEIGHT - 1));
//            } else {
//                Item container = ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("container").getAsString()));
//                if (nonnulllist.stream().anyMatch(ingredient -> {
//                    assert container != null;
//                    return ingredient.test(new ItemStack(container));
//                }))
//                    throw new JsonParseException("Cannot set container to ingredient item");
//                int countLimit = GsonHelper.getAsInt(json, "containerCountLimit", Integer.MAX_VALUE);
//                Item out = ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("result").getAsString()));
//                return new ContainerFillingRecipe(id, group, nonnulllist, container, countLimit, out);
//            }
//        }
//
//        private static NonNullList<Ingredient> itemsFromJson(JsonArray p_44276_) {
//            NonNullList<Ingredient> nonnulllist = NonNullList.create();
//
//            for (int i = 0; i < p_44276_.size(); ++i) {
//                Ingredient ingredient = Ingredient.fromJson(p_44276_.get(i));
//                if (!ingredient.isEmpty()) {
//                    nonnulllist.add(ingredient);
//                }
//            }
//
//            return nonnulllist;
//        }
//
//        public ContainerFillingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
//            String group = buffer.readUtf();
//
//            int i = buffer.readVarInt();
//            NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);
//            for (int j = 0; j < ingredients.size(); ++j) {
//                ingredients.set(j, Ingredient.fromNetwork(buffer));
//            }
//
//            Item container = ForgeRegistries.ITEMS.getValue(new ResourceLocation(buffer.readUtf()));
//            int countLimit = buffer.readVarInt();
//            Item out = ForgeRegistries.ITEMS.getValue(new ResourceLocation(buffer.readUtf()));
//            return new ContainerFillingRecipe(id, group, ingredients, container, countLimit, out);
//        }
//
//        public void toNetwork(FriendlyByteBuf buffer, ContainerFillingRecipe recipe) {
//            buffer.writeUtf(recipe.group);
//
//            buffer.writeVarInt(recipe.ingredients.size());
//
//            for (Ingredient ingredient : recipe.ingredients) {
//                ingredient.toNetwork(buffer);
//            }
//
//            buffer.writeUtf(ForgeRegistries.ITEMS.getKey(recipe.container).toString());
//            buffer.writeVarInt(recipe.containerCountLimit);
//            buffer.writeUtf(ForgeRegistries.ITEMS.getKey(recipe.result).toString());
//        }
//    }
//}

public class ContainerFillingRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    final String group;
    final NonNullList<Ingredient> ingredients;
    final NonNullList<Ingredient> minimumIngredients;
    final Item container;
    final int containerCountLimit;
    final Item result;

    public ContainerFillingRecipe(ResourceLocation id, String group, NonNullList<Ingredient> ingredients, Item container, int containerCountLimit, Item result) {
        this.id = id;
        this.group = group;
        this.ingredients = ingredients;
        this.minimumIngredients = NonNullList.create();
        minimumIngredients.addAll(this.ingredients);
        minimumIngredients.add(Ingredient.of(container));

        this.container = container;
        this.containerCountLimit = containerCountLimit;
        this.result = result;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    // --- 1. 删除 category() 方法 (1.18.2 没有) ---
    // @Override
    // public @NotNull CraftingBookCategory category() { ... }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return minimumIngredients;
    }

    @Override
    public boolean matches(@NotNull CraftingContainer container, @NotNull Level level) {
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int nonEmptyStacks = 0;
        int containerStacks = 0;

        for(int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemstack = container.getItem(j);
            if (!itemstack.isEmpty()) {
                if (!itemstack.is(this.container))
                    ++nonEmptyStacks;
                else {
                    ++containerStacks;
                    continue;
                }

                inputs.add(itemstack);
            }
        }

        return nonEmptyStacks == this.ingredients.size() &&
                containerStacks > 0 && containerStacks <= containerCountLimit &&
                net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.ingredients) != null;
    }

    // --- 2. 修改 assemble 方法 ---
    // 1.20.1: assemble(CraftingContainer, RegistryAccess)
    // 1.18.2: assemble(CraftingContainer)
    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer container) {
        int containerStacks = 0;

        for(int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemstack = container.getItem(j);
            if (!itemstack.isEmpty()) {
                if (itemstack.is(this.container))
                    ++containerStacks;
            }
        }

        return new ItemStack(this.result, Mth.clamp(containerStacks, 1, this.containerCountLimit));
    }

    // --- 3. 修改 getResultItem 方法 ---
    // 1.20.1: getResultItem(RegistryAccess)
    // 1.18.2: getResultItem()
    @Override
    public @NotNull ItemStack getResultItem() {
        return new ItemStack(this.result);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= ingredients.size() + 1;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ChangedRecipeSerializers.CONTAINER_FILL_RECIPE.get();
    }

    @Override
    public @NotNull String getGroup() {
        return group;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    // --- 4. 修改 Serializer 继承关系 ---
    // 1.18.2 中 ForgeRegistryEntry 是一个独立的类，不在 registers 包下，或者泛型写法略有不同
    // 通常可以直接继承 ForgeRegistryEntry<RecipeSerializer<?>>
    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ContainerFillingRecipe> {

        @Override
        public ContainerFillingRecipe fromJson(ResourceLocation id, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for container filling recipe");
            } else if (nonnulllist.size() > 3 * 3 - 1) {
                throw new JsonParseException("Too many ingredients for container filling recipe. The maximum is " + (3 * 3 - 1));
            } else {
                Item container = ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("container").getAsString()));
                if (nonnulllist.stream().anyMatch(ingredient -> {
                    assert container != null;
                    return ingredient.test(new ItemStack(container));
                }))
                    throw new JsonParseException("Cannot set container to ingredient item");
                int countLimit = GsonHelper.getAsInt(json, "containerCountLimit", Integer.MAX_VALUE);
                Item out = ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("result").getAsString()));
                return new ContainerFillingRecipe(id, group, nonnulllist, container, countLimit, out);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray p_44276_) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for (int i = 0; i < p_44276_.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(p_44276_.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        @Override
        public ContainerFillingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();

            int i = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < ingredients.size(); ++j) {
                ingredients.set(j, Ingredient.fromNetwork(buffer));
            }

            Item container = ForgeRegistries.ITEMS.getValue(new ResourceLocation(buffer.readUtf()));
            int countLimit = buffer.readVarInt();
            Item out = ForgeRegistries.ITEMS.getValue(new ResourceLocation(buffer.readUtf()));
            return new ContainerFillingRecipe(id, group, ingredients, container, countLimit, out);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ContainerFillingRecipe recipe) {
            buffer.writeUtf(recipe.group);

            buffer.writeVarInt(recipe.ingredients.size());

            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buffer);
            }

            // 1.18.2: 建议判空或者确保 container/result 不为空
            ResourceLocation containerLoc = ForgeRegistries.ITEMS.getKey(recipe.container);
            buffer.writeUtf(containerLoc != null ? containerLoc.toString() : "minecraft:air");

            buffer.writeVarInt(recipe.containerCountLimit);

            ResourceLocation resultLoc = ForgeRegistries.ITEMS.getKey(recipe.result);
            buffer.writeUtf(resultLoc != null ? resultLoc.toString() : "minecraft:air");
        }
    }
}
