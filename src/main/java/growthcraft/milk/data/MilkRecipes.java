package growthcraft.milk.data;

import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.Items;

public class MilkRecipes extends RecipeProvider {
    public MilkRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        // Common tags (NeoForge 'c' convention)
        TagKey<Item> IRON_INGOTS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ingots/iron"));
        TagKey<Item> IRON_NUGGETS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "nuggets/iron"));

        // Iron Milking Bucket: uses iron nuggets and ingots, classic bucket-like pattern
        shaped(RecipeCategory.MISC, GrowthcraftMilkItems.MILKING_BUCKET_IRON.get())
                .pattern("NNN")
                .pattern("I I")
                .pattern(" I ")
                .define('N', IRON_NUGGETS)
                .define('I', IRON_INGOTS)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output, ResourceKey.create(
                        Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(growthcraft.milk.config.Reference.MODID, "milking_bucket_iron")));
    }
}
