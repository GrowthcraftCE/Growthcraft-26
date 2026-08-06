package growthcraft.apples.client;

import growthcraft.apples.GrowthcraftApples;
import growthcraft.apples.config.Reference;
import growthcraft.apples.init.GrowthcraftApplesFluids;
import growthcraft.lib.client.GrowthcraftFluidModels;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;

@EventBusSubscriber(modid = GrowthcraftApples.MODID, value = Dist.CLIENT)
public final class GrowthcraftApplesClient {
    private GrowthcraftApplesClient() {
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
        GrowthcraftFluidModels.registerContainers(event, Reference.MODID, GrowthcraftApplesFluids.ALL);
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        GrowthcraftFluidModels.registerClientExtensions(event, GrowthcraftApplesFluids.ALL);
    }
}
