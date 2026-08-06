package growthcraft.bamboo;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BambooGameplayParityTest {
    @Test
    void placementSelectsTheStablePostIdAndPreservesWaterlogging() throws IOException {
        String block = source("block/BambooPostBlock.java");

        assertTrue(block.contains("axis == Direction.Axis.Y"));
        assertTrue(block.contains("BAMBOO_POST_VERTICAL"));
        assertTrue(block.contains("BAMBOO_POST_HORIZONTAL"));
        assertTrue(block.contains("BlockStateProperties.WATERLOGGED"));
        assertTrue(block.contains("fluidState.is(Fluids.WATER)"));
    }

    @Test
    void horizontalPostsRemainClimbableAndDropTheVerticalItem() throws IOException {
        String tags = Files.readString(Path.of(
                "src/main/java/growthcraft/core/data/tags/GrowthcraftBlockTags.java"));
        String loot = source("data/loot/BambooBlockLoot.java");

        assertTrue(tags.contains("this.tag(BlockTags.CLIMBABLE)"));
        assertTrue(tags.contains("GrowthcraftBambooBlocks.BAMBOO_POST_HORIZONTAL.get()"));
        assertTrue(loot.contains("dropOther(GrowthcraftBambooBlocks.BAMBOO_POST_HORIZONTAL.get(), GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get())"));
    }

    @Test
    void bambooBlockRecipeProducesTwoPostItems() throws IOException {
        String recipes = Files.readString(Path.of(
                "src/main/java/growthcraft/core/data/recipe/GrowthcraftRecipeProvider.java"));

        assertTrue(recipes.contains("GrowthcraftBambooItems.BAMBOO_POST_VERTICAL.get(), 2"));
        assertTrue(recipes.contains(".define('B', Items.BAMBOO_BLOCK)"));
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/bamboo", relativePath));
    }
}
