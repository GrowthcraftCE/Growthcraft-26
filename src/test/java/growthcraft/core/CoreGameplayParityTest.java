package growthcraft.core;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CoreGameplayParityTest {
    @Test
    void wrenchIsNonStackableAndSaltBlockIsPickaxeMineable() throws IOException {
        String items = source("init/GrowthcraftItems.java");
        String blockTags = source("data/tags/GrowthcraftBlockTags.java");

        assertTrue(items.contains("properties -> new Item(properties.stacksTo(1))"));
        assertTrue(blockTags.contains("GrowthcraftBlocks.SALT_BLOCK.get()"));
    }

    @Test
    void stableLootModifiersUseTheAutomaticallyLoaded26xLayout() {
        for (String modifier : new String[]{
                "seeds_from_short_grass",
                "seeds_from_tall_grass",
                "seeds_from_fern",
                "seeds_from_large_fern"
        }) {
            assertTrue(Files.exists(Path.of(
                    "src/main/resources/data/growthcraft/loot_modifiers", modifier + ".json")));
        }
        assertTrue(Files.exists(Path.of(
                "src/main/resources/data/growthcraft_cellar/loot_modifiers/bottles_in_loot_chest.json")));
    }

    @Test
    void compostingAndRuminantFoodDataRemainPresent() throws IOException {
        String compostables = resource("data/neoforge/data_maps/item/compostables.json");

        assertTrue(compostables.contains("growthcraft_apples:apple_seeds"));
        assertTrue(compostables.contains("growthcraft_cellar:hops"));
        assertTrue(compostables.contains("growthcraft_milk:thistle"));
        assertTrue(compostables.contains("growthcraft_rice:rice_stalk"));

        for (String animal : new String[]{"cow", "goat", "sheep"}) {
            assertTrue(resource("data/minecraft/tags/item/" + animal + "_food.json")
                    .contains("growthcraft_rice:rice_stalk"));
        }
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/core", relativePath));
    }

    private static String resource(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/resources", relativePath));
    }
}
