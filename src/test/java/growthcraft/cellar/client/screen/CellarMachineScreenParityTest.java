package growthcraft.cellar.client.screen;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CellarMachineScreenParityTest {
    @Test
    void cultureJarShowsHeatProgressAndProgressTooltip() throws IOException {
        String source = source("CultureJarScreen.java");

        assertTrue(source.contains("this.menu.isHeated()"));
        assertTrue(source.contains("BUBBLE_PIXELS"));
        assertTrue(source.contains("this.menu.getPercentProgress()"));
    }

    @Test
    void fermentationBarrelShowsProgressAndYeastFeedback() throws IOException {
        String source = source("FermentationBarrelScreen.java");

        assertTrue(source.contains("this.menu.getProgressionScaled(PROGRESS_H)"));
        assertTrue(source.contains("YEAST_WARNING"));
        assertTrue(source.contains("YEAST_ERROR"));
    }

    @Test
    void fruitPressAndRoasterShowProcessingState() throws IOException {
        String fruitPress = source("FruitPressScreen.java");
        String roaster = source("RoasterScreen.java");

        assertTrue(fruitPress.contains("this.menu.getProgressionScaled(PROGRESS_H)"));
        assertTrue(roaster.contains("this.menu.getProgressionScaled(PROGRESS_WIDTH)"));
        assertTrue(roaster.contains("this.menu.isHeated()"));
        assertTrue(roaster.contains("this.menu.getRoastingLevel(), this.menu.getPercentProgress()"));
    }

    private static String source(String name) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/cellar/client/screen", name));
    }
}
