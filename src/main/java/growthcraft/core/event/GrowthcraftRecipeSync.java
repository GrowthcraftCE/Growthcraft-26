package growthcraft.core.event;

import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.core.Growthcraft;
import growthcraft.milk.init.GrowthcraftMilkRecipes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

@EventBusSubscriber(modid = Growthcraft.MODID)
public final class GrowthcraftRecipeSync {
    private GrowthcraftRecipeSync() {
    }

    @SubscribeEvent
    static void sendMachineRecipes(OnDatapackSyncEvent event) {
        event.sendRecipes(
                GrowthcraftCellarRecipes.BREW_KETTLE_TYPE.get(),
                GrowthcraftCellarRecipes.CULTURE_JAR_TYPE.get(),
                GrowthcraftCellarRecipes.FERMENTATION_BARREL_TYPE.get(),
                GrowthcraftCellarRecipes.FRUIT_PRESS_TYPE.get(),
                GrowthcraftCellarRecipes.ROASTER_TYPE.get(),
                GrowthcraftMilkRecipes.CHEESE_PRESS_TYPE.get(),
                GrowthcraftMilkRecipes.CHURN_TYPE.get(),
                GrowthcraftMilkRecipes.MIXING_VAT_TYPE.get(),
                GrowthcraftMilkRecipes.PANCHEON_TYPE.get());
    }
}
