package growthcraft.cellar.block;

import growthcraft.cellar.block.support.VineGrowthHelper;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.core.block.RopeBlock2Base;
import growthcraft.core.init.GrowthcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class HopsCropBlock extends RopeBlock2Base implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final int MAX_AGE = 7;
    private static final VoxelShape SEEDLING = Block.box(6, 0, 6, 10, 5, 10);
    private static final VoxelShape MATURE = Block.box(4, 0, 4, 12, 16, 12);

    public HopsCropBlock(BlockBehaviour.Properties properties) {
        super(properties.randomTicks().noCollision().instabreak().sound(SoundType.CROP));
        registerDefaultState(stateDefinition.any().setValue(NORTH, 0).setValue(EAST, 0).setValue(SOUTH, 0)
                .setValue(WEST, 0).setValue(UP, 0).setValue(DOWN, 0).setValue(AGE, 0));
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder); builder.add(AGE);
    }
    @Override protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AGE) < 4 ? SEEDLING : MATURE;
    }
    @Override protected boolean isRandomlyTicking(BlockState state) { return true; }
    @Override protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1) || level.getRawBrightness(pos, 0) < 9 || random.nextInt(4) != 0) return;
        int age = state.getValue(AGE);
        if (age < MAX_AGE) level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_ALL);
        else tryGrow(level, pos);
    }
    private void tryGrow(ServerLevel level, BlockPos pos) {
        Direction expansion = VineGrowthHelper.tryHopsExpand(level, pos);
        if (expansion != null) {
            BlockPos target = pos.relative(expansion);
            level.setBlock(target, getConnectedState(level, target).setValue(AGE, 0), Block.UPDATE_ALL);
        }
    }
    @Override public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < MAX_AGE || level.getBlockState(pos.above()).is(GrowthcraftBlocks.ROPE_LINEN.get());
    }
    @Override public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
    @Override public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (state.getValue(AGE) < MAX_AGE) {
            level.setBlock(pos, state.setValue(AGE, Math.min(MAX_AGE, state.getValue(AGE) + Mth.nextInt(random, 1, 2))), Block.UPDATE_ALL);
        } else tryGrow(level, pos);
    }
    @Override public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(GrowthcraftCellarItems.HOPS_SEEDS.get());
    }
    @Override protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValue(AGE) != MAX_AGE || player.getMainHandItem().is(Items.BONE_MEAL)) return InteractionResult.PASS;
        popResource(level, pos, new ItemStack(GrowthcraftCellarItems.HOPS.get()));
        level.setBlock(pos, state.setValue(AGE, MAX_AGE - 1), Block.UPDATE_ALL);
        return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
    }
    @Override protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return VineGrowthHelper.canHopsSurvive(level, pos);
    }
    @Override protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block,
                                             @Nullable Orientation orientation, boolean piston) {
        if (!canSurvive(state, level, pos)) level.destroyBlock(pos, true);
        else super.neighborChanged(state, level, pos, block, orientation, piston);
    }
}
