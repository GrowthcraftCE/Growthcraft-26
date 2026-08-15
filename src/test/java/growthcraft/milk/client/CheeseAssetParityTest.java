package growthcraft.milk.client;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheeseAssetParityTest {
    @Test
    void cheesePressAndChurnScreensAreRegistered() throws IOException {
        String client = Files.readString(Path.of("src/main/java/growthcraft/milk/client/GrowthcraftMilkClient.java"));

        assertTrue(client.contains("GrowthcraftMilkMenus.CHEESE_PRESS.get(), CheesePressScreen::new"));
        assertTrue(client.contains("GrowthcraftMilkMenus.CHURN.get(), ChurnScreen::new"));
    }

    @Test
    void cheesePressTextureContainsItsVerticalProgressSprite() throws IOException {
        BufferedImage texture = ImageIO.read(ASSETS.resolve("textures/gui/cheese_press_screen.png").toFile());

        assertEquals(256, texture.getWidth());
        assertEquals(256, texture.getHeight());
        for (int y = 0; y < 28; y++) {
            for (int x = 176; x < 185; x++) {
                assertTrue((texture.getRGB(x, y) >>> 24) != 0,
                        "Transparent progress pixel at " + x + "," + y);
            }
        }
    }

    @Test
    void cheesePressBackgroundUsesTheFruitPressDownArrow() throws IOException {
        BufferedImage cheesePress = ImageIO.read(ASSETS.resolve("textures/gui/cheese_press_screen.png").toFile());
        BufferedImage fruitPress = ImageIO.read(Path.of(
                "src/main/resources/assets/growthcraft_cellar/textures/gui/fruit_press_screen.png").toFile());

        for (int y = 22; y < 44; y++) {
            for (int x = 59; x < 62; x++) {
                assertEquals(fruitPress.getRGB(x, y), cheesePress.getRGB(x + 27, y + 7));
            }
        }
        for (int x = 57; x < 62; x++) assertEquals(fruitPress.getRGB(x, 44), cheesePress.getRGB(x + 27, 51));
        for (int x = 58; x < 62; x++) assertEquals(fruitPress.getRGB(x, 45), cheesePress.getRGB(x + 27, 52));
        for (int x = 59; x < 62; x++) assertEquals(fruitPress.getRGB(x, 46), cheesePress.getRGB(x + 27, 53));
        assertEquals(fruitPress.getRGB(60, 47), cheesePress.getRGB(87, 54));
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

    @Test
    void everyFreshCurdBlockItemHasAnItemTranslation() throws IOException {
        String language = Files.readString(ASSETS.resolve("lang/en_us.json"));
        for (String cheese : new String[]{"appenzeller", "asiago", "casu_marzu", "cheddar", "emmentaler",
                "gorgonzola", "gouda", "monterey", "parmesan", "provolone", "ricotta"}) {
            assertTrue(language.contains("\"item.growthcraft_milk." + cheese + "_cheese_curds\""),
                    () -> "Missing fresh curd item translation for " + cheese);
        }
    }

    @Test
    void everyCheeseWheelBlockItemHasAnItemTranslation() throws IOException {
        String language = Files.readString(ASSETS.resolve("lang/en_us.json"));
        for (String cheese : new String[]{"appenzeller", "asiago", "casu_marzu", "cheddar", "emmentaler",
                "gorgonzola", "gouda", "monterey", "parmesan", "provolone"}) {
            assertTrue(language.contains("\"item.growthcraft_milk." + cheese + "_cheese\""),
                    () -> "Missing fresh cheese item translation for " + cheese);
            assertTrue(language.contains("\"item.growthcraft_milk." + cheese + "_cheese_aged\""),
                    () -> "Missing aged cheese item translation for " + cheese);
        }
        for (String cheese : new String[]{"cheddar", "gouda", "monterey", "provolone"}) {
            assertTrue(language.contains("\"item.growthcraft_milk." + cheese + "_cheese_waxed\""),
                    () -> "Missing waxed cheese item translation for " + cheese);
        }
    }
}
