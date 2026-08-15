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
        assertTrue(category.contains("drawArrow(graphics"));
        assertTrue(category.contains("drawSlotFrame(graphics"));
        assertTrue(category.contains("x + width + 1"));
    }

    @Test
    void machineCategoriesRenderTheirMinecraft26FramesAndBackgrounds() throws IOException {
        String cheesePress = source("compat/jei/CheesePressRecipeCategory.java");
        assertTrue(cheesePress.contains("drawableBuilder(TEXTURE, 10, 10, WIDTH, HEIGHT)"));
        assertTrue(cheesePress.contains("background.draw(graphics, 0, 0);"));

        for (String categoryName : new String[]{"PancheonRecipeCategory.java", "MixingVatRecipeCategory.java",
                "ChurnRecipeCategory.java"}) {
            String category = source("compat/jei/" + categoryName);
            assertTrue(category.contains("graphics.fill("),
                    () -> categoryName + " does not render its frames with GuiGraphicsExtractor");
            assertTrue(category.contains("graphics.text("),
                    () -> categoryName + " does not render its stable helper label");
            assertTrue(category.contains("0xFF404040"),
                    () -> categoryName + " uses a transparent pre-Minecraft-26 text color");
        }
        assertTrue(source("compat/jei/PancheonRecipeCategory.java").contains("x + 1, centerY + 1"));
        assertTrue(cheesePress.contains("graphics.text(font, time"));
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

    @Test
    void milkFluidTanksExposeClickableFluidIngredients() throws IOException {
        String plugin = source("compat/jei/GrowthcraftMilkJeiPlugin.java");

        for (String screen : new String[]{"ChurnScreen", "MixingVatScreen", "PancheonScreen"}) {
            assertTrue(plugin.contains("addGuiContainerHandler(" + screen + ".class"));
        }
    }

    @Test
    void milkFluidRecipesExposeBucketsAsHiddenNavigationIngredients() throws IOException {
        for (String fileName : new String[]{"ChurnRecipeCategory.java", "MixingVatRecipeCategory.java",
                "PancheonRecipeCategory.java"}) {
            String category = source("compat/jei/" + fileName);
            assertTrue(category.contains("getBucket().getDefaultInstance()"),
                    () -> fileName + " does not derive a bucket from its recipe fluid");
            assertTrue(category.contains("addInvisibleIngredients("),
                    () -> fileName + " exposes the bucket in its visible JEI layout");
            assertTrue(category.contains("!bucket.isEmpty()"),
                    () -> fileName + " does not safely skip bucketless fluids");
        }
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/milk", relativePath));
    }
}
