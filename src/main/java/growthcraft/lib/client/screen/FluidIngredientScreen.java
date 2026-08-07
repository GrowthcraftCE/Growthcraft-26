package growthcraft.lib.client.screen;

import net.minecraft.client.renderer.Rect2i;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public interface FluidIngredientScreen {
    List<FluidIngredientArea> getFluidIngredientAreas();

    record FluidIngredientArea(FluidStack fluid, Rect2i area) {
    }
}
