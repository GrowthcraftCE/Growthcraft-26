package growthcraft.milk.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jspecify.annotations.Nullable;

public final class CheeseProcessRecipeCategory implements IRecipeCategory<CheeseProcessRecipeCategory.Recipe> {
    private static final int WIDTH = 110;
    private static final int HEIGHT = 26;
    private final RecipeType<Recipe> recipeType;
    private final Component title;
    private final IDrawable icon;

    public CheeseProcessRecipeCategory(IGuiHelper guiHelper, RecipeType<Recipe> recipeType, String titleKey, ItemStack icon) {
        this.recipeType = recipeType;
        this.title = Component.translatable(titleKey);
        this.icon = guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public RecipeType<Recipe> getRecipeType() {
        return recipeType;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 5).addItemStack(recipe.input());
        if (recipe.secondaryInput() != null) {
            builder.addSlot(RecipeIngredientRole.INPUT, 41, 5).addIngredients(recipe.secondaryInput());
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 85, 5).addItemStack(recipe.output());
    }

    public record Recipe(ItemStack input, @Nullable Ingredient secondaryInput, ItemStack output) {
        public static Recipe oneToOne(net.minecraft.world.item.Item input, net.minecraft.world.item.Item output) {
            return new Recipe(input.getDefaultInstance(), null, output.getDefaultInstance());
        }

        public static Recipe twoToOne(net.minecraft.world.item.Item input, Ingredient secondaryInput,
                                      net.minecraft.world.item.Item output) {
            return new Recipe(input.getDefaultInstance(), secondaryInput, output.getDefaultInstance());
        }
    }
}
