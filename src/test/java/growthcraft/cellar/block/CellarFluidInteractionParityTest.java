package growthcraft.cellar.block;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CellarFluidInteractionParityTest {
    @Test
    void kettleRoutesFilledContainersToInputAndEmptyContainersToOutputFirst() throws IOException {
        String source = source("BrewKettleBlock.java");

        assertTrue(source.contains("containsFluid(heldFluidHandler)"));
        assertTrue(source.contains("pos, Direction.UP"));
        assertTrue(source.indexOf("pos, Direction.DOWN") < source.lastIndexOf("pos, Direction.UP"));
    }

    @Test
    void fermentationBarrelAcceptsVanillaAndGrowthcraftMilkBuckets() throws IOException {
        String source = source("FermentationBarrelBlock.java");

        assertTrue(source.contains("heldStack.is(Items.MILK_BUCKET)"));
        assertTrue(source.contains("heldStack.getItem() instanceof GrowthcraftMilkBucketItem"));
        assertTrue(source.contains("GrowthcraftMilkFluids.MILK.source.get()"));
        assertTrue(source.contains("IFluidHandler.FluidAction.SIMULATE"));
    }

    @Test
    void cultureJarMilkTransferRequiresRoomForAWholeBucket() throws IOException {
        String source = source("CultureJarBlock.java");

        assertTrue(source.contains("fill(toInsert, IFluidHandler.FluidAction.SIMULATE)"));
    }

    private static String source(String name) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/cellar/block", name));
    }
}
