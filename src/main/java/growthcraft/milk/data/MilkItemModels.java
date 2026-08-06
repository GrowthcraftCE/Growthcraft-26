package growthcraft.milk.data;

import com.google.gson.JsonObject;
import growthcraft.milk.config.Reference;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MilkItemModels implements DataProvider {
    private static final String[] CHEESE_NAMES = {
            Reference.UnlocalizedName.APPENZELLER,
            Reference.UnlocalizedName.ASIAGO,
            Reference.UnlocalizedName.CASU_MARZU,
            Reference.UnlocalizedName.CHEDDAR,
            Reference.UnlocalizedName.EMMENTALER,
            Reference.UnlocalizedName.GORGONZOLA,
            Reference.UnlocalizedName.GOUDA,
            Reference.UnlocalizedName.MONTEREY,
            Reference.UnlocalizedName.PARMESAN,
            Reference.UnlocalizedName.PROVOLONE
    };

    private final PackOutput.PathProvider pathProvider;

    public MilkItemModels(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        layered(futures, output, Reference.UnlocalizedName.MILKING_BUCKET_IRON,
                modLoc("item/milking_bucket_contents_default"),
                modLoc("item/milking_bucket_base"));

        for (String cheeseName : CHEESE_NAMES) {
            generated(futures, output, cheeseName + "_cut", modLoc("item/cheese/" + cheeseName + "_cut"));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Growthcraft Milk Item Models";
    }

    private void generated(List<CompletableFuture<?>> futures, CachedOutput output, String name, Identifier texture) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", texture.toString());
        model.add("textures", textures);
        save(futures, output, name, model);
    }

    private void layered(List<CompletableFuture<?>> futures, CachedOutput output, String name, Identifier layer0, Identifier layer1) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", layer0.toString());
        textures.addProperty("layer1", layer1.toString());
        model.add("textures", textures);
        save(futures, output, name, model);
    }

    private void save(List<CompletableFuture<?>> futures, CachedOutput output, String name, JsonObject model) {
        Path path = pathProvider.json(Identifier.fromNamespaceAndPath(Reference.MODID, name));
        futures.add(DataProvider.saveStable(output, model, path));
    }

    private Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(Reference.MODID, path);
    }
}
