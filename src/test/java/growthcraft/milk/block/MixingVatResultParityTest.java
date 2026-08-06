package growthcraft.milk.block;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MixingVatResultParityTest {
    @Test
    void resultPickupToolIsPersistedAsAHiddenMachineSlot() throws IOException {
        String entity = source("block/entity/MixingVatBlockEntity.java");
        String menu = source("menu/MixingVatMenu.java");

        assertTrue(entity.contains("SLOT_RESULT_TOOL = 4"));
        assertTrue(entity.contains("setItem(SLOT_RESULT_TOOL, recipe.getResultActivationTool())"));
        assertTrue(entity.contains("setItem(SLOT_RESULT_TOOL, ItemStack.EMPTY)"));
        assertTrue(menu.contains("public boolean isFake() { return true; }"));
    }

    @Test
    void removalDoesNotDuplicateToolGatedResults() throws IOException {
        String block = source("block/MixingVatBlock.java");

        assertTrue(block.contains("slot == MixingVatBlockEntity.SLOT_RESULT_TOOL"));
        assertTrue(block.contains("slot == MixingVatBlockEntity.SLOT_RESULT"));
    }

    @Test
    void cheeseClothCanCollectCurdsFromTheVatResultSlot() throws IOException {
        String item = source("item/CheeseCurdsBlockItem.java");

        assertTrue(item.contains("overrideOtherStackedOnMe"));
        assertTrue(item.contains("other.is(GrowthcraftMilkItems.CHEESE_CLOTH.get())"));
        assertTrue(item.contains("slot.set(ItemStack.EMPTY)"));
    }

    @Test
    void fluidBucketsWithoutCraftingRemaindersCanFillTheVat() throws IOException {
        String block = source("block/MixingVatBlock.java");

        assertTrue(block.contains("remainderTemplate == null ? ItemStack.EMPTY : remainderTemplate.create()"));
        assertTrue(block.contains("remainder.isEmpty() ? new ItemStack(Items.BUCKET) : remainder"));
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/milk", relativePath));
    }
}
