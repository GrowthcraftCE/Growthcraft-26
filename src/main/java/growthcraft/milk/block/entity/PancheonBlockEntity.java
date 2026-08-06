package growthcraft.milk.block.entity;

import growthcraft.lib.util.FluidTankPersistence;
import growthcraft.lib.recipe.RecipeLookup;

import growthcraft.milk.block.PancheonBlock;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.GrowthcraftMilkRecipes;
import growthcraft.milk.menu.PancheonMenu;
import growthcraft.milk.recipe.PancheonRecipe;
import growthcraft.milk.recipe.input.PancheonInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.Optional;

public class PancheonBlockEntity extends BlockEntity implements MenuProvider {
    public static final int INPUT_TANK_CAPACITY = 2000;
    public static final int OUTPUT_TANK_CAPACITY = 1000;

    private final FluidTank inputTank = createTank(INPUT_TANK_CAPACITY);
    private final FluidTank outputTank0 = createTank(OUTPUT_TANK_CAPACITY);
    private final FluidTank outputTank1 = createTank(OUTPUT_TANK_CAPACITY);
    private final IFluidHandler fluidHandler = new PancheonFluidHandler();

    private int processTime;
    private int processTimeTotal;

    public PancheonBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftMilkBlockEntities.PANCHEON.get(), pos, state);
    }

    private FluidTank createTank(int capacity) {
        return new FluidTank(capacity) {
            @Override
            protected void onContentsChanged() {
                setChangedAndUpdate();
            }
        };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, PancheonBlockEntity pancheon) {
        if (pancheon.inputTank.isEmpty()) {
            pancheon.resetProgress();
            return;
        }

        Optional<RecipeHolder<PancheonRecipe>> match = pancheon.findMatch(level);
        if (match.isEmpty()) {
            pancheon.resetProgress();
            return;
        }

        PancheonRecipe recipe = match.get().value();
        FluidStack output0 = recipe.getOutputFluidStack(0);
        FluidStack output1 = recipe.getOutputFluidStack(1);
        if (output0.isEmpty() || output1.isEmpty() || !pancheon.outputsCanAccept(output0, output1)) {
            pancheon.resetProgress();
            return;
        }

        pancheon.processTimeTotal = recipe.getProcessingTime();
        pancheon.processTime++;
        if (pancheon.processTime >= pancheon.processTimeTotal) {
            pancheon.completeRecipe(level, pos, state, recipe, output0, output1);
        } else if ((pancheon.processTime & 15) == 0) {
            pancheon.setChanged();
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, PancheonBlockEntity pancheon) {
        RandomSource random = level.getRandom();
        if (random.nextFloat() < 0.11F) {
            PancheonBlock.makeParticles(level, pos, state);
        }
    }

    private Optional<RecipeHolder<PancheonRecipe>> findMatch(Level level) {
        PancheonInput input = new PancheonInput(inputTank.getFluid());
        return RecipeLookup.getAll(level, GrowthcraftMilkRecipes.PANCHEON_TYPE.get()).stream()
                .filter(holder -> holder.value().matches(input, level))
                .findFirst();
    }

    private boolean outputsCanAccept(FluidStack output0, FluidStack output1) {
        if (!outputTank0.isEmpty() || !outputTank1.isEmpty()) {
            return false;
        }
        return outputTank0.fill(output0.copy(), IFluidHandler.FluidAction.SIMULATE) == output0.getAmount()
                && outputTank1.fill(output1.copy(), IFluidHandler.FluidAction.SIMULATE) == output1.getAmount();
    }

    private void completeRecipe(Level level, BlockPos pos, BlockState state, PancheonRecipe recipe, FluidStack output0, FluidStack output1) {
        inputTank.drain(recipe.getInputFluid().amount(), IFluidHandler.FluidAction.EXECUTE);
        outputTank0.fill(output0, IFluidHandler.FluidAction.EXECUTE);
        outputTank1.fill(output1, IFluidHandler.FluidAction.EXECUTE);
        processTime = 0;
        processTimeTotal = 0;
        setChanged();
        level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
    }

    private void resetProgress() {
        if (processTime != 0 || processTimeTotal != 0) {
            processTime = 0;
            processTimeTotal = 0;
            setChangedAndUpdate();
        }
    }

    private void setChangedAndUpdate() {
        setChanged();
        if (level != null && !level.isClientSide()) {
            BlockState state = getBlockState();
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
        }
    }

    public boolean isProcessing() {
        return processTime > 0 && processTimeTotal > 0;
    }

    public int getProcessTime() {
        return processTime;
    }

    public int getProcessTimeTotal() {
        return processTimeTotal;
    }

    public FluidTank getInputTank() {
        return inputTank;
    }

    public FluidTank getOutputTank0() {
        return outputTank0;
    }

    public FluidTank getOutputTank1() {
        return outputTank1;
    }

    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.growthcraft_milk.pancheon");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new PancheonMenu(containerId, playerInventory, this);
    }


    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        FluidTankPersistence.load(input, "InputTank", this.inputTank);
        FluidTankPersistence.load(input, "OutputTank0", this.outputTank0);
        FluidTankPersistence.load(input, "OutputTank1", this.outputTank1);
        this.processTime = input.getIntOr("ProcessTime", 0);
        this.processTimeTotal = input.getIntOr("ProcessTimeTotal", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        FluidTankPersistence.save(output, "InputTank", this.inputTank);
        FluidTankPersistence.save(output, "OutputTank0", this.outputTank0);
        FluidTankPersistence.save(output, "OutputTank1", this.outputTank1);
        output.putInt("ProcessTime", this.processTime);
        output.putInt("ProcessTimeTotal", this.processTimeTotal);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }


    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }



    private class PancheonFluidHandler implements IFluidHandler {
        @Override
        public int getTanks() {
            return 3;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return getTank(tank).getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return getTank(tank).getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return tank == 0 && inputTank.isFluidValid(stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (!outputTank0.isEmpty() || !outputTank1.isEmpty()) {
                return 0;
            }
            return inputTank.fill(resource, action);
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            FluidStack drained = outputTank0.drain(resource, action);
            if (!drained.isEmpty()) {
                return drained;
            }
            drained = outputTank1.drain(resource, action);
            if (!drained.isEmpty() || isProcessing()) {
                return drained;
            }
            return inputTank.drain(resource, action);
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            FluidStack drained = outputTank0.drain(maxDrain, action);
            if (!drained.isEmpty()) {
                return drained;
            }
            drained = outputTank1.drain(maxDrain, action);
            if (!drained.isEmpty() || isProcessing()) {
                return drained;
            }
            return inputTank.drain(maxDrain, action);
        }

        private FluidTank getTank(int tank) {
            return switch (tank) {
                case 1 -> outputTank0;
                case 2 -> outputTank1;
                default -> inputTank;
            };
        }
    }
}
