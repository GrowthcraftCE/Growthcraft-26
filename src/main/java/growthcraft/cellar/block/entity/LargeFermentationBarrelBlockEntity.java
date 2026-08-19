package growthcraft.cellar.block.entity;

import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.block.LargeBarrelPart;
import growthcraft.cellar.block.LargeFermentationBarrelBlock;
import growthcraft.cellar.config.GrowthcraftCellarConfig;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;

public class LargeFermentationBarrelBlockEntity extends FermentationBarrelBlockEntity {
    public static final int TANK_CAPACITY = 32_000;

    public LargeFermentationBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftCellarBlockEntities.LARGE_FERMENTATION_BARREL.get(), pos, state, TANK_CAPACITY);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.growthcraft_cellar.large_fermentation_barrel");
    }

    @Override
    public boolean allowsManualUnlock() {
        return GrowthcraftCellarConfig.isLargeFermentationBarrelManualUnlockAllowed();
    }

    @Override
    protected boolean hasPauseSignal(Level level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(LargeFermentationBarrelBlock.FACING);
        for (LargeBarrelPart part : LargeBarrelPart.values()) {
            if (level.hasNeighborSignal(part.fromController(pos, facing))) {
                return true;
            }
        }
        return false;
    }
}
