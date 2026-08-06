package growthcraft.cellar.init;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.block.entity.BrewKettleBlockEntity;
import growthcraft.cellar.block.entity.CultureJarBlockEntity;
import growthcraft.cellar.block.entity.FermentationBarrelBlockEntity;
import growthcraft.cellar.block.entity.FruitPressBlockEntity;
import growthcraft.cellar.config.GrowthcraftCellarConfig;
import growthcraft.lib.fluid.LegacyFluidResourceHandler;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

/**
 * Registers NeoForge capabilities for the Cellar module.
 */
public final class GrowthcraftCellarCapabilities {
    private GrowthcraftCellarCapabilities() {}

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        if (GrowthcraftCellarConfig.isCapabilitiesDebugEnabled()) {
            GrowthcraftCellar.LOGGER.debug("[Capabilities] Registering Cellar fluid transfer capabilities");
        }
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, GrowthcraftCellarBlockEntities.CULTURE_JAR.get(),
                (CultureJarBlockEntity be, Direction side) -> LegacyFluidResourceHandler.of(be.getTank()));
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, GrowthcraftCellarBlockEntities.BREW_KETTLE.get(),
                (BrewKettleBlockEntity be, Direction side) -> side == Direction.UP
                        ? LegacyFluidResourceHandler.of(be.getInputTank(), true, true)
                        : LegacyFluidResourceHandler.of(be.getOutputTank(), false, true));
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, GrowthcraftCellarBlockEntities.FERMENTATION_BARREL.get(),
                (FermentationBarrelBlockEntity be, Direction side) -> LegacyFluidResourceHandler.of(be.getTank()));
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, GrowthcraftCellarBlockEntities.FRUIT_PRESS.get(),
                (FruitPressBlockEntity be, Direction side) -> LegacyFluidResourceHandler.of(be.getTank(), false, true));
    }
}
