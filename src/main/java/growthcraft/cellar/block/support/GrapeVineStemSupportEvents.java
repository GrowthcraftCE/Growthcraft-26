package growthcraft.cellar.block.support;

import growthcraft.cellar.block.GrapeVineStemBlock;
import growthcraft.cellar.config.Reference;
import growthcraft.core.init.GrowthcraftBlocks;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

@EventBusSubscriber(modid = Reference.MODID)
public final class GrapeVineStemSupportEvents {
    private GrapeVineStemSupportEvents() {}

    /** Remove the planted stem before its farmland support disappears. */
    @SubscribeEvent
    public static void onFarmlandBreak(BreakBlockEvent event) {
        if (event.getState().is(Tags.Blocks.VILLAGER_FARMLANDS)
                && event.getLevel().getBlockState(event.getPos().above()).getBlock() instanceof GrapeVineStemBlock) {
            event.getLevel().destroyBlock(event.getPos().above(), false);
        }
    }

    /** Allow dirt directly below a rope to be tilled for grape planting. */
    @SubscribeEvent
    public static void onHoeBelowRope(PlayerInteractEvent.RightClickBlock event) {
        if (event.getFace() != Direction.UP || !event.getItemStack().canPerformAction(ItemAbilities.HOE_TILL)) return;
        if (event.getLevel().getBlockState(event.getPos()).is(BlockTags.DIRT)
                && event.getLevel().getBlockState(event.getPos().above()).is(GrowthcraftBlocks.ROPE_LINEN.get())) {
            event.getLevel().setBlock(event.getPos(), Blocks.FARMLAND.defaultBlockState(), Block.UPDATE_ALL);
            event.setCancellationResult(event.getLevel().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
            event.setCanceled(true);
        }
    }
}
