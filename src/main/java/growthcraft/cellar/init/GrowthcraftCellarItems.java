package growthcraft.cellar.init;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.item.CellarPotionItem;
import growthcraft.cellar.item.EtherealYeastItem;
import growthcraft.cellar.item.GrapeSeedsItem;
import growthcraft.cellar.item.HopsSeedsItem;
import growthcraft.lib.item.GrowthcraftFoodItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Item registrations for Growthcraft Cellar (MC 1.21 / NeoForge).
 */
public class GrowthcraftCellarItems {
    // Register items under the Cellar modid
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    // Blocks (BlockItems)
    public static final DeferredItem<Item> BREW_KETTLE = blockItem(Reference.UnlocalizedName.Block.BREW_KETTLE, GrowthcraftCellarBlocks.BREW_KETTLE);
    public static final DeferredItem<Item> CULTURE_JAR = blockItem(Reference.UnlocalizedName.Block.CULTURE_JAR, GrowthcraftCellarBlocks.CULTURE_JAR);
    public static final DeferredItem<Item> FERMENTATION_BARREL_OAK = blockItem(Reference.UnlocalizedName.Block.FERMENT_BARREL_OAK, GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK);
    public static final DeferredItem<Item> FRUIT_PRESS = blockItem(Reference.UnlocalizedName.Block.FRUIT_PRESS, GrowthcraftCellarBlocks.FRUIT_PRESS);
    public static final DeferredItem<Item> ROASTER = blockItem(Reference.UnlocalizedName.Block.ROASTER, GrowthcraftCellarBlocks.ROASTER);
    public static final DeferredItem<Item> CORK_COASTER = blockItem(Reference.UnlocalizedName.Item.CORK_COASTER, GrowthcraftCellarBlocks.CORK_COASTER);
    public static final DeferredItem<Item> CORK_TREE_LEAVES = blockItem(Reference.UnlocalizedName.Block.CORK_TREE_LEAVES, GrowthcraftCellarBlocks.CORK_TREE_LEAVES);
    public static final DeferredItem<Item> CORK_TREE_SAPLING = blockItem(Reference.UnlocalizedName.Block.CORK_TREE_SAPLING, GrowthcraftCellarBlocks.CORK_TREE_SAPLING);
    public static final DeferredItem<Item> CORK_WOOD = blockItem(Reference.UnlocalizedName.Block.CORK_WOOD, GrowthcraftCellarBlocks.CORK_WOOD);
    public static final DeferredItem<Item> CORK_WOOD_LOG = blockItem(Reference.UnlocalizedName.Block.CORK_WOOD_LOG, GrowthcraftCellarBlocks.CORK_WOOD_LOG);
    public static final DeferredItem<Item> CORK_WOOD_LOG_STRIPPED = blockItem(Reference.UnlocalizedName.Block.CORK_WOOD_LOG_STRIPPED, GrowthcraftCellarBlocks.CORK_WOOD_LOG_STRIPPED);
    public static final DeferredItem<Item> CORK_WOOD_STRIPPED = blockItem(Reference.UnlocalizedName.Block.CORK_WOOD_STRIPPED, GrowthcraftCellarBlocks.CORK_WOOD_STRIPPED);

