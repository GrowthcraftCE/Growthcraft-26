package growthcraft.milk.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.milk.init.GrowthcraftMilkRecipes;
import growthcraft.milk.recipe.input.CheesePressInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import growthcraft.lib.recipe.MachineRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class CheesePressRecipe implements MachineRecipe<CheesePressInput> {
    private final int processingTime;
    private final ItemStackTemplate inputItem;
    private final ItemStackTemplate resultItem;
    private final Optional<ItemStackTemplate> sliceItem;

    public CheesePressRecipe(int processingTime, ItemStackTemplate inputItem, ItemStackTemplate resultItem,
                             Optional<ItemStackTemplate> sliceItem) {
        this.processingTime = Math.max(1, processingTime);
        this.inputItem = inputItem;
        this.resultItem = resultItem;
        this.sliceItem = sliceItem;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public ItemStack getInputItem() {
        return inputItem.create();
    }

    public ItemStack getResultItem() {
        return resultItem.create();
    }

    public ItemStack getSliceItem() {
        return sliceItem.map(ItemStackTemplate::create).orElse(ItemStack.EMPTY);
    }

    @Override
    public boolean matches(CheesePressInput input, Level level) {
        ItemStack stack = input.getItem(0);
        ItemStack required = inputItem.create();
        return ItemStack.isSameItemSameComponents(stack, required) && stack.getCount() >= required.getCount();
    }

    @Override
    public ItemStack assemble(CheesePressInput input) {
        return resultItem.create();
    }
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return resultItem.create();
    }

    @Override
    public RecipeSerializer<? extends net.minecraft.world.item.crafting.Recipe<CheesePressInput>> getSerializer() {
        return GrowthcraftMilkRecipes.CHEESE_PRESS_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends net.minecraft.world.item.crafting.Recipe<CheesePressInput>> getType() {
        return GrowthcraftMilkRecipes.CHEESE_PRESS_TYPE.get();
    }

    public static class Serializer  {
        public static final MapCodec<CheesePressRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("processing_time", 6000).forGetter(CheesePressRecipe::getProcessingTime),
                ItemStackTemplate.MAP_CODEC.codec().fieldOf("ingredient").forGetter(recipe -> recipe.inputItem),
                ItemStackTemplate.MAP_CODEC.codec().fieldOf("result_item").forGetter(recipe -> recipe.resultItem),
                ItemStackTemplate.MAP_CODEC.codec().optionalFieldOf("slice").forGetter(recipe -> recipe.sliceItem)
        ).apply(instance, CheesePressRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CheesePressRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public CheesePressRecipe decode(RegistryFriendlyByteBuf buf) {
                int processingTime = buf.readVarInt();
                ItemStackTemplate input = ItemStackTemplate.STREAM_CODEC.decode(buf);
                ItemStackTemplate result = ItemStackTemplate.STREAM_CODEC.decode(buf);
                Optional<ItemStackTemplate> slice = ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC).decode(buf);
                return new CheesePressRecipe(processingTime, input, result, slice);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, CheesePressRecipe recipe) {
                buf.writeVarInt(recipe.processingTime);
                ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.inputItem);
                ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.resultItem);
                ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC).encode(buf, recipe.sliceItem);
            }
        };
        public MapCodec<CheesePressRecipe> codec() {
            return CODEC;
        }
        public StreamCodec<RegistryFriendlyByteBuf, CheesePressRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
