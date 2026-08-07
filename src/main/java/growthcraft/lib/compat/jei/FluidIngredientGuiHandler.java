package growthcraft.lib.compat.jei;

import growthcraft.lib.client.screen.FluidIngredientScreen;
import mezz.jei.api.gui.builder.IClickableIngredientFactory;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Optional;

public final class FluidIngredientGuiHandler<M extends AbstractContainerMenu,
        S extends AbstractContainerScreen<M> & FluidIngredientScreen>
        implements IGuiContainerHandler<S> {
    private final IIngredientType<FluidStack> fluidIngredientType;

    @SuppressWarnings("unchecked")
    public FluidIngredientGuiHandler(IPlatformFluidHelper<?> fluidHelper) {
        this.fluidIngredientType = (IIngredientType<FluidStack>) fluidHelper.getFluidIngredientType();
    }

    @Override
    public Optional<? extends IClickableIngredient<?>> getClickableIngredientUnderMouse(
            IClickableIngredientFactory factory, S screen, double mouseX, double mouseY) {
        for (var ingredientArea : screen.getFluidIngredientAreas()) {
            if (!ingredientArea.fluid().isEmpty()
                    && ingredientArea.area().contains((int) mouseX, (int) mouseY)) {
                return factory.createBuilder(fluidIngredientType, ingredientArea.fluid())
                        .buildWithArea(ingredientArea.area());
            }
        }
        return Optional.empty();
    }
}
