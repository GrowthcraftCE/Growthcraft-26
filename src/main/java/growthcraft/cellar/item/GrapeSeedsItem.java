package growthcraft.cellar.item;

import growthcraft.cellar.block.GrapeVineStemBlock;
import growthcraft.core.block.RopeBlock2Base;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GrapeSeedsItem extends Item {
    private final java.util.function.Supplier<? extends GrapeVineStemBlock> grapeVineCropBlock;

    public GrapeSeedsItem(Properties properties, java.util.function.Supplier<? extends GrapeVineStemBlock> grapeVineCropBlock) {
        super(properties);
        this.grapeVineCropBlock = grapeVineCropBlock;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos plantPos = pos.above();
        Block block = level.getBlockState(pos).getBlock();

        if (block instanceof FarmlandBlock
                && level.getBlockState(plantPos).isAir()
                && RopeBlock2Base.canConnect(level.getBlockState(pos.above(2)))) {
            if (!level.isClientSide()) {
                BlockState state = grapeVineCropBlock.get().defaultBlockState();
                level.setBlock(plantPos, state, Block.UPDATE_ALL);
                growthcraft.core.block.RopeBlock2Base.refreshAdjacentConnections(level, plantPos);
                if (context.getPlayer() == null || !context.getPlayer().isCreative()) {
                    context.getItemInHand().shrink(1);
                }
            }
            return (level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
        }

        return InteractionResult.PASS;
    }
}
