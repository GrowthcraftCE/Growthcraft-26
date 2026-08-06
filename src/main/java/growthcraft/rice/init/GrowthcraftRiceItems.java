package growthcraft.rice.init;

import growthcraft.lib.item.GrowthcraftBowlFoodItem;
import growthcraft.lib.item.GrowthcraftFoodItem;
import growthcraft.rice.config.Reference;
import growthcraft.rice.item.CultivatorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftRiceItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    public static final DeferredItem<BlockItem> CULTIVATED_FARMLAND = ITEMS.registerSimpleBlockItem(Reference.UnlocalizedName.Block.CULTIVATED_FARMLAND, GrowthcraftRiceBlocks.CULTIVATED_FARMLAND);
    public static final DeferredItem<BlockItem> RICE_CROP = ITEMS.registerSimpleBlockItem(Reference.UnlocalizedName.Block.RICE_CROP, GrowthcraftRiceBlocks.RICE_CROP);
    public static final DeferredItem<CultivatorItem> CULTIVATOR = ITEMS.registerItem(Reference.UnlocalizedName.Item.CULTIVATOR, CultivatorItem::new);
    public static final DeferredItem<Item> KNIFE = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.KNIFE);
    public static final DeferredItem<BlockItem> RICE_GRAINS = ITEMS.registerItem(
            Reference.UnlocalizedName.Item.RICE_GRAINS,
            properties -> new BlockItem(GrowthcraftRiceBlocks.RICE_CROP.get(), properties));
    public static final DeferredItem<Item> RICE = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.RICE);
    public static final DeferredItem<GrowthcraftFoodItem> RICE_COOKED = ITEMS.registerItem(Reference.UnlocalizedName.Item.RICE_COOKED, properties -> new GrowthcraftFoodItem(properties, 6, 0.4F, 64));
    public static final DeferredItem<Item> RICE_STALK = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.RICE_STALK);
    public static final DeferredItem<GrowthcraftFoodItem> SUSHI_ROLL = ITEMS.registerItem(Reference.UnlocalizedName.Item.SUSHI_ROLL, properties -> new GrowthcraftFoodItem(properties, 3, 0.4F, 64));
    public static final DeferredItem<GrowthcraftFoodItem> ONIGIRI = ITEMS.registerItem(Reference.UnlocalizedName.Item.ONIGIRI, properties -> new GrowthcraftFoodItem(properties, 8, 0.5F, 64));
    public static final DeferredItem<GrowthcraftBowlFoodItem> CHICKEN_RICE = ITEMS.registerItem(Reference.UnlocalizedName.Item.CHICKEN_RICE, properties -> new GrowthcraftBowlFoodItem(properties, 12, 0.8F, 8));
    public static final DeferredItem<Item> YEAST_SEISHU = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.YEAST_SEISHU);

    private GrowthcraftRiceItems() {}
}
