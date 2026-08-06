package growthcraft.lib.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class GrowthcraftBowlFoodItem extends Item {
    public GrowthcraftBowlFoodItem(int nutrition, float saturationModifier, int maxStackSize) {
        this(new Item.Properties(), nutrition, saturationModifier, maxStackSize);
    }

    public GrowthcraftBowlFoodItem(Item.Properties properties, int nutrition, float saturationModifier, int maxStackSize) {
        super(properties
                .stacksTo(maxStackSize)
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition)
                        .saturationModifier(saturationModifier)
                        .build())
                .usingConvertsTo(Items.BOWL));
    }
}
