package growthcraft.core.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.cellar.config.GrowthcraftCellarConfig;
import growthcraft.core.Growthcraft;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.Optional;

public record ConfigValueCondition(String module, String name) implements ICondition {
    public static final MapCodec<ConfigValueCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("module").forGetter(condition -> "feature".equals(condition.module()) ? Optional.empty() : Optional.of(condition.module())),
            Codec.STRING.optionalFieldOf("name").forGetter(condition -> "feature".equals(condition.module()) ? Optional.empty() : Optional.of(condition.name())),
            Codec.STRING.optionalFieldOf("feature").forGetter(condition -> "feature".equals(condition.module()) ? Optional.of(condition.name()) : Optional.empty())
    ).apply(instance, ConfigValueCondition::create));

    private static ConfigValueCondition create(Optional<String> module, Optional<String> name, Optional<String> feature) {
        if (module.isPresent() && name.isPresent()) {
            return new ConfigValueCondition(module.get(), name.get());
        }
        return feature
                .map(value -> new ConfigValueCondition("feature", value))
                .orElseGet(() -> new ConfigValueCondition("", ""));
    }

    @Override
    public boolean test(IContext context) {
        if ("feature".equals(module)) {
            return isLegacyFeatureEnabled(name);
        }

        if ("cellar".equals(module) && "brewing.allow_additional_adjunct_grains".equals(name)) {
            return GrowthcraftCellarConfig.isSecondaryAdjunctGrainsAllowed();
        }

        Growthcraft.LOGGER.error("Growthcraft condition error: invalid config value {}.{}", module, name);
        return false;
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    private static boolean isLegacyFeatureEnabled(String feature) {
        return switch (feature) {
            case "apiary", "apiary/basic_wax", "apiary/beverages",
                 "apples",
                 "milk",
                 "rice", "rice/beverages" -> true;
            default -> {
                Growthcraft.LOGGER.error("Growthcraft condition error: invalid legacy feature {}", feature);
                yield false;
            }
        };
    }
}
