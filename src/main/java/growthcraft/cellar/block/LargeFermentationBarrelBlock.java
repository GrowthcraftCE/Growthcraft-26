package growthcraft.cellar.block;

import growthcraft.cellar.block.entity.FermentationBarrelBlockEntity;
import growthcraft.cellar.block.entity.LargeFermentationBarrelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class LargeFermentationBarrelBlock extends Block implements EntityBlock, LiquidBlockContainer {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<LargeBarrelPart> PART = EnumProperty.create("part", LargeBarrelPart.class);

    private static final double[][] PROFILE = {
            {0, 2, 10, 22, 8, 24}, {2, 4, 6, 26, 0.2, 31.8},
            {4, 6, 4, 28, 0.2, 31.8}, {6, 10, 2, 30, 0.2, 31.8},
            {10, 22, 2, 30, 0.2, 31.8}, {22, 26, 2, 30, 0.2, 31.8},
            {26, 28, 4, 28, 0.2, 31.8}, {28, 30, 6, 26, 0.2, 31.8},
            {30, 32, 10, 22, 8, 24}
    };

    public LargeFermentationBarrelBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, LargeBarrelPart.BOTTOM_NEAR_LEFT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction forward = context.getHorizontalDirection();
        BlockPos controllerPos = context.getClickedPos();
        if (!canFormAt(context.getLevel(), controllerPos, forward)) {
            return null;
        }
        return defaultBlockState()
                .setValue(FACING, forward)
                .setValue(PART, LargeBarrelPart.BOTTOM_NEAR_LEFT);
    }

    private boolean canFormAt(LevelReader level, BlockPos controllerPos, Direction forward) {
        for (LargeBarrelPart part : LargeBarrelPart.values()) {
            BlockPos partPos = part.fromController(controllerPos, forward);
            if (!level.getBlockState(partPos).canBeReplaced()) {
                return false;
            }
        }
        return true;
    }

    public static BlockPos getControllerPos(BlockPos pos, BlockState state) {
        return state.getValue(PART).controllerFrom(pos, state.getValue(FACING));
    }

    @Nullable
    public static LargeFermentationBarrelBlockEntity getController(Level level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = level.getBlockEntity(getControllerPos(pos, state));
        return blockEntity instanceof LargeFermentationBarrelBlockEntity controller ? controller : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction forward = state.getValue(FACING);
        for (LargeBarrelPart part : LargeBarrelPart.values()) {
            BlockPos partPos = part.fromController(pos, forward);
            level.setBlock(partPos, state.setValue(PART, part), Block.UPDATE_ALL_IMMEDIATE);
        }
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            Direction forward = state.getValue(FACING);
            LargeBarrelPart brokenPart = state.getValue(PART);
            BlockPos controllerPos = brokenPart.controllerFrom(pos, forward);
            BlockEntity controller = level.getBlockEntity(controllerPos);
            if (controller instanceof LargeFermentationBarrelBlockEntity barrel) {
                Containers.dropContents(level, controllerPos, barrel);
                barrel.clearContent();
            }
            for (LargeBarrelPart part : LargeBarrelPart.values()) {
                BlockPos partPos = part.fromController(controllerPos, forward);
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockPos controllerPos = getControllerPos(pos, state);
        return FermentationBarrelBlock.useWithoutItemAt(level, controllerPos, player);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos controllerPos = getControllerPos(pos, state);
        BlockState controllerState = level.getBlockState(controllerPos);
        return FermentationBarrelBlock.useItemOnAt(
                heldStack, controllerState, level, controllerPos, player, hand, hitResult.getDirection());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(PART).isController() ? new LargeFermentationBarrelBlockEntity(pos, state) : null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide() || !state.getValue(PART).isController()) {
            return null;
        }
        return (tickerLevel, tickerPos, tickerState, blockEntity) -> {
            if (blockEntity instanceof LargeFermentationBarrelBlockEntity barrel) {
                FermentationBarrelBlockEntity.serverTick(tickerLevel, tickerPos, tickerState, barrel);
            }
        };
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return state.getValue(PART).isController() ? super.getDrops(state, params) : Collections.emptyList();
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }

    private VoxelShape getPartShape(BlockState state) {
        LargeBarrelPart part = state.getValue(PART);
        Direction facing = state.getValue(FACING);
        int rightPart = part.rightOffset();
        int forwardPart = part.forwardOffset();
        if (facing.getClockWise().getAxisDirection() == Direction.AxisDirection.NEGATIVE) rightPart = 1 - rightPart;
        if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE) forwardPart = 1 - forwardPart;
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

    @Override
    protected boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity user, BlockGetter level, BlockPos pos,
                                  BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }
}
