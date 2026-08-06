package growthcraft.cellar.block;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GrapeVineGrowthParityTest {
    @Test
    void bonemealTriggersMatureStemAndLeavesGrowthImmediately() throws IOException {
        String stem = source("GrapeVineStemBlock.java");
        String leaves = source("GrapeVineLeavesBlock.java");

        assertTrue(stem.contains("if (age == MAX_AGE) tryGrow(level, pos)"));
        assertTrue(leaves.contains("if (age == MAX_AGE) growFruitAndExpand(level, pos)"));
    }

    private static String source(String name) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/cellar/block", name));
    }
}
