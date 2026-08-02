package growthcraft.core.block;

import growthcraft.lib.block.GrowthcraftCropsRopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/** @deprecated compatibility name for the support-aware rope implementation. */
@Deprecated
public class RopeBlock extends RopeBlock2 {
    public RopeBlock(Properties properties) { super(properties); }

    public BlockState getConnectedState(LevelAccessor level, BlockPos pos, boolean knot) {
        return getConnectedState(level, pos).setValue(KNOT, knot);
    }

    public static boolean canConnect(BlockState state) {
        return state.getBlock() instanceof RopeBlock2Base
                || state.getBlock() instanceof RopeFenceBlock
                || state.getBlock() instanceof GrowthcraftCropsRopeBlock
                || state.is(growthcraft.core.init.GrowthcraftTags.Blocks.ROPE);
    }

    public static void refreshConnections(LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof RopeBlock2Base rope) {
            level.setBlock(pos, rope.getConnectedState(level, pos), Block.UPDATE_ALL);
        } else if (state.getBlock() instanceof RopeFenceBlock fence) {
            level.setBlock(pos, fence.withRopeConnections(level, pos, state), Block.UPDATE_ALL);
        }
    }

    public static void refreshAdjacentConnections(LevelAccessor level, BlockPos pos) {
        for (var direction : net.minecraft.core.Direction.values()) refreshConnections(level, pos.relative(direction));
    }
}
