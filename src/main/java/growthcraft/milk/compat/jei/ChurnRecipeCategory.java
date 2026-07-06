package growthcraft.milk.compat.jei;

import growthcraft.milk.config.Reference;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.recipe.ChurnRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class ChurnRecipeCategory implements IRecipeCategory<RecipeHolder<ChurnRecipe>> {
    private static final int WIDTH = 160;
    private static final int HEIGHT = 70;
    private static final int INPUT_TANK_X = 20;
    private static final int INPUT_TANK_Y = 10;
    private static final int OUTPUT_TANK_X = 114;
    private static final int OUTPUT_TANK_Y = 10;
    private static final int TANK_WIDTH = 14;
    private static final int TANK_HEIGHT = 44;
    private static final int ARROW_X = 50;
    private static final int ARROW_Y = 23;
    private static final int ARROW_WIDTH = 32;
    private static final int BYPRODUCT_X = 140;
    private static final int BYPRODUCT_Y = 25;
    private final IDrawable icon;

    public ChurnRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(GrowthcraftMilkItems.CHURN.get()));
    }

    @Override
    public RecipeType<RecipeHolder<ChurnRecipe>> getRecipeType() {
        return GrowthcraftMilkJeiPlugin.CHURN;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.growthcraft_milk.category.churn");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ChurnRecipe> holder, IFocusGroup focuses) {
        ChurnRecipe recipe = holder.value();
        Fluid inputFluid = BuiltInRegistries.FLUID.getValue(recipe.getInputFluid().fluidId());
        if (inputFluid != Fluids.EMPTY && recipe.getInputFluid().amount() > 0) {
            builder.addSlot(RecipeIngredientRole.INPUT, INPUT_TANK_X, INPUT_TANK_Y)
                    .setFluidRenderer(1000, true, TANK_WIDTH, TANK_HEIGHT)
                    .addFluidStack(inputFluid, recipe.getInputFluid().amount());
        }

        Fluid outputFluid = BuiltInRegistries.FLUID.getValue(recipe.getOutputFluid().fluidId());
        if (outputFluid != Fluids.EMPTY && recipe.getOutputFluid().amount() > 0) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_TANK_X, OUTPUT_TANK_Y)
                    .setFluidRenderer(1000, true, TANK_WIDTH, TANK_HEIGHT)
                    .addFluidStack(outputFluid, recipe.getOutputFluid().amount());
        }

        ItemStack byProduct = recipe.getByProduct();
        if (!byProduct.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, BYPRODUCT_X, BYPRODUCT_Y)
                    .addItemStack(byProduct);
        }
    }

    @Override
    public void draw(RecipeHolder<ChurnRecipe> holder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        drawTankFrame(graphics, INPUT_TANK_X, INPUT_TANK_Y);
        drawTankFrame(graphics, OUTPUT_TANK_X, OUTPUT_TANK_Y);
        drawSlotFrame(graphics, BYPRODUCT_X, BYPRODUCT_Y);
        drawArrow(graphics);

        Font font = Minecraft.getInstance().font;
        Component plunges = Component.translatable("message.growthcraft_milk.churn.jei_plunges", holder.value().getPlungesNeeded());
    }
private static void drawTankFrame(GuiGraphicsExtractor graphics, int x, int y) {
    }

    private static void drawArrow(GuiGraphicsExtractor graphics) {
        int centerY = ARROW_Y + 4;
    }

    private static void drawSlotFrame(GuiGraphicsExtractor graphics, int x, int y) {
    }
}
