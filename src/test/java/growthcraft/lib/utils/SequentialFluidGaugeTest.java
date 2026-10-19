package growthcraft.lib.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SequentialFluidGaugeTest {
    @Test
    void fillsAndEmptiesInOrderAtBucketAndPartialBucketBoundaries() {
        assertArrayEquals(new int[]{0, 0, 0, 0}, amounts(0, 128000));
        assertArrayEquals(new int[]{8000, 0, 0, 0}, amounts(8000, 128000));
        assertArrayEquals(new int[]{31999, 0, 0, 0}, amounts(31999, 128000));
        assertArrayEquals(new int[]{32000, 0, 0, 0}, amounts(32000, 128000));
        assertArrayEquals(new int[]{32000, 1, 0, 0}, amounts(32001, 128000));
        assertArrayEquals(new int[]{32000, 16000, 0, 0}, amounts(48000, 128000));
        assertArrayEquals(new int[]{32000, 32000, 0, 0}, amounts(64000, 128000));
        assertArrayEquals(new int[]{32000, 32000, 32000, 0}, amounts(96000, 128000));
        assertArrayEquals(new int[]{32000, 32000, 32000, 31999}, amounts(127999, 128000));
        assertArrayEquals(new int[]{32000, 32000, 32000, 32000}, amounts(128000, 128000));
    }

    @Test
    void everyMillibucketIsRepresentedExactlyOnce() {
        for (int stored = 0; stored <= 128000; stored++) {
            assertEquals(stored, Arrays.stream(amounts(stored, 128000)).sum());
        }
        // Preserve remainders if a different capacity is ever synchronized.
        for (int stored = 0; stored <= 129; stored++) {
            assertEquals(stored, Arrays.stream(amounts(stored, 129)).sum());
        }
    }

    @Test
    void clampsDisplayAmountsWithoutOverflow() {
        assertArrayEquals(new int[]{0, 0, 0, 0}, amounts(Integer.MIN_VALUE, 128000));
        assertArrayEquals(new int[]{32000, 32000, 32000, 32000}, amounts(Integer.MAX_VALUE, 128000));
        assertArrayEquals(new int[]{0, 0, 0, 0}, amounts(0, 0));
    }

    private static int[] amounts(int stored, int capacity) {
        int[] result = new int[4];
        for (int i = 0; i < result.length; i++) {
            result[i] = SequentialFluidGauge.amount(stored, capacity, result.length, i);
        }
        return result;
    }
}
