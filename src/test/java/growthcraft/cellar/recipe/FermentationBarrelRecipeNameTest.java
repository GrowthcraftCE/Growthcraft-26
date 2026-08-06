package growthcraft.cellar.recipe;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FermentationBarrelRecipeNameTest {
    private static final Path RECIPE_SOURCE =
            Path.of("src/main/java/growthcraft/cellar/recipe/FermentationBarrelRecipe.java");
    private static final Path POTION_ITEM_SOURCE =
            Path.of("src/main/java/growthcraft/cellar/item/CellarPotionItem.java");

    @Test
    void bottleUsesFluidItemNameWithoutCreatingCustomName() throws IOException {
        String source = Files.readString(RECIPE_SOURCE);
        int methodStart = source.indexOf("private static ItemStack createBottleStack");
        int nextMethod = source.indexOf("private static ItemStack createStack", methodStart);
        String createBottleStack = source.substring(methodStart, nextMethod);

        assertTrue(createBottleStack.contains("DataComponents.ITEM_NAME"));
        assertFalse(createBottleStack.contains("DataComponents.CUSTOM_NAME"));
        assertTrue(createBottleStack.contains("DataComponents.POTION_CONTENTS"));
    }

    @Test
    void cellarPotionHonorsTheRecipeAssignedItemName() throws IOException {
        String source = Files.readString(POTION_ITEM_SOURCE);

        assertTrue(source.contains("public Component getName(ItemStack stack)"));
        assertTrue(source.contains("stack.get(DataComponents.ITEM_NAME)"));
        assertTrue(source.contains("!PotionContents.EMPTY.equals(contents)"));
        assertTrue(source.contains("return super.getName(stack)"));
    }

    @Test
    void cellarPotionRetainsMinecraft26DrinkBehavior() throws IOException {
        String source = Files.readString(POTION_ITEM_SOURCE);

        assertTrue(source.contains("DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK"));
        assertTrue(source.contains("usingConvertsTo(Items.GLASS_BOTTLE)"));
    }
}
