package growthcraft.cellar.data.loot;

import growthcraft.cellar.block.LargeBarrelPart;
import growthcraft.cellar.block.LargeFermentationBarrelBlock;
import growthcraft.cellar.block.LargeStorageBarrelBlock;
import growthcraft.cellar.block.StorageBarrelPart;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Set;
import java.util.stream.Collectors;

public class CellarBlockLoot extends BlockLootSubProvider {
    public CellarBlockLoot(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    protected void generate() {
        dropSelf(GrowthcraftCellarBlocks.BREW_KETTLE.get());
        dropSelf(GrowthcraftCellarBlocks.CULTURE_JAR.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_ACACIA.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_APPLE.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_BAMBOO.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_BIRCH.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_CHERRY.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_CRIMSON.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_DARK_OAK.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_JUNGLE.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_MANGROVE.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_PALE_OAK.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_SPRUCE.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_WARPED.get());
        addLargeBarrelDrop(GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_ACACIA.get());
        addLargeBarrelDrop(GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_APPLE.get());
        addLargeBarrelDrop(GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_BAMBOO.get());
        addLargeBarrelDrop(GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_BIRCH.get());
        addLargeBarrelDrop(GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_CHERRY.get());
        addLargeBarrelDrop(GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_CRIMSON.get());
        addLargeBarrelDrop(GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_DARK_OAK.get());
        addLargeBarrelDrop(GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_JUNGLE.get());
        addLargeBarrelDrop(GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_MANGROVE.get());
        addLargeBarrelDrop(GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_OAK.get());
        addLargeBarrelDrop(GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_PALE_OAK.get());
        addLargeBarrelDrop(GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_SPRUCE.get());
        addLargeBarrelDrop(GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_WARPED.get());
        addStorageBarrelDrop(GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_ACACIA.get());
        addStorageBarrelDrop(GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_APPLE.get());
        addStorageBarrelDrop(GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_BAMBOO.get());
        addStorageBarrelDrop(GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_BIRCH.get());
        addStorageBarrelDrop(GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_CHERRY.get());
        addStorageBarrelDrop(GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_CRIMSON.get());
        addStorageBarrelDrop(GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_DARK_OAK.get());
        addStorageBarrelDrop(GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_JUNGLE.get());
        addStorageBarrelDrop(GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_MANGROVE.get());
        addStorageBarrelDrop(GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_OAK.get());
        addStorageBarrelDrop(GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_PALE_OAK.get());
        addStorageBarrelDrop(GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_SPRUCE.get());
        addStorageBarrelDrop(GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_WARPED.get());
        dropSelf(GrowthcraftCellarBlocks.FRUIT_PRESS.get());
        add(GrowthcraftCellarBlocks.FRUIT_PRESS_PISTON.get(), noDrop());
        dropSelf(GrowthcraftCellarBlocks.ROASTER.get());
        add(GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE.get(), noDrop());
        add(GrowthcraftCellarBlocks.RED_GRAPE_VINE.get(), noDrop());
        add(GrowthcraftCellarBlocks.WHITE_GRAPE_VINE.get(), noDrop());
        add(GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE_LEAVES.get(), noDrop());
        add(GrowthcraftCellarBlocks.RED_GRAPE_VINE_LEAVES.get(), noDrop());
        add(GrowthcraftCellarBlocks.WHITE_GRAPE_VINE_LEAVES.get(), noDrop());
        dropOther(GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE_FRUIT.get(), GrowthcraftCellarItems.GRAPE_PURPLE.get());
        dropOther(GrowthcraftCellarBlocks.RED_GRAPE_VINE_FRUIT.get(), GrowthcraftCellarItems.GRAPE_RED.get());
        dropOther(GrowthcraftCellarBlocks.WHITE_GRAPE_VINE_FRUIT.get(), GrowthcraftCellarItems.GRAPE_WHITE.get());
        dropOther(GrowthcraftCellarBlocks.HOPS_VINE.get(), GrowthcraftCellarItems.HOPS.get());
        dropSelf(GrowthcraftCellarBlocks.CORK_COASTER.get());
        add(GrowthcraftCellarBlocks.CORK_TREE_LEAVES.get(), createLeavesDrops(
                GrowthcraftCellarBlocks.CORK_TREE_LEAVES.get(),
                GrowthcraftCellarBlocks.CORK_TREE_SAPLING.get(),
                NORMAL_LEAVES_SAPLING_CHANCES));
        dropSelf(GrowthcraftCellarBlocks.CORK_TREE_SAPLING.get());
        dropSelf(GrowthcraftCellarBlocks.CORK_WOOD.get());
        dropSelf(GrowthcraftCellarBlocks.CORK_WOOD_LOG.get());
        dropSelf(GrowthcraftCellarBlocks.CORK_WOOD_LOG_STRIPPED.get());
        dropSelf(GrowthcraftCellarBlocks.CORK_WOOD_STRIPPED.get());
    }

    private void addLargeBarrelDrop(Block barrel) {
        add(barrel, createLargeBarrelDrop(barrel));
    }

    private LootTable.Builder createLargeBarrelDrop(Block barrel) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(applyExplosionCondition(barrel, LootItem.lootTableItem(barrel)
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(barrel)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(LargeFermentationBarrelBlock.PART, LargeBarrelPart.BOTTOM_NEAR_LEFT))))));
    }

    private void addStorageBarrelDrop(Block barrel) {
        add(barrel, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(applyExplosionCondition(barrel, LootItem.lootTableItem(barrel)
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(barrel)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(LargeStorageBarrelBlock.PART, StorageBarrelPart.BOTTOM_NEAR_LEFT)))))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GrowthcraftCellarBlocks.BLOCKS.getEntries().stream()
                .map(entry -> entry.get())
                .collect(Collectors.toList());
    }
}
