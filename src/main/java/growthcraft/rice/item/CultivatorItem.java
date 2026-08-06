package growthcraft.rice.item;

import growthcraft.rice.init.GrowthcraftRiceBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class CultivatorItem extends HoeItem {
    public CultivatorItem() {
        this(new Item.Properties());
    }

    public CultivatorItem(Item.Properties properties) {
        super(ToolMaterial.IRON, -2.0F, -1.0F, properties.stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (context.getClickedFace() != Direction.DOWN
                && level.getBlockState(pos.above()).isAir()
                && level.getBlockState(pos).is(Blocks.FARMLAND)) {
            Player player = context.getPlayer();
            BlockState cultivatedFarmland = GrowthcraftRiceBlocks.CULTIVATED_FARMLAND.get().defaultBlockState();
            level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (level instanceof ServerLevel serverLevel) {
                level.setBlock(pos, cultivatedFarmland, Block.UPDATE_ALL);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, cultivatedFarmland));
                if (player != null) {
                    EquipmentSlot slot = context.getHand() == net.minecraft.world.InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                    context.getItemInHand().hurtAndBreak(1, serverLevel, player, item -> player.onEquippedItemBroken(item, slot));
                }
            }

            return (level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
        }

        return super.useOn(context);
    }
}
