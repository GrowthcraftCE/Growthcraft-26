package growthcraft.milk.block;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CheeseCuttingParityTest {
    @Test
    void cheeseWheelsUseTheDedicatedCuttingToolTag() throws IOException {
        String source = Files.readString(Path.of("src/main/java/growthcraft/milk/block/CheeseWheelBlock.java"));

        assertTrue(source.contains("GrowthcraftTags.Items.CHEESE_CUTTING_TOOLS"));
    }

    @Test
    void cuttingToolTagIncludesKnivesAndVanillaSwords() throws IOException {
        String source = Files.readString(Path.of("src/main/java/growthcraft/core/data/tags/GrowthcraftItemTags.java"));

        assertTrue(source.contains("addTag(GrowthcraftTags.Items.KNIVES)"));
        assertTrue(source.contains("Items.WOODEN_SWORD"));
        assertTrue(source.contains("Items.NETHERITE_SWORD"));
        assertTrue(source.contains("addOptionalTag(C_SWORDS)"));
    }
}
