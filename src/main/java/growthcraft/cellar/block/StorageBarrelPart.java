package growthcraft.cellar.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum StorageBarrelPart implements StringRepresentable {
    BOTTOM_NEAR_LEFT(0, 0, 0), BOTTOM_NEAR_CENTER(1, 0, 0), BOTTOM_NEAR_RIGHT(2, 0, 0),
    BOTTOM_CENTER_LEFT(0, 0, 1), BOTTOM_CENTER(1, 0, 1), BOTTOM_CENTER_RIGHT(2, 0, 1),
    BOTTOM_FAR_LEFT(0, 0, 2), BOTTOM_FAR_CENTER(1, 0, 2), BOTTOM_FAR_RIGHT(2, 0, 2),
    MIDDLE_NEAR_LEFT(0, 1, 0), MIDDLE_NEAR_CENTER(1, 1, 0), MIDDLE_NEAR_RIGHT(2, 1, 0),
    MIDDLE_CENTER_LEFT(0, 1, 1), MIDDLE_CENTER(1, 1, 1), MIDDLE_CENTER_RIGHT(2, 1, 1),
    MIDDLE_FAR_LEFT(0, 1, 2), MIDDLE_FAR_CENTER(1, 1, 2), MIDDLE_FAR_RIGHT(2, 1, 2),
    TOP_NEAR_LEFT(0, 2, 0), TOP_NEAR_CENTER(1, 2, 0), TOP_NEAR_RIGHT(2, 2, 0),
    TOP_CENTER_LEFT(0, 2, 1), TOP_CENTER(1, 2, 1), TOP_CENTER_RIGHT(2, 2, 1),
    TOP_FAR_LEFT(0, 2, 2), TOP_FAR_CENTER(1, 2, 2), TOP_FAR_RIGHT(2, 2, 2);

    private final int rightOffset;
    private final int upOffset;
    private final int forwardOffset;
    private final String serializedName;

    StorageBarrelPart(int rightOffset, int upOffset, int forwardOffset) {
        this.rightOffset = rightOffset;
        this.upOffset = upOffset;
        this.forwardOffset = forwardOffset;
        this.serializedName = name().toLowerCase(Locale.ROOT);
    }

    public boolean isController() {
        return this == BOTTOM_NEAR_LEFT;
    }

    public int rightOffset() {
        return rightOffset;
    }

    public int upOffset() {
        return upOffset;
    }

    public int forwardOffset() {
        return forwardOffset;
    }

    public BlockPos fromController(BlockPos controllerPos, Direction forward) {
        return controllerPos.relative(forward.getClockWise(), rightOffset)
                .above(upOffset)
                .relative(forward, forwardOffset);
    }

    public BlockPos controllerFrom(BlockPos partPos, Direction forward) {
        return partPos.relative(forward.getClockWise(), -rightOffset)
                .below(upOffset)
                .relative(forward, -forwardOffset);
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }
}
