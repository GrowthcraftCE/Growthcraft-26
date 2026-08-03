package growthcraft.milk.client;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheeseAssetParityTest {
    @Test
    void cheesePressAndChurnScreensAreRegistered() throws IOException {
        String client = Files.readString(Path.of("src/main/java/growthcraft/milk/client/GrowthcraftMilkClient.java"));

        assertTrue(client.contains("GrowthcraftMilkMenus.CHEESE_PRESS.get(), MachineScreen::new"));
        assertTrue(client.contains("GrowthcraftMilkMenus.CHURN.get(), MachineScreen::new"));
    }

    private static final Path ASSETS = Path.of("src/main/resources/assets/growthcraft_milk");
    private static final Pattern MODEL_REFERENCE = Pattern.compile("\\\"model\\\"\\s*:\\s*\\\"growthcraft_milk:block/([^\\\"]+)\\\"");

    @Test
    void everyCheeseBlockstateModelReferenceExists() throws IOException {
        try (var blockstates = Files.list(ASSETS.resolve("blockstates"))) {
            for (Path blockstate : blockstates.filter(path -> path.getFileName().toString().contains("cheese")).toList()) {
                Matcher matcher = MODEL_REFERENCE.matcher(Files.readString(blockstate));
                while (matcher.find()) {
                    Path model = ASSETS.resolve("models/block").resolve(matcher.group(1) + ".json");
                    assertTrue(Files.isRegularFile(model), () -> "Missing model referenced by " + blockstate + ": " + model);
                }
            }
        }
    }

    @Test
    void authoredWheelModelsReplaceRuntimeWheelTinting() throws IOException {
        Path authored = ASSETS.resolve("models/block/cheese_wheel/textured/asiago/unaged/slices_bottom_1.json");
        String client = Files.readString(Path.of("src/main/java/growthcraft/milk/client/GrowthcraftMilkClient.java"));

        assertTrue(Files.isRegularFile(authored));
        assertFalse(client.contains("GrowthcraftMilkBlocks.ASIAGO_CHEESE.get()"));
        assertTrue(client.contains("GrowthcraftMilkBlocks.ASIAGO_CHEESE_CURDS.get()"));
    }
}
