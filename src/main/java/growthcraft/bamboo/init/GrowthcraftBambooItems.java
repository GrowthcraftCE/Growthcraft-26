package growthcraft.bamboo.init;

import growthcraft.bamboo.config.Reference;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftBambooItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    public static final DeferredItem<BlockItem> BAMBOO_POST_VERTICAL = ITEMS.registerSimpleBlockItem(
            Reference.UnlocalizedName.Block.BAMBOO_POST_VERTICAL,
            GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL);

    private GrowthcraftBambooItems() {}
}
