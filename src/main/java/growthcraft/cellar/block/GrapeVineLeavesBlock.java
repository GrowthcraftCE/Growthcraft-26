package growthcraft.cellar.block;

import growthcraft.cellar.block.support.VineGrowthHelper;
import growthcraft.core.block.RopeBlock2Base;
import growthcraft.core.init.GrowthcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class GrapeVineLeavesBlock extends RopeBlock2Base implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final int MAX_AGE = 7;
    private static final VoxelShape SHAPE = Block.box(0, 1, 0, 16, 15.9, 16);
    private final Supplier<? extends GrapeVineFruitBlock> fruit;
    private final Supplier<? extends Item> seeds;

    public GrapeVineLeavesBlock(Supplier<? extends GrapeVineFruitBlock> fruit, Supplier<? extends Item> seeds,
                                BlockBehaviour.Properties properties) {
        super(properties.randomTicks().strength(0.1F).noOcclusion().sound(SoundType.CROP));
        this.fruit = fruit;
        this.seeds = seeds;
        registerDefaultState(stateDefinition.any().setValue(NORTH, 0).setValue(EAST, 0).setValue(SOUTH, 0)
                .setValue(WEST, 0).setValue(UP, 0).setValue(DOWN, 0).setValue(AGE, 0));
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder); builder.add(AGE);
    }
    @Override protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
    @Override protected boolean isRandomlyTicking(BlockState state) { return true; }
    @Override protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1) || level.getRawBrightness(pos.above(), 0) < 9 || random.nextInt(4) != 0) return;
        int age = state.getValue(AGE);
        if (age < MAX_AGE) { level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_ALL); return; }
        growFruitAndExpand(level, pos);
    }

    private void growFruitAndExpand(ServerLevel level, BlockPos pos) {
        BlockPos fruitPos = pos.below();
        if (level.getBlockState(fruitPos).isAir()) level.setBlock(fruitPos, fruit.get().defaultBlockState(), Block.UPDATE_ALL);
        Direction expansion = VineGrowthHelper.tryGrapeLeavesExpand(level, pos);
        if (expansion != null) {
            BlockPos target = pos.relative(expansion);
            level.setBlock(target, getConnectedState(level, target), Block.UPDATE_ALL);
        }
    }
    @Override protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return VineGrowthHelper.canGrapeLeavesSurvive(level, pos); }
    @Override protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean piston) {
        if (!canSurvive(state, level, pos)) level.destroyBlock(pos, true);
        else super.neighborChanged(state, level, pos, block, orientation, piston);
    }
    @Override public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < MAX_AGE
                || level.getBlockState(pos.below()).isAir()
                || VineGrowthHelper.canGrapeLeavesExpand(level, pos);
    }
    @Override public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(seeds.get());
    }
    @Override public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
    @Override public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int age = Math.min(MAX_AGE, state.getValue(AGE) + Mth.nextInt(random, 1, 2));
        level.setBlock(pos, state.setValue(AGE, age), Block.UPDATE_ALL);
        if (age == MAX_AGE) growFruitAndExpand(level, pos);
    }
}
