package growthcraft.apiary;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiaryContentParityTest {
    @Test
    void legacyBeeAndCandleContentIsNotRegistered() throws IOException {
        String blocks = source("init/GrowthcraftApiaryBlocks.java");
        String items = source("init/GrowthcraftApiaryItems.java");

        assertFalse(blocks.contains("CANDLE_"));
        assertFalse(items.contains("CANDLE_"));
        assertFalse(items.contains("DeferredItem<Item> BEE ="));
    }

    @Test
    void supportedWaxHoneycombAndFluidContentRemainsRegistered() throws IOException {
        String items = source("init/GrowthcraftApiaryItems.java");
        String fluids = source("init/GrowthcraftApiaryFluids.java");

        assertTrue(items.contains("BEES_WAX"));
        assertTrue(items.contains("HONEY_COMB_EMPTY"));
        assertTrue(items.contains("HONEY_COMB_FULL"));
        assertTrue(fluids.contains("HONEY_MEAD_MUST"));
        assertTrue(fluids.contains("WAXES"));
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/apiary", relativePath));
    }
}
