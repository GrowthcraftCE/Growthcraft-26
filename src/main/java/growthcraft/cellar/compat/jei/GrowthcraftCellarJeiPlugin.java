package growthcraft.cellar.compat.jei;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.recipe.BrewKettleRecipe;
import growthcraft.cellar.recipe.CultureJarRecipe;
import growthcraft.cellar.recipe.FermentationBarrelRecipe;
import growthcraft.cellar.recipe.FruitPressRecipe;
import growthcraft.cellar.recipe.RoasterRecipe;
import growthcraft.lib.recipe.RecipeLookup;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

@JeiPlugin
public class GrowthcraftCellarJeiPlugin implements IModPlugin {
    public static final Identifier PLUGIN_UID = Identifier.fromNamespaceAndPath(Reference.MODID, "jei_plugin");
    public static final RecipeType<RecipeHolder<BrewKettleRecipe>> BREW_KETTLE =
            new RecipeType<>(Identifier.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Recipe.BREW_KETTLE_RECIPE), GrowthcraftCellarJeiPlugin.<BrewKettleRecipe>recipeHolderClass());
    public static final RecipeType<RecipeHolder<CultureJarRecipe>> CULTURE_JAR =
            new RecipeType<>(Identifier.fromNamespaceAndPath(Reference.MODID, "culture_jar"), GrowthcraftCellarJeiPlugin.<CultureJarRecipe>recipeHolderClass());
    public static final RecipeType<RecipeHolder<FermentationBarrelRecipe>> FERMENTATION_BARREL =
            new RecipeType<>(Identifier.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Recipe.FERMENT_BARREL_RECIPE), GrowthcraftCellarJeiPlugin.<FermentationBarrelRecipe>recipeHolderClass());
    public static final RecipeType<RecipeHolder<FruitPressRecipe>> FRUIT_PRESS =
            new RecipeType<>(Identifier.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Recipe.FRUIT_PRESS_RECIPE), GrowthcraftCellarJeiPlugin.<FruitPressRecipe>recipeHolderClass());
    public static final RecipeType<RecipeHolder<RoasterRecipe>> ROASTER =
            new RecipeType<>(Identifier.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Recipe.ROASTER_RECIPE), GrowthcraftCellarJeiPlugin.<RoasterRecipe>recipeHolderClass());

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
                new BrewKettleRecipeCategory(guiHelper),
                new CultureJarRecipeCategory(guiHelper),
                new FermentationBarrelRecipeCategory(guiHelper),
                new FruitPressRecipeCategory(guiHelper),
                new RoasterRecipeCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return;

        registration.addRecipes(BREW_KETTLE, brewKettleRecipes(minecraft));
        registration.addRecipes(CULTURE_JAR, cultureJarRecipes(minecraft));
        registration.addRecipes(FERMENTATION_BARREL, fermentationBarrelRecipes(minecraft));
        registration.addRecipes(FRUIT_PRESS, fruitPressRecipes(minecraft));
        registration.addRecipes(ROASTER, roasterRecipes(minecraft));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftCellarItems.BREW_KETTLE.get()), BREW_KETTLE);
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftCellarItems.CULTURE_JAR.get()), CULTURE_JAR);
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftCellarItems.FERMENTATION_BARREL_OAK.get()), FERMENTATION_BARREL);
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftCellarItems.FRUIT_PRESS.get()), FRUIT_PRESS);
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftCellarItems.ROASTER.get()), ROASTER);
    }

    private static java.util.List<RecipeHolder<BrewKettleRecipe>> brewKettleRecipes(Minecraft minecraft) {
        return RecipeLookup.getAll(minecraft.level, GrowthcraftCellarRecipes.BREW_KETTLE_TYPE.get());
    }

    private static java.util.List<RecipeHolder<CultureJarRecipe>> cultureJarRecipes(Minecraft minecraft) {
        return RecipeLookup.getAll(minecraft.level, GrowthcraftCellarRecipes.CULTURE_JAR_TYPE.get());
    }

    private static java.util.List<RecipeHolder<FermentationBarrelRecipe>> fermentationBarrelRecipes(Minecraft minecraft) {
        return RecipeLookup.getAll(minecraft.level, GrowthcraftCellarRecipes.FERMENTATION_BARREL_TYPE.get());
    }

    private static java.util.List<RecipeHolder<FruitPressRecipe>> fruitPressRecipes(Minecraft minecraft) {
        return RecipeLookup.getAll(minecraft.level, GrowthcraftCellarRecipes.FRUIT_PRESS_TYPE.get());
    }

    private static java.util.List<RecipeHolder<RoasterRecipe>> roasterRecipes(Minecraft minecraft) {
        return RecipeLookup.getAll(minecraft.level, GrowthcraftCellarRecipes.ROASTER_TYPE.get());
    }
}
