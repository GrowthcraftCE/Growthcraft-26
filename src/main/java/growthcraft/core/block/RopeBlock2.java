package growthcraft.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RopeBlock2 extends RopeBlock2Base implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty KNOT = BooleanProperty.create("knot");
    private static final VoxelShape KNOT_SHAPE = Block.box(7, 7, 7, 9, 9, 9);
    private static final VoxelShape NORTH_SHAPE = Block.box(7.5, 7.5, 0, 8.5, 8.5, 7);
    private static final VoxelShape EAST_SHAPE = Block.box(9, 7.5, 7.5, 16, 8.5, 8.5);
    private static final VoxelShape SOUTH_SHAPE = Block.box(7.5, 7.5, 9, 8.5, 8.5, 16);
    private static final VoxelShape WEST_SHAPE = Block.box(0, 7.5, 7.5, 7, 8.5, 8.5);
    private static final VoxelShape UP_SHAPE = Block.box(7.5, 9, 7.5, 8.5, 16, 8.5);
    private static final VoxelShape DOWN_SHAPE = Block.box(7.5, 0, 7.5, 8.5, 7, 8.5);

    public RopeBlock2(Properties properties) {
        super(properties.noOcclusion());
        registerDefaultState(stateDefinition.any().setValue(NORTH, 0).setValue(EAST, 0).setValue(SOUTH, 0)
                .setValue(WEST, 0).setValue(UP, 0).setValue(DOWN, 0).setValue(KNOT, true).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(KNOT, WATERLOGGED);
    }

    @Override
    public BlockState getConnectedState(LevelAccessor level, BlockPos pos) {
        return super.getConnectedState(level, pos).setValue(KNOT, true)
                .setValue(WATERLOGGED, level.getFluidState(pos).is(Fluids.WATER));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = KNOT_SHAPE;
        if (state.getValue(NORTH) > 0) shape = Shapes.or(shape, NORTH_SHAPE);
        if (state.getValue(EAST) > 0) shape = Shapes.or(shape, EAST_SHAPE);
        if (state.getValue(SOUTH) > 0) shape = Shapes.or(shape, SOUTH_SHAPE);
        if (state.getValue(WEST) > 0) shape = Shapes.or(shape, WEST_SHAPE);
        if (state.getValue(UP) > 0) shape = Shapes.or(shape, UP_SHAPE);
        if (state.getValue(DOWN) > 0) shape = Shapes.or(shape, DOWN_SHAPE);
        return shape;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 15; }
    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 50; }
}
