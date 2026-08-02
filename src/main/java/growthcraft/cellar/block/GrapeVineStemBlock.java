package growthcraft.cellar.block;

import com.mojang.serialization.MapCodec;
import growthcraft.cellar.config.GrowthcraftCellarConfig;
import growthcraft.core.block.RopeBlock2Base;
import growthcraft.core.init.GrowthcraftBlocks;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;

import java.util.function.Supplier;

public class GrapeVineStemBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final int MAX_AGE = 7;
    private static final VoxelShape YOUNG = Block.box(7, 0, 7, 9, 5, 9);
    private static final VoxelShape MATURE = Block.box(6, 0, 6, 10, 15.98, 10);
    private final Supplier<? extends GrapeVineLeavesBlock> leaves;
    private final Supplier<? extends Item> seeds;

    public GrapeVineStemBlock(Supplier<? extends GrapeVineLeavesBlock> leaves, Supplier<? extends Item> seeds,
                              BlockBehaviour.Properties properties) {
        super(properties.randomTicks().strength(0.1F).sound(SoundType.CROP));
        this.leaves = leaves;
        this.seeds = seeds;
        registerDefaultState(stateDefinition.any().setValue(AGE, 0));
    }

    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(AGE); }
    @Override protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AGE) <= 3 ? YOUNG : MATURE;
    }
    @Override protected boolean isRandomlyTicking(BlockState state) { return true; }
    @Override protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1) || level.getRawBrightness(pos, 0) < 9 || random.nextInt(4) != 0) return;
        int age = state.getValue(AGE);
        if (age < MAX_AGE) {
            level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_ALL);
            return;
        }
        BlockPos above = pos.above();
        if (!(level.getBlockState(above).getBlock() instanceof RopeBlock2Base)) return;
        BlockState replacement = canGrowHigher(level, pos) && level.getBlockState(pos.above(2)).getBlock() instanceof RopeBlock2Base
                ? defaultBlockState() : leaves.get().getConnectedState(level, above);
        level.setBlock(above, replacement, Block.UPDATE_ALL);
    }
    private boolean canGrowHigher(BlockGetter level, BlockPos pos) {
        int height = 1;
        for (BlockPos cursor = pos.below(); level.getBlockState(cursor).is(this); cursor = cursor.below()) height++;
        return height < GrowthcraftCellarConfig.getGrapeVineMaxHeight();
    }
    @Override protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(Tags.Blocks.VILLAGER_FARMLANDS) || below.is(this) || below.getBlock() instanceof GrapeVineLeavesBlock;
    }
    @Override public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return state.getValue(AGE) < MAX_AGE; }
    @Override public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(seeds.get());
    }
    @Override public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
    @Override public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(AGE, Math.min(MAX_AGE, state.getValue(AGE) + Mth.nextInt(random, 1, 2))), Block.UPDATE_ALL);
    }
    @Override public MapCodec<BushBlock> codec() { return null; }
}
