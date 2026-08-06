package growthcraft.lib.client.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.neoforged.neoforge.fluids.FluidStack;

public class MachineFluidRenderState extends BlockEntityRenderState {
    public FluidStack inputFluid = FluidStack.EMPTY;
    public int inputCapacity;
    public FluidStack outputFluid = FluidStack.EMPTY;
    public int outputCapacity;
    public FluidStack outputFluid1 = FluidStack.EMPTY;
    public int outputCapacity1;
}
