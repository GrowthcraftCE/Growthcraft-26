package growthcraft.core.init;

import growthcraft.apples.init.GrowthcraftApplesBlocks;
import growthcraft.cellar.block.BrewKettleBlock;
import growthcraft.cellar.block.CultureJarBlock;
import growthcraft.cellar.block.RoasterBlock;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.core.config.Reference;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

    private GrowthcraftGameTests() {
    }

    public static void registerTests(RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition<?>> environment = event.registerEnvironment(
                ENVIRONMENT_ID,
                new TestEnvironmentDefinition.AllOf()
        );
        event.registerTest(
                TEST_ID,
                new FunctionGameTestInstance(
                        REPRESENTATIVE_BLOCK_LIFECYCLE.getKey(),
                        new TestData<>(environment, EMPTY_STRUCTURE, 100, 0, true)
                )
        );
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
