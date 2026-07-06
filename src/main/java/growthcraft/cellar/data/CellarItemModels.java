package growthcraft.cellar.data;

import com.google.gson.JsonObject;
import growthcraft.cellar.config.Reference;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the small set of Cellar item models that are color-tinted or layered.
 */
public class CellarItemModels implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public CellarItemModels(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        generatedWithTexture(futures, output, Reference.UnlocalizedName.Item.GRAIN, rlTex("item/grain_base"));
        generatedWithTexture(futures, output, Reference.UnlocalizedName.Item.GRAIN_AMBER, rlTex("item/grain_base"));
        generatedWithTexture(futures, output, Reference.UnlocalizedName.Item.GRAIN_BROWN, rlTex("item/grain_base"));
        generatedWithTexture(futures, output, Reference.UnlocalizedName.Item.GRAIN_COPPER, rlTex("item/grain_base"));
        generatedWithTexture(futures, output, Reference.UnlocalizedName.Item.GRAIN_DARK, rlTex("item/grain_base"));
        generatedWithTexture(futures, output, Reference.UnlocalizedName.Item.GRAIN_DEEP_AMBER, rlTex("item/grain_base"));
        generatedWithTexture(futures, output, Reference.UnlocalizedName.Item.GRAIN_DEEP_COPPER, rlTex("item/grain_base"));
        generatedWithTexture(futures, output, Reference.UnlocalizedName.Item.GRAIN_GOLDEN, rlTex("item/grain_base"));
        generatedWithTexture(futures, output, Reference.UnlocalizedName.Item.GRAIN_PALE_GOLDEN, rlTex("item/grain_base"));

        generatedWithTexture(futures, output, "starter_culture", Identifier.fromNamespaceAndPath("growthcraft_milk", "item/starter_culture"));

        bucket(futures, output, Reference.UnlocalizedName.Item.AMBER_ALE_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.AMBER_LAGER_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.AMBER_WORT_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.BROWN_ALE_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.BROWN_LAGER_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.BROWN_WORT_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.COPPER_ALE_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.COPPER_LAGER_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.COPPER_WORT_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.DARK_LAGER_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.DARK_WORT_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.DEEP_AMBER_WORT_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.DEEP_COPPER_WORT_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.GOLDEN_WORT_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.HOPPED_GOLDEN_WORT_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.IPA_ALE_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.OLD_PORT_ALE_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.PALE_ALE_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.PALE_GOLDEN_WORT_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.PALE_LAGER_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.PILSNER_LAGER_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.PURPLE_GRAPE_JUICE_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.PURPLE_GRAPE_WINE_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.RED_GRAPE_JUICE_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.RED_GRAPE_WINE_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.STOUT_ALE_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.VIENNA_LAGER_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.WHITE_GRAPE_JUICE_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.WHITE_GRAPE_WINE_FLUID_BUCKET);
        bucket(futures, output, Reference.UnlocalizedName.Item.WORT_FLUID_BUCKET);

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Growthcraft Cellar Item Models";
    }

    private void generatedWithTexture(List<CompletableFuture<?>> futures, CachedOutput output, String name, Identifier texture) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", texture.toString());
        model.add("textures", textures);
        save(futures, output, name, model);
    }

    private void bucket(List<CompletableFuture<?>> futures, CachedOutput output, String name) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "growthcraft:item/bucket/bucket_fluid");
        textures.addProperty("layer1", "growthcraft:item/bucket/bucket_base");
        model.add("textures", textures);
        save(futures, output, name, model);
    }

    private void save(List<CompletableFuture<?>> futures, CachedOutput output, String name, JsonObject model) {
        Path path = pathProvider.json(Identifier.fromNamespaceAndPath(Reference.MODID, name));
        futures.add(DataProvider.saveStable(output, model, path));
    }

    private Identifier rlTex(String path) {
        return Identifier.fromNamespaceAndPath(Reference.MODID, path);
    }
}
