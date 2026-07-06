package growthcraft.rice.client;

import growthcraft.lib.client.GrowthcraftFluidModels;
import growthcraft.rice.config.Reference;
import growthcraft.rice.init.GrowthcraftRiceFluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;

@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public final class GrowthcraftRiceClient {
    private GrowthcraftRiceClient() {
    }

    @SubscribeEvent
    public static void onRegisterFluidModels(RegisterFluidModelsEvent event) {
        GrowthcraftFluidModels.registerContainers(event, Reference.MODID, GrowthcraftRiceFluids.ALL);
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        GrowthcraftFluidModels.registerClientExtensions(event, GrowthcraftRiceFluids.ALL);
    }
}
