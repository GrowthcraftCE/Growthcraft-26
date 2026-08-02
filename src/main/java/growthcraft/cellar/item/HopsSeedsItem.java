package growthcraft.cellar.item;

import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.core.init.GrowthcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

public class HopsSeedsItem extends Item {
    public HopsSeedsItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos plantPos = pos.above();
        if (level.getBlockState(pos).is(Tags.Blocks.VILLAGER_FARMLANDS)
                && level.getBlockState(plantPos).is(GrowthcraftBlocks.ROPE_LINEN.get())) {
            if (!level.isClientSide()) {
                level.setBlock(plantPos, GrowthcraftCellarBlocks.HOPS_VINE.get().getConnectedState(level, plantPos), Block.UPDATE_ALL);
                if (context.getPlayer() == null || !context.getPlayer().isCreative()) {
                    context.getItemInHand().shrink(1);
                }
            }
            return (level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
        }

        return InteractionResult.PASS;
    }
}
