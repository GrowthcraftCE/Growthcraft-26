package growthcraft.milk.compat.jei;

import growthcraft.milk.config.Reference;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.recipe.CheesePressRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CheesePressRecipeCategory implements IRecipeCategory<RecipeHolder<CheesePressRecipe>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/cheese_press_screen.png");
    private static final int WIDTH = 160;
    private static final int HEIGHT = 70;
    private static final int INPUT_X = 43;
    private static final int INPUT_Y = 25;
    private static final int OUTPUT_X = 96;
    private static final int OUTPUT_Y = 25;
    private static final int PROGRESS_X = 66;
    private static final int PROGRESS_Y = 31;
    private static final int PROGRESS_WIDTH = 22;
    private static final int PROGRESS_HEIGHT = 6;
    private final IDrawableStatic background;
    private final IDrawable icon;

    public CheesePressRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(TEXTURE, 10, 10, WIDTH, HEIGHT)
                .setTextureSize(256, 256)
                .build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(GrowthcraftMilkItems.CHEESE_PRESS.get()));
    }

    @Override
    public RecipeType<RecipeHolder<CheesePressRecipe>> getRecipeType() {
        return GrowthcraftMilkJeiPlugin.CHEESE_PRESS;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.growthcraft_milk.category.cheese_press");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CheesePressRecipe> holder, IFocusGroup focuses) {
        CheesePressRecipe recipe = holder.value();
        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, INPUT_Y)
                .addItemStack(recipe.getInputItem());

        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(RecipeHolder<CheesePressRecipe> holder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        background.draw(graphics, 0, 0);
        Font font = Minecraft.getInstance().font;
        Component time = Component.literal(formatTicks(holder.value().getProcessingTime()));
        graphics.text(font, time, (WIDTH - font.width(time)) / 2, 58, 0xFF404040, false);
    }
private static String formatTicks(int ticks) {
        int seconds = Math.max(1, ticks / 20);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return minutes > 0 ? minutes + "m " + remainingSeconds + "s" : seconds + "s";
    }
}
