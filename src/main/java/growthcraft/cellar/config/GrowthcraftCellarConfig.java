package growthcraft.cellar.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class GrowthcraftCellarConfig {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC;

    // Do we allow rice and corn as adjunct grains in brewing recipes
    private static ModConfigSpec.BooleanValue secondaryAdjunctGrainsAllowed;
    private static ModConfigSpec.IntValue grapeVineMaxHeight;
    private static ModConfigSpec.BooleanValue debugEnabled;
    private static ModConfigSpec.BooleanValue brewKettleDebugEnabled;
    private static ModConfigSpec.BooleanValue cultureJarDebugEnabled;
    private static ModConfigSpec.BooleanValue fermentationBarrelDebugEnabled;
    private static ModConfigSpec.BooleanValue fermentationBarrelManualUnlockAllowed;
    private static ModConfigSpec.BooleanValue fruitPressDebugEnabled;
    private static ModConfigSpec.BooleanValue roasterDebugEnabled;
    private static ModConfigSpec.BooleanValue cropsDebugEnabled;
    private static ModConfigSpec.BooleanValue capabilitiesDebugEnabled;
    private static ModConfigSpec.IntValue barrelFluidTemperatureLimit;

    static {
        SERVER_BUILDER.push("barrels");
        barrelFluidTemperatureLimit = SERVER_BUILDER
                .comment("Reject incoming fluids at or above this temperature in Kelvin in all fermentation and storage barrels.",
                        "Server-wide ceiling: the lower of this value and the growthcraft:barrel_temperature_limit gamerule applies.",
                        "Default 2147483647 K leaves temperature control to the gamerule (default 573 K). Existing contents remain drainable.",
                        "The growthcraft_cellar:barrel_blocked_fluids tag always blocks insertion, regardless of temperature.")
                .defineInRange("fluid_temperature_limit", Integer.MAX_VALUE, 1, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("brewing");
        secondaryAdjunctGrainsAllowed = SERVER_BUILDER
                .comment("Do we allow rice and corn as adjunct grains")
                .define("allow_additional_adjunct_grains", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("crops");
        grapeVineMaxHeight = SERVER_BUILDER
                .comment("Maximum height that grape vines can climb before producing horizontal leaves.")
                .defineInRange("grape_vine_max_height", 8, 1, 32);
        cropsDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional debug logging for Cellar crops.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("debug");
        debugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional Growthcraft Cellar debug logging.")
                .define("enabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("brew_kettle");
        brewKettleDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional logging to debug the Brew Kettle.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("culture_jar");
        cultureJarDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional logging to debug Culture Jar interactions and tank sync.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("fermentation_barrel");
        fermentationBarrelManualUnlockAllowed = SERVER_BUILDER
                .comment("Allow players to stop and restart processing from standard and large fermentation barrel GUIs.")
                .define("allow_manual_unlock", false);
        fermentationBarrelDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional logging to debug the Fermentation Barrel.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("fruit_press");
        fruitPressDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional logging to debug the Fruit Press.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("roaster");
        roasterDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional logging to debug the Roaster.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("capabilities");
        capabilitiesDebugEnabled = SERVER_BUILDER
                .comment("Set to true to log Cellar capability registration details.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SPEC = SERVER_BUILDER.build();
    }

    // values can be pulled via recipe conditions. if this is gray, doesn't mean it is unused.
    public static boolean isSecondaryAdjunctGrainsAllowed() {
        return secondaryAdjunctGrainsAllowed.get();
    }

    public static int getGrapeVineMaxHeight() {
        return grapeVineMaxHeight.get();
    }

    public static int getBarrelFluidTemperatureLimit() {
        return barrelFluidTemperatureLimit.get();
    }

    public static boolean isDebugEnabled() { return debugEnabled.get(); }
    public static boolean isBrewKettleDebugEnabled() { return brewKettleDebugEnabled.get(); }
    public static boolean isCultureJarDebugEnabled() { return cultureJarDebugEnabled.get(); }
    public static boolean isFermentationBarrelDebugEnabled() { return fermentationBarrelDebugEnabled.get(); }
    public static boolean isFermentationBarrelManualUnlockAllowed() {
        return fermentationBarrelManualUnlockAllowed.get();
    }
    public static void setFermentationBarrelManualUnlockAllowed(boolean allowed) {
        fermentationBarrelManualUnlockAllowed.set(allowed);
        fermentationBarrelManualUnlockAllowed.save();
    }
    public static boolean isFruitPressDebugEnabled() { return fruitPressDebugEnabled.get(); }
    public static boolean isRoasterDebugEnabled() { return roasterDebugEnabled.get(); }
    public static boolean isCropsDebugEnabled() { return cropsDebugEnabled.get(); }
    public static boolean isCapabilitiesDebugEnabled() { return capabilitiesDebugEnabled.get(); }

    private GrowthcraftCellarConfig() {}
}
