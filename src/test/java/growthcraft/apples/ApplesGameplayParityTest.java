package growthcraft.apples;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplesGameplayParityTest {
    @Test
    void axesStripAppleLogsAndWoodWhilePreservingAxis() throws IOException {
        String module = source("GrowthcraftApples.java");

        assertTrue(module.contains("NeoForge.EVENT_BUS.register(this)"));
        assertTrue(module.contains("ItemAbilities.AXE_STRIP"));
        assertTrue(module.contains("APPLE_WOOD_LOG_STRIPPED"));
        assertTrue(module.contains("APPLE_WOOD_STRIPPED"));
        assertTrue(module.contains("RotatedPillarBlock.AXIS"));
    }

    @Test
    void appleFruitGrowsUnderLeavesAndResetsAfterHarvest() throws IOException {
        String fruit = source("block/AppleTreeFruitBlock.java");
        String leaves = source("block/AppleTreeLeavesBlock.java");

        assertTrue(fruit.contains("pos.above()).is(GrowthcraftApplesBlocks.APPLE_TREE_LEAVES"));
        assertTrue(fruit.contains("popResource(level, pos, new ItemStack(Items.APPLE))"));
        assertTrue(fruit.contains("this.getStateForAge(0)"));
        assertTrue(leaves.contains("MAX_APPLES_IN_AREA = 2"));
    }

    @Test
    void appleFenceAndGateRetainTheirVanillaConnectivityTags() throws IOException {
        String tags = Files.readString(Path.of(
                "src/main/java/growthcraft/core/data/tags/GrowthcraftBlockTags.java"));

        assertTrue(tags.contains("this.tag(BlockTags.WOODEN_FENCES)"));
        assertTrue(tags.contains("GrowthcraftApplesBlocks.APPLE_PLANK_FENCE.get()"));
        assertTrue(tags.contains("this.tag(BlockTags.FENCE_GATES)"));
        assertTrue(tags.contains("GrowthcraftApplesBlocks.APPLE_PLANK_FENCE_GATE.get()"));
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/apples", relativePath));
    }
}
