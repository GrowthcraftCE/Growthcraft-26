package growthcraft.cellar.init;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.block.entity.BrewKettleBlockEntity;
import growthcraft.cellar.block.entity.CultureJarBlockEntity;
import growthcraft.cellar.block.entity.FermentationBarrelBlockEntity;
import growthcraft.cellar.block.entity.FruitPressBlockEntity;
import growthcraft.cellar.block.LargeFermentationBarrelBlock;
import growthcraft.cellar.block.LargeStorageBarrelBlock;
import growthcraft.cellar.block.entity.LargeFermentationBarrelBlockEntity;
import growthcraft.cellar.block.entity.LargeStorageBarrelBlockEntity;
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
        event.registerBlock(Capabilities.Fluid.BLOCK,
                (level, pos, state, blockEntity, side) -> {
                    var controller = level.getBlockEntity(LargeFermentationBarrelBlock.getControllerPos(pos, state));
                    return controller instanceof LargeFermentationBarrelBlockEntity barrel
                            ? LegacyFluidResourceHandler.of(barrel.getTank())
                            : null;
                },
                GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_ACACIA.get(),
                GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_APPLE.get(),
                GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_BAMBOO.get(),
                GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_BIRCH.get(),
                GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_CHERRY.get(),
                GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_CRIMSON.get(),
                GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_DARK_OAK.get(),
                GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_JUNGLE.get(),
                GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_MANGROVE.get(),
                GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_OAK.get(),
                GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_PALE_OAK.get(),
                GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_SPRUCE.get(),
                GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_WARPED.get());
        event.registerBlock(Capabilities.Fluid.BLOCK,
                (level, pos, state, blockEntity, side) -> {
                    var controller = level.getBlockEntity(LargeStorageBarrelBlock.getControllerPos(pos, state));
                    return controller instanceof LargeStorageBarrelBlockEntity barrel
                            ? LegacyFluidResourceHandler.of(barrel.getTank())
                            : null;
                }, GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_ACACIA.get(), GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_APPLE.get(),
                GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_BAMBOO.get(), GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_BIRCH.get(),
                GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_CHERRY.get(), GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_CRIMSON.get(),
                GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_DARK_OAK.get(), GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_JUNGLE.get(),
                GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_MANGROVE.get(), GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_OAK.get(),
                GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_PALE_OAK.get(), GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_SPRUCE.get(),
                GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_WARPED.get());
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, GrowthcraftCellarBlockEntities.FRUIT_PRESS.get(),
                (FruitPressBlockEntity be, Direction side) -> LegacyFluidResourceHandler.of(be.getTank(), false, true));
    }
}
