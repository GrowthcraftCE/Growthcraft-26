package growthcraft.core.data.recipe;

import growthcraft.core.config.ConfigValueCondition;
import growthcraft.core.config.Reference;
import growthcraft.apples.init.GrowthcraftApplesItems;
import growthcraft.apiary.init.GrowthcraftApiaryItems;
import growthcraft.apiary.init.GrowthcraftApiaryTags;
import growthcraft.bamboo.init.GrowthcraftBambooItems;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.core.init.GrowthcraftItems;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.init.GrowthcraftMilkTags;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class GrowthcraftRecipeProvider extends RecipeProvider {
    public GrowthcraftRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        // Shaped: 3x3 salt -> 1 salt_block
        shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftItems.SALT_BLOCK.get())
                .pattern("sss")
                .pattern("sss")
                .pattern("sss")
                .define('s', GrowthcraftItems.SALT.get())
                .unlockedBy(getHasName(GrowthcraftItems.SALT.get()), has(GrowthcraftItems.SALT.get()))
                .save(output, recipeKey(Reference.MODID, "salt_block"));

        // Shapeless: 1 salt_block -> 9 salt
        shapeless(RecipeCategory.MISC, GrowthcraftItems.SALT.get(), 9)
                .requires(GrowthcraftItems.SALT_BLOCK.get())
                .unlockedBy(getHasName(GrowthcraftItems.SALT_BLOCK.get()), has(GrowthcraftItems.SALT_BLOCK.get()))
                .save(output, recipeKey(Reference.MODID, "salt"));

        // Rope (linen) recipe: 8x rope_linen from string and lead
        shaped(RecipeCategory.MISC, GrowthcraftItems.ROPE_LINEN.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.STRING)
                .define('B', Items.LEAD)
                .group("growthcraft")
                .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                .unlockedBy(getHasName(Items.LEAD), has(Items.LEAD))
                .save(output, recipeKey(Reference.MODID, "rope_linen"));

        shaped(RecipeCategory.MISC, GrowthcraftItems.ROPE_LINEN.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.STRING)
                .define('B', GrowthcraftItems.ROPE_LINEN.get())
                .group("growthcraft")
                .unlockedBy(getHasName(GrowthcraftItems.ROPE_LINEN.get()), has(GrowthcraftItems.ROPE_LINEN.get()))
                .save(output, recipeKey(Reference.MODID, "rope_linen_lengthen"));

        // Common iron ingots tag used by crowbar recipes
        TagKey<Item> IRON_INGOTS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ingots/iron"));

        // Crowbar recipes (shaped) using common tags for NeoForge
        TagKey<Item> IRON_NUGGETS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "nuggets/iron"));

        // Pattern:
        //   "  A"
        //   "CBC"
        //   "A  "
        // A = iron nuggets, B = iron ingots, C = color matching carpet
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_BLACK.get(), Blocks.BLACK_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_black");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_BLUE.get(), Blocks.BLUE_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_blue");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_BROWN.get(), Blocks.BROWN_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_brown");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_CYAN.get(), Blocks.CYAN_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_cyan");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_GRAY.get(), Blocks.GRAY_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_gray");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_GREEN.get(), Blocks.GREEN_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_green");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_LIGHT_BLUE.get(), Blocks.LIGHT_BLUE_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_light_blue");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_LIGHT_GRAY.get(), Blocks.LIGHT_GRAY_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_light_gray");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_LIME.get(), Blocks.LIME_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_lime");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_MAGENTA.get(), Blocks.MAGENTA_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_magenta");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_ORANGE.get(), Blocks.ORANGE_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_orange");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_PINK.get(), Blocks.PINK_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_pink");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_PURPLE.get(), Blocks.PURPLE_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_purple");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_RED.get(), Blocks.RED_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_red");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_WHITE.get(), Blocks.WHITE_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_white");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_YELLOW.get(), Blocks.YELLOW_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_yellow");

        shaped(RecipeCategory.TOOLS, GrowthcraftItems.WRENCH.get())
                .pattern(" BB")
                .pattern(" AB")
                .pattern("A  ")
                .define('A', IRON_INGOTS)
                .define('B', IRON_NUGGETS)
                .group("growthcraft")
                .unlockedBy("has_iron_ingots", has(IRON_INGOTS))
                .save(output, recipeKey(Reference.MODID, "wrench"));

        // --- Milk module recipes ---
        // Iron Milking Bucket
        shaped(RecipeCategory.MISC, GrowthcraftMilkItems.MILKING_BUCKET_IRON.get())
                .pattern("NNN")
                .pattern("I I")
                .pattern(" I ")
                .define('N', IRON_NUGGETS)
                .define('I', IRON_INGOTS)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output, recipeKey(growthcraft.milk.config.Reference.MODID, "milking_bucket_iron"));

        addMilkIngredientRecipes(output);
        addMilkBakingRecipes(output);
        addMilkBowlFoodRecipes(output);
        addMilkMachineRecipes(output, IRON_INGOTS);

        shaped(RecipeCategory.MISC, GrowthcraftCellarItems.CULTURE_JAR.get())
                .pattern("BAB")
                .pattern("B B")
                .pattern("BBB")
                .define('A', ItemTags.PLANKS)
                .define('B', Tags.Items.GLASS_PANES)
                .group("growthcraft_cellar")
                .unlockedBy("has_glass_panes", has(Tags.Items.GLASS_PANES))
                .save(output, recipeKey(growthcraft.cellar.config.Reference.MODID, growthcraft.cellar.config.Reference.UnlocalizedName.Block.CULTURE_JAR));

        addCellarMachineRecipes(output, IRON_INGOTS);

        shapeless(RecipeCategory.MISC, GrowthcraftCellarItems.HOPS_SEEDS.get(), 2)
                .requires(GrowthcraftCellarItems.HOPS.get())
                .group("growthcraft_cellar")
                .unlockedBy(getHasName(GrowthcraftCellarItems.HOPS.get()), has(GrowthcraftCellarItems.HOPS.get()))
                .save(output, recipeKey(growthcraft.cellar.config.Reference.MODID, growthcraft.cellar.config.Reference.UnlocalizedName.Item.HOPS_SEEDS));

        shapeless(RecipeCategory.MISC, GrowthcraftCellarItems.CORK_COASTER.get(), 2)
                .requires(GrowthcraftCellarItems.CORK_BARK.get())
                .group("growthcraft_cellar")
                .unlockedBy(getHasName(GrowthcraftCellarItems.CORK_BARK.get()), has(GrowthcraftCellarItems.CORK_BARK.get()))
                .save(output, recipeKey(growthcraft.cellar.config.Reference.MODID, growthcraft.cellar.config.Reference.UnlocalizedName.Item.CORK_COASTER));

        shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftCellarItems.CORK_WOOD.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', GrowthcraftCellarItems.CORK_WOOD_LOG.get())
                .group(growthcraft.cellar.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftCellarItems.CORK_WOOD_LOG.get()), has(GrowthcraftCellarItems.CORK_WOOD_LOG.get()))
                .save(output, recipeKey(growthcraft.cellar.config.Reference.MODID, growthcraft.cellar.config.Reference.UnlocalizedName.Block.CORK_WOOD));

        shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftCellarItems.CORK_WOOD_STRIPPED.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', GrowthcraftCellarItems.CORK_WOOD_LOG_STRIPPED.get())
                .group(growthcraft.cellar.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftCellarItems.CORK_WOOD_LOG_STRIPPED.get()), has(GrowthcraftCellarItems.CORK_WOOD_LOG_STRIPPED.get()))
                .save(output, recipeKey(growthcraft.cellar.config.Reference.MODID, growthcraft.cellar.config.Reference.UnlocalizedName.Block.CORK_WOOD_STRIPPED));

        addCellarGrainRecipes(output);

        addAppleWoodRecipes(output);
        addApiaryBeeswaxRecipes(output);
        addApiaryCandleRecipes(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftBambooItems.BAMBOO_POST_VERTICAL.get(), 2)
                .pattern("B")
                .pattern("B")
                .pattern("B")
                .define('B', Items.BAMBOO_BLOCK)
                .group(growthcraft.bamboo.config.Reference.MODID)
                .unlockedBy(getHasName(Items.BAMBOO_BLOCK), has(Items.BAMBOO_BLOCK))
                .save(output, recipeKey(
                        growthcraft.bamboo.config.Reference.MODID,
                        growthcraft.bamboo.config.Reference.UnlocalizedName.Block.BAMBOO_POST_VERTICAL));
    }

    private void addCellarMachineRecipes(RecipeOutput output, TagKey<Item> ironIngots) {
        shapeless(RecipeCategory.MISC, GrowthcraftCellarItems.BREW_KETTLE.get())
                .requires(Blocks.CAULDRON)
                .group(growthcraft.cellar.config.Reference.MODID)
                .unlockedBy(getHasName(Items.CAULDRON), has(Items.CAULDRON))
                .save(output, recipeKey(
                        growthcraft.cellar.config.Reference.MODID,
                        growthcraft.cellar.config.Reference.UnlocalizedName.Block.BREW_KETTLE));

        addFermentationBarrelRecipe(output, ironIngots, GrowthcraftCellarItems.FERMENTATION_BARREL_ACACIA.get(),
                Blocks.ACACIA_PLANKS, growthcraft.cellar.config.Reference.UnlocalizedName.Block.FERMENT_BARREL_ACACIA);
        addFermentationBarrelRecipe(output, ironIngots, GrowthcraftCellarItems.FERMENTATION_BARREL_APPLE.get(),
                GrowthcraftApplesItems.APPLE_PLANK.get(), growthcraft.cellar.config.Reference.UnlocalizedName.Block.FERMENT_BARREL_APPLE);
        addFermentationBarrelRecipe(output, ironIngots, GrowthcraftCellarItems.FERMENTATION_BARREL_BAMBOO.get(),
                Blocks.BAMBOO_PLANKS, growthcraft.cellar.config.Reference.UnlocalizedName.Block.FERMENT_BARREL_BAMBOO);
        addFermentationBarrelRecipe(output, ironIngots, GrowthcraftCellarItems.FERMENTATION_BARREL_BIRCH.get(),
                Blocks.BIRCH_PLANKS, growthcraft.cellar.config.Reference.UnlocalizedName.Block.FERMENT_BARREL_BIRCH);
        addFermentationBarrelRecipe(output, ironIngots, GrowthcraftCellarItems.FERMENTATION_BARREL_CHERRY.get(),
                Blocks.CHERRY_PLANKS, growthcraft.cellar.config.Reference.UnlocalizedName.Block.FERMENT_BARREL_CHERRY);
        addFermentationBarrelRecipe(output, ironIngots, GrowthcraftCellarItems.FERMENTATION_BARREL_CRIMSON.get(),
                Blocks.CRIMSON_PLANKS, growthcraft.cellar.config.Reference.UnlocalizedName.Block.FERMENT_BARREL_CRIMSON);
        addFermentationBarrelRecipe(output, ironIngots, GrowthcraftCellarItems.FERMENTATION_BARREL_DARK_OAK.get(),
                Blocks.DARK_OAK_PLANKS, growthcraft.cellar.config.Reference.UnlocalizedName.Block.FERMENT_BARREL_DARK_OAK);
        addFermentationBarrelRecipe(output, ironIngots, GrowthcraftCellarItems.FERMENTATION_BARREL_JUNGLE.get(),
                Blocks.JUNGLE_PLANKS, growthcraft.cellar.config.Reference.UnlocalizedName.Block.FERMENT_BARREL_JUNGLE);
        addFermentationBarrelRecipe(output, ironIngots, GrowthcraftCellarItems.FERMENTATION_BARREL_MANGROVE.get(),
                Blocks.MANGROVE_PLANKS, growthcraft.cellar.config.Reference.UnlocalizedName.Block.FERMENT_BARREL_MANGROVE);
        addFermentationBarrelRecipe(output, ironIngots, GrowthcraftCellarItems.FERMENTATION_BARREL_OAK.get(),
                Blocks.OAK_PLANKS, growthcraft.cellar.config.Reference.UnlocalizedName.Block.FERMENT_BARREL_OAK);
        addFermentationBarrelRecipe(output, ironIngots, GrowthcraftCellarItems.FERMENTATION_BARREL_PALE_OAK.get(),
                Blocks.PALE_OAK_PLANKS, growthcraft.cellar.config.Reference.UnlocalizedName.Block.FERMENT_BARREL_PALE_OAK);
        addFermentationBarrelRecipe(output, ironIngots, GrowthcraftCellarItems.FERMENTATION_BARREL_SPRUCE.get(),
                Blocks.SPRUCE_PLANKS, growthcraft.cellar.config.Reference.UnlocalizedName.Block.FERMENT_BARREL_SPRUCE);
        addFermentationBarrelRecipe(output, ironIngots, GrowthcraftCellarItems.FERMENTATION_BARREL_WARPED.get(),
                Blocks.WARPED_PLANKS, growthcraft.cellar.config.Reference.UnlocalizedName.Block.FERMENT_BARREL_WARPED);

        shaped(RecipeCategory.MISC, GrowthcraftCellarItems.LARGE_FERMENTATION_BARREL_OAK.get())
                .pattern("BBB")
                .pattern("B B")
                .pattern("BBB")
                .define('B', GrowthcraftCellarItems.FERMENTATION_BARREL_OAK.get())
                .group(growthcraft.cellar.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftCellarItems.FERMENTATION_BARREL_OAK.get()), has(GrowthcraftCellarItems.FERMENTATION_BARREL_OAK.get()))
                .save(output, recipeKey(growthcraft.cellar.config.Reference.MODID,
                        growthcraft.cellar.config.Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_OAK));

        shaped(RecipeCategory.MISC, GrowthcraftCellarItems.FRUIT_PRESS.get())
                .pattern("ABA")
                .pattern("CCC")
                .pattern("DDD")
                .define('A', Tags.Items.FENCES)
                .define('B', Blocks.PISTON)
                .define('C', ironIngots)
                .define('D', ItemTags.PLANKS)
                .group(growthcraft.cellar.config.Reference.MODID)
                .unlockedBy(getHasName(Items.PISTON), has(Items.PISTON))
                .save(output, recipeKey(
                        growthcraft.cellar.config.Reference.MODID,
                        growthcraft.cellar.config.Reference.UnlocalizedName.Block.FRUIT_PRESS));

        shaped(RecipeCategory.MISC, GrowthcraftCellarItems.ROASTER.get())
                .pattern(" I ")
                .pattern(" B ")
                .pattern("I I")
                .define('I', ironIngots)
                .define('B', GrowthcraftCellarItems.BREW_KETTLE.get())
                .group(growthcraft.cellar.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftCellarItems.BREW_KETTLE.get()), has(GrowthcraftCellarItems.BREW_KETTLE.get()))
                .save(output, recipeKey(
                        growthcraft.cellar.config.Reference.MODID,
                        growthcraft.cellar.config.Reference.UnlocalizedName.Block.ROASTER));
    }

    private void addFermentationBarrelRecipe(RecipeOutput output, TagKey<Item> ironIngots,
                                             ItemLike barrel, ItemLike planks, String recipeName) {
        shaped(RecipeCategory.MISC, barrel)
                .pattern("AAA")
                .pattern("BBB")
                .pattern("AAA")
                .define('A', ironIngots)
                .define('B', planks)
                .group(growthcraft.cellar.config.Reference.MODID)
                .unlockedBy(getHasName(planks), has(planks))
                .save(output, recipeKey(
                        growthcraft.cellar.config.Reference.MODID,
                        recipeName));
    }

    private void addMilkMachineRecipes(RecipeOutput output, TagKey<Item> ironIngots) {
        shaped(RecipeCategory.MISC, GrowthcraftMilkItems.CHEESE_PRESS.get())
                .pattern("III")
                .pattern("ICI")
                .pattern("SSS")
                .define('I', ironIngots)
                .define('C', Tags.Items.CHESTS_WOODEN)
                .define('S', ItemTags.WOODEN_SLABS)
                .group(growthcraft.milk.config.Reference.MODID)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output, recipeKey(
                        growthcraft.milk.config.Reference.MODID,
                        growthcraft.milk.config.Reference.UnlocalizedName.CHEESE_PRESS));

        shaped(RecipeCategory.MISC, GrowthcraftMilkItems.CHURN.get())
                .pattern(" S ")
                .pattern("P P")
                .pattern("PPP")
                .define('P', ItemTags.PLANKS)
                .define('S', Tags.Items.RODS_WOODEN)
                .group(growthcraft.milk.config.Reference.MODID)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(output, recipeKey(
                        growthcraft.milk.config.Reference.MODID,
                        growthcraft.milk.config.Reference.UnlocalizedName.CHURN));

        shaped(RecipeCategory.MISC, GrowthcraftMilkItems.MIXING_VAT.get())
                .pattern("   ")
                .pattern(" B ")
                .pattern("I I")
                .define('B', GrowthcraftCellarItems.BREW_KETTLE.get())
                .define('I', ironIngots)
                .group(growthcraft.milk.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftCellarItems.BREW_KETTLE.get()), has(GrowthcraftCellarItems.BREW_KETTLE.get()))
                .save(output, recipeKey(
                        growthcraft.milk.config.Reference.MODID,
                        growthcraft.milk.config.Reference.UnlocalizedName.MIXING_VAT));

        shaped(RecipeCategory.MISC, GrowthcraftMilkItems.PANCHEON.get())
                .pattern("C C")
                .pattern("CCC")
                .define('C', Items.CLAY_BALL)
                .group(growthcraft.milk.config.Reference.MODID)
                .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .save(output, recipeKey(
                        growthcraft.milk.config.Reference.MODID,
                        growthcraft.milk.config.Reference.UnlocalizedName.PANCHEON));
    }

    private void addAppleWoodRecipes(RecipeOutput output) {
        shapeless(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesItems.APPLE_PLANK.get(), 4)
                .requires(GrowthcraftApplesItems.APPLE_WOOD_LOG.get())
                .group(growthcraft.apples.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftApplesItems.APPLE_WOOD_LOG.get()), has(GrowthcraftApplesItems.APPLE_WOOD_LOG.get()))
                .save(output, recipeKey(growthcraft.apples.config.Reference.MODID, growthcraft.apples.config.Reference.UnlocalizedName.Block.APPLE_PLANK));

        shapeless(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesItems.APPLE_PLANK_BUTTON.get())
                .requires(GrowthcraftApplesItems.APPLE_PLANK.get())
                .group("wooden_button")
                .unlockedBy(getHasName(GrowthcraftApplesItems.APPLE_PLANK.get()), has(GrowthcraftApplesItems.APPLE_PLANK.get()))
                .save(output, recipeKey(growthcraft.apples.config.Reference.MODID, growthcraft.apples.config.Reference.UnlocalizedName.Block.APPLE_PLANK_BUTTON));

        shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesItems.APPLE_PLANK_DOOR.get(), 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', GrowthcraftApplesItems.APPLE_PLANK.get())
                .group(growthcraft.apples.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftApplesItems.APPLE_PLANK.get()), has(GrowthcraftApplesItems.APPLE_PLANK.get()))
                .save(output, recipeKey(growthcraft.apples.config.Reference.MODID, growthcraft.apples.config.Reference.UnlocalizedName.Block.APPLE_PLANK_DOOR));

        shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesItems.APPLE_PLANK_FENCE.get(), 3)
                .pattern("212")
                .pattern("212")
                .define('1', Items.STICK)
                .define('2', GrowthcraftApplesItems.APPLE_PLANK.get())
                .group(growthcraft.apples.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftApplesItems.APPLE_PLANK.get()), has(GrowthcraftApplesItems.APPLE_PLANK.get()))
                .save(output, recipeKey(growthcraft.apples.config.Reference.MODID, growthcraft.apples.config.Reference.UnlocalizedName.Block.APPLE_PLANK_FENCE));

        shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesItems.APPLE_PLANK_FENCE_GATE.get(), 3)
                .pattern("121")
                .pattern("121")
                .define('1', Items.STICK)
                .define('2', GrowthcraftApplesItems.APPLE_PLANK.get())
                .group(growthcraft.apples.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftApplesItems.APPLE_PLANK.get()), has(GrowthcraftApplesItems.APPLE_PLANK.get()))
                .save(output, recipeKey(growthcraft.apples.config.Reference.MODID, growthcraft.apples.config.Reference.UnlocalizedName.Block.APPLE_PLANK_FENCE_GATE));

        shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesItems.APPLE_PLANK_PRESSURE_PLATE.get())
                .pattern("##")
                .define('#', GrowthcraftApplesItems.APPLE_PLANK.get())
                .group(growthcraft.apples.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftApplesItems.APPLE_PLANK.get()), has(GrowthcraftApplesItems.APPLE_PLANK.get()))
                .save(output, recipeKey(growthcraft.apples.config.Reference.MODID, growthcraft.apples.config.Reference.UnlocalizedName.Block.APPLE_PLANK_PRESSURE_PLATE));

        shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesItems.APPLE_PLANK_SLAB.get(), 6)
                .pattern("###")
                .define('#', GrowthcraftApplesItems.APPLE_PLANK.get())
                .group(growthcraft.apples.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftApplesItems.APPLE_PLANK.get()), has(GrowthcraftApplesItems.APPLE_PLANK.get()))
                .save(output, recipeKey(growthcraft.apples.config.Reference.MODID, growthcraft.apples.config.Reference.UnlocalizedName.Block.APPLE_PLANK_SLAB));

        shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesItems.APPLE_PLANK_STAIRS.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', GrowthcraftApplesItems.APPLE_PLANK.get())
                .group(growthcraft.apples.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftApplesItems.APPLE_PLANK.get()), has(GrowthcraftApplesItems.APPLE_PLANK.get()))
                .save(output, recipeKey(growthcraft.apples.config.Reference.MODID, growthcraft.apples.config.Reference.UnlocalizedName.Block.APPLE_PLANK_STAIRS));

        shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesItems.APPLE_PLANK_TRAPDOOR.get())
                .pattern("###")
                .pattern("###")
                .define('#', GrowthcraftApplesItems.APPLE_PLANK.get())
                .group(growthcraft.apples.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftApplesItems.APPLE_PLANK.get()), has(GrowthcraftApplesItems.APPLE_PLANK.get()))
                .save(output, recipeKey(growthcraft.apples.config.Reference.MODID, growthcraft.apples.config.Reference.UnlocalizedName.Block.APPLE_PLANK_TRAPDOOR));

        shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesItems.APPLE_WOOD.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', GrowthcraftApplesItems.APPLE_WOOD_LOG.get())
                .group(growthcraft.apples.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftApplesItems.APPLE_WOOD_LOG.get()), has(GrowthcraftApplesItems.APPLE_WOOD_LOG.get()))
                .save(output, recipeKey(growthcraft.apples.config.Reference.MODID, growthcraft.apples.config.Reference.UnlocalizedName.Block.APPLE_WOOD));

        shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftApplesItems.APPLE_WOOD_STRIPPED.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', GrowthcraftApplesItems.APPLE_WOOD_LOG_STRIPPED.get())
                .group(growthcraft.apples.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftApplesItems.APPLE_WOOD_LOG_STRIPPED.get()), has(GrowthcraftApplesItems.APPLE_WOOD_LOG_STRIPPED.get()))
                .save(output, recipeKey(growthcraft.apples.config.Reference.MODID, growthcraft.apples.config.Reference.UnlocalizedName.Block.APPLE_WOOD_STRIPPED));

        shapeless(RecipeCategory.FOOD, GrowthcraftApplesItems.APPLE_SEEDS.get())
                .requires(Items.APPLE)
                .group(growthcraft.apples.config.Reference.MODID)
                .unlockedBy(getHasName(Items.APPLE), has(Items.APPLE))
                .save(output, recipeKey(growthcraft.apples.config.Reference.MODID, growthcraft.apples.config.Reference.UnlocalizedName.Item.APPLE_SEEDS));
    }

    private void addApiaryBeeswaxRecipes(RecipeOutput output) {
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_BLACK.get(), Tags.Items.DYES_BLACK, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_BLACK);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_BLUE.get(), Tags.Items.DYES_BLUE, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_BLUE);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_BROWN.get(), Tags.Items.DYES_BROWN, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_BROWN);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_CYAN.get(), Tags.Items.DYES_CYAN, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_CYAN);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_GRAY.get(), Tags.Items.DYES_GRAY, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_GRAY);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_GREEN.get(), Tags.Items.DYES_GREEN, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_GREEN);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_LIGHT_BLUE.get(), Tags.Items.DYES_LIGHT_BLUE, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_LIGHT_BLUE);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_LIGHT_GRAY.get(), Tags.Items.DYES_LIGHT_GRAY, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_LIGHT_GRAY);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_LIME.get(), Tags.Items.DYES_LIME, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_LIME);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_MAGENTA.get(), Tags.Items.DYES_MAGENTA, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_MAGENTA);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_ORANGE.get(), Tags.Items.DYES_ORANGE, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_ORANGE);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_PINK.get(), Tags.Items.DYES_PINK, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_PINK);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_PURPLE.get(), Tags.Items.DYES_PURPLE, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_PURPLE);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_RED.get(), Tags.Items.DYES_RED, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_RED);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_WHITE.get(), Tags.Items.DYES_WHITE, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_WHITE);
        addBeeswaxDyeRecipe(output, GrowthcraftApiaryItems.BEES_WAX_YELLOW.get(), Tags.Items.DYES_YELLOW, growthcraft.apiary.config.Reference.UnlocalizedName.BEES_WAX_YELLOW);
    }

    private void addBeeswaxDyeRecipe(RecipeOutput output, Item result, TagKey<Item> dye, String name) {
        shaped(RecipeCategory.MISC, result, 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', GrowthcraftApiaryItems.BEES_WAX.get())
                .define('B', dye)
                .group(growthcraft.apiary.config.Reference.MODID)
                .unlockedBy(getHasName(GrowthcraftApiaryItems.BEES_WAX.get()), has(GrowthcraftApiaryItems.BEES_WAX.get()))
                .save(output, recipeKey(growthcraft.apiary.config.Reference.MODID, name));
    }

    private void addApiaryCandleRecipes(RecipeOutput output) {
        shaped(RecipeCategory.DECORATIONS, Items.CANDLE)
                .pattern("S")
                .pattern("H")
                .define('S', Items.STRING)
                .define('H', GrowthcraftApiaryTags.Items.HONEY_COMB)
                .group(growthcraft.apiary.config.Reference.MODID)
                .unlockedBy("has_honey_comb", has(GrowthcraftApiaryTags.Items.HONEY_COMB))
                .save(output, recipeKey("minecraft", "candle"));

        addCandleDyeRecipe(output, Items.BLACK_CANDLE, Tags.Items.DYES_BLACK, "black_candle");
        addCandleDyeRecipe(output, Items.BLUE_CANDLE, Tags.Items.DYES_BLUE, "blue_candle");
        addCandleDyeRecipe(output, Items.BROWN_CANDLE, Tags.Items.DYES_BROWN, "brown_candle");
        addCandleDyeRecipe(output, Items.CYAN_CANDLE, Tags.Items.DYES_CYAN, "cyan_candle");
        addCandleDyeRecipe(output, Items.GRAY_CANDLE, Tags.Items.DYES_GRAY, "gray_candle");
        addCandleDyeRecipe(output, Items.GREEN_CANDLE, Tags.Items.DYES_GREEN, "green_candle");
        addCandleDyeRecipe(output, Items.LIGHT_BLUE_CANDLE, Tags.Items.DYES_LIGHT_BLUE, "light_blue_candle");
        addCandleDyeRecipe(output, Items.LIGHT_GRAY_CANDLE, Tags.Items.DYES_LIGHT_GRAY, "light_gray_candle");
        addCandleDyeRecipe(output, Items.LIME_CANDLE, Tags.Items.DYES_LIME, "lime_candle");
        addCandleDyeRecipe(output, Items.MAGENTA_CANDLE, Tags.Items.DYES_MAGENTA, "magenta_candle");
        addCandleDyeRecipe(output, Items.ORANGE_CANDLE, Tags.Items.DYES_ORANGE, "orange_candle");
        addCandleDyeRecipe(output, Items.PINK_CANDLE, Tags.Items.DYES_PINK, "pink_candle");
        addCandleDyeRecipe(output, Items.PURPLE_CANDLE, Tags.Items.DYES_PURPLE, "purple_candle");
        addCandleDyeRecipe(output, Items.RED_CANDLE, Tags.Items.DYES_RED, "red_candle");
        addCandleDyeRecipe(output, Items.WHITE_CANDLE, Tags.Items.DYES_WHITE, "white_candle");
        addCandleDyeRecipe(output, Items.YELLOW_CANDLE, Tags.Items.DYES_YELLOW, "yellow_candle");
    }

    private void addCandleDyeRecipe(RecipeOutput output, Item result, TagKey<Item> dye, String name) {
        shapeless(RecipeCategory.DECORATIONS, result)
                .requires(Items.CANDLE)
                .requires(dye)
                .group("dyed_candle")
                .unlockedBy(getHasName(Items.CANDLE), has(Items.CANDLE))
                .save(output, recipeKey("minecraft", name));
    }

    private void addMilkIngredientRecipes(RecipeOutput output) {
        TagKey<Item> salt = itemTag("c", "dusts/salt");

        shapeless(RecipeCategory.MISC, GrowthcraftMilkItems.THISTLE_SEED.get(), 2)
                .requires(GrowthcraftMilkItems.THISTLE.get())
                .group("growthcraft_milk")
                .unlockedBy(getHasName(GrowthcraftMilkItems.THISTLE.get()), has(GrowthcraftMilkItems.THISTLE.get()))
                .save(output, recipeKey(growthcraft.milk.config.Reference.MODID, growthcraft.milk.config.Reference.UnlocalizedName.THISTLE_SEED));

        shaped(RecipeCategory.MISC, GrowthcraftMilkItems.CHEESE_CLOTH.get())
                .pattern("sss")
                .pattern("s s")
                .pattern("sss")
                .define('s', Items.STRING)
                .group("growthcraft_milk")
                .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                .save(output, recipeKey(growthcraft.milk.config.Reference.MODID, growthcraft.milk.config.Reference.UnlocalizedName.CHEESE_CLOTH));

        shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.BUTTER_SALTED.get())
                .requires(GrowthcraftMilkItems.BUTTER.get())
                .requires(salt)
                .group("growthcraft_milk")
                .unlockedBy(getHasName(GrowthcraftMilkItems.BUTTER.get()), has(GrowthcraftMilkItems.BUTTER.get()))
                .unlockedBy("has_salt", has(salt))
                .save(output, recipeKey(growthcraft.milk.config.Reference.MODID, growthcraft.milk.config.Reference.UnlocalizedName.BUTTER_SALTED));
    }

    private void addMilkBakingRecipes(RecipeOutput output) {
        TagKey<Item> butter = itemTag("c", "butter");

        shaped(RecipeCategory.FOOD, Items.CAKE)
                .pattern("MBM")
                .pattern("SES")
                .pattern("WWW")
                .define('M', GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
                .define('B', butter)
                .define('S', Items.SUGAR)
                .define('E', Items.EGG)
                .define('W', Items.WHEAT)
                .group("growthcraft_milk")
                .unlockedBy("has_butter", has(butter))
                .unlockedBy("has_milk_bucket", has(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS))
                .save(output, recipeKey(growthcraft.milk.config.Reference.MODID, "butter_cake"));

        shaped(RecipeCategory.FOOD, Items.CAKE)
                .pattern("CBC")
                .pattern(" E ")
                .pattern("WWW")
                .define('C', GrowthcraftMilkFluids.CONDENSED_MILK.bucket.get())
                .define('B', butter)
                .define('E', Items.EGG)
                .define('W', Items.WHEAT)
                .group("growthcraft_milk")
                .unlockedBy("has_condensed_milk_bucket", has(GrowthcraftMilkFluids.CONDENSED_MILK.bucket.get()))
                .save(output, recipeKey(growthcraft.milk.config.Reference.MODID, "condensed_milk_butter_cake"));

        shaped(RecipeCategory.FOOD, Items.COOKIE, 12)
                .pattern("WBW")
                .pattern(" C ")
                .define('W', Items.WHEAT)
                .define('B', butter)
                .define('C', Items.COCOA_BEANS)
                .group("growthcraft_milk")
                .unlockedBy("has_butter", has(butter))
                .unlockedBy(getHasName(Items.COCOA_BEANS), has(Items.COCOA_BEANS))
                .save(output, recipeKey(growthcraft.milk.config.Reference.MODID, "butter_cookies"));

        shapeless(RecipeCategory.FOOD, Items.COOKIE, 24)
                .requires(Items.WHEAT, 2)
                .requires(butter)
                .requires(Items.COCOA_BEANS)
                .requires(GrowthcraftMilkFluids.CONDENSED_MILK.bucket.get())
                .group("growthcraft_milk")
                .unlockedBy("has_condensed_milk_bucket", has(GrowthcraftMilkFluids.CONDENSED_MILK.bucket.get()))
                .unlockedBy("has_butter", has(butter))
                .unlockedBy(getHasName(Items.COCOA_BEANS), has(Items.COCOA_BEANS))
                .save(output, recipeKey(growthcraft.milk.config.Reference.MODID, "condensed_milk_butter_cookies"));
    }

    private void addMilkBowlFoodRecipes(RecipeOutput output) {
        addIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_APPLE.get(), Items.APPLE, growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_APPLE);
        addIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_CHOCOLATE.get(), Items.COCOA_BEANS, growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_CHOCOLATE);
        addIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_GRAPE_PURPLE.get(), GrowthcraftCellarItems.GRAPE_PURPLE.get(), growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_GRAPE_PURPLE);
        addIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_GRAPE_RED.get(), GrowthcraftCellarItems.GRAPE_RED.get(), growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_GRAPE_RED);
        addIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_GRAPE_WHITE.get(), GrowthcraftCellarItems.GRAPE_WHITE.get(), growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_GRAPE_WHITE);
        addIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_HONEY.get(), Items.HONEYCOMB, growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_HONEY);
        addIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_PUMPKIN.get(), Items.PUMPKIN, growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_PUMPKIN);
        addIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_WATERMELON.get(), Items.MELON_SLICE, growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_WATERMELON);

        addCondensedMilkIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_APPLE.get(), Items.APPLE, growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_APPLE);
        addCondensedMilkIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_CHOCOLATE.get(), Items.COCOA_BEANS, growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_CHOCOLATE);
        addCondensedMilkIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_GRAPE_PURPLE.get(), GrowthcraftCellarItems.GRAPE_PURPLE.get(), growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_GRAPE_PURPLE);
        addCondensedMilkIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_GRAPE_RED.get(), GrowthcraftCellarItems.GRAPE_RED.get(), growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_GRAPE_RED);
        addCondensedMilkIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_GRAPE_WHITE.get(), GrowthcraftCellarItems.GRAPE_WHITE.get(), growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_GRAPE_WHITE);
        addCondensedMilkIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_HONEY.get(), Items.HONEYCOMB, growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_HONEY);
        addCondensedMilkIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_PUMPKIN.get(), Items.PUMPKIN, growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_PUMPKIN);
        addCondensedMilkIceCreamRecipe(output, GrowthcraftMilkItems.ICE_CREAM_WATERMELON.get(), Items.MELON_SLICE, growthcraft.milk.config.Reference.UnlocalizedName.ICE_CREAM_WATERMELON);

        addYogurtRecipe(output, GrowthcraftMilkItems.YOGURT_APPLE.get(), Items.APPLE, growthcraft.milk.config.Reference.UnlocalizedName.YOGURT_APPLE);
        addYogurtRecipe(output, GrowthcraftMilkItems.YOGURT_CHOCOLATE.get(), Items.COCOA_BEANS, growthcraft.milk.config.Reference.UnlocalizedName.YOGURT_CHOCOLATE);
        addYogurtRecipe(output, GrowthcraftMilkItems.YOGURT_GRAPE_PURPLE.get(), GrowthcraftCellarItems.GRAPE_PURPLE.get(), growthcraft.milk.config.Reference.UnlocalizedName.YOGURT_GRAPE_PURPLE);
        addYogurtRecipe(output, GrowthcraftMilkItems.YOGURT_GRAPE_RED.get(), GrowthcraftCellarItems.GRAPE_RED.get(), growthcraft.milk.config.Reference.UnlocalizedName.YOGURT_GRAPE_RED);
        addYogurtRecipe(output, GrowthcraftMilkItems.YOGURT_GRAPE_WHITE.get(), GrowthcraftCellarItems.GRAPE_WHITE.get(), growthcraft.milk.config.Reference.UnlocalizedName.YOGURT_GRAPE_WHITE);
        addYogurtRecipe(output, GrowthcraftMilkItems.YOGURT_HONEY.get(), Items.HONEYCOMB, growthcraft.milk.config.Reference.UnlocalizedName.YOGURT_HONEY);
        addYogurtRecipe(output, GrowthcraftMilkItems.YOGURT_PLAIN.get(), null, growthcraft.milk.config.Reference.UnlocalizedName.YOGURT_PLAIN);
        addYogurtRecipe(output, GrowthcraftMilkItems.YOGURT_PUMPKIN.get(), Items.PUMPKIN, growthcraft.milk.config.Reference.UnlocalizedName.YOGURT_PUMPKIN);
        addYogurtRecipe(output, GrowthcraftMilkItems.YOGURT_WATERMELON.get(), Items.MELON_SLICE, growthcraft.milk.config.Reference.UnlocalizedName.YOGURT_WATERMELON);

        shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.RICOTTA_CHEESE_SLICE.get())
                .requires(Items.BOWL)
                .requires(GrowthcraftMilkItems.RICOTTA_CHEESE_CURDS_DRAINED.get())
                .requires(GrowthcraftMilkItems.RICOTTA_CHEESE_CURDS_DRAINED.get())
                .group("growthcraft_milk")
                .unlockedBy(getHasName(GrowthcraftMilkItems.RICOTTA_CHEESE_CURDS_DRAINED.get()),
                        has(GrowthcraftMilkItems.RICOTTA_CHEESE_CURDS_DRAINED.get()))
                .save(output, recipeKey(
                        growthcraft.milk.config.Reference.MODID,
                        growthcraft.milk.config.Reference.UnlocalizedName.RICOTTA + "_cheese_slice"));
    }

    private void addIceCreamRecipe(RecipeOutput output, Item result, Item flavor, String name) {
        shapeless(RecipeCategory.FOOD, result)
                .requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
                .requires(flavor)
                .requires(Items.SUGAR)
                .requires(Items.BOWL)
                .group("growthcraft_milk")
                .unlockedBy("has_milk_bucket", has(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS))
                .save(output, recipeKey(growthcraft.milk.config.Reference.MODID, name));
    }

    private void addCondensedMilkIceCreamRecipe(RecipeOutput output, Item result, Item flavor, String name) {
        shapeless(RecipeCategory.FOOD, result)
                .requires(GrowthcraftMilkFluids.CONDENSED_MILK.bucket.get())
                .requires(flavor)
                .requires(Items.BOWL)
                .group("growthcraft_milk")
                .unlockedBy("has_condensed_milk_bucket", has(GrowthcraftMilkFluids.CONDENSED_MILK.bucket.get()))
                .save(output, recipeKey(growthcraft.milk.config.Reference.MODID, name + "_from_condensed_milk"));
    }

    private void addYogurtRecipe(RecipeOutput output, Item result, Item flavor, String name) {
        ShapelessRecipeBuilder builder = shapeless(RecipeCategory.FOOD, result)
                .requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
                .requires(GrowthcraftMilkItems.STARTER_CULTURE.get())
                .requires(Items.BOWL)
                .group("growthcraft_milk")
                .unlockedBy("has_milk_bucket", has(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS));
        if (flavor != null) {
            builder.requires(flavor);
        }
        builder.save(output, recipeKey(growthcraft.milk.config.Reference.MODID, name));
    }

    private void addCrowbarRecipe(RecipeOutput output, Item result, Item carpet, TagKey<Item> nuggets, TagKey<Item> ingots, String name) {
        shaped(RecipeCategory.TOOLS, result)
                .pattern("  A")
                .pattern("CBC")
                .pattern("A  ")
                .define('A', nuggets)
                .define('B', ingots)
                .define('C', carpet)
                .unlockedBy("has_iron_ingots", has(ingots))
                .unlockedBy("has_" + name + "_carpet", has(carpet))
                .save(output, recipeKey(Reference.MODID, name));
    }

    private void addCellarGrainRecipes(RecipeOutput output) {
        TagKey<Item> barley = itemTag("c", "grain/barley");
        TagKey<Item> basicAdjunctGrains = itemTag(growthcraft.cellar.config.Reference.MODID, "adjunct_grains_basic");
        TagKey<Item> extendedAdjunctGrains = itemTag(growthcraft.cellar.config.Reference.MODID, "adjunct_grains_extended");
        TagKey<Item> extendedAdjunctGrainsMinusWheat = itemTag(growthcraft.cellar.config.Reference.MODID, "adjunct_grains_extended_minus_wheat");

        ICondition barleyMissing = new TagEmptyCondition(barley);
        ICondition barleyPresent = new NotCondition(barleyMissing);
        ICondition basicAdjunctsPresent = new NotCondition(new TagEmptyCondition(basicAdjunctGrains));
        ICondition extendedAdjunctsPresent = new NotCondition(new TagEmptyCondition(extendedAdjunctGrains));
        ICondition extendedMinusWheatPresent = new NotCondition(new TagEmptyCondition(extendedAdjunctGrainsMinusWheat));
        ICondition additionalAdjunctsEnabled = new ConfigValueCondition("cellar", "brewing.allow_additional_adjunct_grains");
        ICondition additionalAdjunctsDisabled = new NotCondition(additionalAdjunctsEnabled);

        shapelessGrain(output.withConditions(barleyMissing), "grain", Items.WHEAT, Items.WHEAT, Items.WHEAT, Items.WHEAT);
        shapelessGrain(output.withConditions(barleyPresent), "grain_2", Items.WHEAT, Items.WHEAT, Items.WHEAT, Items.WHEAT, Items.WHEAT);
        shapelessGrain(output.withConditions(barleyPresent), "grain_3", barley, barley, barley);
        shapelessGrain(output.withConditions(barleyPresent, additionalAdjunctsDisabled, basicAdjunctsPresent), "grain_4", barley, barley, basicAdjunctGrains, basicAdjunctGrains);
        shapelessGrain(output.withConditions(barleyPresent, additionalAdjunctsEnabled, extendedAdjunctsPresent), "grain_5", barley, barley, extendedAdjunctGrains, extendedAdjunctGrains);
        shapelessGrain(output.withConditions(barleyMissing, additionalAdjunctsDisabled, basicAdjunctsPresent), "grain_6", Items.WHEAT, Items.WHEAT, Items.WHEAT, basicAdjunctGrains, basicAdjunctGrains);
        shapelessGrain(output.withConditions(barleyMissing, additionalAdjunctsEnabled, extendedMinusWheatPresent), "grain_7", Items.WHEAT, Items.WHEAT, Items.WHEAT, extendedAdjunctGrainsMinusWheat, extendedAdjunctGrainsMinusWheat);
    }

    private void shapelessGrain(RecipeOutput output, String recipeName, Object... ingredients) {
        ShapelessRecipeBuilder builder = shapeless(RecipeCategory.MISC, GrowthcraftCellarItems.GRAIN.get(), 3)
                .group("growthcraft_cellar")
                .unlockedBy(getHasName(Items.WHEAT), has(Items.WHEAT));

        for (Object ingredient : ingredients) {
            if (ingredient instanceof Item item) {
                builder.requires(item);
            } else if (ingredient instanceof TagKey<?> tag) {
                @SuppressWarnings("unchecked")
                TagKey<Item> itemTag = (TagKey<Item>) tag;
                builder.requires(itemTag);
            }
        }

        builder.save(output, recipeKey(growthcraft.cellar.config.Reference.MODID, recipeName));
    }

    private static TagKey<Item> itemTag(String namespace, String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(namespace, path));
    }
    private static ResourceKey<Recipe<?>> recipeKey(String namespace, String path) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(namespace, path));
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new GrowthcraftRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Growthcraft Recipes";
        }
    }

}
