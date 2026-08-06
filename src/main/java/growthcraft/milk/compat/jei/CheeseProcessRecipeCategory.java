package growthcraft.milk.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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

    @Override
    public void draw(Recipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView,
                     GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        drawSlotFrame(graphics, 8, 5);
        if (recipe.secondaryInput() != null) {
            drawSlotFrame(graphics, 41, 5);
        }
        drawArrow(graphics, recipe.secondaryInput() == null ? 36 : 64, 13, recipe.secondaryInput() == null ? 42 : 21);
        drawSlotFrame(graphics, 85, 5);
    }

    private static void drawSlotFrame(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.fill(x - 1, y - 1, x + 17, y + 17, 0xFF6B6B6B);
        graphics.fill(x, y, x + 16, y + 16, 0xFFE5E0CF);
        graphics.fill(x + 1, y + 1, x + 15, y + 15, 0xFFB8B8B8);
        graphics.fill(x + 2, y + 2, x + 14, y + 14, 0xFFEDEDED);
    }

    private static void drawArrow(GuiGraphicsExtractor graphics, int x, int centerY, int width) {
        graphics.fill(x, centerY - 1, x + width - 7, centerY + 1, 0xFF6B6B6B);
        graphics.fill(x + width - 7, centerY - 4, x + width - 5, centerY + 4, 0xFF6B6B6B);
        graphics.fill(x + width - 5, centerY - 3, x + width - 3, centerY + 3, 0xFF6B6B6B);
        graphics.fill(x + width - 3, centerY - 2, x + width - 1, centerY + 2, 0xFF6B6B6B);
        graphics.fill(x + width - 1, centerY - 1, x + width + 1, centerY + 1, 0xFF6B6B6B);
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
