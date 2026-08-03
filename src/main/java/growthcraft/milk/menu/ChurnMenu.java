package growthcraft.milk.menu;

import growthcraft.milk.block.entity.ChurnBlockEntity;
import growthcraft.milk.init.GrowthcraftMilkMenus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public class ChurnMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;

    public ChurnMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(ChurnBlockEntity.SLOT_COUNT), new SimpleContainerData(4));
    }

    public ChurnMenu(int containerId, Inventory inventory, ChurnBlockEntity churn) {
        this(containerId, inventory, churn, new ContainerData() {
            @Override public int get(int index) {
                return switch (index) {
                    case 0 -> churn.getTank().getFluidAmount();
                    case 1 -> churn.getTank().isEmpty() ? -1 : BuiltInRegistries.FLUID.getId(churn.getTank().getFluid().getFluid());
                    case 2 -> churn.getPlungeCount();
                    case 3 -> churn.getPlungesNeeded();
                    default -> 0;
                };
            }
            @Override public void set(int index, int value) {}
            @Override public int getCount() { return 4; }
        });
    }

    private ChurnMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(GrowthcraftMilkMenus.CHURN.get(), containerId);
        this.container = container;
        this.data = data;
        checkContainerSize(container, ChurnBlockEntity.SLOT_COUNT);
        checkContainerDataCount(data, 4);
        addSlot(new Slot(container, ChurnBlockEntity.SLOT_BYPRODUCT, 93, 35) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
        });
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        }
        for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col, 8 + col * 18, 142));
        addDataSlots(data);
    }

    public FluidStack getFluidStack() {
        int id = data.get(1), amount = data.get(0);
        if (id < 0 || amount <= 0 || BuiltInRegistries.FLUID.byId(id) == null) return FluidStack.EMPTY;
        return new FluidStack(BuiltInRegistries.FLUID.byId(id), amount);
    }

    public int getTankCapacity() { return ChurnBlockEntity.TANK_CAPACITY; }
    public int getPlungeCount() { return data.get(2); }
    public int getPlungesNeeded() { return data.get(3); }

    @Override public boolean stillValid(Player player) { return container.stillValid(player); }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem() || index >= ChurnBlockEntity.SLOT_COUNT) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (!moveItemStackTo(stack, ChurnBlockEntity.SLOT_COUNT, slots.size(), true)) return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        slot.onTake(player, stack);
        return original;
    }
}
