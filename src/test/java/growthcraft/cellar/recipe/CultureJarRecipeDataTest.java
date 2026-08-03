package growthcraft.cellar.recipe;

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

class CultureJarRecipeDataTest {
    private static final Path DATA_ROOT = Path.of("src/main/resources/data");
    private static final Pattern EAGER_RESULT = Pattern.compile(
            "\\\"result\\\"\\s*:\\s*\\{[^}]*\\\"item\\\"", Pattern.DOTALL);

    @Test
    void cultureJarResultsUseDelayedItemStackTemplateFields() throws IOException {
        List<Path> recipes;
        try (Stream<Path> paths = Files.walk(DATA_ROOT)) {
            recipes = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .filter(CultureJarRecipeDataTest::isCultureJarRecipe)
                    .toList();
        }

        assertEquals(11, recipes.size());
        for (Path recipe : recipes) {
            String json = Files.readString(recipe);
            assertTrue(json.contains("\"id\""), () -> recipe + " must use ItemStackTemplate's id field");
            assertFalse(EAGER_RESULT.matcher(json).find(),
                    () -> recipe + " must not eagerly decode its result ItemStack");
        }
    }

    private static boolean isCultureJarRecipe(Path path) {
        try {
            return Files.readString(path).contains("\"growthcraft_cellar:culture_jar\"");
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read recipe " + path, e);
        }
    }
}
