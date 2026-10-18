package growthcraft.lib.utils;

/** Partitions one tank into display segments without changing its contents. */
public final class SequentialFluidGauge {
    private SequentialFluidGauge() {}

    public static int capacity(int tankCapacity, int segments, int index) {
        return boundary(tankCapacity, segments, index + 1) - boundary(tankCapacity, segments, index);
    }

    public static int amount(int stored, int tankCapacity, int segments, int index) {
        return (int) Math.clamp((long) stored - boundary(tankCapacity, segments, index),
                0L, capacity(tankCapacity, segments, index));
    }

    private static int boundary(int capacity, int segments, int index) {
        if (capacity < 0 || segments <= 0 || index < 0 || index > segments) {
            throw new IllegalArgumentException("Invalid gauge partition");
        }
        return (int) ((long) capacity * index / segments);
    }
}
