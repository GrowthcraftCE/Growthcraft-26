package growthcraft.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeResourceTest {
    private static final List<Path> DATA_ROOTS = List.of(
            Path.of("src/main/resources/data"),
            Path.of("src/generated/resources/data")
    );
    private static final Pattern TYPE_FIELD = Pattern.compile("\"type\"\\s*:\\s*\"[^\"]+\"");
    private static final Pattern RECIPE_PATH = Pattern.compile("^[^/\\\\]+[/\\\\]recipes?[/\\\\].+\\.json$");

    @Test
    void recipeJsonFilesDeclareType() throws IOException {
        List<Path> missingType = new java.util.ArrayList<>();
        for (Path root : DATA_ROOTS) {
            if (!Files.exists(root)) {
                continue;
            }

            try (Stream<Path> files = Files.walk(root)) {
                missingType.addAll(files
                        .filter(Files::isRegularFile)
                        .filter(path -> RECIPE_PATH.matcher(root.relativize(path).toString()).matches())
                        .filter(path -> !declaresType(path))
                        .toList());
            }
        }
        List<Path> sortedMissingType = missingType.stream().sorted().toList();

        assertTrue(
                sortedMissingType.isEmpty(),
                () -> "Recipe JSON files missing a type field:" + System.lineSeparator()
                        + String.join(System.lineSeparator(), sortedMissingType.stream().map(Path::toString).toList())
        );
    }

    @Test
    void mysticalAgricultureRecipesUseGrowthcraftNoMirrorSerializer() throws IOException {
        List<Path> recipes;
        try (Stream<Path> files = Files.walk(DATA_ROOTS.getFirst())) {
            recipes = files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().contains("mystical_agriculture"))
                    .filter(path -> path.toString().endsWith(".json"))
                    .toList();
        }

        assertEquals(25, recipes.size());
        for (Path recipe : recipes) {
            String json = Files.readString(recipe);
            assertTrue(json.contains("\"growthcraft:shaped_no_mirror\""), recipe::toString);
            assertFalse(json.contains("cucumber:shaped_no_mirror"), recipe::toString);
            assertTrue(json.contains("\"id\""), () -> recipe + " must use the 26.1 result schema");
        }
    }

    @Test
    void customRecipesDoNotEagerlyDecodeItemStacks() throws IOException {
        Path sourceRoot = Path.of("src/main/java/growthcraft");
        List<Path> offenders;
        try (Stream<Path> files = Files.walk(sourceRoot)) {
            offenders = files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith("Recipe.java"))
                    .filter(path -> {
                        try {
                            String source = Files.readString(path);
                            return source.contains("LEGACY_ITEM_STACK_CODEC")
                                    || source.contains("ItemStack.STREAM_CODEC")
                                    || source.contains("ItemStack.OPTIONAL_STREAM_CODEC");
                        } catch (IOException e) {
                            throw new IllegalStateException("Unable to read " + path, e);
                        }
                    })
                    .toList();
        }

        assertTrue(offenders.isEmpty(), () -> "Custom recipes must delay ItemStack creation: " + offenders);
    }

    private static boolean declaresType(Path path) {
        try {
            return TYPE_FIELD.matcher(Files.readString(path)).find();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read recipe JSON: " + path, e);
        }
    }
}
