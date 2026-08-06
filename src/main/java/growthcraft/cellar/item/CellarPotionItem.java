package growthcraft.cellar.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumables;

public class CellarPotionItem extends PotionItem {
    public CellarPotionItem(Properties properties) {
        super(properties
                .stacksTo(16)
                .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
                .usingConvertsTo(Items.GLASS_BOTTLE));
    }

    public String getDescriptionId(ItemStack stack) {
        return this.getDescriptionId() + ".effect.empty";
    }

    @Override
    public Component getName(ItemStack stack) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        Component itemName = stack.get(DataComponents.ITEM_NAME);
        if (contents != null && !PotionContents.EMPTY.equals(contents) && itemName != null) {
            return itemName;
        }
        return super.getName(stack);
    }
}
