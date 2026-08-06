package growthcraft.milk.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.milk.init.GrowthcraftMilkRecipes;
import growthcraft.milk.recipe.input.PancheonInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import growthcraft.lib.recipe.MachineRecipe;
import growthcraft.lib.recipe.RecipeCodecs;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class PancheonRecipe implements MachineRecipe<PancheonInput> {
    public record FluidAmount(Identifier fluidId, int amount) {}

    private final int processingTime;
    private final FluidAmount inputFluid;
    private final List<FluidAmount> outputFluids;

    public PancheonRecipe(int processingTime, FluidAmount inputFluid, List<FluidAmount> outputFluids) {
        this.processingTime = Math.max(1, processingTime);
        this.inputFluid = inputFluid;
        this.outputFluids = List.copyOf(outputFluids);
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public FluidAmount getInputFluid() {
        return inputFluid;
    }

    public List<FluidAmount> getOutputFluids() {
        return outputFluids;
    }

    public FluidStack getOutputFluidStack(int index) {
        if (index < 0 || index >= outputFluids.size()) {
            return FluidStack.EMPTY;
        }
        FluidAmount output = outputFluids.get(index);
        var fluid = BuiltInRegistries.FLUID.getValue(output.fluidId());
        if (fluid == Fluids.EMPTY || output.amount() <= 0) {
            return FluidStack.EMPTY;
        }
        return new FluidStack(fluid, output.amount());
    }

    @Override
    public boolean matches(PancheonInput input, Level level) {
        FluidStack stack = input.fluid();
        if (stack.isEmpty() || stack.getAmount() < inputFluid.amount()) {
            return false;
        }
        var fluid = BuiltInRegistries.FLUID.getValue(inputFluid.fluidId());
        return fluid != Fluids.EMPTY && fluid.getFluidType() == stack.getFluid().getFluidType();
    }

    @Override
    public ItemStack assemble(PancheonInput input) {
        return ItemStack.EMPTY;
    }
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<? extends net.minecraft.world.item.crafting.Recipe<PancheonInput>> getSerializer() {
        return GrowthcraftMilkRecipes.PANCHEON_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends net.minecraft.world.item.crafting.Recipe<PancheonInput>> getType() {
        return GrowthcraftMilkRecipes.PANCHEON_TYPE.get();
    }

    public static class Serializer  {
        private static final Codec<FluidAmount> FLUID_AMOUNT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("fluid").forGetter(FluidAmount::fluidId),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(FluidAmount::amount)
        ).apply(instance, FluidAmount::new));

        public static final MapCodec<PancheonRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("processing_time", 1200).forGetter(PancheonRecipe::getProcessingTime),
                FLUID_AMOUNT_CODEC.fieldOf("input_fluid").forGetter(PancheonRecipe::getInputFluid),
                FLUID_AMOUNT_CODEC.listOf(2, 2).fieldOf("output_fluids").forGetter(PancheonRecipe::getOutputFluids)
        ).apply(instance, PancheonRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PancheonRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public PancheonRecipe decode(RegistryFriendlyByteBuf buf) {
                int processingTime = buf.readVarInt();
                FluidAmount inputFluid = new FluidAmount(buf.readIdentifier(), buf.readVarInt());
                FluidAmount outputFluid0 = new FluidAmount(buf.readIdentifier(), buf.readVarInt());
                FluidAmount outputFluid1 = new FluidAmount(buf.readIdentifier(), buf.readVarInt());
                return new PancheonRecipe(processingTime, inputFluid, List.of(outputFluid0, outputFluid1));
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, PancheonRecipe recipe) {
                buf.writeVarInt(recipe.processingTime);
                buf.writeIdentifier(recipe.inputFluid.fluidId());
                buf.writeVarInt(recipe.inputFluid.amount());
                buf.writeIdentifier(recipe.outputFluids.get(0).fluidId());
                buf.writeVarInt(recipe.outputFluids.get(0).amount());
                buf.writeIdentifier(recipe.outputFluids.get(1).fluidId());
                buf.writeVarInt(recipe.outputFluids.get(1).amount());
            }
        };
        public MapCodec<PancheonRecipe> codec() {
            return CODEC;
        }
        public StreamCodec<RegistryFriendlyByteBuf, PancheonRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
