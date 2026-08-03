package growthcraft.milk.item;

import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CheeseCurdsBlockItem extends BlockItem {
    public CheeseCurdsBlockItem(Block block) {
        this(block, new Item.Properties());
    }

    public CheeseCurdsBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action,
                                            Player player, SlotAccess access) {
        if (slot.mayPickup(player) || slot.isFake()) {
            return false;
        }
        if (other.is(GrowthcraftMilkItems.CHEESE_CLOTH.get()) && action == ClickAction.SECONDARY) {
            if (other.getCount() > 1 && !player.isLocalPlayer()) {
                ItemStack remainingCloth = other.copyWithCount(other.getCount() - 1);
                if (!player.addItem(remainingCloth)) {
                    player.drop(remainingCloth, false);
                }
            }
            access.set(slot.getItem().copy());
            slot.set(ItemStack.EMPTY);
            return true;
        }
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
    }
}
