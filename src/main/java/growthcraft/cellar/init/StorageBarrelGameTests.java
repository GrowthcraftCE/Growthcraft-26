package growthcraft.cellar.init;

import growthcraft.cellar.block.LargeStorageBarrelBlock;
import growthcraft.cellar.block.StorageBarrelPart;
import growthcraft.cellar.block.entity.LargeStorageBarrelBlockEntity;
import growthcraft.lib.utils.SequentialFluidGauge;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/** Display segments must continue to represent one tank shared by every port. */
public final class StorageBarrelGameTests {
    private StorageBarrelGameTests() {}

    public static void sharedTankAutomation(GameTestHelper helper) {
        BlockPos origin = new BlockPos(3, 1, 3);
        var level = helper.getLevel();
        var block = GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_OAK.get();
        var water = FluidResource.of(new FluidStack(Fluids.WATER, 1));
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            for (StorageBarrelPart part : StorageBarrelPart.values()) {
                helper.setBlock(part.fromController(origin, facing), block.defaultBlockState()
                        .setValue(LargeStorageBarrelBlock.FACING, facing).setValue(LargeStorageBarrelBlock.PART, part));
            }
            var barrel = (LargeStorageBarrelBlockEntity) level.getBlockEntity(helper.absolutePos(origin));
            helper.assertTrue(barrel.getTank().fill(new FluidStack(Fluids.WATER, 48000), IFluidHandler.FluidAction.EXECUTE) == 48000,
                    "Storage barrel must retain one 128 B tank");
            for (StorageBarrelPart part : StorageBarrelPart.values()) {
                for (Direction side : Direction.values()) {
                    var port = level.getCapability(Capabilities.Fluid.BLOCK,
                            helper.absolutePos(part.fromController(origin, facing)), side);
                    helper.assertTrue(port != null && port.size() == 1 && port.getCapacityAsLong(0, water) == 128000,
                            "Every part/side must expose the same single tank: " + facing + " / " + part + " / " + side);
                    try (var transaction = Transaction.openRoot()) {
                        helper.assertTrue(port.insert(0, water, 1000, transaction) == 1000, "Automation must insert fluid");
                        transaction.commit();
                    }
                    helper.assertTrue(barrel.getTank().getFluidAmount() == 49000, "Insertion must reach the controller");
                    try (var transaction = Transaction.openRoot()) {
                        helper.assertTrue(port.extract(0, water, 1000, transaction) == 1000, "Automation must extract fluid");
                        transaction.commit();
                    }
                    helper.assertTrue(barrel.getTank().getFluidAmount() == 48000, "Extraction must reach the controller");
                }
            }
            helper.assertTrue(SequentialFluidGauge.amount(barrel.getTank().getFluidAmount(), 128000, 4, 0) == 32000
                            && SequentialFluidGauge.amount(barrel.getTank().getFluidAmount(), 128000, 4, 1) == 16000,
                    "Automation contents must display as one full gauge and one half-full gauge");
            for (StorageBarrelPart part : StorageBarrelPart.values()) {
                helper.setBlock(part.fromController(origin, facing), Blocks.AIR);
            }
        }
        helper.succeed();
    }
}
