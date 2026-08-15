package growthcraft.milk.block;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheeseLifecycleParityTest {
    @Test
    void cheeseAgingRequiresAProtectedDryEnvironment() throws IOException {
        String source = source("CheeseWheelBlock.java");

        assertTrue(source.contains("AGING_RANDOM_TICK_CHANCE = 0.4F"));
        assertTrue(source.contains("level.getFluidState(pos).is(FluidTags.WATER)"));
        assertTrue(source.contains("level.canSeeSky(pos.above())"));
        assertTrue(source.contains("level.isRainingAt(pos.above())"));
    }

    @Test
    void cheeseWheelAndClosedPressExposeLifecycleStatus() throws IOException {
        String wheel = source("CheeseWheelBlock.java");
        String press = source("CheesePressBlock.java");

        assertTrue(wheel.contains("message.growthcraft_milk.cheese_wheel.blocked_open_sky"));
        assertTrue(wheel.contains("sendOverlayMessage"));
        assertTrue(press.contains("percent < 40"));
        assertTrue(press.contains("percent < 80"));
        assertTrue(press.contains("message.growthcraft_milk.cheese_press.processing."));
        assertTrue(press.contains("press.canProcessInput(level)"));
    }

    @Test
    void cheesePressHandlesItemsWithoutCraftingRemainders() throws IOException {
        String press = source("CheesePressBlock.java");

        assertTrue(press.contains("ItemStack remainder = getCraftingRemainder(toInsert);"));
        assertTrue(press.contains("ItemStack requiredContainer = getCraftingRemainder(stack);"));
        assertTrue(press.contains("remainder == null ? ItemStack.EMPTY : remainder.create()"));
    }

    @Test
    void drainedCurdsReturnClothAndPressGuiCannotBypassLifecycle() throws IOException {
        String items = Files.readString(Path.of("src/main/java/growthcraft/milk/init/GrowthcraftMilkItems.java"));
        String press = source("CheesePressBlock.java");
        String menu = Files.readString(Path.of("src/main/java/growthcraft/milk/menu/CheesePressMenu.java"));

        assertTrue(items.contains("properties.craftRemainder(CHEESE_CLOTH.get())"));
        assertTrue(press.contains("player.isShiftKeyDown()"));
        assertTrue(press.contains("player.openMenu(press)"));
        assertFalse(press.contains("sendSystemMessage(message)"));
        assertTrue(press.contains("sendOverlayMessage(message)"));
        assertTrue(menu.contains("@Override public boolean mayPlace(ItemStack stack) { return false; }"));
        assertTrue(menu.contains("@Override public boolean mayPickup(Player player) { return false; }"));
        assertTrue(menu.contains("public ItemStack quickMoveStack(Player player, int index)"));
        assertFalse(menu.contains("moveItemStackTo("));
    }

    private static String source(String name) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/milk/block", name));
    }
}
