package growthcraft.lib.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FluidVolumeFormatTest {
    @Test void wholeBucketAmountsStayCompact() {
        assertEquals("0 B / 32 B", FluidVolumeFormat.tank(0, 32000));
        assertEquals("4 B / 4 B", FluidVolumeFormat.tank(4000, 4000));
        assertEquals("32 B / 32 B", FluidVolumeFormat.tank(32000, 32000));
        assertEquals("128 B / 128 B", FluidVolumeFormat.tank(128000, 128000));
    }

    @Test void partialBucketAmountsNeverLosePrecision() {
        assertEquals("500 mB / 32 B", FluidVolumeFormat.tank(500, 32000));
        assertEquals("31500 mB / 32 B", FluidVolumeFormat.tank(31500, 32000));
        assertEquals("127999 mB / 128 B", FluidVolumeFormat.tank(127999, 128000));
        assertEquals("1 mB / 1500 mB", FluidVolumeFormat.tank(1, 1500));
    }
}
