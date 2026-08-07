package growthcraft.apples;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        assertTrue(fruit.contains("level.setBlock(pos, this.getStateForAge(0), 2)"));
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

    @Test
    void appleWoodRetainsStableBlockAndItemTagFamilies() throws IOException {
        String blockTags = Files.readString(Path.of(
                "src/main/java/growthcraft/core/data/tags/GrowthcraftBlockTags.java"));
        String itemTags = Files.readString(Path.of(
                "src/main/java/growthcraft/core/data/tags/GrowthcraftItemTags.java"));

        for (String family : new String[]{
                "PLANKS", "WOODEN_BUTTONS", "WOODEN_DOORS", "WOODEN_FENCES", "FENCE_GATES",
                "WOODEN_PRESSURE_PLATES", "WOODEN_SLABS", "WOODEN_STAIRS", "WOODEN_TRAPDOORS",
                "LOGS_THAT_BURN"
        }) {
            assertTrue(blockTags.contains("BlockTags." + family), () -> "Missing apple block tag family " + family);
            assertTrue(itemTags.contains("ItemTags." + family), () -> "Missing apple item tag family " + family);
        }
        assertTrue(blockTags.contains("Tags.Blocks.FENCES_WOODEN"));
        assertTrue(blockTags.contains("Tags.Blocks.FENCE_GATES_WOODEN"));
        assertTrue(itemTags.contains("Tags.Items.FENCES_WOODEN"));
        assertTrue(itemTags.contains("Tags.Items.FENCE_GATES_WOODEN"));
        assertTrue(blockTags.contains("GrowthcraftCellarBlocks.CORK_WOOD_LOG.get()"));
        assertTrue(itemTags.contains("GrowthcraftCellarItems.CORK_WOOD_LOG.get()"));
        assertFalse(Files.exists(Path.of("src/main/resources/data/minecraft/tags/block/logs_that_burn.json")));
        assertFalse(Files.exists(Path.of("src/main/resources/data/minecraft/tags/item/logs_that_burn.json")));
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/apples", relativePath));
    }
}
