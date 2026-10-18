package growthcraft.cellar.fluid;

import growthcraft.cellar.config.GrowthcraftCellarConfig;
import growthcraft.cellar.config.Reference;
import growthcraft.core.init.GrowthcraftGameRules;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

/** Runtime insertion policy only: persisted fluids and extraction are unaffected. */
public final class BarrelFluidPolicy {
    public static final TagKey<Fluid> BLOCKED_FLUIDS = TagKey.create(Registries.FLUID,
            Identifier.fromNamespaceAndPath(Reference.MODID, "barrel_blocked_fluids"));

    private BarrelFluidPolicy() {}

    public static boolean allowsInsertion(FluidStack stack, @Nullable Level level) {
        int limit = GrowthcraftCellarConfig.getBarrelFluidTemperatureLimit();
        if (level instanceof ServerLevel serverLevel) {
            limit = Math.min(limit, serverLevel.getGameRules().get(GrowthcraftGameRules.BARREL_TEMPERATURE_LIMIT.get()));
        }
        return !stack.isEmpty() && !stack.getFluid().is(BLOCKED_FLUIDS)
                && stack.getFluidType().getTemperature(stack) < limit;
    }
}
