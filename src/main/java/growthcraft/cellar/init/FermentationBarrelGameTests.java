package growthcraft.cellar.init;

import com.mojang.authlib.GameProfile;
import growthcraft.cellar.block.entity.FermentationBarrelBlockEntity;
import growthcraft.cellar.block.entity.LargeFermentationBarrelBlockEntity;
import growthcraft.cellar.config.GrowthcraftCellarConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import growthcraft.cellar.menu.FermentationBarrelMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/** Verifies GUI controls and inventory locks without changing hopper auto-start behavior. */
public final class FermentationBarrelGameTests {
    private FermentationBarrelGameTests() {}

    public static void feedbackControlsAndHoppers(GameTestHelper helper) {
        ModConfigSpec.BooleanValue setting = GrowthcraftCellarConfig.SPEC.getValues().get("fermentation_barrel.allow_manual_unlock");
        boolean previous = setting.get();
        try {
            var small = new FermentationBarrelBlockEntity(BlockPos.ZERO,
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_BIRCH.get().defaultBlockState());
            var large = new LargeFermentationBarrelBlockEntity(BlockPos.ZERO,
                    GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_BIRCH.get().defaultBlockState());
            setting.set(false);
            helper.assertTrue(!small.allowsManualUnlock() && !large.allowsManualUnlock(),
                    "One disabled setting must hide and reject manual controls for both sizes");
            var dispatcher = helper.getLevel().getServer().getCommands().getDispatcher();
            for (String command : java.util.List.of("growthcraft cellar barrelManualUnlock true",
                    "growthcraft cellar largeBarrelManualUnlock false")) {
                var parsed = dispatcher.parse(command, helper.getLevel().getServer().createCommandSourceStack());
                helper.assertTrue(!parsed.getReader().canRead() && parsed.getExceptions().isEmpty(),
                        "Canonical command and legacy alias must accept settings: " + command);
            }
            setting.set(true);
            helper.assertTrue(small.allowsManualUnlock() && large.allowsManualUnlock(),
                    "One enabled setting must allow manual controls for both sizes");
            verifyFeedbackControlsAndHoppers(helper, setting);
        } finally {
            setting.set(previous);
        }
        helper.succeed();
    }

