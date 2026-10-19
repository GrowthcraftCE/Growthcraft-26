package growthcraft.cellar.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BarrelCraftingRecipeTest {
    private static final Path DATA = Path.of("src/generated/resources/data/growthcraft_cellar");

    @ParameterizedTest
    @ValueSource(strings = {"acacia", "apple", "bamboo", "birch", "cherry", "crimson", "dark_oak",
            "jungle", "mangrove", "oak", "pale_oak", "spruce", "warped"})
    void standardRecipeRetainsSixIngotsAndThreePlanks(String wood) throws IOException {
        JsonObject recipe = readRecipe("fermentation_barrel_" + wood);
        assertEquals(List.of("AAA", "BBB", "AAA"), pattern(recipe));
        assertEquals(2, recipe.getAsJsonObject("key").size());
        assertEquals("#c:ingots/iron", recipe.getAsJsonObject("key").get("A").getAsString());
        assertEquals(planks(wood), recipe.getAsJsonObject("key").get("B").getAsString());
        assertSingleResult(recipe, "fermentation_barrel_" + wood);
    }

    @ParameterizedTest
    @ValueSource(strings = {"acacia", "apple", "bamboo", "birch", "cherry", "crimson", "dark_oak",
            "jungle", "mangrove", "oak", "pale_oak", "spruce", "warped"})
    void largeFermentationRecipeUsesTwoStandardBarrelsThreePlanksAndFourNuggets(String wood) throws IOException {
        assertUpgradeRecipe("large_fermentation_barrel_" + wood, wood, "P", planks(wood), List.of("NBN", "PPP", "NBN"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"acacia", "apple", "bamboo", "birch", "cherry", "crimson", "dark_oak",
            "jungle", "mangrove", "oak", "pale_oak", "spruce", "warped"})
    void storageRecipeUsesThreeStandardBarrelsTwoLogsAndFourNuggets(String wood) throws IOException {
        String material = switch (wood) {
            case "apple" -> "growthcraft_apples:apple_wood_log";
            case "bamboo" -> "minecraft:bamboo_block";
            case "crimson", "warped" -> "minecraft:" + wood + "_stem";
            default -> "minecraft:" + wood + "_log";
        };
        assertUpgradeRecipe("large_storage_barrel_" + wood, wood, "L", material, List.of("NLN", "BBB", "NLN"));
    }

    private static void assertUpgradeRecipe(String id, String variant, String woodKey, String wood, List<String> expectedPattern) throws IOException {
        JsonObject recipe = readRecipe(id);
        assertEquals(expectedPattern, pattern(recipe));
        JsonObject key = recipe.getAsJsonObject("key");
        assertEquals(3, key.size());
        assertEquals(wood, key.get(woodKey).getAsString());
        assertEquals("#c:nuggets/iron", key.get("N").getAsString());
        String barrel = "growthcraft_cellar:fermentation_barrel_" + variant;
        assertEquals(barrel, key.get("B").getAsString());
        assertSingleResult(recipe, id);
        String advancement = Files.readString(DATA.resolve("advancement/recipes/misc/" + id + ".json"));
        var criteria = JsonParser.parseString(advancement).getAsJsonObject().getAsJsonObject("criteria");
        assertEquals(2, criteria.size());
        var unlock = criteria.getAsJsonObject("has_fermentation_barrel_" + variant);
        assertEquals("minecraft:inventory_changed", unlock.get("trigger").getAsString());
        assertEquals(barrel, unlock.getAsJsonObject("conditions").getAsJsonArray("items")
                .get(0).getAsJsonObject().get("items").getAsString());
    }

    private static String planks(String wood) {
        return wood.equals("apple") ? "growthcraft_apples:apple_plank" : "minecraft:" + wood + "_planks";
    }

    private static List<String> pattern(JsonObject recipe) {
        return recipe.getAsJsonArray("pattern").asList().stream().map(value -> value.getAsString()).toList();
    }

    private static void assertSingleResult(JsonObject recipe, String id) {
        assertEquals("minecraft:crafting_shaped", recipe.get("type").getAsString());
        JsonObject result = recipe.getAsJsonObject("result");
        assertEquals("growthcraft_cellar:" + id, result.get("id").getAsString());
        assertEquals(1, result.has("count") ? result.get("count").getAsInt() : 1);
    }

    private static JsonObject readRecipe(String id) throws IOException {
        return JsonParser.parseString(Files.readString(DATA.resolve("recipe/" + id + ".json"))).getAsJsonObject();
    }
}
