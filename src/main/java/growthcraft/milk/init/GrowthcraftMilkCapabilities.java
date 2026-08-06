package growthcraft.milk.init;

import growthcraft.lib.fluid.LegacyFluidResourceHandler;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;

public final class GrowthcraftMilkCapabilities {
    private GrowthcraftMilkCapabilities() {
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, GrowthcraftMilkBlockEntities.CHURN.get(),
                (be, side) -> LegacyFluidResourceHandler.of(be.getTank()));
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, GrowthcraftMilkBlockEntities.PANCHEON.get(),
                (be, side) -> LegacyFluidResourceHandler.of(be.getFluidHandler()));
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, GrowthcraftMilkBlockEntities.MIXING_VAT.get(),
                (be, side) -> LegacyFluidResourceHandler.of(be.getFluidHandler()));
    }
}
