package growthcraft.cellar.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.recipe.input.FermentationBarrelInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
import growthcraft.lib.recipe.MachineRecipe;
import growthcraft.lib.recipe.RecipeCodecs;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public class FermentationBarrelRecipe implements MachineRecipe<FermentationBarrelInput> {
    public record FluidAmount(Identifier fluidId, int amount) {}
    public record EffectSpec(Identifier effectId, int duration, int amplifier) {}
    public record BottleSpec(Identifier itemId, int count) {}
    public record CountedIngredient(Ingredient ingredient, int count) {
        public boolean accepts(ItemStack stack) {
            return ingredient.test(stack);
        }

        public boolean test(ItemStack stack) {
            return stack.getCount() >= count && ingredient.test(stack);
        }
    }

    private final int processingTime;
    private final CountedIngredient ingredientItem;
    private final FluidAmount ingredientFluid;
    private final FluidAmount result;
    private final List<EffectSpec> effects;
    private final BottleSpec bottle;
    private final int color;

    public FermentationBarrelRecipe(int processingTime, CountedIngredient ingredientItem, FluidAmount ingredientFluid,
                                    FluidAmount result, List<EffectSpec> effects, BottleSpec bottle, int color) {
        this.processingTime = processingTime <= 0 ? 1200 : processingTime;
        this.ingredientItem = ingredientItem;
        this.ingredientFluid = ingredientFluid;
        this.result = result;
        this.effects = List.copyOf(effects);
        this.bottle = bottle;
        this.color = color;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public CountedIngredient getIngredientItem() {
        return ingredientItem;
    }

    public FluidAmount getIngredientFluid() {
        return ingredientFluid;
    }

    public FluidAmount getResult() {
        return result;
    }

    public List<EffectSpec> getEffects() {
        return effects;
    }

    public ItemStack getBottle() {
        return createBottleStack(createStack(bottle), result, this.effects, color);
    }

    public BottleSpec getBottleSpec() {
        return bottle;
    }

    public int getColor() {
        return color;
    }

    private static ItemStack createBottleStack(ItemStack bottle, FluidAmount result, List<EffectSpec> effects, int color) {
        ItemStack stack = bottle.copy();
        if (stack.isEmpty()) return stack;

        var fluid = BuiltInRegistries.FLUID.getValue(result.fluidId());
        if (fluid != Fluids.EMPTY) {
            stack.set(DataComponents.ITEM_NAME,
                    new FluidStack(fluid, Math.max(1, result.amount())).getHoverName());
        }

        List<MobEffectInstance> potionEffects = effects.stream()
                .map(FermentationBarrelRecipe::createEffectInstance)
                .flatMap(Optional::stream)
                .toList();
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(color), potionEffects, Optional.empty()));
        return stack;
    }

    private static ItemStack createStack(BottleSpec spec) {
        if (spec == null || spec.itemId() == null || spec.count() <= 0) {
            return ItemStack.EMPTY;
        }
        try {
            return new ItemStack(BuiltInRegistries.ITEM.getValue(spec.itemId()), spec.count());
        } catch (NullPointerException exception) {
            return ItemStack.EMPTY;
        }
    }

    private static Optional<MobEffectInstance> createEffectInstance(EffectSpec spec) {
        return BuiltInRegistries.MOB_EFFECT.get(spec.effectId())
                .map(effect -> new MobEffectInstance(effect, spec.duration(), spec.amplifier()));
    }

    public int getOutputMultiplier(FermentationBarrelInput input) {
        int unit = ingredientFluid.amount();
        int amount = input.fluid().getAmount();
        if (unit <= 0 || amount < unit || amount % unit != 0) return 0;
        return amount / unit;
    }

    @Override
    public boolean matches(FermentationBarrelInput input, Level level) {
        int multiplier = getOutputMultiplier(input);
        return multiplier > 0
                && input.fluid().getFluid() == BuiltInRegistries.FLUID.getValue(ingredientFluid.fluidId())
                && input.getItem(0).getCount() >= ingredientItem.count() * multiplier
                && ingredientItem.accepts(input.getItem(0));
    }

    @Override
    public ItemStack assemble(FermentationBarrelInput input) {
        return getBottle();
    }
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return getBottle();
    }

    @Override
    public RecipeSerializer<? extends net.minecraft.world.item.crafting.Recipe<FermentationBarrelInput>> getSerializer() {
        return GrowthcraftCellarRecipes.FERMENTATION_BARREL_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends net.minecraft.world.item.crafting.Recipe<FermentationBarrelInput>> getType() {
        return GrowthcraftCellarRecipes.FERMENTATION_BARREL_TYPE.get();
    }

    public static class Serializer  {
        private static final Codec<FluidAmount> FLUID_AMOUNT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("fluid").forGetter(FluidAmount::fluidId),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(FluidAmount::amount)
        ).apply(instance, FluidAmount::new));

        private static final Codec<CountedIngredient> COUNTED_INGREDIENT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.optionalFieldOf("item").forGetter(ingredient -> Optional.empty()),
                Identifier.CODEC.optionalFieldOf("tag").forGetter(ingredient -> Optional.empty()),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(CountedIngredient::count)
        ).apply(instance, (itemId, tagId, count) -> new CountedIngredient(RecipeCodecs.ingredient(itemId, tagId), count)));

        private static final Codec<EffectSpec> EFFECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("effect").forGetter(EffectSpec::effectId),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("duration", 200).forGetter(EffectSpec::duration),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("amplifier", 0).forGetter(EffectSpec::amplifier)
        ).apply(instance, EffectSpec::new));

        private static final Codec<BottleSpec> BOTTLE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("item").forGetter(BottleSpec::itemId),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("count", 1).forGetter(BottleSpec::count)
        ).apply(instance, BottleSpec::new));

        private static final Codec<Integer> COLOR_CODEC = Codec.withAlternative(
                ExtraCodecs.NON_NEGATIVE_INT,
                Codec.STRING.xmap(Integer::decode, color -> "0x" + Integer.toHexString(color).toUpperCase())
        );

        public static final MapCodec<FermentationBarrelRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("processing_time", 1200).forGetter(FermentationBarrelRecipe::getProcessingTime),
                COUNTED_INGREDIENT_CODEC.fieldOf("ingredient_item").forGetter(FermentationBarrelRecipe::getIngredientItem),
                FLUID_AMOUNT_CODEC.fieldOf("ingredient_fluid").forGetter(FermentationBarrelRecipe::getIngredientFluid),
                FLUID_AMOUNT_CODEC.fieldOf("result").forGetter(FermentationBarrelRecipe::getResult),
                EFFECT_CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(FermentationBarrelRecipe::getEffects),
                BOTTLE_CODEC.optionalFieldOf("bottle", new BottleSpec(Identifier.withDefaultNamespace("air"), 0)).forGetter(FermentationBarrelRecipe::getBottleSpec),
                COLOR_CODEC.optionalFieldOf("color", 0xFFFFFF).forGetter(FermentationBarrelRecipe::getColor)
        ).apply(instance, FermentationBarrelRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, FermentationBarrelRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public FermentationBarrelRecipe decode(RegistryFriendlyByteBuf buf) {
                int processingTime = buf.readVarInt();
                Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                int ingredientCount = buf.readVarInt();
                FluidAmount ingredientFluid = new FluidAmount(buf.readIdentifier(), buf.readVarInt());
                FluidAmount result = new FluidAmount(buf.readIdentifier(), buf.readVarInt());
                int effectCount = buf.readVarInt();
                var effects = new java.util.ArrayList<EffectSpec>(effectCount);
                for (int i = 0; i < effectCount; i++) {
                    effects.add(new EffectSpec(buf.readIdentifier(), buf.readVarInt(), buf.readVarInt()));
                }
                BottleSpec bottle = new BottleSpec(buf.readIdentifier(), buf.readVarInt());
                int color = buf.readVarInt();
                return new FermentationBarrelRecipe(processingTime, new CountedIngredient(ingredient, ingredientCount),
                        ingredientFluid, result, effects, bottle, color);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, FermentationBarrelRecipe recipe) {
                buf.writeVarInt(recipe.processingTime);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.ingredientItem.ingredient());
                buf.writeVarInt(recipe.ingredientItem.count());
                buf.writeIdentifier(recipe.ingredientFluid.fluidId());
                buf.writeVarInt(recipe.ingredientFluid.amount());
                buf.writeIdentifier(recipe.result.fluidId());
                buf.writeVarInt(recipe.result.amount());
                buf.writeVarInt(recipe.effects.size());
                for (EffectSpec effect : recipe.effects) {
                    buf.writeIdentifier(effect.effectId());
                    buf.writeVarInt(effect.duration());
                    buf.writeVarInt(effect.amplifier());
                }
                buf.writeIdentifier(recipe.bottle.itemId());
                buf.writeVarInt(recipe.bottle.count());
                buf.writeVarInt(recipe.color);
            }
        };
        public MapCodec<FermentationBarrelRecipe> codec() {
            return CODEC;
        }
        public StreamCodec<RegistryFriendlyByteBuf, FermentationBarrelRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
