package growthcraft.core.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.core.init.GrowthcraftRecipes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

public final class NoMirrorShapedRecipe extends ShapedRecipe {
    public static final MapCodec<NoMirrorShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(recipe -> recipe.commonInfo),
            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(recipe -> recipe.bookInfo),
            ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
            ItemStackTemplate.MAP_CODEC.codec().fieldOf("result").forGetter(recipe -> recipe.result)
    ).apply(instance, NoMirrorShapedRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, NoMirrorShapedRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Recipe.CommonInfo.STREAM_CODEC, recipe -> recipe.commonInfo,
                    CraftingRecipe.CraftingBookInfo.STREAM_CODEC, recipe -> recipe.bookInfo,
                    ShapedRecipePattern.STREAM_CODEC, recipe -> recipe.pattern,
                    ItemStackTemplate.STREAM_CODEC, recipe -> recipe.result,
                    NoMirrorShapedRecipe::new);

    private final ItemStackTemplate result;

    public NoMirrorShapedRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo,
                                ShapedRecipePattern pattern, ItemStackTemplate result) {
        super(commonInfo, bookInfo, pattern, result);
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.width() != pattern.width() || input.height() != pattern.height()) {
            return false;
        }

        for (int y = 0; y < pattern.height(); y++) {
            for (int x = 0; x < pattern.width(); x++) {
                var ingredient = pattern.ingredients().get(x + y * pattern.width());
                if (!Ingredient.testOptionalIngredient(ingredient, input.getItem(x, y))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public RecipeSerializer<ShapedRecipe> getSerializer() {
        return (RecipeSerializer) GrowthcraftRecipes.SHAPED_NO_MIRROR.get();
    }
}
