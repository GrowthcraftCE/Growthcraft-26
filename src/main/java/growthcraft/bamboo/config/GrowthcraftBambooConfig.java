package growthcraft.bamboo.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class GrowthcraftBambooConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.BooleanValue DEBUG = BUILDER.define("debug.enabled", false);
    private static final ModConfigSpec.BooleanValue BLOCKS_DEBUG = BUILDER.define("blocks.debugEnabled", false);
    public static final ModConfigSpec SPEC = BUILDER.build();
    public static boolean isDebugEnabled() { return DEBUG.get(); }
    public static boolean isBlocksDebugEnabled() { return BLOCKS_DEBUG.get(); }
    private GrowthcraftBambooConfig() {}
}
