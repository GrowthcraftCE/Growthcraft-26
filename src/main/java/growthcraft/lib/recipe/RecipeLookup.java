package growthcraft.lib.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public final class RecipeLookup {
    private RecipeLookup() {
    }

    @SuppressWarnings("unchecked")
    public static <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> getAll(Level level, RecipeType<T> type) {
        if (!(level.recipeAccess() instanceof RecipeManager manager)) {
            return List.of();
        }

        return manager.getRecipes().stream()
                .filter(holder -> holder.value().getType() == type)
                .map(holder -> (RecipeHolder<T>) holder)
                .toList();
    }
}
