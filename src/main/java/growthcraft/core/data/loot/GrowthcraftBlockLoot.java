package growthcraft.core.data.loot;

import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.GrowthcraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.stream.Collectors;

public class GrowthcraftBlockLoot extends BlockLootSubProvider {
    public GrowthcraftBlockLoot(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    protected void generate() {
        // Generate loot tables for Growthcraft blocks
        // Rope block should drop the rope item (since there is no BlockItem for the rope block)
        this.add(GrowthcraftBlocks.ROPE_LINEN.get(), createSingleItemTable(GrowthcraftItems.ROPE_LINEN.get()));

        // Salt ore tables are authored resources because they drop salt with fortune/silk-touch behavior.
        // Do not generate competing self-drop tables for them.
        this.dropSelf(GrowthcraftBlocks.SALT_BLOCK.get());

        // Rope Linen Fence variants
        this.dropRope(GrowthcraftBlocks.ROPE_LINEN_OAK_FENCE.get());
        this.dropRope(GrowthcraftBlocks.ROPE_LINEN_SPRUCE_FENCE.get());
        this.dropRope(GrowthcraftBlocks.ROPE_LINEN_BIRCH_FENCE.get());
        this.dropRope(GrowthcraftBlocks.ROPE_LINEN_JUNGLE_FENCE.get());
        this.dropRope(GrowthcraftBlocks.ROPE_LINEN_DARK_OAK_FENCE.get());
        this.dropRope(GrowthcraftBlocks.ROPE_LINEN_ACACIA_FENCE.get());
        this.dropRope(GrowthcraftBlocks.ROPE_LINEN_MANGROVE_FENCE.get());
        this.dropRope(GrowthcraftBlocks.ROPE_LINEN_CHERRY_FENCE.get());
        this.dropRope(GrowthcraftBlocks.ROPE_LINEN_BAMBOO_FENCE.get());
        this.dropRope(GrowthcraftBlocks.ROPE_LINEN_NETHER_BRICK_FENCE.get());
        this.dropRope(GrowthcraftBlocks.ROPE_LINEN_CRIMSON_FENCE.get());
        this.dropRope(GrowthcraftBlocks.ROPE_LINEN_WARPED_FENCE.get());
    }

    private void dropRope(Block block) {
        this.add(block, createSingleItemTable(GrowthcraftItems.ROPE_LINEN.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GrowthcraftBlocks.BLOCKS.getEntries().stream()
                .map(entry -> entry.get())
                .filter(block -> block != GrowthcraftBlocks.SALT_ORE.get())
                .filter(block -> block != GrowthcraftBlocks.SALT_ORE_DEEPSLATE.get())
                .filter(block -> block != GrowthcraftBlocks.SALT_ORE_NETHER.get())
                .filter(block -> block != GrowthcraftBlocks.SALT_ORE_END.get())
                .collect(Collectors.toList());
    }
}
