package growthcraft.core.block;

import growthcraft.core.config.Reference;
import growthcraft.core.init.GrowthcraftItems;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

@EventBusSubscriber(modid = Reference.MODID)
public final class RopeBlock2SupportEvents {
    private RopeBlock2SupportEvents() {}

    /** Disconnect an adjacent wrapped rope before allowing its supporting fence to break. */
    @SubscribeEvent
    public static void onFenceBreak(BreakBlockEvent event) {
        if (!event.getState().is(BlockTags.FENCES)) return;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState rope = event.getLevel().getBlockState(event.getPos().relative(direction));
            if (rope.getBlock() instanceof RopeBlock2Base) {
                var property = RopeBlock2Base.property(direction.getOpposite());
                if (rope.getValue(property) == 2) {
                    event.getLevel().setBlock(event.getPos().relative(direction), rope.setValue(property, 0), Block.UPDATE_ALL);
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    /** Attach an existing rope to an adjacent fence without consuming another rope item. */
    @SubscribeEvent
    public static void onRopeUse(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getItemStack().is(GrowthcraftItems.ROPE_LINEN.get()) || event.getFace() == null) return;
        BlockState clicked = event.getLevel().getBlockState(event.getPos());
        Direction face = event.getFace();
        if (clicked.is(BlockTags.FENCES)) {
            BlockState adjacent = event.getLevel().getBlockState(event.getPos().relative(face));
            setFenceConnection(event, adjacent, event.getPos().relative(face), face.getOpposite());
        } else if (clicked.getBlock() instanceof RopeBlock2Base) {
            BlockState adjacent = event.getLevel().getBlockState(event.getPos().relative(face));
            if (adjacent.is(BlockTags.FENCES)) setFenceConnection(event, clicked, event.getPos(), face);
        }
    }

    private static void setFenceConnection(PlayerInteractEvent.RightClickBlock event, BlockState rope,
                                           net.minecraft.core.BlockPos pos, Direction direction) {
        if (!(rope.getBlock() instanceof RopeBlock2Base)) return;
        var property = RopeBlock2Base.property(direction);
        if (rope.getValue(property) == 0) {
            event.getLevel().setBlock(pos, rope.setValue(property, 2), Block.UPDATE_ALL);
        }
    }
}
