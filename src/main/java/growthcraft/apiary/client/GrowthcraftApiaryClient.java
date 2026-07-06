package growthcraft.apiary.client;

import growthcraft.apiary.GrowthcraftApiary;
import growthcraft.apiary.config.Reference;
import growthcraft.apiary.init.GrowthcraftApiaryBlocks;
import growthcraft.apiary.init.GrowthcraftApiaryFluids;
import growthcraft.lib.client.GrowthcraftFluidModels;
import growthcraft.lib.fluid.FluidRegistryContainer;
import growthcraft.lib.utils.ColorUtils;
import net.minecraft.client.color.block.BlockTintSources;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;

import java.util.List;

@EventBusSubscriber(modid = GrowthcraftApiary.MODID, value = Dist.CLIENT)
public final class GrowthcraftApiaryClient {
    private GrowthcraftApiaryClient() {
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
        registerCandleColor(event, Reference.FluidColor.WAX_BLACK, GrowthcraftApiaryBlocks.CANDLE_BLACK, GrowthcraftApiaryBlocks.CANDLE_BLACK_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_BLUE, GrowthcraftApiaryBlocks.CANDLE_BLUE, GrowthcraftApiaryBlocks.CANDLE_BLUE_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_BROWN, GrowthcraftApiaryBlocks.CANDLE_BROWN, GrowthcraftApiaryBlocks.CANDLE_BROWN_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_CYAN, GrowthcraftApiaryBlocks.CANDLE_CYAN, GrowthcraftApiaryBlocks.CANDLE_CYAN_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_GRAY, GrowthcraftApiaryBlocks.CANDLE_GRAY, GrowthcraftApiaryBlocks.CANDLE_GRAY_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_GREEN, GrowthcraftApiaryBlocks.CANDLE_GREEN, GrowthcraftApiaryBlocks.CANDLE_GREEN_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_LIGHT_BLUE, GrowthcraftApiaryBlocks.CANDLE_LIGHT_BLUE, GrowthcraftApiaryBlocks.CANDLE_LIGHT_BLUE_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_LIGHT_GRAY, GrowthcraftApiaryBlocks.CANDLE_LIGHT_GRAY, GrowthcraftApiaryBlocks.CANDLE_LIGHT_GRAY_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_LIME, GrowthcraftApiaryBlocks.CANDLE_LIME, GrowthcraftApiaryBlocks.CANDLE_LIME_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_MAGENTA, GrowthcraftApiaryBlocks.CANDLE_MAGENTA, GrowthcraftApiaryBlocks.CANDLE_MAGENTA_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_ORANGE, GrowthcraftApiaryBlocks.CANDLE_ORANGE, GrowthcraftApiaryBlocks.CANDLE_ORANGE_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_PINK, GrowthcraftApiaryBlocks.CANDLE_PINK, GrowthcraftApiaryBlocks.CANDLE_PINK_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_PURPLE, GrowthcraftApiaryBlocks.CANDLE_PURPLE, GrowthcraftApiaryBlocks.CANDLE_PURPLE_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_RED, GrowthcraftApiaryBlocks.CANDLE_RED, GrowthcraftApiaryBlocks.CANDLE_RED_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_WHITE, GrowthcraftApiaryBlocks.CANDLE_WHITE, GrowthcraftApiaryBlocks.CANDLE_WHITE_WALL);
        registerCandleColor(event, Reference.FluidColor.WAX_YELLOW, GrowthcraftApiaryBlocks.CANDLE_YELLOW, GrowthcraftApiaryBlocks.CANDLE_YELLOW_WALL);
    }

    @SubscribeEvent
    public static void onRegisterFluidModels(RegisterFluidModelsEvent event) {
        GrowthcraftFluidModels.registerContainers(event, Reference.MODID, GrowthcraftApiaryFluids.ALL);
        for (GrowthcraftApiaryFluids.WaxFluid wax : GrowthcraftApiaryFluids.WAXES) {
            var extension = FluidRegistryContainer.createExtension(wax.client);
            GrowthcraftFluidModels.register(
                    event,
                    wax.client.still,
                    wax.client.flowing,
                    wax.client.overlay,
                    GrowthcraftFluidModels.tint(extension),
                    wax.source,
                    wax.flowing
            );
        }
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        GrowthcraftFluidModels.registerClientExtensions(event, GrowthcraftApiaryFluids.ALL);
        for (GrowthcraftApiaryFluids.WaxFluid wax : GrowthcraftApiaryFluids.WAXES) {
            event.registerFluidType(FluidRegistryContainer.createExtension(wax.client), wax.type.get());
        }
    }

    private static void registerCandleColor(RegisterColorHandlersEvent.BlockTintSources event, ColorUtils.GrowthcraftColor color,
                                            net.neoforged.neoforge.registries.DeferredBlock<net.minecraft.world.level.block.Block> standing,
                                            net.neoforged.neoforge.registries.DeferredBlock<net.minecraft.world.level.block.Block> wall) {
        int tint = color.toIntValue() & 0x00FFFFFF;
        event.register(List.of(BlockTintSources.constant(tint)), standing.get(), wall.get());
    }
}
