package growthcraft.lib.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class GrowthcraftFoodItem extends Item {
    public GrowthcraftFoodItem(int nutrition, float saturationModifier, int maxStackSize) {
        this(new Item.Properties(), nutrition, saturationModifier, maxStackSize);
    }

    public GrowthcraftFoodItem(Item.Properties properties, int nutrition, float saturationModifier, int maxStackSize) {
        super(properties
                .stacksTo(maxStackSize)
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition)
                        .saturationModifier(saturationModifier)
                        .build()));
    }
}
