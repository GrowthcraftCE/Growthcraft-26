package growthcraft.lib.client.recipe;

import growthcraft.core.Growthcraft;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.List;

@EventBusSubscriber(modid = Growthcraft.MODID, value = Dist.CLIENT)
public final class ClientRecipeLookup {
    private static RecipeMap recipes = RecipeMap.EMPTY;

    private ClientRecipeLookup() {
    }

    public static <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> getAll(RecipeType<T> type) {
        return List.copyOf(recipes.byType(type));
    }

    @SubscribeEvent
    static void receiveRecipes(RecipesReceivedEvent event) {
        recipes = event.getRecipeMap();
    }

    @SubscribeEvent
    static void clearRecipes(ClientPlayerNetworkEvent.LoggingOut event) {
        recipes = RecipeMap.EMPTY;
    }
}
