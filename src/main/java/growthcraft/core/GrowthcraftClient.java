package growthcraft.core;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = Growthcraft.MODID, dist = Dist.CLIENT)
public class GrowthcraftClient {
    public GrowthcraftClient(ModContainer container) {
        Growthcraft.LOGGER.info("Growthcraft 26.x client bootstrap loaded.");
    }
}
