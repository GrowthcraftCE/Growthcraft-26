package growthcraft.milk.client;

import growthcraft.lib.client.GrowthcraftFluidModels;
import growthcraft.milk.config.Reference;
import growthcraft.milk.client.renderer.MixingVatBlockEntityRenderer;
import growthcraft.milk.client.renderer.PancheonBlockEntityRenderer;
import growthcraft.milk.client.renderer.ShopSignRenderer;
import growthcraft.milk.client.screen.MixingVatScreen;
import growthcraft.milk.client.screen.PancheonScreen;
import growthcraft.milk.client.screen.CheesePressScreen;
import growthcraft.milk.client.screen.ChurnScreen;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import growthcraft.milk.init.GrowthcraftMilkMenus;
import growthcraft.lib.utils.ColorUtils;
import net.minecraft.client.color.block.BlockTintSources;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.List;

/**
 * Client-only registrations for Growthcraft Milk.
 */
@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public final class GrowthcraftMilkClient {
    private GrowthcraftMilkClient() {}

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(GrowthcraftMilkBlockEntities.MIXING_VAT.get(), MixingVatBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(GrowthcraftMilkBlockEntities.PANCHEON.get(), PancheonBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(GrowthcraftMilkBlockEntities.SHOP_SIGN.get(), ShopSignRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.ItemTintSources event) {
        if (event == null) {
            return;
        }
        // Growthcraft item tints are provided by 26.x item definition JSON.
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
        if (event == null) {
            return;
        }
        registerCheeseColor(event, Reference.BlockColor.APPENZELLER_CHEESE,
                GrowthcraftMilkBlocks.APPENZELLER_CHEESE_CURDS.get());
        registerCheeseColor(event, Reference.BlockColor.ASIAGO_CHEESE,
                GrowthcraftMilkBlocks.ASIAGO_CHEESE_CURDS.get());
        registerCheeseColor(event, Reference.BlockColor.CASU_MARZU_CHEESE,
                GrowthcraftMilkBlocks.CASU_MARZU_CHEESE_CURDS.get());
        registerCheeseColor(event, Reference.BlockColor.CHEDDAR_CHEESE,
                GrowthcraftMilkBlocks.CHEDDAR_CHEESE_CURDS.get());
        registerCheeseColor(event, Reference.BlockColor.EMMENTALER_CHEESE,
                GrowthcraftMilkBlocks.EMMENTALER_CHEESE_CURDS.get());
        registerCheeseColor(event, Reference.BlockColor.GORGONZOLA_CHEESE,
                GrowthcraftMilkBlocks.GORGONZOLA_CHEESE_CURDS.get());
        registerCheeseColor(event, Reference.BlockColor.GOUDA_CHEESE,
                GrowthcraftMilkBlocks.GOUDA_CHEESE_CURDS.get());
        registerCheeseColor(event, Reference.BlockColor.MONTEREY_CHEESE,
                GrowthcraftMilkBlocks.MONTEREY_CHEESE_CURDS.get());
        registerCheeseColor(event, Reference.BlockColor.PARMESAN_CHEESE,
                GrowthcraftMilkBlocks.PARMESAN_CHEESE_CURDS.get());
        registerCheeseColor(event, Reference.BlockColor.PROVOLONE_CHEESE,
                GrowthcraftMilkBlocks.PROVOLONE_CHEESE_CURDS.get());
        registerCheeseColor(event, Reference.BlockColor.RICOTTA_CHEESE,
                GrowthcraftMilkBlocks.RICOTTA_CHEESE_CURDS.get());
    }

    @SubscribeEvent
    public static void onRegisterFluidModels(RegisterFluidModelsEvent event) {
        GrowthcraftFluidModels.registerContainers(event, Reference.MODID, GrowthcraftMilkFluids.ALL);
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        GrowthcraftFluidModels.registerClientExtensions(event, GrowthcraftMilkFluids.ALL);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(GrowthcraftMilkMenus.CHEESE_PRESS.get(), CheesePressScreen::new);
        event.register(GrowthcraftMilkMenus.CHURN.get(), ChurnScreen::new);
        event.register(GrowthcraftMilkMenus.MIXING_VAT.get(), MixingVatScreen::new);
        event.register(GrowthcraftMilkMenus.PANCHEON.get(), PancheonScreen::new);
    }

    private static void registerCheeseColor(RegisterColorHandlersEvent.BlockTintSources event, ColorUtils.GrowthcraftColor color,
                                            net.minecraft.world.level.block.Block... blocks) {
        int tint = color.toIntValue() & 0x00FFFFFF;
        event.register(List.of(BlockTintSources.constant(tint)), blocks);
    }
}
