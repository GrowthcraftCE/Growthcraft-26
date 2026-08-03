package growthcraft.core.init;

import growthcraft.core.Growthcraft;
import growthcraft.core.recipe.NoMirrorShapedRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Growthcraft.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<NoMirrorShapedRecipe>> SHAPED_NO_MIRROR =
            SERIALIZERS.register("shaped_no_mirror", () -> new RecipeSerializer<>(
                    NoMirrorShapedRecipe.CODEC, NoMirrorShapedRecipe.STREAM_CODEC));

    private GrowthcraftRecipes() {
    }
}
