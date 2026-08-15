package growthcraft.cellar.client;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CellarJeiParityTest {
    @Test
    void cellarMachineCategoriesDrawTheirMachineBackgrounds() throws IOException {
        assertCategoryDrawsBackground("CultureJarRecipeCategory.java", "30, 10, WIDTH, HEIGHT");
        assertCategoryDrawsBackground("FermentationBarrelRecipeCategory.java",
                "BACKGROUND_U, BACKGROUND_V, WIDTH, HEIGHT");
        assertCategoryDrawsBackground("FruitPressRecipeCategory.java",
                "BACKGROUND_U, BACKGROUND_V, WIDTH, HEIGHT");
        assertCategoryDrawsBackground("BrewKettleRecipeCategory.java", "10, 10, WIDTH, HEIGHT");
        assertCategoryDrawsBackground("RoasterRecipeCategory.java", "30, 20, WIDTH, HEIGHT");
    }

    @Test
    void cellarMachineCategoriesRenderTheirStableHelperText() throws IOException {
        String kettle = categorySource("BrewKettleRecipeCategory.java");
        assertTrue(kettle.contains("recipe.getByProductChance() + \"%\""));
        assertTrue(kettle.contains("formatTicks(recipe.getProcessingTime())"));
        assertTrue(kettle.contains("graphics.text(font, lidInfo"));
        assertTrue(kettle.contains("0xFF404040"));

        for (String fileName : new String[]{"CultureJarRecipeCategory.java", "FermentationBarrelRecipeCategory.java",
                "FruitPressRecipeCategory.java", "RoasterRecipeCategory.java"}) {
            assertTrue(categorySource(fileName).contains("graphics.text("),
                    () -> fileName + " does not render its stable helper label");
        }
    }

    @Test
    void cellarFluidTanksExposeClickableFluidIngredients() throws IOException {
        String plugin = Files.readString(Path.of(
                "src/main/java/growthcraft/cellar/compat/jei/GrowthcraftCellarJeiPlugin.java"));
        String handler = Files.readString(Path.of(
                "src/main/java/growthcraft/lib/compat/jei/FluidIngredientGuiHandler.java"));

        for (String screen : new String[]{"BrewKettleScreen", "CultureJarScreen",
                "FermentationBarrelScreen", "FruitPressScreen"}) {
            assertTrue(plugin.contains("addGuiContainerHandler(" + screen + ".class"));
        }
        assertTrue(handler.contains("getFluidIngredientAreas()"));
        assertTrue(handler.contains("createBuilder(fluidIngredientType, ingredientArea.fluid())"));
    }

    @Test
    void cellarFluidRecipesExposeBucketsAsHiddenNavigationIngredients() throws IOException {
        for (String fileName : new String[]{"BrewKettleRecipeCategory.java", "CultureJarRecipeCategory.java",
                "FermentationBarrelRecipeCategory.java", "FruitPressRecipeCategory.java"}) {
            String category = categorySource(fileName);
            assertTrue(category.contains("getBucket().getDefaultInstance()"),
                    () -> fileName + " does not derive a bucket from its recipe fluid");
            assertTrue(category.contains("addInvisibleIngredients("),
                    () -> fileName + " exposes the bucket in its visible JEI layout");
            assertTrue(category.contains("!bucket.isEmpty()"),
                    () -> fileName + " does not safely skip bucketless fluids");
        }
    }

    private static void assertCategoryDrawsBackground(String fileName, String textureRegion) throws IOException {
        String source = categorySource(fileName);

        assertTrue(source.contains("drawableBuilder(TEXTURE, " + textureRegion + ")"),
                () -> fileName + " does not create the stable 1.21.1.7 background region");
        assertTrue(source.contains("background.draw(graphics, 0, 0);"),
                () -> fileName + " does not draw its background in the Minecraft 26 JEI render pass");
    }

    private static String categorySource(String fileName) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/cellar/compat/jei", fileName));
    }
}
