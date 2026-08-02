package growthcraft.cellar.block.support;

import growthcraft.cellar.block.GrapeVineStemBlock;
import growthcraft.cellar.block.GrapeVineLeavesBlock;
import growthcraft.cellar.block.HopsCropBlock;
import growthcraft.core.init.GrowthcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

public final class VineGrowthHelper {
    private static final int GRAPE_EXPANSION_DISTANCE = 5;
    private static final int GRAPE_SURVIVAL_DISTANCE = 8;
    private static final int HOPS_MAX_DISTANCE_FROM_GROUND = 6;

    private VineGrowthHelper() {}

    @Nullable
    public static Direction tryGrapeLeavesExpand(Level level, BlockPos pos) {
        if (!isConnectedToGrapeStem(level, pos, GRAPE_EXPANSION_DISTANCE, null)) return null;
        Direction[] directions = { Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.DOWN };
        int horizontalOffset = level.getRandom().nextInt(4);
        for (int index = 0; index < directions.length; index++) {
            Direction direction = directions[index];
            if (index >= 1 && index <= 4) direction = directions[1 + (index - 1 + horizontalOffset) % 4];
            if (level.getBlockState(pos.relative(direction)).is(GrowthcraftBlocks.ROPE_LINEN.get())) return direction;
        }
        return null;
    }

    public static boolean canGrapeLeavesSurvive(LevelReader level, BlockPos pos) {
        return isConnectedToGrapeStem(level, pos, GRAPE_SURVIVAL_DISTANCE, null);
    }

    @Nullable
    public static Direction tryHopsExpand(Level level, BlockPos pos) {
        if (isConnectedToGround(level, pos, HOPS_MAX_DISTANCE_FROM_GROUND - 1, null)
                && level.getBlockState(pos.above()).is(GrowthcraftBlocks.ROPE_LINEN.get())) return Direction.UP;
        return null;
    }

    public static boolean canHopsSurvive(LevelReader level, BlockPos pos) {
        return isConnectedToGround(level, pos, HOPS_MAX_DISTANCE_FROM_GROUND, null);
    }

    private static boolean isConnectedToGrapeStem(LevelReader level, BlockPos pos, int remaining,
                                                   @Nullable Direction skip) {
        for (Direction direction : Direction.values()) {
            if (level.getBlockState(pos.relative(direction)).getBlock() instanceof GrapeVineStemBlock) return true;
        }
        if (remaining < 2) return false;
        for (Direction direction : Direction.values()) {
            if (direction != skip && level.getBlockState(pos.relative(direction)).getBlock() instanceof GrapeVineLeavesBlock
                    && isConnectedToGrapeStem(level, pos.relative(direction), remaining - 1, direction.getOpposite())) return true;
        }
        return false;
    }

    private static boolean isConnectedToGround(LevelReader level, BlockPos pos, int remaining,
                                               @Nullable Direction skip) {
        if (level.getBlockState(pos.below()).is(Tags.Blocks.VILLAGER_FARMLANDS)) return true;
        if (remaining < 2) return false;
        for (Direction direction : Direction.values()) {
            if (direction != skip && level.getBlockState(pos.relative(direction)).getBlock() instanceof HopsCropBlock
                    && isConnectedToGround(level, pos.relative(direction), remaining - 1, direction.getOpposite())) return true;
        }
        return false;
    }
}
