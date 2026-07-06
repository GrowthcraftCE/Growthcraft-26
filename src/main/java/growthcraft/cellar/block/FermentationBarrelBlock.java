package growthcraft.cellar.block;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.block.entity.FermentationBarrelBlockEntity;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
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
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

public class FermentationBarrelBlock extends Block implements EntityBlock {
    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;

    public FermentationBarrelBlock() {
        this(Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)
                .noOcclusion());
    }

    public FermentationBarrelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
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

        if (FluidUtil.getFluidHandler(heldStack).isPresent() && FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection())) {
            return (level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    private boolean isBottleForBarrel(ItemStack stack) {
        return stack.is(Items.GLASS_BOTTLE)
                || stack.getItem() instanceof BottleItem
                || stack.is(GrowthcraftCellarItems.POTION_ALE.get())
                || stack.is(GrowthcraftCellarItems.POTION_LAGER.get())
                || stack.is(GrowthcraftCellarItems.POTION_WINE.get());
    }

    private void fillBottleFromBarrel(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
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
}