    // Grains (base + color variants)
    public static final DeferredItem<Item> GRAIN = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.GRAIN);
    public static final DeferredItem<Item> GRAIN_AMBER = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.GRAIN_AMBER);
    public static final DeferredItem<Item> GRAIN_BROWN = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.GRAIN_BROWN);
    public static final DeferredItem<Item> GRAIN_COPPER = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.GRAIN_COPPER);
    public static final DeferredItem<Item> GRAIN_DARK = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.GRAIN_DARK);
    public static final DeferredItem<Item> GRAIN_DEEP_AMBER = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.GRAIN_DEEP_AMBER);
    public static final DeferredItem<Item> GRAIN_DEEP_COPPER = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.GRAIN_DEEP_COPPER);
    public static final DeferredItem<Item> GRAIN_GOLDEN = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.GRAIN_GOLDEN);
    public static final DeferredItem<Item> GRAIN_PALE_GOLDEN = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.GRAIN_PALE_GOLDEN);

    // Grapes and seeds
    public static final DeferredItem<Item> GRAPE_PURPLE = ITEMS.registerItem(Reference.UnlocalizedName.Item.GRAPE_PURPLE, properties -> new GrowthcraftFoodItem(properties, 1, 0.2F, 64));
    public static final DeferredItem<Item> GRAPE_RED = ITEMS.registerItem(Reference.UnlocalizedName.Item.GRAPE_RED, properties -> new GrowthcraftFoodItem(properties, 1, 0.2F, 64));
    public static final DeferredItem<Item> GRAPE_WHITE = ITEMS.registerItem(Reference.UnlocalizedName.Item.GRAPE_WHITE, properties -> new GrowthcraftFoodItem(properties, 1, 0.2F, 64));

    public static final DeferredItem<Item> GRAPE_SEEDS_PURPLE = ITEMS.registerItem(Reference.UnlocalizedName.Item.GRAPE_SEEDS_PURPLE,
            properties -> new GrapeSeedsItem(properties, GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE));
    public static final DeferredItem<Item> GRAPE_SEEDS_RED = ITEMS.registerItem(Reference.UnlocalizedName.Item.GRAPE_SEEDS_RED,
            properties -> new GrapeSeedsItem(properties, GrowthcraftCellarBlocks.RED_GRAPE_VINE));
    public static final DeferredItem<Item> GRAPE_SEEDS_WHITE = ITEMS.registerItem(Reference.UnlocalizedName.Item.GRAPE_SEEDS_WHITE,
            properties -> new GrapeSeedsItem(properties, GrowthcraftCellarBlocks.WHITE_GRAPE_VINE));

    // Hops
    public static final DeferredItem<Item> HOPS = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.HOPS);
    public static final DeferredItem<Item> HOPS_SEEDS = ITEMS.registerItem(Reference.UnlocalizedName.Item.HOPS_SEEDS, HopsSeedsItem::new);

    // Misc materials
    public static final DeferredItem<Item> CORK_BARK = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.CORK_BARK);

    // Yeasts
    public static final DeferredItem<Item> YEAST_BAYANUS = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.YEAST_BAYANUS);
    public static final DeferredItem<Item> YEAST_BAYANUS_ETHEREAL = ITEMS.registerItem(Reference.UnlocalizedName.Item.YEAST_BAYANUS_ETHEREAL, EtherealYeastItem::new);
    public static final DeferredItem<Item> YEAST_BREWERS = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.YEAST_BREWERS);
    public static final DeferredItem<Item> YEAST_BREWERS_ETHEREAL = ITEMS.registerItem(Reference.UnlocalizedName.Item.YEAST_BREWERS_ETHEREAL, EtherealYeastItem::new);
    public static final DeferredItem<Item> YEAST_ETHEREAL = ITEMS.registerItem(Reference.UnlocalizedName.Item.YEAST_ETHEREAL, EtherealYeastItem::new);
    public static final DeferredItem<Item> YEAST_LAGER = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.YEAST_LAGER);
    public static final DeferredItem<Item> YEAST_LAGER_ETHEREAL = ITEMS.registerItem(Reference.UnlocalizedName.Item.YEAST_LAGER_ETHEREAL, EtherealYeastItem::new);

    // Cultures
    public static final DeferredItem<Item> STARTER_CULTURE = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.STARTER_CULTURE);

    // Serving containers used by Fermentation Barrel recipes.
    public static final DeferredItem<Item> POTION_ALE = ITEMS.registerItem(Reference.UnlocalizedName.Item.POTION_ALE, CellarPotionItem::new);
    public static final DeferredItem<Item> POTION_LAGER = ITEMS.registerItem(Reference.UnlocalizedName.Item.POTION_LAGER, CellarPotionItem::new);
    public static final DeferredItem<Item> POTION_WINE = ITEMS.registerItem(Reference.UnlocalizedName.Item.POTION_WINE, CellarPotionItem::new);

    private static DeferredItem<Item> blockItem(String name, net.neoforged.neoforge.registries.DeferredBlock<? extends Block> block) {
        @SuppressWarnings({"unchecked", "rawtypes"})
        DeferredItem<Item> item = (DeferredItem) ITEMS.registerSimpleBlockItem(name, block);
        return item;
    }

    private GrowthcraftCellarItems() {}
}
