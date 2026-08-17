package growthcraft.cellar.menu;

import growthcraft.cellar.block.entity.LargeStorageBarrelBlockEntity;
import growthcraft.cellar.init.GrowthcraftCellarMenus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class LargeStorageBarrelMenu extends AbstractContainerMenu {
    @Nullable
    private final LargeStorageBarrelBlockEntity barrel;
    private int clientFluidAmount;
    private int clientFluidId = -1;
    private int clientCapacity = LargeStorageBarrelBlockEntity.TANK_CAPACITY;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if (barrel == null) {
                return switch (index) {
                    case 0 -> clientFluidAmount;
                    case 1 -> clientFluidId;
                    case 2 -> clientCapacity;
                    default -> 0;
                };
            }
            return switch (index) {
                case 0 -> barrel.getTank().getFluidAmount();
                case 1 -> barrel.getTank().getFluid().isEmpty()
                        ? -1 : BuiltInRegistries.FLUID.getId(barrel.getTank().getFluid().getFluid());
                case 2 -> barrel.getTank().getCapacity();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (barrel != null) return;
            switch (index) {
                case 0 -> clientFluidAmount = value;
                case 1 -> clientFluidId = value;
                case 2 -> clientCapacity = value;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public LargeStorageBarrelMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, null);
    }

    public LargeStorageBarrelMenu(int containerId, Inventory inventory,
                                  @Nullable LargeStorageBarrelBlockEntity barrel) {
        super(GrowthcraftCellarMenus.LARGE_STORAGE_BARREL.get(), containerId);
        this.barrel = barrel;
        int startX = 8;
        int startY = 84;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, col + row * 9 + 9, startX + col * 18, startY + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inventory, col, startX + col * 18, startY + 58));
        }
        addDataSlots(data);
    }

    public int getTankCapacity() {
        return data.get(2);
    }

    public FluidStack getFluidStack() {
        int id = data.get(1);
        int amount = data.get(0);
        if (id < 0 || amount <= 0) return FluidStack.EMPTY;
        var fluid = BuiltInRegistries.FLUID.byId(id);
        return fluid == null ? FluidStack.EMPTY : new FluidStack(fluid, amount);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        if (barrel == null || barrel.getLevel() == null) return true;
        if (barrel.getLevel().getBlockEntity(barrel.getBlockPos()) != barrel) return false;
        return player.distanceToSqr(barrel.getBlockPos().getCenter()) <= 64.0D;
    }
}
