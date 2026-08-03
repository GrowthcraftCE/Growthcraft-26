package growthcraft.cellar.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.recipe.input.RoasterInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import growthcraft.lib.recipe.MachineRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RoasterRecipe implements MachineRecipe<RoasterInput> {
    private final int roastingLevel;
    private final ItemStackTemplate inputItem;
    private final ItemStackTemplate result;

    public RoasterRecipe(int roastingLevel, ItemStackTemplate inputItem, ItemStackTemplate result) {
        this.roastingLevel = roastingLevel;
        this.inputItem = inputItem;
        this.result = result;
    }

    public int getRoastingLevel() {
        return roastingLevel;
    }

    public ItemStack getInputItem() {
        return inputItem.create();
    }

    public ItemStack getResult() {
        return result.create();
    }

    @Override
    public boolean matches(RoasterInput input, Level level) {
        ItemStack stack = input.getItem(0);
        ItemStack required = inputItem.create();
        return ItemStack.isSameItemSameComponents(stack, required) && stack.getCount() >= required.getCount();
    }

    @Override
    public ItemStack assemble(RoasterInput input) {
        return result.create();
    }
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.create();
    }

    @Override
    public RecipeSerializer<? extends net.minecraft.world.item.crafting.Recipe<RoasterInput>> getSerializer() {
        return GrowthcraftCellarRecipes.ROASTER_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends net.minecraft.world.item.crafting.Recipe<RoasterInput>> getType() {
        return GrowthcraftCellarRecipes.ROASTER_TYPE.get();
    }

    public static class Serializer  {
        public static final MapCodec<RoasterRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.intRange(1, 8).fieldOf("roasting_level").forGetter(RoasterRecipe::getRoastingLevel),
                ItemStackTemplate.MAP_CODEC.codec().fieldOf("input_item").forGetter(recipe -> recipe.inputItem),
                ItemStackTemplate.MAP_CODEC.codec().fieldOf("result").forGetter(recipe -> recipe.result)
        ).apply(instance, RoasterRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, RoasterRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public RoasterRecipe decode(RegistryFriendlyByteBuf buf) {
                int roastingLevel = buf.readVarInt();
                ItemStackTemplate inputItem = ItemStackTemplate.STREAM_CODEC.decode(buf);
                ItemStackTemplate result = ItemStackTemplate.STREAM_CODEC.decode(buf);
                return new RoasterRecipe(roastingLevel, inputItem, result);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, RoasterRecipe recipe) {
                buf.writeVarInt(recipe.roastingLevel);
                ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.inputItem);
                ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.result);
            }
        };
        public MapCodec<RoasterRecipe> codec() {
            return CODEC;
        }
        public StreamCodec<RegistryFriendlyByteBuf, RoasterRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
