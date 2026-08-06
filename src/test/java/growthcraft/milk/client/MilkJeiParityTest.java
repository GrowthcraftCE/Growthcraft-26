package growthcraft.milk.client;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MilkJeiParityTest {
    @Test
    void stableCheeseProcessesAreRegisteredWithJei() throws IOException {
        String plugin = source("compat/jei/GrowthcraftMilkJeiPlugin.java");

        for (String category : new String[]{"CURD_DRYING", "CHEESE_AGING", "CHEESE_WAXING", "CHEESE_CUTTING"}) {
            assertTrue(plugin.contains("RecipeType<CheeseProcessRecipeCategory.Recipe> " + category));
            assertTrue(plugin.contains("registration.addRecipes(" + category));
        }
        assertTrue(plugin.contains("GrowthcraftMilkItems.getCheeseRegistry()"));
        assertTrue(plugin.contains("GrowthcraftTags.Items.CHEESE_CUTTING_TOOLS"));
    }

    @Test
    void processCategorySupportsOneAndTwoInputLayouts() throws IOException {
        String category = source("compat/jei/CheeseProcessRecipeCategory.java");

        assertTrue(category.contains("recipe.secondaryInput() != null"));
        assertTrue(category.contains("RecipeIngredientRole.INPUT"));
        assertTrue(category.contains("RecipeIngredientRole.OUTPUT"));
        assertTrue(category.contains("oneToOne"));
        assertTrue(category.contains("twoToOne"));
    }

    @Test
    void machineRecipesUseTheNeoForgeSynchronizedClientRecipeMap() throws IOException {
        String plugin = source("compat/jei/GrowthcraftMilkJeiPlugin.java");
        String sync = Files.readString(Path.of(
                "src/main/java/growthcraft/core/event/GrowthcraftRecipeSync.java"));
        String lookup = Files.readString(Path.of(
                "src/main/java/growthcraft/lib/client/recipe/ClientRecipeLookup.java"));

        assertTrue(sync.contains("OnDatapackSyncEvent"));
        assertTrue(sync.contains("GrowthcraftMilkRecipes.MIXING_VAT_TYPE.get()"));
        assertTrue(plugin.contains("ClientRecipeLookup.getAll(GrowthcraftMilkRecipes.MIXING_VAT_TYPE.get())"));
        assertTrue(lookup.contains("RecipesReceivedEvent"));
        assertTrue(lookup.contains("recipes.byType(type)"));
        assertTrue(lookup.contains("ClientPlayerNetworkEvent.LoggingOut"));
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/milk", relativePath));
    }
}
