package growthcraft.apples.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class GrowthcraftApplesConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.BooleanValue DEBUG = BUILDER.define("debug.enabled", false);
    private static final ModConfigSpec.BooleanValue BLOCKS_DEBUG = BUILDER.define("blocks.debugEnabled", false);
    private static final ModConfigSpec.BooleanValue CROPS_DEBUG = BUILDER.define("crops.debugEnabled", false);
    private static final ModConfigSpec.BooleanValue FLUIDS_DEBUG = BUILDER.define("fluids.debugEnabled", false);
    public static final ModConfigSpec SPEC = BUILDER.build();
    public static boolean isDebugEnabled() { return DEBUG.get(); }
    public static boolean isBlocksDebugEnabled() { return BLOCKS_DEBUG.get(); }
    public static boolean isCropsDebugEnabled() { return CROPS_DEBUG.get(); }
    public static boolean isFluidsDebugEnabled() { return FLUIDS_DEBUG.get(); }
    private GrowthcraftApplesConfig() {}
}
