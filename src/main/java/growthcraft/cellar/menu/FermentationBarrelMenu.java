package growthcraft.cellar.menu;

import growthcraft.cellar.block.entity.FermentationBarrelBlockEntity;
import growthcraft.cellar.init.GrowthcraftCellarMenus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class FermentationBarrelMenu extends AbstractContainerMenu {
    public static final int YEAST_SLOT = FermentationBarrelBlockEntity.SLOT_YEAST;

    private final Container container;
    @Nullable
    private final FermentationBarrelBlockEntity barrelBE;

    private int clientFluidAmount;
    private int clientFluidId = -1;
    private int clientProcess;
    private int clientProcessTotal;
    private int clientYeastWarning;
    private int clientYeastError;
    private int clientTankCapacity = FermentationBarrelBlockEntity.TANK_CAPACITY;
    private int clientRedstonePaused;
    private int clientManuallyStopped;
    private int clientManualUnlockAllowed = 1;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if (barrelBE == null) {
                return switch (index) {
                    case 0 -> clientFluidAmount;
                    case 1 -> clientFluidId;
                    case 2 -> clientProcess;
                    case 3 -> clientProcessTotal;
                    case 4 -> clientYeastWarning;
                    case 5 -> clientYeastError;
                    case 6 -> clientTankCapacity;
                    case 7 -> clientRedstonePaused;
                    case 8 -> clientManuallyStopped;
                    case 9 -> clientManualUnlockAllowed;
                    default -> 0;
                };
            }
            return switch (index) {
                case 0 -> barrelBE.getTank().getFluidAmount();
                case 1 -> barrelBE.getTank().getFluid().isEmpty() ? -1 : BuiltInRegistries.FLUID.getId(barrelBE.getTank().getFluid().getFluid());
                case 2 -> barrelBE.getProcessTime();
                case 3 -> barrelBE.getProcessTimeTotal();
                case 4 -> barrelBE.hasYeastWarning() ? 1 : 0;
                case 5 -> barrelBE.hasYeastError() ? 1 : 0;
                case 6 -> barrelBE.getTankCapacity();
                case 7 -> barrelBE.isRedstonePaused() ? 1 : 0;
                case 8 -> barrelBE.isManuallyStopped() ? 1 : 0;
                case 9 -> barrelBE.allowsManualUnlock() ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (barrelBE != null) return;
            switch (index) {
                case 0 -> clientFluidAmount = value;
                case 1 -> clientFluidId = value;
                case 2 -> clientProcess = value;
                case 3 -> clientProcessTotal = value;
                case 4 -> clientYeastWarning = value;
                case 5 -> clientYeastError = value;
                case 6 -> clientTankCapacity = value;
                case 7 -> clientRedstonePaused = value;
                case 8 -> clientManuallyStopped = value;
                case 9 -> clientManualUnlockAllowed = value;
            }
        }

        @Override
        public int getCount() {
            return 10;
        }
    };

    public FermentationBarrelMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(FermentationBarrelBlockEntity.SLOT_COUNT));
    }

    public FermentationBarrelMenu(int containerId, Inventory playerInventory, Container container) {
        super(GrowthcraftCellarMenus.FERMENTATION_BARREL.get(), containerId);
        this.container = container;
        this.barrelBE = container instanceof FermentationBarrelBlockEntity be ? be : null;

        this.addSlot(new Slot(container, YEAST_SLOT, 52, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return !isProcessing() && super.mayPlace(stack);
            }

            @Override
            public boolean mayPickup(Player player) {
                return !isProcessing() && super.mayPickup(player);
            }
        });

        int startX = 8;
        int startY = 84;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, startX + col * 18, startY + row * 18));
            }
        }

        int hotbarY = startY + 58;
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, startX + col * 18, hotbarY));
        }

        this.addDataSlots(data);
    }

    public int getTankCapacity() {
        return data.get(6);
    }

    public int getFluidAmount() {
        return data.get(0);
    }

    public FluidStack getFluidStack() {
        int id = data.get(1);
        int amount = data.get(0);
        if (id < 0 || amount <= 0) return FluidStack.EMPTY;
        var fluid = BuiltInRegistries.FLUID.byId(id);
        if (fluid == null) return FluidStack.EMPTY;
        return new FluidStack(fluid, amount);
    }

    public int getProgressionScaled(int pixels) {
        int total = data.get(3);
        if (total <= 0) return 0;
        int process = data.get(2);
        if (process <= 0) return 0;
        return Math.max(1, Math.min(pixels, (process * pixels) / total));
    }

    public int getPercentProgress() {
        int total = data.get(3);
        if (total <= 0) return 0;
        return Math.min(100, (data.get(2) * 100) / total);
    }

    public boolean hasYeastWarning() {
        return data.get(4) == 1;
    }

    public boolean hasYeastError() {
        return data.get(5) == 1;
    }

    public boolean isProcessing() {
        return data.get(2) > 0 && data.get(3) > 0;
    }

    public int getProcessTicks() {
        return data.get(2);
    }

    public boolean isRedstonePaused() {
        return data.get(7) == 1;
    }

    public boolean isManuallyStopped() {
        return data.get(8) == 1;
    }

    public boolean allowsManualUnlock() {
        return data.get(9) == 1;
    }

    public int getRemainingSeconds() {
        return Math.max(0, (data.get(3) - data.get(2) + 19) / 20);
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (buttonId != 0 || barrelBE == null || !barrelBE.allowsManualUnlock()) return false;
        if (barrelBE.isProcessing()) {
            barrelBE.cancelProcessing();
            return true;
        }
        if (barrelBE.isManuallyStopped()) {
            barrelBE.resumeProcessing();
            return true;
        }
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (isProcessing()) {
            showProcessingLocked(player, barrelBE != null && barrelBE.allowsManualUnlock());
            return ItemStack.EMPTY;
        }
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            int beSlots = 1;
            int playerInvStart = beSlots;
            int hotbarEnd = playerInvStart + 36;

            if (index < beSlots) {
                if (!this.moveItemStackTo(stackInSlot, playerInvStart, hotbarEnd, false)) return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(stackInSlot, YEAST_SLOT, YEAST_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(player, stackInSlot);
        }
        return itemstack;
    }

    @Override
    public void clicked(int slotId, int button, ContainerInput input, Player player) {
        if (isProcessing() && slotId == YEAST_SLOT) {
            showProcessingLocked(player, barrelBE != null && barrelBE.allowsManualUnlock());
            return;
        }
        super.clicked(slotId, button, input, player);
    }

    private static void showProcessingLocked(Player player, boolean manualUnlockAllowed) {
        if (!player.level().isClientSide()) {
            player.sendOverlayMessage(
                    net.minecraft.network.chat.Component.translatable(
                            manualUnlockAllowed
                                    ? "growthcraft_cellar.message.fermentation.processing_locked_unlockable"
                                    : "growthcraft_cellar.message.fermentation.processing_locked"));
        }
    }
}
