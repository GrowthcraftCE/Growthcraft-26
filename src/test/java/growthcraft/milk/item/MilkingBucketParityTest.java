package growthcraft.milk.item;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MilkingBucketParityTest {
    @Test
    void milkingUsesTheDataDrivenEntityTag() throws IOException {
        String source = Files.readString(Path.of("src/main/java/growthcraft/milk/item/MilkingBucketItem.java"));
        String tag = Files.readString(Path.of("src/main/resources/data/growthcraft_milk/tags/entity_type/milkable.json"));

        assertTrue(source.contains("builtInRegistryHolder().is(GrowthcraftMilkTags.EntityTypes.MILKABLE)"));
        assertTrue(tag.contains("minecraft:cow"));
        assertTrue(tag.contains("minecraft:sheep"));
    }

    @Test
    void bucketDiagnosticsAreOptIn() throws IOException {
        String milkingBucket = Files.readString(Path.of("src/main/java/growthcraft/milk/item/MilkingBucketItem.java"));
        String fluidBucket = Files.readString(Path.of("src/main/java/growthcraft/milk/item/GrowthcraftMilkBucketItem.java"));

        assertTrue(milkingBucket.contains("GrowthcraftMilkConfig.isBucketsDebugEnabled()"));
        assertTrue(fluidBucket.contains("GrowthcraftMilkConfig.isBucketsDebugEnabled()"));
    }
}
