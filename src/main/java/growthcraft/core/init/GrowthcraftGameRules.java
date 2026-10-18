package growthcraft.core.init;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import growthcraft.core.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public final class GrowthcraftGameRules {
    public static final DeferredRegister<GameRule<?>> GAME_RULES = DeferredRegister.create(Registries.GAME_RULE, Reference.MODID);
    public static final Supplier<GameRule<Boolean>> SALT_GENERATION = GAME_RULES.register("salt_generation", () ->
            new GameRule<>(GameRuleCategory.UPDATES, GameRuleType.BOOL, BoolArgumentType.bool(),
                    GameRuleTypeVisitor::visitBoolean, Codec.BOOL, value -> value ? 1 : 0, true, FeatureFlagSet.of()));

    private GrowthcraftGameRules() {}
}
