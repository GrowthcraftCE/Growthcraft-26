package growthcraft.core.init;

import growthcraft.apples.init.GrowthcraftApplesBlocks;
import growthcraft.cellar.block.BrewKettleBlock;
import growthcraft.cellar.block.CultureJarBlock;
import growthcraft.cellar.block.RoasterBlock;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.core.config.Reference;
import growthcraft.milk.block.entity.MixingVatBlockEntity;
import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.rice.init.GrowthcraftRiceBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

/** Critical-path tests that exercise representative blocks in a real server level. */
public final class GrowthcraftGameTests {
    private static final Identifier TEST_ID = Identifier.fromNamespaceAndPath(Reference.MODID, "representative_block_lifecycle");
    private static final Identifier ENVIRONMENT_ID = Identifier.fromNamespaceAndPath(Reference.MODID, "default_test_environment");
    private static final Identifier EMPTY_STRUCTURE = Identifier.withDefaultNamespace("empty");

    public static final DeferredRegister<Consumer<GameTestHelper>> TEST_FUNCTIONS =
            DeferredRegister.create(BuiltInRegistries.TEST_FUNCTION, Reference.MODID);

    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> REPRESENTATIVE_BLOCK_LIFECYCLE =
            TEST_FUNCTIONS.register("representative_block_lifecycle", () -> GrowthcraftGameTests::representativeBlockLifecycle);

    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> FRUIT_GROWTH_AND_LOOT =
            TEST_FUNCTIONS.register("fruit_growth_and_loot", () -> GrowthcraftGameTests::fruitGrowthAndLoot);

    private GrowthcraftGameTests() {
    }

    public static void registerTests(RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition<?>> environment = event.registerEnvironment(
                ENVIRONMENT_ID,
                new TestEnvironmentDefinition.AllOf()
        );
        event.registerTest(Identifier.fromNamespaceAndPath(Reference.MODID, "fruit_growth_and_loot"),
                new FunctionGameTestInstance(FRUIT_GROWTH_AND_LOOT.getKey(),
                        new TestData<>(environment, EMPTY_STRUCTURE, 100, 0, true)));
        event.registerTest(
                TEST_ID,
                new FunctionGameTestInstance(
                        REPRESENTATIVE_BLOCK_LIFECYCLE.getKey(),
                        new TestData<>(environment, EMPTY_STRUCTURE, 100, 0, true)
                )
        );
    }

    private static void fruitGrowthAndLoot(GameTestHelper helper) {
        BlockPos leaf = new BlockPos(1, 3, 1);
        BlockPos neighbor = leaf.east();
        BlockState leaves = GrowthcraftApplesBlocks.APPLE_TREE_LEAVES.get().defaultBlockState()
                .setValue(LeavesBlock.PERSISTENT, true);
        helper.setBlock(leaf, leaves);
        helper.setBlock(neighbor, leaves);
        RandomSource random = RandomSource.create(88L);
        for (int attempt = 0; attempt < 32; attempt++) {
            helper.setBlock(leaf.below(), Blocks.AIR);
            helper.setBlock(neighbor.below(), Blocks.AIR);
            // Neighbor-update ticks must update leaf distance without spawning fruit.
            helper.tickBlock(leaf);
            helper.assertBlockPresent(Blocks.AIR, leaf.below());
            leaves.randomTick(helper.getLevel(), helper.absolutePos(leaf), random);
            helper.assertBlockPresent(GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get(), leaf.below());
            helper.assertBlockPresent(Blocks.AIR, neighbor.below());
        }
        helper.setBlock(leaf.below(), GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get());
        helper.setBlock(neighbor.below(), GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get());
        BlockPos third = leaf.south();
        helper.setBlock(third, leaves);
        leaves.randomTick(helper.getLevel(), helper.absolutePos(third), random);
        helper.assertBlockPresent(Blocks.AIR, third.below());
        // A decaying leaf must not spawn fruit beneath another surviving leaf.
        helper.setBlock(leaf.below(), Blocks.AIR);
        helper.setBlock(neighbor.below(), Blocks.AIR);
        BlockState decaying = leaves.setValue(LeavesBlock.PERSISTENT, false).setValue(LeavesBlock.DISTANCE, 7);
        helper.setBlock(leaf, decaying);
        decaying.randomTick(helper.getLevel(), helper.absolutePos(leaf), random);
        helper.assertBlockPresent(Blocks.AIR, neighbor.below());
        helper.assertBlockPresent(Blocks.AIR, third.below());

        verifyFruitLoot(helper, GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get(), Items.APPLE);
        verifyFruitLoot(helper, GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE_FRUIT.get(), GrowthcraftCellarItems.GRAPE_PURPLE.get());
        verifyFruitLoot(helper, GrowthcraftCellarBlocks.RED_GRAPE_VINE_FRUIT.get(), GrowthcraftCellarItems.GRAPE_RED.get());
        verifyFruitLoot(helper, GrowthcraftCellarBlocks.WHITE_GRAPE_VINE_FRUIT.get(), GrowthcraftCellarItems.GRAPE_WHITE.get());
        helper.succeed();
    }

