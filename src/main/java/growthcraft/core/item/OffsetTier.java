package growthcraft.core.item;

import net.minecraft.world.item.ToolMaterial;

/**
 * Tool material helpers used to preserve Growthcraft's balanced tool stats.
 */
public final class OffsetTier {
    /**
     * Shared constant used by Growthcraft's crowbar variants. Named after the original field.
     * Note: despite the name, the configured offset is -1.0f to match current balance.
     */
    public static final ToolMaterial IRON_MINUS2 = offsetAttackDamage(ToolMaterial.IRON, -1.0F);

    private OffsetTier() {
    }

    private static ToolMaterial offsetAttackDamage(ToolMaterial base, float attackDamageOffset) {
        return new ToolMaterial(
                base.incorrectBlocksForDrops(),
                base.durability(),
                base.speed(),
                base.attackDamageBonus() + attackDamageOffset,
                base.enchantmentValue(),
                base.repairItems());
    }
}
