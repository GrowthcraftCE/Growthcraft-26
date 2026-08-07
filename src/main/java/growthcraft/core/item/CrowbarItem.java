package growthcraft.core.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

/**
 * Crowbar item that behaves like a sword. The attack damage adjustment (-2) is
 * applied via an OffsetTier wrapper at registration time, allowing vanilla to
 * generate dynamic attribute tooltips correctly.
 */
public class CrowbarItem extends Item {
    public CrowbarItem(ToolMaterial tier, Properties properties) {
        super(properties.sword(tier, 3.0F, -2.4F));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);

        tooltip.accept(Component.translatable("tooltip.growthcraft.crowbar").withStyle(ChatFormatting.GRAY));
    }
}
