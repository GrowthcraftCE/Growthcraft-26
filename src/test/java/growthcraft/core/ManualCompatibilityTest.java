package growthcraft.core;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManualCompatibilityTest {
    private static final Path MANUAL = Path.of(
        "src/main/resources/assets/growthcraft/patchouli_books/growthcraft/en_us"
    );
    private static final Pattern RECIPE_REFERENCE = Pattern.compile(
        "\\\"recipe2?\\\"\\s*:\\s*\\\"([^\\\"]+)\\\""
    );

    @Test
    void patchouliIsAnOptionalDevelopmentDependency() throws IOException {
        String build = Files.readString(Path.of("build.gradle"));
        String properties = Files.readString(Path.of("gradle.properties"));

        assertTrue(build.contains("runtimeOnly \"curse.maven:patchouli-306770:${patchouli_version}\""));
        assertFalse(build.contains("implementation \"maven.modrinth:patchouli"));
        assertTrue(properties.contains("patchouli_version=8404543"));
        assertTrue(build.contains("growthcraftCompatibilityRuntime"));
        assertTrue(build.contains("maven.modrinth:mystical-agriculture:${mystical_agriculture_version}"));
        assertTrue(build.contains("curse.maven:packing-tape-238659:${packing_tape_version}"));
    }

    @Test
    void apiaryManualDoesNotReferenceRemovedBeeContent() throws IOException {
        String category = Files.readString(MANUAL.resolve("categories/apiary.json"));
        String entry = Files.readString(MANUAL.resolve("entries/apiary/bee_keeping.json"));
        String mysticalAgriculture = Files.readString(
            MANUAL.resolve("entries/mystical_agriculture/mystical_agriculture.json")
        );

        assertFalse(category.contains("growthcraft_apiary:bee\""));
        assertFalse(entry.contains("growthcraft_apiary:bee\""));
        assertFalse(mysticalAgriculture.contains("mystical_agriculture_bee"));
        assertTrue(category.contains("growthcraft_apiary:honey_comb_full"));
        assertTrue(entry.contains("minecraft:bee_nest"));
        assertTrue(entry.contains("patchouli:image"));
    }

    @Test
    void manualRecipeReferencesResolveAgainstAuthoredOrGeneratedData() throws IOException {
        Set<String> availableRecipes = new HashSet<>();
        collectRecipeIds(Path.of("src/main/resources/data"), availableRecipes);
        collectRecipeIds(Path.of("src/generated/resources/data"), availableRecipes);

        Set<String> missingRecipes = new HashSet<>();
        try (Stream<Path> files = Files.walk(MANUAL.resolve("entries"))) {
            for (Path file : files.filter(path -> path.toString().endsWith(".json")).toList()) {
                Matcher matcher = RECIPE_REFERENCE.matcher(Files.readString(file));
                while (matcher.find()) {
                    String recipe = matcher.group(1);
                    if (!recipe.startsWith("minecraft:") && !availableRecipes.contains(recipe)) {
                        missingRecipes.add(recipe + " referenced by " + MANUAL.relativize(file));
                    }
                }
            }
        }

        assertTrue(missingRecipes.isEmpty(), "Missing manual recipes: " + missingRecipes);
    }

    @Test
    void manualJsonDoesNotRepeatRecipeKeysWithinAPage() throws IOException {
        Pattern pagePattern = Pattern.compile("\\{[^{}]*\\}", Pattern.DOTALL);
        Pattern recipeKey = Pattern.compile("\\\"recipe2?\\\"\\s*:");
        Set<String> duplicateKeys = new HashSet<>();

        try (Stream<Path> files = Files.walk(MANUAL.resolve("entries"))) {
            for (Path file : files.filter(path -> path.toString().endsWith(".json")).toList()) {
                Matcher pageMatcher = pagePattern.matcher(Files.readString(file));
                while (pageMatcher.find()) {
                    Set<String> keys = new HashSet<>();
                    Matcher keyMatcher = recipeKey.matcher(pageMatcher.group());
                    while (keyMatcher.find()) {
                        String key = keyMatcher.group();
                        if (!keys.add(key)) {
                            duplicateKeys.add(MANUAL.relativize(file) + ": " + key);
                        }
                    }
                }
            }
        }

        assertTrue(duplicateKeys.isEmpty(), "Duplicate recipe keys: " + duplicateKeys);
    }

    @Test
    void everySmallBarrelVariantHasAnExplicitBookHint() throws IOException {
        String registrations = Files.readString(Path.of("src/main/java/growthcraft/cellar/init/GrowthcraftCellarBlocks.java"));
        String entry = Files.readString(MANUAL.resolve("entries/cellar/ferment_barrel.json"));
        Matcher variants = Pattern.compile("DeferredBlock<Block> FERMENTATION_BARREL_([A-Z_]+) =").matcher(registrations);
        Set<String> missing = new HashSet<>();
        int count = 0;
        while (variants.find()) {
            count++;
            String id = "growthcraft_cellar:fermentation_barrel_" + variants.group(1).toLowerCase(java.util.Locale.ROOT);
            if (!Pattern.compile(Pattern.quote("\"" + id + "\"") + "\\s*:\\s*0").matcher(entry).find()) missing.add(id);
        }
        assertTrue(count >= 13, "Expected all registered small barrel variants");
        assertTrue(missing.isEmpty(), "Missing page-zero book hints: " + missing);
    }

    @Test
    void largeBarrelEntriesLinkEveryVariantToItsCraftingPage() throws IOException {
        String registrations = Files.readString(Path.of("src/main/java/growthcraft/cellar/init/GrowthcraftCellarBlocks.java"));
        String standardEntry = Files.readString(MANUAL.resolve("entries/cellar/ferment_barrel.json"));
        for (String kind : java.util.List.of("fermentation", "storage")) {
            String entryName = kind.equals("fermentation") ? "large_ferment_barrel" : "large_storage_barrel";
            var entry = JsonParser.parseString(Files.readString(MANUAL.resolve("entries/cellar/" + entryName + ".json"))).getAsJsonObject();
            var pages = entry.getAsJsonArray("pages");
            var crafting = pages.get(1).getAsJsonObject();
            String prefix = "large_" + kind + "_barrel_";
            assertEquals("patchouli:crafting", crafting.get("type").getAsString());
            assertEquals("growthcraft_cellar:" + prefix + "oak", crafting.get("recipe").getAsString());
            assertTrue(pages.get(0).getAsJsonObject().get("text").getAsString()
                    .contains(kind.equals("fermentation") ? "32 buckets" : "128 buckets"));
            assertTrue(standardEntry.contains("$(l:cellar/" + entryName + ")"), "Standard barrel must link to " + entryName);
            var mappings = entry.getAsJsonObject("extra_recipe_mappings");
            Matcher variants = Pattern.compile("DeferredBlock<Block> " + prefix.toUpperCase(java.util.Locale.ROOT) + "([A-Z_]+) =")
                    .matcher(registrations);
            int count = 0;
            while (variants.find()) {
                count++;
                String id = "growthcraft_cellar:" + prefix + variants.group(1).toLowerCase(java.util.Locale.ROOT);
                assertTrue(mappings.has(id), "Missing large barrel book hint: " + id);
                assertEquals(1, mappings.get(id).getAsInt(), "Book hint must open the crafting page: " + id);
            }
            assertTrue(count >= 13, "Expected all large barrel variants");
            assertEquals(count, mappings.size());
        }
    }

    private static void collectRecipeIds(Path dataRoot, Set<String> recipes) throws IOException {
        if (!Files.isDirectory(dataRoot)) {
            return;
        }
        try (Stream<Path> files = Files.walk(dataRoot)) {
            files.filter(path -> path.toString().endsWith(".json"))
                .forEach(path -> {
                    Path relative = dataRoot.relativize(path);
                    if (relative.getNameCount() >= 3 && relative.getName(1).toString().equals("recipe")) {
                        String recipePath = relative.subpath(2, relative.getNameCount()).toString()
                            .replace('\\', '/')
                            .replaceAll("\\.json$", "");
                        recipes.add(relative.getName(0) + ":" + recipePath);
                    }
                });
        }
    }
}
