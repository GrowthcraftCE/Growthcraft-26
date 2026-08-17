package growthcraft.cellar.init;

import growthcraft.cellar.block.CultureJarBlock;
import growthcraft.cellar.block.CorkCoasterBlock;
import growthcraft.cellar.block.CorkLogBlock;
import growthcraft.cellar.block.BrewKettleBlock;
import growthcraft.cellar.block.FermentationBarrelBlock;
import growthcraft.cellar.block.LargeFermentationBarrelBlock;
import growthcraft.cellar.block.LargeStorageBarrelBlock;
import growthcraft.cellar.block.FruitPressBlock;
import growthcraft.cellar.block.FruitPressPistonBlock;
import growthcraft.cellar.block.GrapeVineStemBlock;
import growthcraft.cellar.block.GrapeVineFruitBlock;
import growthcraft.cellar.block.GrapeVineLeavesBlock;
import growthcraft.cellar.block.HopsCropBlock;
import growthcraft.cellar.block.RoasterBlock;
import growthcraft.cellar.config.Reference;
import growthcraft.cellar.world.CorkTreeGrowers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Block registrations for Growthcraft Cellar (MC 1.21 / NeoForge).
 */
public final class GrowthcraftCellarBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    public static final DeferredBlock<Block> BREW_KETTLE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.BREW_KETTLE, BrewKettleBlock::new,
            () -> Block.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(1.5F)
                    .sound(SoundType.METAL)
                    .noOcclusion());

    public static final DeferredBlock<Block> CULTURE_JAR = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.CULTURE_JAR,
            CultureJarBlock::new,
            () -> Block.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0));

    public static final DeferredBlock<Block> FERMENTATION_BARREL_ACACIA = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FERMENT_BARREL_ACACIA, FermentationBarrelBlock::new,
            () -> Block.Properties.ofFullCopy(Blocks.ACACIA_PLANKS).noOcclusion());
    public static final DeferredBlock<Block> FERMENTATION_BARREL_APPLE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FERMENT_BARREL_APPLE, FermentationBarrelBlock::new,
            () -> Block.Properties.of().mapColor(MapColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion());
    public static final DeferredBlock<Block> FERMENTATION_BARREL_BIRCH = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FERMENT_BARREL_BIRCH, FermentationBarrelBlock::new,
            () -> Block.Properties.of().mapColor(MapColor.SAND).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion());
    public static final DeferredBlock<Block> FERMENTATION_BARREL_BAMBOO = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FERMENT_BARREL_BAMBOO, FermentationBarrelBlock::new,
            () -> Block.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).noOcclusion());
    public static final DeferredBlock<Block> FERMENTATION_BARREL_CHERRY = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FERMENT_BARREL_CHERRY, FermentationBarrelBlock::new,
            () -> Block.Properties.ofFullCopy(Blocks.CHERRY_PLANKS).noOcclusion());
    public static final DeferredBlock<Block> FERMENTATION_BARREL_CRIMSON = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FERMENT_BARREL_CRIMSON, FermentationBarrelBlock::new,
            () -> Block.Properties.ofFullCopy(Blocks.CRIMSON_PLANKS).noOcclusion());
    public static final DeferredBlock<Block> FERMENTATION_BARREL_DARK_OAK = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FERMENT_BARREL_DARK_OAK, FermentationBarrelBlock::new,
            () -> Block.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS).noOcclusion());
    public static final DeferredBlock<Block> FERMENTATION_BARREL_JUNGLE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FERMENT_BARREL_JUNGLE, FermentationBarrelBlock::new,
            () -> Block.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS).noOcclusion());
    public static final DeferredBlock<Block> FERMENTATION_BARREL_MANGROVE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FERMENT_BARREL_MANGROVE, FermentationBarrelBlock::new,
            () -> Block.Properties.ofFullCopy(Blocks.MANGROVE_PLANKS).noOcclusion());
    public static final DeferredBlock<Block> FERMENTATION_BARREL_OAK = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FERMENT_BARREL_OAK, FermentationBarrelBlock::new,
            () -> Block.Properties.of().mapColor(MapColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion());
    public static final DeferredBlock<Block> FERMENTATION_BARREL_PALE_OAK = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FERMENT_BARREL_PALE_OAK, FermentationBarrelBlock::new,
            () -> Block.Properties.ofFullCopy(Blocks.PALE_OAK_PLANKS).noOcclusion());
    public static final DeferredBlock<Block> FERMENTATION_BARREL_SPRUCE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FERMENT_BARREL_SPRUCE, FermentationBarrelBlock::new,
            () -> Block.Properties.of().mapColor(MapColor.PODZOL).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion());
    public static final DeferredBlock<Block> FERMENTATION_BARREL_WARPED = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FERMENT_BARREL_WARPED, FermentationBarrelBlock::new,
            () -> Block.Properties.ofFullCopy(Blocks.WARPED_PLANKS).noOcclusion());
    public static final DeferredBlock<Block> LARGE_FERMENTATION_BARREL_ACACIA = largeBarrel(Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_ACACIA, Blocks.ACACIA_PLANKS);
    public static final DeferredBlock<Block> LARGE_FERMENTATION_BARREL_APPLE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_APPLE, LargeFermentationBarrelBlock::new,
            () -> Block.Properties.of().mapColor(MapColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion().forceSolidOff());
    public static final DeferredBlock<Block> LARGE_FERMENTATION_BARREL_BAMBOO = largeBarrel(Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_BAMBOO, Blocks.BAMBOO_PLANKS);
    public static final DeferredBlock<Block> LARGE_FERMENTATION_BARREL_BIRCH = largeBarrel(Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_BIRCH, Blocks.BIRCH_PLANKS);
    public static final DeferredBlock<Block> LARGE_FERMENTATION_BARREL_CHERRY = largeBarrel(Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_CHERRY, Blocks.CHERRY_PLANKS);
    public static final DeferredBlock<Block> LARGE_FERMENTATION_BARREL_CRIMSON = largeBarrel(Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_CRIMSON, Blocks.CRIMSON_PLANKS);
    public static final DeferredBlock<Block> LARGE_FERMENTATION_BARREL_DARK_OAK = largeBarrel(Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_DARK_OAK, Blocks.DARK_OAK_PLANKS);
    public static final DeferredBlock<Block> LARGE_FERMENTATION_BARREL_JUNGLE = largeBarrel(Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_JUNGLE, Blocks.JUNGLE_PLANKS);
    public static final DeferredBlock<Block> LARGE_FERMENTATION_BARREL_MANGROVE = largeBarrel(Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_MANGROVE, Blocks.MANGROVE_PLANKS);
    public static final DeferredBlock<Block> LARGE_FERMENTATION_BARREL_OAK = largeBarrel(Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_OAK, Blocks.OAK_PLANKS);
    public static final DeferredBlock<Block> LARGE_FERMENTATION_BARREL_PALE_OAK = largeBarrel(Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_PALE_OAK, Blocks.PALE_OAK_PLANKS);
    public static final DeferredBlock<Block> LARGE_FERMENTATION_BARREL_SPRUCE = largeBarrel(Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_SPRUCE, Blocks.SPRUCE_PLANKS);
    public static final DeferredBlock<Block> LARGE_FERMENTATION_BARREL_WARPED = largeBarrel(Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_WARPED, Blocks.WARPED_PLANKS);
    public static final DeferredBlock<Block> LARGE_STORAGE_BARREL_ACACIA = storageBarrel(Reference.UnlocalizedName.Block.LARGE_STORAGE_BARREL_ACACIA, Blocks.ACACIA_PLANKS);
    public static final DeferredBlock<Block> LARGE_STORAGE_BARREL_APPLE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.LARGE_STORAGE_BARREL_APPLE, LargeStorageBarrelBlock::new,
            () -> Block.Properties.of().mapColor(MapColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion().forceSolidOff());
    public static final DeferredBlock<Block> LARGE_STORAGE_BARREL_BAMBOO = storageBarrel(Reference.UnlocalizedName.Block.LARGE_STORAGE_BARREL_BAMBOO, Blocks.BAMBOO_PLANKS);
    public static final DeferredBlock<Block> LARGE_STORAGE_BARREL_BIRCH = storageBarrel(Reference.UnlocalizedName.Block.LARGE_STORAGE_BARREL_BIRCH, Blocks.BIRCH_PLANKS);
    public static final DeferredBlock<Block> LARGE_STORAGE_BARREL_CHERRY = storageBarrel(Reference.UnlocalizedName.Block.LARGE_STORAGE_BARREL_CHERRY, Blocks.CHERRY_PLANKS);
    public static final DeferredBlock<Block> LARGE_STORAGE_BARREL_CRIMSON = storageBarrel(Reference.UnlocalizedName.Block.LARGE_STORAGE_BARREL_CRIMSON, Blocks.CRIMSON_PLANKS);
    public static final DeferredBlock<Block> LARGE_STORAGE_BARREL_DARK_OAK = storageBarrel(Reference.UnlocalizedName.Block.LARGE_STORAGE_BARREL_DARK_OAK, Blocks.DARK_OAK_PLANKS);
    public static final DeferredBlock<Block> LARGE_STORAGE_BARREL_JUNGLE = storageBarrel(Reference.UnlocalizedName.Block.LARGE_STORAGE_BARREL_JUNGLE, Blocks.JUNGLE_PLANKS);
    public static final DeferredBlock<Block> LARGE_STORAGE_BARREL_MANGROVE = storageBarrel(Reference.UnlocalizedName.Block.LARGE_STORAGE_BARREL_MANGROVE, Blocks.MANGROVE_PLANKS);
    public static final DeferredBlock<Block> LARGE_STORAGE_BARREL_OAK = storageBarrel(Reference.UnlocalizedName.Block.LARGE_STORAGE_BARREL_OAK, Blocks.OAK_PLANKS);
    public static final DeferredBlock<Block> LARGE_STORAGE_BARREL_PALE_OAK = storageBarrel(Reference.UnlocalizedName.Block.LARGE_STORAGE_BARREL_PALE_OAK, Blocks.PALE_OAK_PLANKS);
    public static final DeferredBlock<Block> LARGE_STORAGE_BARREL_SPRUCE = storageBarrel(Reference.UnlocalizedName.Block.LARGE_STORAGE_BARREL_SPRUCE, Blocks.SPRUCE_PLANKS);
    public static final DeferredBlock<Block> LARGE_STORAGE_BARREL_WARPED = storageBarrel(Reference.UnlocalizedName.Block.LARGE_STORAGE_BARREL_WARPED, Blocks.WARPED_PLANKS);
    public static final DeferredBlock<Block> FRUIT_PRESS = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FRUIT_PRESS, FruitPressBlock::new,
            () -> Block.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.CHAIN).noOcclusion());
    public static final DeferredBlock<Block> FRUIT_PRESS_PISTON = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.FRUIT_PRESS_PISTON, FruitPressPistonBlock::new,
            () -> Block.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD).noOcclusion().isRedstoneConductor((state, level, pos) -> false));
    public static final DeferredBlock<Block> ROASTER = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.ROASTER, RoasterBlock::new,
            () -> Block.Properties.of().mapColor(MapColor.METAL).strength(1.5F).sound(SoundType.METAL).noOcclusion());
    public static final DeferredBlock<GrapeVineFruitBlock> PURPLE_GRAPE_VINE_FRUIT = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.PURPLE_GRAPE_VINE_FRUIT,
            properties -> new GrapeVineFruitBlock(GrowthcraftCellarItems.GRAPE_PURPLE, properties));
    public static final DeferredBlock<GrapeVineFruitBlock> RED_GRAPE_VINE_FRUIT = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.RED_GRAPE_VINE_FRUIT,
            properties -> new GrapeVineFruitBlock(GrowthcraftCellarItems.GRAPE_RED, properties));
    public static final DeferredBlock<GrapeVineFruitBlock> WHITE_GRAPE_VINE_FRUIT = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.WHITE_GRAPE_VINE_FRUIT,
            properties -> new GrapeVineFruitBlock(GrowthcraftCellarItems.GRAPE_WHITE, properties));
    public static final DeferredBlock<GrapeVineLeavesBlock> PURPLE_GRAPE_VINE_LEAVES = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.PURPLE_GRAPE_VINE_LEAVES,
            properties -> new GrapeVineLeavesBlock(PURPLE_GRAPE_VINE_FRUIT, GrowthcraftCellarItems.GRAPE_SEEDS_PURPLE, properties));
    public static final DeferredBlock<GrapeVineLeavesBlock> RED_GRAPE_VINE_LEAVES = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.RED_GRAPE_VINE_LEAVES,
            properties -> new GrapeVineLeavesBlock(RED_GRAPE_VINE_FRUIT, GrowthcraftCellarItems.GRAPE_SEEDS_RED, properties));
    public static final DeferredBlock<GrapeVineLeavesBlock> WHITE_GRAPE_VINE_LEAVES = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.WHITE_GRAPE_VINE_LEAVES,
            properties -> new GrapeVineLeavesBlock(WHITE_GRAPE_VINE_FRUIT, GrowthcraftCellarItems.GRAPE_SEEDS_WHITE, properties));
    public static final DeferredBlock<GrapeVineStemBlock> PURPLE_GRAPE_VINE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.PURPLE_GRAPE_VINE,
            properties -> new GrapeVineStemBlock(PURPLE_GRAPE_VINE_LEAVES, GrowthcraftCellarItems.GRAPE_SEEDS_PURPLE, properties));
    public static final DeferredBlock<GrapeVineStemBlock> RED_GRAPE_VINE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.RED_GRAPE_VINE,
            properties -> new GrapeVineStemBlock(RED_GRAPE_VINE_LEAVES, GrowthcraftCellarItems.GRAPE_SEEDS_RED, properties));
    public static final DeferredBlock<GrapeVineStemBlock> WHITE_GRAPE_VINE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.WHITE_GRAPE_VINE,
            properties -> new GrapeVineStemBlock(WHITE_GRAPE_VINE_LEAVES, GrowthcraftCellarItems.GRAPE_SEEDS_WHITE, properties));
    public static final DeferredBlock<HopsCropBlock> HOPS_VINE = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.HOPS_VINE, HopsCropBlock::new);

    public static final DeferredBlock<Block> CORK_COASTER = BLOCKS.registerBlock(Reference.UnlocalizedName.Item.CORK_COASTER, CorkCoasterBlock::new);
    public static final DeferredBlock<LeavesBlock> CORK_TREE_LEAVES = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.CORK_TREE_LEAVES,
            growthcraft.lib.block.SimpleLeavesBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES));
    public static final DeferredBlock<SaplingBlock> CORK_TREE_SAPLING = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.CORK_TREE_SAPLING,
            properties -> new SaplingBlock(CorkTreeGrowers.CORK, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING));
    public static final DeferredBlock<CorkLogBlock> CORK_WOOD = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.CORK_WOOD,
            CorkLogBlock::new, GrowthcraftCellarBlocks::corkWoodProperties);
    public static final DeferredBlock<CorkLogBlock> CORK_WOOD_LOG = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.CORK_WOOD_LOG,
            CorkLogBlock::new, GrowthcraftCellarBlocks::corkWoodProperties);
    public static final DeferredBlock<CorkLogBlock> CORK_WOOD_LOG_STRIPPED = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.CORK_WOOD_LOG_STRIPPED,
            CorkLogBlock::new, GrowthcraftCellarBlocks::corkWoodProperties);
    public static final DeferredBlock<CorkLogBlock> CORK_WOOD_STRIPPED = BLOCKS.registerBlock(Reference.UnlocalizedName.Block.CORK_WOOD_STRIPPED,
            CorkLogBlock::new, GrowthcraftCellarBlocks::corkWoodProperties);

    private static BlockBehaviour.Properties corkWoodProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).randomTicks();
    }

    private static DeferredBlock<Block> largeBarrel(String name, Block planks) {
        return BLOCKS.registerBlock(name, LargeFermentationBarrelBlock::new,
                () -> Block.Properties.ofFullCopy(planks).noOcclusion().forceSolidOff());
    }

    private static DeferredBlock<Block> storageBarrel(String name, Block planks) {
        return BLOCKS.registerBlock(name, LargeStorageBarrelBlock::new,
                () -> Block.Properties.ofFullCopy(planks).noOcclusion().forceSolidOff());
    }

    private GrowthcraftCellarBlocks() {}
}
