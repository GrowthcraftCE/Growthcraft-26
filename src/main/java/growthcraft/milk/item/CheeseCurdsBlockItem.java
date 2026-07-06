package growthcraft.milk.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CheeseCurdsBlockItem extends BlockItem {
    public CheeseCurdsBlockItem(Block block) {
        this(block, new Item.Properties());
    }

    public CheeseCurdsBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

}
