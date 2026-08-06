package growthcraft.cellar.client;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.client.renderer.BrewKettleBlockEntityRenderer;
import growthcraft.cellar.client.renderer.CorkCoasterBlockEntityRenderer;
import growthcraft.cellar.client.renderer.CultureJarBlockEntityRenderer;
import growthcraft.cellar.client.renderer.FruitPressBlockEntityRenderer;
import growthcraft.cellar.client.screen.BrewKettleScreen;
import growthcraft.cellar.client.screen.CultureJarScreen;
import growthcraft.cellar.client.screen.FermentationBarrelScreen;
import growthcraft.cellar.client.screen.FruitPressScreen;
import growthcraft.cellar.client.screen.RoasterScreen;
import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.init.GrowthcraftCellarFluids;
import growthcraft.cellar.init.GrowthcraftCellarMenus;
import growthcraft.lib.client.GrowthcraftFluidModels;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/**
 * Client-only registrations for Growthcraft Cellar.
 * Client-only registrations for Growthcraft Cellar.
 */
@EventBusSubscriber(modid = GrowthcraftCellar.MODID, value = Dist.CLIENT)
public final class GrowthcraftCellarClient {
    private GrowthcraftCellarClient() {}

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(GrowthcraftCellarBlockEntities.BREW_KETTLE.get(), BrewKettleBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(GrowthcraftCellarBlockEntities.CULTURE_JAR.get(), CultureJarBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(GrowthcraftCellarBlockEntities.FRUIT_PRESS.get(), FruitPressBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(GrowthcraftCellarBlockEntities.CORK_COASTER.get(), CorkCoasterBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.ItemTintSources event) {
        if (event == null) {
            return;
        }
        // Growthcraft item tints are provided by 26.x item definition JSON.
    }

    @SubscribeEvent
    public static void onRegisterFluidModels(RegisterFluidModelsEvent event) {
        GrowthcraftFluidModels.registerContainers(event, Reference.MODID, GrowthcraftCellarFluids.ALL);
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        GrowthcraftFluidModels.registerClientExtensions(event, GrowthcraftCellarFluids.ALL);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(GrowthcraftCellarMenus.CULTURE_JAR.get(), CultureJarScreen::new);
        event.register(GrowthcraftCellarMenus.BREW_KETTLE.get(), BrewKettleScreen::new);
        event.register(GrowthcraftCellarMenus.FERMENTATION_BARREL.get(), FermentationBarrelScreen::new);
        event.register(GrowthcraftCellarMenus.FRUIT_PRESS.get(), FruitPressScreen::new);
        event.register(GrowthcraftCellarMenus.ROASTER.get(), RoasterScreen::new);
    }
}
