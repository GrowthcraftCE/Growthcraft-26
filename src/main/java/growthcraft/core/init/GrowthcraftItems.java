package growthcraft.core.init;

import growthcraft.core.config.Reference;
import growthcraft.core.item.CrowbarItem;
import growthcraft.core.item.OffsetTier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GrowthcraftItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    // Simple materials / tools
    public static final DeferredItem<Item> SALT = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.SALT);
    public static final DeferredItem<BlockItem> ROPE_LINEN = ITEMS.registerSimpleBlockItem(
            Reference.UnlocalizedName.Item.ROPE_LINEN, GrowthcraftBlocks.ROPE_LINEN);

    // BlockItems
    public static final DeferredItem<BlockItem> SALT_BLOCK = ITEMS.registerSimpleBlockItem(Reference.UnlocalizedName.Block.SALT_BLOCK, GrowthcraftBlocks.SALT_BLOCK);
    public static final DeferredItem<BlockItem> SALT_ORE = ITEMS.registerSimpleBlockItem(Reference.UnlocalizedName.Block.SALT_ORE, GrowthcraftBlocks.SALT_ORE);
    public static final DeferredItem<BlockItem> SALT_ORE_DEEPSLATE = ITEMS.registerSimpleBlockItem("salt_ore_deepslate", GrowthcraftBlocks.SALT_ORE_DEEPSLATE);
    public static final DeferredItem<BlockItem> SALT_ORE_NETHER = ITEMS.registerSimpleBlockItem("salt_ore_nether", GrowthcraftBlocks.SALT_ORE_NETHER);
    public static final DeferredItem<BlockItem> SALT_ORE_END = ITEMS.registerSimpleBlockItem("salt_ore_end", GrowthcraftBlocks.SALT_ORE_END);

    public static final DeferredItem<Item> CROWBAR_WHITE = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_WHITE, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_LIGHT_GRAY = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_LIGHT_GRAY, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_GRAY = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_GRAY, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_BLACK = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_BLACK, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_BROWN = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_BROWN, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_RED = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_RED, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_ORANGE = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_ORANGE, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_YELLOW = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_YELLOW, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_LIME = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_LIME, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_GREEN = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_GREEN, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_CYAN = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_CYAN, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_LIGHT_BLUE = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_LIGHT_BLUE, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_BLUE = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_BLUE, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_PURPLE = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_PURPLE, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_MAGENTA = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_MAGENTA, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> CROWBAR_PINK = ITEMS.registerItem(Reference.UnlocalizedName.Item.CROWBAR_PINK, properties -> new CrowbarItem(OffsetTier.IRON_MINUS2, properties));
    public static final DeferredItem<Item> WRENCH = ITEMS.registerSimpleItem(Reference.UnlocalizedName.Item.WRENCH);

}
