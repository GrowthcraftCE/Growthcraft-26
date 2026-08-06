package growthcraft.milk.block;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        assertTrue(press.contains("message.growthcraft_milk.cheese_press.processing"));
        assertTrue(press.contains("press.canProcessInput(level)"));
    }

    @Test
    void cheesePressHandlesItemsWithoutCraftingRemainders() throws IOException {
        String press = source("CheesePressBlock.java");

        assertTrue(press.contains("ItemStack remainder = getCraftingRemainder(toInsert);"));
        assertTrue(press.contains("ItemStack requiredContainer = getCraftingRemainder(stack);"));
        assertTrue(press.contains("remainder == null ? ItemStack.EMPTY : remainder.create()"));
    }

    private static String source(String name) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/milk/block", name));
    }
}
