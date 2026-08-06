package growthcraft.rice;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RiceGameplayParityTest {
    @Test
    void seishuYeastUsesOnlyTheStableQuarterBucketCultureRecipe() throws IOException {
        Path recipes = Path.of("src/main/resources/data/growthcraft_rice/recipe");
        String recipe = Files.readString(recipes.resolve("culture_jar_yeast_seishu_from_rice_water.json"));

        assertTrue(recipe.contains("\"amount\": 250"));
        assertTrue(recipe.contains("\"time\": 1200"));
        assertFalse(Files.exists(recipes.resolve("culture_jar_starter_yeast_seishu_from_rice_water.json")));
    }

    @Test
    void cultivatedFarmlandIsMaintainedByTaggedCrops() throws IOException {
        String farmland = source("block/CultivatedFarmlandBlock.java");

        assertTrue(farmland.contains("BlockTags.MAINTAINS_FARMLAND"));
        assertTrue(farmland.contains("plant.getBlock() instanceof RiceCropBlock"));
    }

    @Test
    void matureRiceHarvestResetsTheCropWithoutRemovingItsFarmland() throws IOException {
        String crop = source("block/RiceCropBlock.java");

        assertTrue(crop.contains("this.isMaxAge(state)"));
        assertTrue(crop.contains("level.setBlock(pos, this.getStateForAge(1)"));
    }

    @Test
    void sakeRecipeColorMatchesTheRegisteredFluidColor() throws IOException {
        String recipe = Files.readString(Path.of(
                "src/main/resources/data/growthcraft_rice/recipe/fermentation_barrel_sake.json"));
        String reference = source("config/Reference.java");

        assertTrue(recipe.contains("\"color\": \"0xEAECEC\""));
        assertTrue(reference.contains("SAKE = new ColorUtils.GrowthcraftColor(0xFFEAECEC)"));
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/rice", relativePath));
    }
}
