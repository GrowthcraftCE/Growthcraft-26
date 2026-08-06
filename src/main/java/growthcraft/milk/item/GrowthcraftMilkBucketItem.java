package growthcraft.milk.item;

import growthcraft.milk.GrowthcraftMilk;
import growthcraft.milk.config.GrowthcraftMilkConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Bucket item for Growthcraft Milk variants that returns the corresponding
 * empty milking bucket when emptied/used in crafting or fluid transfers.
 */
public class GrowthcraftMilkBucketItem extends BucketItem {
    private final Supplier<Item> emptyReturn;

    private static void debug(String message, Object... arguments) {
        if (GrowthcraftMilkConfig.isBucketsDebugEnabled()) {
            GrowthcraftMilk.LOGGER.debug(message, arguments);
        }
    }

    public GrowthcraftMilkBucketItem(Fluid content, Supplier<Item> emptyReturn, Properties properties) {
        super(content, properties);
        this.emptyReturn = emptyReturn;
    }

    public ItemStack getEmptyReturnStack() {
        return new ItemStack(this.emptyReturn.get());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        InteractionResult ret = super.use(level, player, hand);
        if (!level.isClientSide() && ret.consumesAction() && !player.getAbilities().instabuild) {
            debug("[MilkBucket] use(Server): Player={} Hand={} replacing with emptyReturn={}",
                    player.getName().getString(), hand, this.emptyReturn.get());
            player.setItemInHand(hand, new ItemStack(this.emptyReturn.get()));
        } else if (level.isClientSide()) {
            debug("[MilkBucket] use(Client): deferring to server. Player={} Hand={}", player.getName().getString(), hand);
        }
        return ret;
    }

}
