package growthcraft.milk.recipe;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MilkRecipeDataParityTest {
    private static final Path RECIPE_ROOT = Path.of("src/main/resources/data/growthcraft_milk/recipe");

    @Test
    void starterCultureRecipesUseTheSameQuarterBucketBatch() throws IOException {
        String milk = recipe("culture_jar_starter_culture_from_milk.json");
        String skimMilk = recipe("culture_jar_starter_culture_from_skim_milk.json");

        assertEquals(250, numberField(milk, "amount"));
        assertEquals(250, numberField(skimMilk, "amount"));
        assertEquals(1200, numberField(milk, "time"));
        assertEquals(1200, numberField(skimMilk, "time"));
    }

    @Test
    void cheesePressStacksUseDelayedItemStackTemplateFields() throws IOException {
        List<Path> recipes;
        try (var paths = Files.list(RECIPE_ROOT)) {
            recipes = paths
                    .filter(path -> path.getFileName().toString().startsWith("cheese_press_"))
                    .filter(path -> path.getFileName().toString().endsWith("_cheese.json"))
                    .toList();
        }

        assertEquals(10, recipes.size());
        for (Path path : recipes) {
            String json = Files.readString(path);
            assertTrue(json.contains("\"id\""), () -> path + " must use ItemStackTemplate's id field");
            assertFalse(json.contains("\"item\""), () -> path + " must not eagerly decode an ItemStack");
        }
    }

    private static String recipe(String name) throws IOException {
        return Files.readString(RECIPE_ROOT.resolve(name));
    }

    private static int numberField(String json, String field) {
        String marker = "\"" + field + "\"";
        int fieldIndex = json.indexOf(marker);
        int colonIndex = json.indexOf(':', fieldIndex + marker.length());
        int valueStart = colonIndex + 1;
        while (Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }
        int valueEnd = valueStart;
        while (Character.isDigit(json.charAt(valueEnd))) {
            valueEnd++;
        }
        return Integer.parseInt(json.substring(valueStart, valueEnd));
    }
}
