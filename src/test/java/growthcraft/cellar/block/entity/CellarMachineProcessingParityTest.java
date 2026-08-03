package growthcraft.cellar.block.entity;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CellarMachineProcessingParityTest {
    @Test
    void cultureJarPreservesItsCultureCatalyst() throws IOException {
        String source = source("CultureJarBlockEntity.java");
        String completion = between(source, "// Complete: consume fluid", "jar.insertOutput");

        assertFalse(completion.contains("input.shrink"));
        assertTrue(completion.contains("jar.tank.drain"));
    }

    @Test
    void fermentationBatchSizeScalesProcessingTime() throws IOException {
        String source = source("FermentationBarrelBlockEntity.java");

        assertTrue(source.contains("recipe.getProcessingTime() * multiplier"));
    }

    @Test
    void fruitPressRetainsConfigurableByproductChance() throws IOException {
        String recipe = Files.readString(Path.of("src/main/java/growthcraft/cellar/recipe/FruitPressRecipe.java"));
        String machine = source("FruitPressBlockEntity.java");

        assertTrue(recipe.contains("optionalFieldOf(\"by_product_chance\", 100)"));
        assertTrue(machine.contains("nextInt(100) >= recipe.getByProductChance()"));
    }

    private static String source(String name) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/cellar/block/entity", name));
    }

    private static String between(String source, String start, String end) {
        int startIndex = source.indexOf(start);
        int endIndex = source.indexOf(end, startIndex);
        return source.substring(startIndex, endIndex);
    }
}
