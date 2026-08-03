package growthcraft.milk.compat.jei;

import growthcraft.core.init.GrowthcraftTags;
import growthcraft.milk.config.Reference;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.init.GrowthcraftMilkRecipes;
import growthcraft.milk.recipe.CheesePressRecipe;
import growthcraft.milk.recipe.ChurnRecipe;
import growthcraft.milk.recipe.MixingVatRecipe;
import growthcraft.milk.recipe.PancheonRecipe;
import growthcraft.lib.recipe.RecipeLookup;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;

@JeiPlugin
public class GrowthcraftMilkJeiPlugin implements IModPlugin {
    public static final Identifier PLUGIN_UID = Identifier.fromNamespaceAndPath(Reference.MODID, "jei_plugin");

    public static final RecipeType<RecipeHolder<CheesePressRecipe>> CHEESE_PRESS =
            new RecipeType<>(
                    Identifier.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.CHEESE_PRESS_RECIPE),
                    GrowthcraftMilkJeiPlugin.<CheesePressRecipe>recipeHolderClass());
    public static final RecipeType<RecipeHolder<ChurnRecipe>> CHURN =
            new RecipeType<>(
                    Identifier.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.CHURN_RECIPE),
                    GrowthcraftMilkJeiPlugin.<ChurnRecipe>recipeHolderClass());
    public static final RecipeType<RecipeHolder<PancheonRecipe>> PANCHEON =
            new RecipeType<>(
                    Identifier.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.PANCHEON_RECIPE),
                    GrowthcraftMilkJeiPlugin.<PancheonRecipe>recipeHolderClass());
    public static final RecipeType<RecipeHolder<MixingVatRecipe>> MIXING_VAT =
            new RecipeType<>(
                    Identifier.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.MIXING_VAT_RECIPE),
                    GrowthcraftMilkJeiPlugin.<MixingVatRecipe>recipeHolderClass());
    public static final RecipeType<CheeseProcessRecipeCategory.Recipe> CURD_DRYING = processType("curd_drying");
    public static final RecipeType<CheeseProcessRecipeCategory.Recipe> CHEESE_AGING = processType("cheese_aging");
    public static final RecipeType<CheeseProcessRecipeCategory.Recipe> CHEESE_WAXING = processType("cheese_waxing");
    public static final RecipeType<CheeseProcessRecipeCategory.Recipe> CHEESE_CUTTING = processType("cheese_cutting");

    private static RecipeType<CheeseProcessRecipeCategory.Recipe> processType(String path) {
        return new RecipeType<>(Identifier.fromNamespaceAndPath(Reference.MODID, path), CheeseProcessRecipeCategory.Recipe.class);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T extends Recipe<?>> Class<? extends RecipeHolder<T>> recipeHolderClass() {
        return (Class) RecipeHolder.class;
    }

    @Override
    public Identifier getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new CheesePressRecipeCategory(guiHelper),
                new ChurnRecipeCategory(guiHelper),
                new MixingVatRecipeCategory(guiHelper),
                new PancheonRecipeCategory(guiHelper),
                new CheeseProcessRecipeCategory(guiHelper, CURD_DRYING, "jei.growthcraft_milk.category.curd_drying", GrowthcraftMilkItems.ASIAGO_CHEESE_CURDS.get().getDefaultInstance()),
                new CheeseProcessRecipeCategory(guiHelper, CHEESE_AGING, "jei.growthcraft_milk.category.cheese_aging", GrowthcraftMilkItems.ASIAGO_CHEESE.get().getDefaultInstance()),
                new CheeseProcessRecipeCategory(guiHelper, CHEESE_WAXING, "jei.growthcraft_milk.category.cheese_waxing", GrowthcraftMilkItems.MONTEREY_CHEESE_WAXED.get().getDefaultInstance()),
                new CheeseProcessRecipeCategory(guiHelper, CHEESE_CUTTING, "jei.growthcraft_milk.category.cheese_cutting", GrowthcraftMilkItems.PARMESAN_CHEESE_SLICE.get().getDefaultInstance())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        registration.addRecipes(CHEESE_PRESS, RecipeLookup.getAll(minecraft.level, GrowthcraftMilkRecipes.CHEESE_PRESS_TYPE.get()));
        registration.addRecipes(CHURN, RecipeLookup.getAll(minecraft.level, GrowthcraftMilkRecipes.CHURN_TYPE.get()));
        registration.addRecipes(MIXING_VAT, RecipeLookup.getAll(minecraft.level, GrowthcraftMilkRecipes.MIXING_VAT_TYPE.get()));
        registration.addRecipes(PANCHEON, RecipeLookup.getAll(minecraft.level, GrowthcraftMilkRecipes.PANCHEON_TYPE.get()));

        var drying = new ArrayList<CheeseProcessRecipeCategory.Recipe>();
        var aging = new ArrayList<CheeseProcessRecipeCategory.Recipe>();
        var waxing = new ArrayList<CheeseProcessRecipeCategory.Recipe>();
        var cutting = new ArrayList<CheeseProcessRecipeCategory.Recipe>();
        for (var cheese : GrowthcraftMilkItems.getCheeseRegistry()) {
            if (cheese.curds() != null && cheese.drainedCurds() != null) {
                drying.add(CheeseProcessRecipeCategory.Recipe.oneToOne(cheese.curds(), cheese.drainedCurds()));
            }
            if (cheese.aged() != null) {
                var agingInput = cheese.waxed() != null ? cheese.waxed() : cheese.unprocessed();
                aging.add(CheeseProcessRecipeCategory.Recipe.oneToOne(agingInput, cheese.aged()));
            }
            if (cheese.unprocessed() != null && cheese.waxingItem() != null && cheese.waxed() != null) {
                waxing.add(CheeseProcessRecipeCategory.Recipe.twoToOne(
                        cheese.unprocessed(), Ingredient.of(cheese.waxingItem()), cheese.waxed()));
            }
            if (cheese.aged() != null && cheese.slice() != null) {
                cutting.add(CheeseProcessRecipeCategory.Recipe.twoToOne(
                        cheese.aged(), Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(GrowthcraftTags.Items.CHEESE_CUTTING_TOOLS)), cheese.slice()));
            }
        }
        registration.addRecipes(CURD_DRYING, drying);
        registration.addRecipes(CHEESE_AGING, aging);
        registration.addRecipes(CHEESE_WAXING, waxing);
        registration.addRecipes(CHEESE_CUTTING, cutting);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftMilkItems.CHEESE_PRESS.get()), CHEESE_PRESS);
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftMilkItems.CHURN.get()), CHURN);
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftMilkItems.MIXING_VAT.get()), MIXING_VAT);
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftMilkItems.PANCHEON.get()), PANCHEON);
    }
}