    private static void verifyFruitLoot(GameTestHelper helper, Block fruit, Item item) {
        for (int age = 0; age <= 7; age++) {
            BlockState state = fruit.defaultBlockState().setValue(BlockStateProperties.AGE_7, age);
            var drops = Block.getDrops(state, helper.getLevel(), helper.absolutePos(new BlockPos(1, 2, 1)), null);
            int count = drops.stream().filter(stack -> stack.is(item)).mapToInt(ItemStack::getCount).sum();
            helper.assertTrue(count == (age == 7 ? 1 : 0), fruit + " age " + age + " has incorrect fruit drops");
        }
    }

    private static void representativeBlockLifecycle(GameTestHelper helper) {
        BlockPos testPosition = new BlockPos(1, 1, 1);

        cycleBlock(helper, testPosition, GrowthcraftBlocks.SALT_BLOCK.get());
        cycleBlock(helper, testPosition, GrowthcraftBlocks.ROPE_LINEN.get());
        cycleBlock(helper, testPosition, GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE.get());
        cycleBlock(helper, testPosition, GrowthcraftCellarBlocks.HOPS_VINE.get());
        cycleBlock(helper, testPosition, GrowthcraftCellarBlocks.BREW_KETTLE.get());
        cycleBlock(helper, testPosition, GrowthcraftMilkBlocks.CHEESE_PRESS.get());
        cycleBlock(helper, testPosition, GrowthcraftApplesBlocks.APPLE_PLANK.get());
        cycleBlock(helper, testPosition, GrowthcraftRiceBlocks.CULTIVATED_FARMLAND.get());
        verifyCellarHeatDetection(helper, new BlockPos(1, 2, 1));
        verifyCorkCoasterSupport(helper, new BlockPos(2, 2, 1));

        helper.succeed();
    }

    private static void cycleBlock(GameTestHelper helper, BlockPos position, Block block) {
        helper.setBlock(position, block);
        helper.assertBlockPresent(block, position);
        helper.destroyBlock(position);
        helper.assertBlockNotPresent(block, position);
    }

    private static void verifyCellarHeatDetection(GameTestHelper helper, BlockPos machinePosition) {
        helper.setBlock(machinePosition.below(), Blocks.MAGMA_BLOCK);

        helper.setBlock(machinePosition, GrowthcraftCellarBlocks.BREW_KETTLE.get());
        BrewKettleBlock.updateLitState(helper.getLevel(), helper.absolutePos(machinePosition), helper.getBlockState(machinePosition));
        helper.assertBlockProperty(machinePosition, BrewKettleBlock.LIT, true);

        helper.setBlock(machinePosition, GrowthcraftCellarBlocks.ROASTER.get());
        RoasterBlock.updateLitState(helper.getLevel(), helper.absolutePos(machinePosition), helper.getBlockState(machinePosition));
        helper.assertBlockProperty(machinePosition, RoasterBlock.LIT, true);

        helper.setBlock(machinePosition, GrowthcraftCellarBlocks.CULTURE_JAR.get());
        CultureJarBlock.updateLitState(helper.getLevel(), helper.absolutePos(machinePosition), helper.getBlockState(machinePosition));
        helper.assertBlockProperty(machinePosition, CultureJarBlock.LIT, true);

        helper.setBlock(machinePosition, GrowthcraftMilkBlocks.MIXING_VAT.get());
        helper.assertTrue(MixingVatBlockEntity.isHeated(helper.getLevel(), helper.absolutePos(machinePosition)),
                "Mixing Vat should recognize the shared heat source");

        helper.destroyBlock(machinePosition);
        helper.destroyBlock(machinePosition.below());
    }

    private static void verifyCorkCoasterSupport(GameTestHelper helper, BlockPos coasterPosition) {
        helper.setBlock(coasterPosition.below(), Blocks.STONE);
        helper.setBlock(coasterPosition, GrowthcraftCellarBlocks.CORK_COASTER.get());
        helper.assertBlockPresent(GrowthcraftCellarBlocks.CORK_COASTER.get(), coasterPosition);

        helper.destroyBlock(coasterPosition.below());
        helper.assertBlockNotPresent(GrowthcraftCellarBlocks.CORK_COASTER.get(), coasterPosition);
    }
}
