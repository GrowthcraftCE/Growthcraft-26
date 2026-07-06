package growthcraft.lib.block;

import com.mojang.serialization.MapCodec;
import growthcraft.core.block.RopeBlock;
import growthcraft.core.init.GrowthcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class GrowthcraftCropsRopeBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<GrowthcraftCropsRopeBlock> CODEC = simpleCodec(GrowthcraftCropsRopeBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public GrowthcraftCropsRopeBlock() {
        this(BlockBehaviour.Properties.of()
                .noCollision()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP));
    }

    public GrowthcraftCropsRopeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, true)
                .setValue(AGE, 0));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public MapCodec<BushBlock> codec() {
        return (MapCodec) CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, AGE);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof FarmlandBlock || RopeBlock.canConnect(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return super.updateShape(getActualBlockStateWithAge(level, pos, getAge(state)), level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return !isMaxAge(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1) || level.getRawBrightness(pos, 0) < 9 || random.nextInt(4) != 0) {
            return;
        }

        int age = getAge(state);
        if (age < getMaxAge()) {
            level.setBlock(pos, getActualBlockStateWithAge(level, pos, age + 1), Block.UPDATE_ALL);
            RopeBlock.refreshAdjacentConnections(level, pos);
        }
    }

    public BlockState getActualBlockStateWithAge(BlockGetter level, BlockPos pos, int age) {
        return defaultBlockState()
                .setValue(AGE, Mth.clamp(age, 0, getMaxAge()))
                .setValue(NORTH, RopeBlock.canConnect(level.getBlockState(pos.north())))
                .setValue(EAST, RopeBlock.canConnect(level.getBlockState(pos.east())))
                .setValue(SOUTH, RopeBlock.canConnect(level.getBlockState(pos.south())))
                .setValue(WEST, RopeBlock.canConnect(level.getBlockState(pos.west())))
                .setValue(UP, RopeBlock.canConnect(level.getBlockState(pos.above())))
                .setValue(DOWN, RopeBlock.canConnect(level.getBlockState(pos.below())));
    }

    public boolean connectsAsRope() {
        return true;
    }

    public boolean isMaxAge(BlockState state) {
        return getAge(state) >= getMaxAge();
    }

    public int getMaxAge() {
        return 7;
    }

    protected int getAge(BlockState state) {
        return state.getValue(AGE);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !isMaxAge(state);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int age = Math.min(getAge(state) + Mth.nextInt(random, 1, 2), getMaxAge());
        level.setBlock(pos, getActualBlockStateWithAge(level, pos, age), Block.UPDATE_ALL);
        RopeBlock.refreshAdjacentConnections(level, pos);
    }

    protected void setCropBlock(Level level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state, Block.UPDATE_ALL);
        RopeBlock.refreshAdjacentConnections(level, pos);
    }

    protected void refreshCropAndRopeConnections(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);
            if (neighborState.getBlock() instanceof GrowthcraftCropsRopeBlock crop) {
                level.setBlock(neighborPos, crop.getActualBlockStateWithAge(level, neighborPos, crop.getAge(neighborState)), Block.UPDATE_ALL);
            }
        }
        RopeBlock.refreshAdjacentConnections(level, pos);
    }

}
