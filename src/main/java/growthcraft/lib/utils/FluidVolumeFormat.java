package growthcraft.lib.utils;

/** Exact barrel volumes: whole buckets when possible, millibuckets otherwise. */
public final class FluidVolumeFormat {
    private FluidVolumeFormat() {}

    public static String format(int millibuckets) {
        return millibuckets % 1000 == 0 ? (millibuckets / 1000) + " B" : millibuckets + " mB";
    }

    public static String tank(int amount, int capacity) {
        return format(amount) + " / " + format(capacity);
    }
}
