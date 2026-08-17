package growthcraft.cellar.block.entity;

import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.menu.LargeStorageBarrelMenu;
import growthcraft.lib.util.FluidTankPersistence;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class LargeStorageBarrelBlockEntity extends BlockEntity implements MenuProvider {
    public static final int TANK_CAPACITY = 128_000;

    private final FluidTank tank = new FluidTank(TANK_CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide()) {
                BlockState state = getBlockState();
                level.sendBlockUpdated(worldPosition, state, state, 3);
            }
        }
    };

    public LargeStorageBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftCellarBlockEntities.LARGE_STORAGE_BARREL.get(), pos, state);
    }

    public FluidTank getTank() {
        return tank;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.growthcraft_cellar.large_storage_barrel");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new LargeStorageBarrelMenu(containerId, inventory, this);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        FluidTankPersistence.load(input, "Tank", tank);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        FluidTankPersistence.save(output, "Tank", tank);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
