package growthcraft.cellar.compat.jei;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.recipe.BrewKettleRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
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
import net.minecraft.world.level.material.Fluids;

public class BrewKettleRecipeCategory implements IRecipeCategory<RecipeHolder<BrewKettleRecipe>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/brew_kettle_screen.png");
    private static final int WIDTH = 160;
    private static final int HEIGHT = 70;

    private static final Component INFO_NO_LID = Component.translatable("message.growthcraft_cellar.kettle.jei_info_no_lid");
    private static final Component INFO_NEED_LID = Component.translatable("message.growthcraft_cellar.kettle.jei_info_need_lid");
    private final IDrawable icon;
    private final IDrawableAnimated progress;
    private final IDrawableStatic heat;
    private final IDrawableStatic infoIcon;
    private final IDrawableStatic timeIcon;

    public BrewKettleRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(GrowthcraftCellarItems.BREW_KETTLE.get()));
        this.progress = guiHelper.drawableBuilder(TEXTURE, 176, 0, 9, 28)
                .setTextureSize(256, 256)
                .buildAnimated(120, IDrawableAnimated.StartDirection.TOP, false);
        this.heat = guiHelper.drawableBuilder(TEXTURE, 176, 28, 13, 13)
                .setTextureSize(256, 256)
                .build();
        this.timeIcon = guiHelper.drawableBuilder(TEXTURE, 54, 184, 11, 11)
                .setTextureSize(256, 256)
                .build();
        this.infoIcon = guiHelper.drawableBuilder(TEXTURE, 66, 184, 11, 11)
                .setTextureSize(256, 256)
                .build();
    }

    @Override
    public RecipeType<RecipeHolder<BrewKettleRecipe>> getRecipeType() {
        return GrowthcraftCellarJeiPlugin.BREW_KETTLE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.growthcraft_cellar.category.brew_kettle");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<BrewKettleRecipe> holder, IFocusGroup focuses) {
        BrewKettleRecipe recipe = holder.value();

        builder.addSlot(RecipeIngredientRole.INPUT, 70, 25)
                .addIngredients(recipe.getInputItem().ingredient());

        var inputFluid = BuiltInRegistries.FLUID.getValue(recipe.getInputFluid().fluidId());
        if (inputFluid != Fluids.EMPTY && recipe.getInputFluid().amount() > 0) {
            builder.addSlot(RecipeIngredientRole.INPUT, 36, 7)
                    .setFluidRenderer(4000, true, 16, 52)
                    .addFluidStack(inputFluid, recipe.getInputFluid().amount());
        }

        var outputFluid = BuiltInRegistries.FLUID.getValue(recipe.getOutputFluid().fluidId());
        if (outputFluid != Fluids.EMPTY && recipe.getOutputFluid().amount() > 0) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 7)
                    .setFluidRenderer(4000, true, 16, 52)
                    .addFluidStack(outputFluid, recipe.getOutputFluid().amount());
        }

        ItemStack byProduct = recipe.getByProduct();
        if (!byProduct.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 131, 7)
                    .addItemStack(byProduct);
        }
    }

    @Override
    public void draw(RecipeHolder<BrewKettleRecipe> holder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        BrewKettleRecipe recipe = holder.value();
        progress.draw(graphics, 88, 20);
        if (recipe.requiresHeat()) {
            heat.draw(graphics, 58, 43);
        }

        Font font = Minecraft.getInstance().font;
        if (!recipe.getByProduct().isEmpty()) {
        }

        timeIcon.draw(graphics, 2, 27);

        infoIcon.draw(graphics, 2, 60);
        Component lidInfo = recipe.requiresLid() ? INFO_NEED_LID : INFO_NO_LID;
    }
private static String formatTicks(int ticks) {
        int seconds = Math.max(1, ticks / 20);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return minutes > 0 ? minutes + "m " + remainingSeconds + "s" : seconds + "s";
    }
}
