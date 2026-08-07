package growthcraft.cellar.compat.jei;

import growthcraft.cellar.client.screen.BrewKettleScreen;
import mezz.jei.api.gui.builder.IClickableIngredientFactory;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Optional;

final class BrewKettleGuiHandler implements IGuiContainerHandler<BrewKettleScreen> {
    private final IIngredientType<FluidStack> fluidIngredientType;

    @SuppressWarnings("unchecked")
    BrewKettleGuiHandler(IPlatformFluidHelper<?> fluidHelper) {
        this.fluidIngredientType = (IIngredientType<FluidStack>) fluidHelper.getFluidIngredientType();
    }

    @Override
    public Optional<? extends IClickableIngredient<?>> getClickableIngredientUnderMouse(
            IClickableIngredientFactory factory, BrewKettleScreen screen, double mouseX, double mouseY) {
        Optional<? extends IClickableIngredient<?>> input = clickable(
                factory, screen.getMenu().getInputFluidStack(), screen.getInputTankArea(), mouseX, mouseY);
        if (input.isPresent()) {
            return input;
        }
        return clickable(factory, screen.getMenu().getOutputFluidStack(), screen.getOutputTankArea(), mouseX, mouseY);
    }

    private Optional<? extends IClickableIngredient<?>> clickable(IClickableIngredientFactory factory,
                                                                   FluidStack fluid, Rect2i area,
                                                                   double mouseX, double mouseY) {
        if (fluid.isEmpty() || !area.contains((int) mouseX, (int) mouseY)) {
            return Optional.empty();
        }
        return factory.createBuilder(fluidIngredientType, fluid).buildWithArea(area);
    }
}
