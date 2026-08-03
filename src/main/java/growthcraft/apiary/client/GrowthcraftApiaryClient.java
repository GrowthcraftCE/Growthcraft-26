package growthcraft.apiary.client;

import growthcraft.apiary.GrowthcraftApiary;
import growthcraft.apiary.config.Reference;
import growthcraft.apiary.init.GrowthcraftApiaryFluids;
import growthcraft.lib.client.GrowthcraftFluidModels;
import growthcraft.lib.client.ClientFluidTypeExtensions;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;


@EventBusSubscriber(modid = GrowthcraftApiary.MODID, value = Dist.CLIENT)
public final class GrowthcraftApiaryClient {
    private GrowthcraftApiaryClient() {
    }

    @SubscribeEvent
    public static void onRegisterFluidModels(RegisterFluidModelsEvent event) {
        GrowthcraftFluidModels.registerContainers(event, Reference.MODID, GrowthcraftApiaryFluids.ALL);
        for (GrowthcraftApiaryFluids.WaxFluid wax : GrowthcraftApiaryFluids.WAXES) {
            var extension = new ClientFluidTypeExtensions(wax.client);
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
            event.registerFluidType(new ClientFluidTypeExtensions(wax.client), wax.type.get());
        }
    }

}
