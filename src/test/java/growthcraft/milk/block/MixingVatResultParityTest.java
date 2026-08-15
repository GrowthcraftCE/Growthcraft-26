package growthcraft.milk.block;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MixingVatResultParityTest {
    @Test
    void resultPickupToolIsPersistedAsAHiddenMachineSlot() throws IOException {
        String entity = source("block/entity/MixingVatBlockEntity.java");
        String menu = source("menu/MixingVatMenu.java");

        assertTrue(entity.contains("SLOT_RESULT_TOOL = 4"));
        assertTrue(entity.contains("setItem(SLOT_RESULT_TOOL, recipe.getResultActivationTool())"));
        assertTrue(entity.contains("setItem(SLOT_RESULT_TOOL, ItemStack.EMPTY)"));
        assertTrue(entity.contains("clearResultToolIfResultWasRemoved(index)"));
        assertTrue(entity.contains("index == SLOT_RESULT && items.get(SLOT_RESULT).isEmpty()"));
        assertTrue(entity.contains("items.set(SLOT_RESULT_TOOL, ItemStack.EMPTY)"));
        assertTrue(entity.contains("public ItemStack getRequiredResultTool()"));
        assertTrue(entity.contains("ItemStack.isSameItemSameComponents(recipe.getResultItemStack(), result)"));
        assertTrue(menu.contains("public boolean isFake() { return true; }"));
    }

    @Test
    void removalDoesNotDuplicateToolGatedResults() throws IOException {
        String entity = source("block/entity/MixingVatBlockEntity.java");
        String block = source("block/MixingVatBlock.java");

        assertTrue(entity.contains("public void preRemoveSideEffects(BlockPos pos, BlockState state)"));
        assertTrue(entity.contains("shouldDropWhenBroken(slot)"));
        assertTrue(entity.contains("slot == SLOT_RESULT_TOOL"));
        assertTrue(entity.contains("slot != SLOT_RESULT || getRequiredResultTool().isEmpty()"));
        assertTrue(entity.contains("Containers.dropItemStack"));
        assertFalse(block.contains("affectNeighborsAfterRemoval"));
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

    @Test
    void invalidItemsCannotInterruptAnActivatedBatch() throws IOException {
        String entity = source("block/entity/MixingVatBlockEntity.java");

        assertTrue(entity.contains("heldStack.isEmpty() || activated || isProcessing()"));
        assertTrue(entity.contains("!isMixingVatIngredient(heldStack)"));
        assertTrue(entity.contains("ingredient.ingredient().test(stack)"));
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/milk", relativePath));
    }
}
