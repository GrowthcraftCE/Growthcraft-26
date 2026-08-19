package growthcraft.cellar.block;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.block.entity.FermentationBarrelBlockEntity;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import growthcraft.milk.item.GrowthcraftMilkBucketItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class FermentationBarrelBlock extends Block implements EntityBlock, LiquidBlockContainer {
    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
    private static final VoxelShape NORTH_SOUTH_SHAPE = Shapes.or(
            box(5, 0, 4, 11, 1, 12),
            box(3, 1, 0.1, 13, 2, 15.9),
            box(2, 2, 0.1, 14, 3, 15.9),
            box(1, 3, 0.1, 15, 5, 15.9),
            box(1, 5, 0.1, 15, 11, 15.9),
            box(1, 11, 0.1, 15, 13, 15.9),
            box(2, 13, 0.1, 14, 14, 15.9),
            box(3, 14, 0.1, 13, 15, 15.9),
            box(5, 15, 4, 11, 16, 12));

    public FermentationBarrelBlock() {
        this(Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)
                .noOcclusion());
    }

    public FermentationBarrelBlock(Properties properties) {
        super(properties.forceSolidOff());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    private static VoxelShape rotateShape(VoxelShape source, Direction.Axis axis) {
        if (axis == Direction.Axis.Z) {
            return source;
        }

        VoxelShape[] rotated = new VoxelShape[]{Shapes.empty()};
        source.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            if (axis == Direction.Axis.X) {
                rotated[0] = Shapes.or(rotated[0], box(
                        minZ * 16.0, minY * 16.0, minX * 16.0,
                        maxZ * 16.0, maxY * 16.0, maxX * 16.0));
            } else {
                rotated[0] = Shapes.or(rotated[0], box(
                        minX * 16.0, minZ * 16.0, minY * 16.0,
                        maxX * 16.0, maxZ * 16.0, maxY * 16.0));
            }
        });
        return rotated[0];
    }

    private VoxelShape getBarrelShape(BlockState state) {
        return rotateShape(NORTH_SOUTH_SHAPE, state.getValue(FACING).getAxis()).optimize();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getBarrelShape(state);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getBarrelShape(state);
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
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
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return useWithoutItemAt(level, pos, player);
    }

    public static InteractionResult useWithoutItemAt(Level level, BlockPos pos, Player player) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof net.minecraft.world.MenuProvider provider) {
                player.openMenu(provider);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return useItemOnAt(heldStack, state, level, pos, player, hand, hitResult.getDirection());
    }

    public static InteractionResult useItemOnAt(ItemStack heldStack, BlockState state, Level level, BlockPos pos,
                                                Player player, InteractionHand hand, Direction interactionSide) {
        boolean fluidMutation = heldStack.is(Items.MILK_BUCKET)
                || heldStack.getItem() instanceof GrowthcraftMilkBucketItem
                || isBottleForBarrel(heldStack)
                || FluidUtil.getFluidHandler(heldStack).isPresent();
        if (fluidMutation && level.getBlockEntity(pos) instanceof FermentationBarrelBlockEntity barrel
                && barrel.isProcessing()) {
            if (!level.isClientSide()) {
                player.sendOverlayMessage(
                        Component.translatable(barrel.allowsManualUnlock()
                                ? "growthcraft_cellar.message.fermentation.processing_locked_unlockable"
                                : "growthcraft_cellar.message.fermentation.processing_locked"));
            }
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        if (tryMilkBucketInteraction(heldStack, level, pos, player, hand)) {
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        if (isBottleForBarrel(heldStack)) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FermentationBarrelBlockEntity barrel) {
                ItemStack result = barrel.getResultingPotionItemStack();
                if (result.isEmpty()) {
                    if (!level.isClientSide()) {
                        GrowthcraftCellar.LOGGER.debug(
                                "[FermentationBarrel] No bottled result for held={} tank={} amount={} at {}",
                                BuiltInRegistries.ITEM.getKey(heldStack.getItem()),
                                BuiltInRegistries.FLUID.getKey(barrel.getTank().getFluid().getFluid()),
                                barrel.getTank().getFluidAmount(),
                                pos
                        );
                    }
                    return InteractionResult.TRY_WITH_EMPTY_HAND;
                }
                if (!level.isClientSide()) {
                    fillBottleFromBarrel(state, level, pos, player, hand, heldStack, barrel, result);
                }
                return (level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
            }
        }

        if (FluidUtil.getFluidHandler(heldStack).isPresent() && FluidUtil.interactWithFluidHandler(player, hand, level, pos, interactionSide)) {
            return (level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    private static boolean tryMilkBucketInteraction(ItemStack heldStack, Level level, BlockPos pos, Player player, InteractionHand hand) {
        boolean vanillaMilk = heldStack.is(Items.MILK_BUCKET);
        boolean growthcraftMilk = heldStack.getItem() instanceof GrowthcraftMilkBucketItem;
        if (!vanillaMilk && !growthcraftMilk) {
            return false;
        }

        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof FermentationBarrelBlockEntity barrel) {
            FluidStack milk = new FluidStack(GrowthcraftMilkFluids.MILK.source.get(), 1000);
            int filled = barrel.getTank().fill(milk, IFluidHandler.FluidAction.SIMULATE);
            if (filled == 1000) {
                filled = barrel.getTank().fill(milk, IFluidHandler.FluidAction.EXECUTE);
            }
            if (filled == 1000 && !player.getAbilities().instabuild) {
                ItemStack remainder = vanillaMilk
                        ? new ItemStack(Items.BUCKET)
                        : ((GrowthcraftMilkBucketItem) heldStack.getItem()).getEmptyReturnStack();
                player.setItemInHand(hand, remainder.copy());
            }
        }
        return true;
    }

    private static boolean isBottleForBarrel(ItemStack stack) {
        return stack.is(Items.GLASS_BOTTLE)
                || stack.getItem() instanceof BottleItem
                || stack.is(GrowthcraftCellarItems.POTION_ALE.get())
                || stack.is(GrowthcraftCellarItems.POTION_LAGER.get())
                || stack.is(GrowthcraftCellarItems.POTION_WINE.get());
    }

    private static void fillBottleFromBarrel(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                      ItemStack heldStack, FermentationBarrelBlockEntity barrel, ItemStack result) {
        barrel.drainBottleAmount(state);
        if (!player.getAbilities().instabuild) {
            heldStack.shrink(1);
        }

        if (heldStack.isEmpty()) {
            player.setItemInHand(hand, result);
        } else if (!player.getInventory().add(result)) {
            player.drop(result, false);
        }

        level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FermentationBarrelBlockEntity barrel) {
            Containers.dropContents(level, pos, barrel);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FermentationBarrelBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : (lvl, pos, st, be) -> {
            if (be instanceof FermentationBarrelBlockEntity barrel) {
                FermentationBarrelBlockEntity.serverTick(lvl, pos, st, barrel);
            }
        };
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
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
