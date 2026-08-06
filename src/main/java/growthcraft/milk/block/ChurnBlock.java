package growthcraft.milk.block;

import growthcraft.lib.fluid.FluidRegistryContainer;
import growthcraft.milk.block.entity.ChurnBlockEntity;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class ChurnBlock extends Block implements EntityBlock {
    public static final BooleanProperty PLUNGED = BooleanProperty.create("plunged");
    private static final VoxelShape SHAPE_UP = Shapes.or(
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 5.0D, 14.0D),
            Block.box(3.0D, 5.0D, 3.0D, 13.0D, 10.0D, 13.0D),
            Block.box(4.0D, 10.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(7.0D, 4.0D, 7.0D, 9.0D, 28.0D, 9.0D));
    private static final VoxelShape SHAPE_DOWN = Shapes.or(
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 5.0D, 14.0D),
            Block.box(3.0D, 5.0D, 3.0D, 13.0D, 10.0D, 13.0D),
            Block.box(4.0D, 10.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(7.0D, 4.0D, 7.0D, 9.0D, 20.0D, 9.0D));

    public ChurnBlock() {
        this(Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .noOcclusion());
    }

    public ChurnBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PLUNGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PLUNGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(PLUNGED) ? SHAPE_DOWN : SHAPE_UP;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof ChurnBlockEntity churn)) {
            return InteractionResult.PASS;
        }

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide()) player.openMenu(churn);
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        if (!level.isClientSide() && churn.collectByProduct(player)) {
            return InteractionResult.CONSUME;
        }

        if (!level.isClientSide()) {
            togglePlunger(level, pos, state, churn);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (tryGrowthcraftBucketInteraction(heldStack, level, pos, player, hand)) {
            return (level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
        }

        if (FluidUtil.getFluidHandler(heldStack).isPresent() && FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection())) {
            return (level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
        }

        if (!(level.getBlockEntity(pos) instanceof ChurnBlockEntity churn)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide()) player.openMenu(churn);
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        if (!level.isClientSide() && churn.collectByProduct(player)) {
            return InteractionResult.CONSUME;
        }

        if (!level.isClientSide()) {
            togglePlunger(level, pos, state, churn);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    private static boolean tryGrowthcraftBucketInteraction(ItemStack heldStack, Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (!(level.getBlockEntity(pos) instanceof ChurnBlockEntity churn)) {
            return false;
        }

        Fluid filledFluid = getFluidFromBucket(heldStack);
        if (filledFluid != Fluids.EMPTY) {
            if (!level.isClientSide()) {
                FluidStack bucketFluid = new FluidStack(filledFluid, 1000);
                int filled = churn.getTank().fill(bucketFluid, IFluidHandler.FluidAction.SIMULATE);
                if (filled == 1000) {
                    churn.getTank().fill(bucketFluid, IFluidHandler.FluidAction.EXECUTE);
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        replaceHeldItem(player, hand, heldStack, getEmptyBucketRemainder(heldStack));
                    }
                }
            }
            return true;
        }

        if (heldStack.is(Items.BUCKET) || heldStack.is(GrowthcraftMilkItems.MILKING_BUCKET_IRON.get())) {
            FluidStack drained = churn.getTank().drain(1000, IFluidHandler.FluidAction.SIMULATE);
            ItemStack filledBucket = getBucketForFluid(drained);
            if (drained.getAmount() == 1000 && !filledBucket.isEmpty()) {
                if (!level.isClientSide()) {
                    churn.getTank().drain(drained, IFluidHandler.FluidAction.EXECUTE);
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        replaceHeldItem(player, hand, heldStack, filledBucket);
                    }
                }
                return true;
            }
        }

        return false;
    }

    private static Fluid getFluidFromBucket(ItemStack stack) {
        if (stack.is(Items.MILK_BUCKET) || stack.is(GrowthcraftMilkItems.MILK_BUCKET_IRON.get())) {
            return GrowthcraftMilkFluids.MILK.source.get();
        }

        for (FluidRegistryContainer container : GrowthcraftMilkFluids.ALL) {
            if (container.bucket != null && stack.is(container.bucket.get())) {
                return container.source.get();
            }
        }
        return Fluids.EMPTY;
    }

    private static ItemStack getBucketForFluid(FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (fluidStack.getFluid().getFluidType() == GrowthcraftMilkFluids.MILK.source.get().getFluidType()) {
            return new ItemStack(GrowthcraftMilkItems.MILK_BUCKET_IRON.get());
        }

        for (FluidRegistryContainer container : GrowthcraftMilkFluids.ALL) {
            if (container.bucket != null && fluidStack.getFluid().getFluidType() == container.source.get().getFluidType()) {
                return new ItemStack(container.bucket.get());
            }
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack getEmptyBucketRemainder(ItemStack stack) {
        ItemStack remainder = stack.getItem().getCraftingRemainder().create();
        return remainder.isEmpty() ? new ItemStack(Items.BUCKET) : remainder;
    }

    private static void replaceHeldItem(Player player, InteractionHand hand, ItemStack heldStack, ItemStack replacement) {
        if (heldStack.getCount() == 1) {
            player.setItemInHand(hand, replacement);
            return;
        }

        heldStack.shrink(1);
        if (!player.addItem(replacement)) {
            player.drop(replacement, false);
        }
    }

    private static void togglePlunger(Level level, BlockPos pos, BlockState state, ChurnBlockEntity churn) {
        boolean plunged = state.getValue(PLUNGED);
        if (plunged) {
            level.playSound(null, pos, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
        } else {
            level.playSound(null, pos, SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.BLOCKS, 1.0F, 1.0F);
            churn.tryPlunger();
        }
        level.setBlock(pos, state.setValue(PLUNGED, !plunged), Block.UPDATE_ALL);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof ChurnBlockEntity churn) {
            Containers.dropContents(level, pos, churn);
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChurnBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : type == GrowthcraftMilkBlockEntities.CHURN.get()
                ? (tickerLevel, pos, tickerState, blockEntity) -> {
                    if (blockEntity instanceof ChurnBlockEntity churn) {
                        ChurnBlockEntity.serverTick(tickerLevel, pos, tickerState, churn);
                    }
                }
                : null;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }
}
