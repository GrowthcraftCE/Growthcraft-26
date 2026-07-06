package growthcraft.core.init;

import growthcraft.core.block.RopeBlock;
import growthcraft.core.block.RopeFenceBlock;
import growthcraft.core.config.Reference;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GrowthcraftBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    // Rope blocks
    public static final DeferredBlock<RopeBlock> ROPE_LINEN = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROPE_LINEN,
            RopeBlock::new, () -> BlockBehaviour.Properties.of().strength(0.2F))
    ;

    // Salt blocks and ores
    public static final DeferredBlock<Block> SALT_BLOCK = BLOCKS.registerSimpleBlock(Reference.UnlocalizedName.Block.SALT_BLOCK,
            () -> BlockBehaviour.Properties.of().strength(3.0F, 6.0F))
    ;

    public static final DeferredBlock<Block> SALT_ORE = BLOCKS.registerSimpleBlock(Reference.UnlocalizedName.Block.SALT_ORE,
            () -> BlockBehaviour.Properties.of().strength(3.0F, 3.0F).requiresCorrectToolForDrops())
    ;

    public static final DeferredBlock<Block> SALT_ORE_DEEPSLATE = BLOCKS.registerSimpleBlock("salt_ore_deepslate",
            () -> BlockBehaviour.Properties.of().strength(4.5F, 3.0F).requiresCorrectToolForDrops())
    ;

    public static final DeferredBlock<Block> SALT_ORE_NETHER = BLOCKS.registerSimpleBlock("salt_ore_nether",
            () -> BlockBehaviour.Properties.of().strength(3.0F, 3.0F).requiresCorrectToolForDrops())
    ;

    public static final DeferredBlock<Block> SALT_ORE_END = BLOCKS.registerSimpleBlock("salt_ore_end",
            () -> BlockBehaviour.Properties.of().strength(3.0F, 3.0F).requiresCorrectToolForDrops())
    ;

    // Rope Linen Fence variants
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_OAK_FENCE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROPE_LINEN_OAK_FENCE,
            RopeFenceBlock::new, () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_SPRUCE_FENCE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROPE_LINEN_SPRUCE_FENCE,
            RopeFenceBlock::new, () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_BIRCH_FENCE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROPE_LINEN_BIRCH_FENCE,
            RopeFenceBlock::new, () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_JUNGLE_FENCE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROPE_LINEN_JUNGLE_FENCE,
            RopeFenceBlock::new, () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_DARK_OAK_FENCE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROPE_LINEN_DARK_OAK_FENCE,
            RopeFenceBlock::new, () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_ACACIA_FENCE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROPE_LINEN_ACACIA_FENCE,
            RopeFenceBlock::new, () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_MANGROVE_FENCE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROPE_LINEN_MANGROVE_FENCE,
            RopeFenceBlock::new, () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_CHERRY_FENCE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROPE_LINEN_CHERRY_FENCE,
            RopeFenceBlock::new, () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_BAMBOO_FENCE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROPE_LINEN_BAMBOO_FENCE,
            RopeFenceBlock::new, () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_NETHER_BRICK_FENCE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROPE_LINEN_NETHER_BRICK_FENCE,
            RopeFenceBlock::new, () -> BlockBehaviour.Properties.of().strength(2.0F, 6.0F))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_CRIMSON_FENCE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROPE_LINEN_CRIMSON_FENCE,
            RopeFenceBlock::new, () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_WARPED_FENCE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROPE_LINEN_WARPED_FENCE,
            RopeFenceBlock::new, () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    ;
}

