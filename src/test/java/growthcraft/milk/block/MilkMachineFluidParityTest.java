package growthcraft.milk.block;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MilkMachineFluidParityTest {
    @Test
    void mixingVatUsesSharedHeatSourceDetection() throws IOException {
        String source = source("entity/MixingVatBlockEntity.java");

        assertTrue(source.contains("HeatSourceUtils.hasHeatSourceBelow(level, pos)"));
    }

    @Test
    void bucketDepositsRequireRoomForTheWholeBucket() throws IOException {
        assertSimulatesBeforeExecuting(source("MixingVatBlock.java"));
        assertSimulatesBeforeExecuting(source("PancheonBlock.java"));
    }

    private static void assertSimulatesBeforeExecuting(String source) {
        int simulate = source.indexOf("fill(bucketFluid, IFluidHandler.FluidAction.SIMULATE)");
        int execute = source.indexOf("fill(bucketFluid, IFluidHandler.FluidAction.EXECUTE)");
        assertTrue(simulate >= 0 && execute > simulate);
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/milk/block", relativePath));
    }
}
