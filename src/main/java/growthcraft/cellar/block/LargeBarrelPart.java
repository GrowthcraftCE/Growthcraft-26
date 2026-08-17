package growthcraft.cellar.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

public enum LargeBarrelPart implements StringRepresentable {
    BOTTOM_NEAR_LEFT(0, 0, 0),
    BOTTOM_NEAR_RIGHT(1, 0, 0),
    BOTTOM_FAR_LEFT(0, 0, 1),
    BOTTOM_FAR_RIGHT(1, 0, 1),
    TOP_NEAR_LEFT(0, 1, 0),
    TOP_NEAR_RIGHT(1, 1, 0),
    TOP_FAR_LEFT(0, 1, 1),
    TOP_FAR_RIGHT(1, 1, 1);

    private final int rightOffset;
    private final int upOffset;
    private final int forwardOffset;
    private final String serializedName;

    LargeBarrelPart(int rightOffset, int upOffset, int forwardOffset) {
        this.rightOffset = rightOffset;
        this.upOffset = upOffset;
        this.forwardOffset = forwardOffset;
        this.serializedName = name().toLowerCase(java.util.Locale.ROOT);
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
