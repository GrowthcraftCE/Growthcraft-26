package growthcraft.lib.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;
import java.util.stream.StreamSupport;

public final class RecipeCodecs {
    public static final Codec<ItemStack> LEGACY_ITEM_STACK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.optionalFieldOf("item").forGetter(stack -> Optional.of(BuiltInRegistries.ITEM.getKey(stack.getItem()))),
            Identifier.CODEC.optionalFieldOf("id").forGetter(stack -> Optional.empty()),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount)
    ).apply(instance, RecipeCodecs::itemStack));

    private static final Codec<Ingredient> LEGACY_INGREDIENT_OBJECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.optionalFieldOf("item").forGetter(ingredient -> Optional.empty()),
            Identifier.CODEC.optionalFieldOf("tag").forGetter(ingredient -> Optional.empty())
    ).apply(instance, RecipeCodecs::ingredient));

    public static final Codec<Ingredient> INGREDIENT_CODEC = Codec.withAlternative(Ingredient.CODEC, LEGACY_INGREDIENT_OBJECT_CODEC);

    private RecipeCodecs() {
    }

    public static ItemStack itemStack(Identifier id, int count) {
        if (count <= 0 || id == null) {
            return ItemStack.EMPTY;
        }
        try {
            return new ItemStack(BuiltInRegistries.ITEM.getValue(id), count);
        } catch (NullPointerException exception) {
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack itemStack(Optional<Identifier> item, Optional<Identifier> id, int count) {
        return item.or(() -> id)
                .map(identifier -> itemStack(identifier, count))
                .orElse(ItemStack.EMPTY);
    }

    public static Fluid fluid(Identifier id) {
        return BuiltInRegistries.FLUID.getValue(id);
    }

    public static Ingredient ingredient(Optional<Identifier> itemId, Optional<Identifier> tagId) {
        return itemId
                .map(id -> Ingredient.of(BuiltInRegistries.ITEM.getValue(id)))
                .orElseGet(() -> tagId
                        .map(RecipeCodecs::tagIngredient)
                        .orElseGet(RecipeCodecs::missingIngredient));
    }

    private static Ingredient tagIngredient(Identifier id) {
        TagKey<Item> tag = TagKey.create(Registries.ITEM, id);
        var holders = StreamSupport.stream(BuiltInRegistries.ITEM.getTagOrEmpty(tag).spliterator(), false)
                .map(holder -> (Holder<Item>) holder)
                .toList();
        return holders.isEmpty() ? missingIngredient() : Ingredient.of(HolderSet.direct(holders));
    }

    private static Ingredient missingIngredient() {
        return Ingredient.of(Items.BARRIER);
    }
}
