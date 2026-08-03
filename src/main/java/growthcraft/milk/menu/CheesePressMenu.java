package growthcraft.milk.menu;

import growthcraft.milk.block.entity.CheesePressBlockEntity;
import growthcraft.milk.init.GrowthcraftMilkMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CheesePressMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;

    public CheesePressMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(CheesePressBlockEntity.SLOT_COUNT), new SimpleContainerData(3));
    }

    public CheesePressMenu(int containerId, Inventory inventory, CheesePressBlockEntity press) {
        this(containerId, inventory, press, new ContainerData() {
            @Override public int get(int index) {
                return switch (index) {
                    case 0 -> press.getProcessTime();
                    case 1 -> press.getProcessTimeTotal();
                    case 2 -> press.isOpen() ? 1 : 0;
                    default -> 0;
                };
            }
            @Override public void set(int index, int value) {}
            @Override public int getCount() { return 3; }
        });
    }

    private CheesePressMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(GrowthcraftMilkMenus.CHEESE_PRESS.get(), containerId);
        this.container = container;
        this.data = data;
        checkContainerSize(container, CheesePressBlockEntity.SLOT_COUNT);
        checkContainerDataCount(data, 3);

        addSlot(new Slot(container, CheesePressBlockEntity.SLOT_INPUT, 51, 34));
        addSlot(new Slot(container, CheesePressBlockEntity.SLOT_OUTPUT, 104, 34) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
        });
        addPlayerSlots(inventory);
        addDataSlots(data);
    }

    private void addPlayerSlots(Inventory inventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        }
        for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col, 8 + col * 18, 142));
    }

    public int getProgressionScaled(int pixels) {
        int total = data.get(1);
        return total <= 0 ? 0 : Math.min(pixels, data.get(0) * pixels / total);
    }

    public int getPercentProgress() {
        return getProgressionScaled(100);
    }

    public boolean isOpen() { return data.get(2) == 1; }

    @Override public boolean stillValid(Player player) { return container.stillValid(player); }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack original = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return original;
        ItemStack stack = slot.getItem();
        original = stack.copy();
        int machineSlots = CheesePressBlockEntity.SLOT_COUNT;
        if (index < machineSlots) {
            if (!moveItemStackTo(stack, machineSlots, slots.size(), true)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(stack, CheesePressBlockEntity.SLOT_INPUT, CheesePressBlockEntity.SLOT_INPUT + 1, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return original;
    }
}