    private static void verifyFeedbackControlsAndHoppers(GameTestHelper helper, ModConfigSpec.BooleanValue setting) {
        BlockPos pos = new BlockPos(2, 2, 2);
        helper.setBlock(pos, GrowthcraftCellarBlocks.FERMENTATION_BARREL_BIRCH.get());
        helper.setBlock(pos.above(), Blocks.HOPPER.defaultBlockState().setValue(HopperBlock.FACING, Direction.DOWN));
        var level = helper.getLevel();
        var absolute = helper.absolutePos(pos);
        var barrel = (FermentationBarrelBlockEntity) level.getBlockEntity(absolute);
        var hopper = (HopperBlockEntity) level.getBlockEntity(absolute.above());
        barrel.getTank().fill(new FluidStack(GrowthcraftCellarFluids.BROWN_WORT.source.get(), 1000), IFluidHandler.FluidAction.EXECUTE);
        hopper.setItem(0, new ItemStack(GrowthcraftCellarItems.YEAST_BREWERS.get(), 2));
        HopperBlockEntity.pushItemsTick(level, absolute.above(), hopper.getBlockState(), hopper);
        helper.assertTrue(barrel.getItem(0).getCount() == 1, "Hopper must insert yeast through the barrel's sided inventory");
        FermentationBarrelBlockEntity.serverTick(level, absolute, barrel.getBlockState(), barrel);
        helper.assertTrue(barrel.isProcessing(), "Hopper-supplied ingredients must automatically start fermentation");

        AtomicInteger overlays = new AtomicInteger();
        Player player = new Player(level, new GameProfile(UUID.randomUUID(), "barrel-test")) {
            @Override public GameType gameMode() { return GameType.SURVIVAL; }
            @Override public boolean isClientAuthoritative() { return false; }
            @Override public void sendOverlayMessage(Component message) { overlays.incrementAndGet(); }
        };
        verifyProgressTooltips(helper, player);
        var menu = new FermentationBarrelMenu(0, player.getInventory(), barrel);
        helper.assertTrue(menu.isProcessing(), "Menu must expose processing state for the yeast tooltip and stop control");
        menu.clicked(FermentationBarrelMenu.YEAST_SLOT, 0, ContainerInput.PICKUP, player);
        menu.quickMoveStack(player, FermentationBarrelMenu.YEAST_SLOT);
        helper.assertTrue(menu.getCarried().isEmpty() && barrel.getItem(0).getCount() == 1,
                "Normal and shift clicks must not remove yeast while fermenting");
        helper.assertTrue(overlays.get() == 0, "Locked yeast must not send action-bar messages behind the GUI");
        helper.assertTrue(!barrel.canTakeItemThroughFace(0, barrel.getItem(0), Direction.DOWN)
                && !barrel.canPlaceItemThroughFace(0, new ItemStack(GrowthcraftCellarItems.YEAST_BREWERS.get()), Direction.UP),
                "Automation must remain locked during processing");
        setting.set(false);
        helper.assertTrue(!menu.allowsManualUnlock() && !menu.clickMenuButton(player, 0) && barrel.isProcessing(),
                "Disabled setting must reject server-side stop requests");
        setting.set(true);
        helper.assertTrue(!menu.clickMenuButton(player, 99) && barrel.isProcessing(), "Unknown control IDs must be rejected");
        helper.assertTrue(menu.clickMenuButton(player, 0), "Stop control must be accepted");
        helper.assertTrue(!barrel.isProcessing() && barrel.isManuallyStopped() && barrel.getProcessTime() == 0,
                "Stopping must discard progress and retain the stopped state");
        helper.assertTrue(barrel.getTank().getFluidAmount() == 1000 && barrel.getItem(0).getCount() == 1,
                "Stopping must preserve fluid and yeast");
        helper.assertTrue(menu.getSlot(0).mayPickup(player), "Stopping must unlock yeast for manual editing");
        FermentationBarrelBlockEntity.serverTick(level, absolute, barrel.getBlockState(), barrel);
        helper.assertTrue(!barrel.isProcessing(), "A stopped barrel must wait for start or ingredient edits");
        setting.set(false);
        FermentationBarrelBlockEntity.serverTick(level, absolute, barrel.getBlockState(), barrel);
        helper.assertTrue(barrel.isProcessing() && !barrel.isManuallyStopped(),
                "Disabling manual controls must release a manually stopped standard barrel");
        setting.set(true);
        menu.clickMenuButton(player, 0);
        helper.assertTrue(menu.clickMenuButton(player, 0), "Start control must be accepted");
        FermentationBarrelBlockEntity.serverTick(level, absolute, barrel.getBlockState(), barrel);
        helper.assertTrue(barrel.isProcessing() && !barrel.isManuallyStopped() && !menu.getSlot(0).mayPickup(player),
                "Starting must restart processing and lock the contents");
        menu.clickMenuButton(player, 0);
        helper.assertTrue(barrel.removeItem(0, 1).getCount() == 1, "Yeast must be removable after stopping");
        // The hopper still contains one yeast; an inventory edit must retain existing auto-start behavior.
        hopper.setItem(0, ItemStack.EMPTY);
        helper.assertTrue(HopperBlockEntity.addItem(hopper, barrel,
                new ItemStack(GrowthcraftCellarItems.YEAST_BREWERS.get()), Direction.UP).isEmpty(),
                "Hopper insertion must still work after stopping and editing contents");
        FermentationBarrelBlockEntity.serverTick(level, absolute, barrel.getBlockState(), barrel);
        helper.assertTrue(barrel.isProcessing(), "Automation must still restart a barrel after ingredient edits");
    }
    private static void verifyProgressTooltips(GameTestHelper helper, Player player) {
        // Exercise the same synchronized menu data consumed by both barrel screens.
        var menu = new FermentationBarrelMenu(1, player.getInventory());
        helper.assertTrue(menu.getProgressTooltip().getString().equals("Not fermenting"), "Idle tooltip");
        menu.setData(2, 1800);
        menu.setData(3, 7200);
        helper.assertTrue(menu.getProgressTooltip().getString().equals("Fermenting: 25%"),
                "Progress tooltip must show percent without a countdown");
        menu.setData(7, 1);
        helper.assertTrue(menu.getProgressTooltip().getString().equals("Paused by redstone: 25%"), "Paused tooltip");
        menu.setData(7, 0);
        menu.setData(8, 1);
        menu.setData(2, 0);
        menu.setData(3, 0);
        helper.assertTrue(menu.getProgressTooltip().getString().equals("Fermentation stopped"), "Manually stopped tooltip");
    }

