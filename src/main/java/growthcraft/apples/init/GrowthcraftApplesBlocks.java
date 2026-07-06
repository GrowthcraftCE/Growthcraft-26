package growthcraft.apples.init;

import growthcraft.apples.block.AppleTreeFruitBlock;
import growthcraft.apples.block.AppleTreeLeavesBlock;
import growthcraft.apples.config.Reference;
import growthcraft.apples.world.AppleTreeGrowers;
import growthcraft.core.block.RopeFenceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftApplesBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    public static final DeferredBlock<Block> APPLE_PLANK = BLOCKS.registerSimpleBlock(
            Reference.UnlocalizedName.Block.APPLE_PLANK,
            () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F)
    );
    public static final DeferredBlock<ButtonBlock> APPLE_PLANK_BUTTON = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_PLANK_BUTTON,
            properties -> new ButtonBlock(BlockSetType.OAK, 30, properties),
            () -> BlockBehaviour.Properties.of().noCollision().strength(0.5F)
    );
    public static final DeferredBlock<DoorBlock> APPLE_PLANK_DOOR = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_PLANK_DOOR,
            properties -> new DoorBlock(BlockSetType.OAK, properties),
            () -> BlockBehaviour.Properties.of().strength(3.0F).noOcclusion()
    );
    public static final DeferredBlock<FenceBlock> APPLE_PLANK_FENCE = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_PLANK_FENCE,
            FenceBlock::new,
            () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F)
    );
    public static final DeferredBlock<FenceGateBlock> APPLE_PLANK_FENCE_GATE = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_PLANK_FENCE_GATE,
            properties -> new FenceGateBlock(WoodType.OAK, properties),
            () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F)
    );
    public static final DeferredBlock<RopeFenceBlock> APPLE_PLANK_FENCE_ROPE_LINEN = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_PLANK_FENCE_ROPE_LINEN,
            RopeFenceBlock::new,
            () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F)
    );
    public static final DeferredBlock<PressurePlateBlock> APPLE_PLANK_PRESSURE_PLATE = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_PLANK_PRESSURE_PLATE,
            properties -> new PressurePlateBlock(BlockSetType.OAK, properties),
            () -> BlockBehaviour.Properties.of().strength(0.5F)
    );
    public static final DeferredBlock<SlabBlock> APPLE_PLANK_SLAB = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_PLANK_SLAB,
            SlabBlock::new,
            () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F)
    );
    public static final DeferredBlock<StairBlock> APPLE_PLANK_STAIRS = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_PLANK_STAIRS,
            properties -> new StairBlock(APPLE_PLANK.get().defaultBlockState(), properties),
            () -> BlockBehaviour.Properties.of().strength(2.0F, 3.0F)
    );
    public static final DeferredBlock<TrapDoorBlock> APPLE_PLANK_TRAPDOOR = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_PLANK_TRAPDOOR,
            properties -> new TrapDoorBlock(BlockSetType.OAK, properties),
            () -> BlockBehaviour.Properties.of().strength(3.0F).noOcclusion()
    );
    public static final DeferredBlock<AppleTreeFruitBlock> APPLE_TREE_FRUIT = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_TREE_FRUIT,
            AppleTreeFruitBlock::new,
            AppleTreeFruitBlock::fruitProperties
    );
    public static final DeferredBlock<AppleTreeLeavesBlock> APPLE_TREE_LEAVES = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_TREE_LEAVES,
            AppleTreeLeavesBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
    );
    public static final DeferredBlock<SaplingBlock> APPLE_TREE_SAPLING = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_TREE_SAPLING,
            properties -> new SaplingBlock(AppleTreeGrowers.APPLE, properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)
    );
    public static final DeferredBlock<RotatedPillarBlock> APPLE_WOOD = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_WOOD,
            RotatedPillarBlock::new,
            () -> BlockBehaviour.Properties.of().strength(2.0F)
    );
    public static final DeferredBlock<RotatedPillarBlock> APPLE_WOOD_LOG = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_WOOD_LOG,
            RotatedPillarBlock::new,
            () -> BlockBehaviour.Properties.of().strength(2.0F)
    );
    public static final DeferredBlock<RotatedPillarBlock> APPLE_WOOD_LOG_STRIPPED = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_WOOD_LOG_STRIPPED,
            RotatedPillarBlock::new,
            () -> BlockBehaviour.Properties.of().strength(2.0F)
    );
    public static final DeferredBlock<RotatedPillarBlock> APPLE_WOOD_STRIPPED = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.APPLE_WOOD_STRIPPED,
            RotatedPillarBlock::new,
            () -> BlockBehaviour.Properties.of().strength(2.0F)
    );
    private GrowthcraftApplesBlocks() {}
}
