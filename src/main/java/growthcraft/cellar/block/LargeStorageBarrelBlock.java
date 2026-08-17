package growthcraft.cellar.block;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.block.entity.LargeStorageBarrelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class LargeStorageBarrelBlock extends Block implements EntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<StorageBarrelPart> PART = EnumProperty.create("part", StorageBarrelPart.class);

    private static final double[][] PROFILE = {
            {0, 3, 15, 33, 12, 36}, {3, 6, 9, 39, 0, 48},
            {6, 9, 6, 42, 0, 48}, {9, 15, 3, 45, 0, 48},
            {15, 33, 3, 45, 0, 48}, {33, 39, 3, 45, 0, 48},
            {39, 42, 6, 42, 0, 48}, {42, 45, 9, 39, 0, 48},
            {45, 48, 15, 33, 12, 36}
    };

    public LargeStorageBarrelBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, StorageBarrelPart.BOTTOM_NEAR_LEFT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        BlockPos controllerPos = context.getClickedPos();
        for (StorageBarrelPart part : StorageBarrelPart.values()) {
            if (!context.getLevel().getBlockState(part.fromController(controllerPos, facing)).canBeReplaced()) {
                return null;
            }
        }
        return defaultBlockState().setValue(FACING, facing).setValue(PART, StorageBarrelPart.BOTTOM_NEAR_LEFT);
    }

    public static BlockPos getControllerPos(BlockPos pos, BlockState state) {
        return state.getValue(PART).controllerFrom(pos, state.getValue(FACING));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction facing = state.getValue(FACING);
        for (StorageBarrelPart part : StorageBarrelPart.values()) {
            if (part.isController()) continue;
            placePart(level, pos, state, facing, part);
        }
        // Assign the controller last so neighbour updates cannot leave its
        // default state copied into a structure position during formation.
        placePart(level, pos, state, facing, StorageBarrelPart.BOTTOM_NEAR_LEFT);
    }

    private void placePart(Level level, BlockPos controllerPos, BlockState baseState,
                           Direction facing, StorageBarrelPart part) {
        BlockPos partPos = part.fromController(controllerPos, facing);
        BlockState expectedState = baseState.setValue(FACING, facing).setValue(PART, part);
        level.setBlock(partPos, expectedState, Block.UPDATE_ALL_IMMEDIATE);
        BlockState actualState = level.getBlockState(partPos);
        if (actualState.getBlock() != this || actualState.getValue(PART) != part
                || actualState.getValue(FACING) != facing) {
            GrowthcraftCellar.LOGGER.error(
                    "Large storage barrel part assignment failed at {}: expected {} facing {}, found {}",
                    partPos, part.getSerializedName(), facing, actualState);
        }
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            Direction facing = state.getValue(FACING);
            StorageBarrelPart brokenPart = state.getValue(PART);
            BlockPos controllerPos = brokenPart.controllerFrom(pos, facing);
            for (StorageBarrelPart part : StorageBarrelPart.values()) {
                BlockPos partPos = part.fromController(controllerPos, facing);
                if (!partPos.equals(pos) && level.getBlockState(partPos).is(this)) {
                    level.removeBlock(partPos, false);
                }
            }
            if (!brokenPart.isController() && !player.getAbilities().instabuild) {
                popResource(level, pos, new ItemStack(this));
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hitResult) {
        BlockEntity controller = level.getBlockEntity(getControllerPos(pos, state));
        if (!(controller instanceof LargeStorageBarrelBlockEntity barrel)) return InteractionResult.PASS;
        if (!level.isClientSide()) player.openMenu(barrel);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos controllerPos = getControllerPos(pos, state);
        return FluidUtil.interactWithFluidHandler(player, hand, level, controllerPos, hitResult.getDirection())
                ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(PART).isController() ? new LargeStorageBarrelBlockEntity(pos, state) : null;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return state.getValue(PART).isController() ? super.getDrops(state, params) : Collections.emptyList();
    }

    private VoxelShape getPartShape(BlockState state) {
        StorageBarrelPart part = state.getValue(PART);
        Direction facing = state.getValue(FACING);
        int rightPart = part.rightOffset();
        int forwardPart = part.forwardOffset();
        if (facing.getClockWise().getAxisDirection() == Direction.AxisDirection.NEGATIVE) rightPart = 2 - rightPart;
        if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE) forwardPart = 2 - forwardPart;
        double cellX = (facing.getAxis() == Direction.Axis.Z ? rightPart : forwardPart) * 16.0;
        double cellY = part.upOffset() * 16.0;
        double cellZ = (facing.getAxis() == Direction.Axis.Z ? forwardPart : rightPart) * 16.0;
        VoxelShape profile = Shapes.empty();
        for (double[] band : PROFILE) {
            double minY = Math.max(band[0], cellY);
            double maxY = Math.min(band[1], cellY + 16.0);
            double minX = Math.max(facing.getAxis() == Direction.Axis.Z ? band[2] : band[4], cellX);
            double maxX = Math.min(facing.getAxis() == Direction.Axis.Z ? band[3] : band[5], cellX + 16.0);
            double minZ = Math.max(facing.getAxis() == Direction.Axis.Z ? band[4] : band[2], cellZ);
            double maxZ = Math.min(facing.getAxis() == Direction.Axis.Z ? band[5] : band[3], cellZ + 16.0);
            if (minY < maxY && minX < maxX && minZ < maxZ) {
                profile = Shapes.or(profile, box(minX - cellX, minY - cellY, minZ - cellZ,
                        maxX - cellX, maxY - cellY, maxZ - cellZ));
            }
        }
        return profile.optimize();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getPartShape(state);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getPartShape(state);
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
