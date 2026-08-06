package growthcraft.core.compat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Defers decoding a condition owned by an optional mod until its serializer is
 * actually registered. This prevents absent integrations from breaking recipe
 * loading while retaining the third party's own feature toggle when installed.
 */
public record ThirdPartyRecipeCondition(String targetType, String targetContent,
                                        boolean valueIfNotPresent) implements ICondition {
    public static final MapCodec<ThirdPartyRecipeCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.STRING.fieldOf("target_type").forGetter(ThirdPartyRecipeCondition::targetType),
            Codec.STRING.fieldOf("target_content").forGetter(ThirdPartyRecipeCondition::targetContent),
            Codec.BOOL.fieldOf("value_if_not_present").forGetter(ThirdPartyRecipeCondition::valueIfNotPresent)
    ).apply(builder, ThirdPartyRecipeCondition::new));

    @Override
    public boolean test(IContext context) {
        var serializer = NeoForgeRegistries.CONDITION_SERIALIZERS.get(Identifier.parse(targetType));
        if (serializer.isEmpty()) return valueIfNotPresent;
        MapCodec<? extends ICondition> codec = serializer.get().value();

        JsonObject content = JsonParser.parseString(targetContent).getAsJsonObject();
        return codec.codec().decode(JsonOps.INSTANCE, content)
                .result()
                .map(pair -> pair.getFirst().test(context))
                .orElse(valueIfNotPresent);
    }

    @Override
    public MapCodec<? extends ICondition> codec() { return CODEC; }
}
