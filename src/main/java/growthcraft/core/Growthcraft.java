package growthcraft.core;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(Growthcraft.MODID)
public class Growthcraft {
    public static final String MODID = "growthcraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Growthcraft(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Growthcraft 26.x bootstrap loaded.");
    }
}
