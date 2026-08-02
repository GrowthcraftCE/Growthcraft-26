package growthcraft.core.block;

import growthcraft.cellar.block.GrapeVineStemBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;

public abstract class RopeBlock2Base extends Block {
    public static final IntegerProperty NORTH = IntegerProperty.create("north_ex", 0, 2);
    public static final IntegerProperty EAST = IntegerProperty.create("east_ex", 0, 2);
    public static final IntegerProperty SOUTH = IntegerProperty.create("south_ex", 0, 2);
    public static final IntegerProperty WEST = IntegerProperty.create("west_ex", 0, 2);
    public static final IntegerProperty UP = IntegerProperty.create("up_ex", 0, 2);
    public static final IntegerProperty DOWN = IntegerProperty.create("down_ex", 0, 2);

    protected RopeBlock2Base(Properties properties) { super(properties); }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getConnectedState(context.getLevel(), context.getClickedPos());
    }

    public BlockState getConnectedState(LevelAccessor level, BlockPos pos) {
        return defaultBlockState()
                .setValue(NORTH, connection(level.getBlockState(pos.north()), false))
                .setValue(EAST, connection(level.getBlockState(pos.east()), false))
                .setValue(SOUTH, connection(level.getBlockState(pos.south()), false))
                .setValue(WEST, connection(level.getBlockState(pos.west()), false))
                .setValue(UP, connection(level.getBlockState(pos.above()), true))
                .setValue(DOWN, connection(level.getBlockState(pos.below()), true));
    }

    protected int connection(BlockState state, boolean vertical) {
        if (state.getBlock() instanceof RopeBlock2Base) return 1;
        if (vertical && state.getBlock() instanceof GrapeVineStemBlock) return 1;
        return state.is(BlockTags.FENCES) ? 2 : 0;
    }

    public static IntegerProperty property(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH; case EAST -> EAST; case SOUTH -> SOUTH;
            case WEST -> WEST; case UP -> UP; case DOWN -> DOWN;
        };
    }

    public static boolean canConnect(BlockState state) {
        return state.getBlock() instanceof RopeBlock2Base
                || state.getBlock() instanceof RopeFenceBlock
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
        for (Direction direction : Direction.values()) refreshConnections(level, pos.relative(direction));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        boolean vertical = direction.getAxis().isVertical();
        return state.setValue(property(direction), connection(neighborState, vertical));
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 75; }
    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 10; }
    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return true; }
}
