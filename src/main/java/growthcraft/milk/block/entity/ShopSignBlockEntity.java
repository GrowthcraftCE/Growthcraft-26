package growthcraft.milk.block.entity;

import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ShopSignBlockEntity extends SignBlockEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public ShopSignBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftMilkBlockEntities.SHOP_SIGN.get(), pos, state);
    }

    public ItemStack getItem() {
        return items.get(0);
    }

    public void setItem(ItemStack stack) {
        ItemStack displayStack = stack.copy();
        displayStack.setCount(1);
        displayStack.remove(net.minecraft.core.component.DataComponents.ENCHANTMENTS);
        items.set(0, displayStack);
        setChangedAndUpdate();
    }

    @Override
    public boolean setWaxed(boolean waxed) {
        boolean changed = super.setWaxed(waxed);
        setChangedAndUpdate();
        return changed;
    }

    private void setChangedAndUpdate() {
        setChanged();
        Level level = getLevel();
        if (level != null && !level.isClientSide()) {
            BlockState state = getBlockState();
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
        }
    }



    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        items.clear();
        ContainerHelper.loadAllItems(input, items);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
    }

    @Override
    public int getTextLineHeight() {
        return 9;
    }

    @Override
    public int getMaxTextLineWidth() {
        return 60;
    }
}
