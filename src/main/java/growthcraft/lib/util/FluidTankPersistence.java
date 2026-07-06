package growthcraft.lib.util;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public final class FluidTankPersistence {
    private FluidTankPersistence() {
    }

    public static void save(ValueOutput output, String key, FluidTank tank) {
        if (tank.getFluid().isEmpty()) return;
        tank.serialize(output.child(key));
    }

    public static void load(ValueInput input, String key, FluidTank tank) {
        input.child(key).ifPresentOrElse(tank::deserialize, () -> tank.setFluid(FluidStack.EMPTY));
    }
}