    public static void largeBarrelHopperPorts(GameTestHelper helper) {
        var level = helper.getLevel();
        BlockPos origin = new BlockPos(2, 1, 2);
        for (Direction facing : java.util.List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)) {
            for (var part : growthcraft.cellar.block.LargeBarrelPart.values()) {
                helper.setBlock(part.fromController(origin, facing), GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_BIRCH.get()
                        .defaultBlockState().setValue(growthcraft.cellar.block.LargeFermentationBarrelBlock.FACING, facing)
                        .setValue(growthcraft.cellar.block.LargeFermentationBarrelBlock.PART, part));
            }
            var absolute = helper.absolutePos(origin);
            var barrel = (LargeFermentationBarrelBlockEntity) level.getBlockEntity(absolute);
            barrel.getTank().fill(new FluidStack(GrowthcraftCellarFluids.BROWN_WORT.source.get(), 1000), IFluidHandler.FluidAction.EXECUTE);
            for (var part : growthcraft.cellar.block.LargeBarrelPart.values()) {
                if (part.upOffset() != 1) continue;
                BlockPos hopperPos = part.fromController(origin, facing).above();
                helper.setBlock(hopperPos, Blocks.HOPPER.defaultBlockState().setValue(HopperBlock.FACING, Direction.DOWN));
                var hopper = (HopperBlockEntity) level.getBlockEntity(helper.absolutePos(hopperPos));
                hopper.setItem(0, new ItemStack(GrowthcraftCellarItems.YEAST_BREWERS.get(), 2));
                HopperBlockEntity.pushItemsTick(level, hopper.getBlockPos(), hopper.getBlockState(), hopper);
                helper.assertTrue(barrel.getItem(0).getCount() == 1 && hopper.getItem(0).getCount() == 1,
                        "Top hopper must reach controller: " + facing + " / " + part);
                FermentationBarrelBlockEntity.serverTick(level, absolute, barrel.getBlockState(), barrel);
                helper.assertTrue(barrel.isProcessing(), "Large barrel must auto-start from hopper yeast");
                // A fresh hopper bypasses cooldown so a second attempt exercises the processing lock.
                hopper.clearContent();
                helper.setBlock(hopperPos, Blocks.AIR);
                helper.setBlock(hopperPos, Blocks.HOPPER.defaultBlockState().setValue(HopperBlock.FACING, Direction.DOWN));
                hopper = (HopperBlockEntity) level.getBlockEntity(helper.absolutePos(hopperPos));
                hopper.setItem(0, new ItemStack(GrowthcraftCellarItems.YEAST_BREWERS.get()));
                HopperBlockEntity.pushItemsTick(level, hopper.getBlockPos(), hopper.getBlockState(), hopper);
                helper.assertTrue(barrel.getItem(0).getCount() == 1 && hopper.getItem(0).getCount() == 1,
                        "Processing lock must reject top hopper insertion: " + facing + " / " + part
                                + " (barrel=" + barrel.getItem(0).getCount() + ", hopper=" + hopper.getItem(0).getCount() + ")");
                hopper.clearContent();
                helper.setBlock(hopperPos, Blocks.AIR);
                barrel.cancelProcessing();
                barrel.clearContent();
            }
            for (var part : growthcraft.cellar.block.LargeBarrelPart.values()) {
                helper.setBlock(part.fromController(origin, facing), Blocks.AIR);
            }
        }
        helper.succeed();
    }

}
