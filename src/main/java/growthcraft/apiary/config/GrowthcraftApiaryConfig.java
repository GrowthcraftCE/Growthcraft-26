package growthcraft.apiary.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class GrowthcraftApiaryConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.BooleanValue DEBUG = BUILDER.define("debug.enabled", false);
    private static final ModConfigSpec.BooleanValue FLUIDS_DEBUG = BUILDER.define("fluids.debugEnabled", false);
    public static final ModConfigSpec SPEC = BUILDER.build();
    public static boolean isDebugEnabled() { return DEBUG.get(); }
    public static boolean isFluidsDebugEnabled() { return FLUIDS_DEBUG.get(); }
    private GrowthcraftApiaryConfig() {}
}
