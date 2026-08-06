package growthcraft.core.config;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigValueConditionTest {
    private static final Path RESOLVER = Path.of("src/main/java/growthcraft/core/config/ConfigValueConditionResolver.java");
    private static final Path CONDITION = Path.of("src/main/java/growthcraft/core/config/ConfigValueCondition.java");

    @Test
    void resolverIncludesEveryRegisteredModuleAndAlias() throws IOException {
        String source = Files.readString(RESOLVER);
        for (String expected : new String[] {
                "\"core\", GrowthcraftConfig.SPEC", "\"growthcraft\", GrowthcraftConfig.SPEC",
                "\"cellar\", GrowthcraftCellarConfig.SPEC", "\"growthcraft_cellar\", GrowthcraftCellarConfig.SPEC",
                "\"milk\", GrowthcraftMilkConfig.SPEC", "\"growthcraft_milk\", GrowthcraftMilkConfig.SPEC",
                "\"apiary\", GrowthcraftApiaryConfig.SPEC", "\"growthcraft_apiary\", GrowthcraftApiaryConfig.SPEC",
                "\"apples\", GrowthcraftApplesConfig.SPEC", "\"growthcraft_apples\", GrowthcraftApplesConfig.SPEC",
                "\"bamboo\", GrowthcraftBambooConfig.SPEC", "\"growthcraft_bamboo\", GrowthcraftBambooConfig.SPEC",
                "\"rice\", GrowthcraftRiceConfig.SPEC", "\"growthcraft_rice\", GrowthcraftRiceConfig.SPEC" }) {
            assertTrue(source.contains(expected), expected);
        }
    }

    @Test
    void conditionFailsClosedAndRetainsLegacyFeatureCompatibility() throws IOException {
        String resolver = Files.readString(RESOLVER);
        String condition = Files.readString(CONDITION);
        assertTrue(resolver.contains("unknown config module"));
        assertTrue(resolver.contains("invalid config value"));
        assertTrue(resolver.contains("instanceof ModConfigSpec.BooleanValue"));
        assertTrue(condition.contains("configValue.isEmpty()"));
        assertTrue(condition.contains("isLegacyFeatureEnabled"));
    }
}
