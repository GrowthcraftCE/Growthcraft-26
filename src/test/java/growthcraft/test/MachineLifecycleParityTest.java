package growthcraft.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MachineLifecycleParityTest {
    @Test
    void processingMachinesPersistInventoryFluidAndProgressState() throws IOException {
        assertPersistence("cellar", "BrewKettleBlockEntity", true, true, "ProcessTime", "ProcessTimeTotal");
        assertPersistence("cellar", "CultureJarBlockEntity", true, true, "ProcessTime", "ProcessTimeTotal");
        assertPersistence("cellar", "FermentationBarrelBlockEntity", true, true,
                "ProcessTime", "ProcessTimeTotal", "YeastWarning", "YeastError");
        assertPersistence("cellar", "FruitPressBlockEntity", true, true, "ProcessTime", "ProcessTimeTotal");
        assertPersistence("cellar", "RoasterBlockEntity", true, false, "ProcessTime", "ProcessTimeTotal");
        assertPersistence("milk", "CheesePressBlockEntity", true, false,
                "ProcessTime", "ProcessTimeTotal", "Rotation");
        assertPersistence("milk", "ChurnBlockEntity", true, true, "PlungeCount", "PlungesNeeded");
        assertPersistence("milk", "MixingVatBlockEntity", true, true,
                "ProcessTime", "ProcessTimeTotal", "Activated");
        assertPersistence("milk", "PancheonBlockEntity", false, true, "ProcessTime", "ProcessTimeTotal");
    }

    @Test
    void everyInventoryMachineHasIntentionalBreakDropHandling() throws IOException {
        for (String machine : new String[]{
                "cellar/block/BrewKettleBlock.java",
                "cellar/block/CorkCoasterBlock.java",
                "cellar/block/CultureJarBlock.java",
                "cellar/block/FermentationBarrelBlock.java",
                "cellar/block/FruitPressBlock.java",
                "cellar/block/RoasterBlock.java",
                "milk/block/CheesePressBlock.java",
                "milk/block/ChurnBlock.java"
        }) {
            String source = Files.readString(Path.of("src/main/java/growthcraft", machine));
            assertTrue(source.contains("affectNeighborsAfterRemoval"), machine);
        }

        String mixingVat = Files.readString(Path.of(
                "src/main/java/growthcraft/milk/block/entity/MixingVatBlockEntity.java"));
        assertTrue(mixingVat.contains("preRemoveSideEffects"), "MixingVatBlockEntity");
        assertTrue(mixingVat.contains("shouldDropWhenBroken"), "MixingVatBlockEntity");
    }

    @Test
    void implementedContainerMenusDelegateValidityToTheirMachine() throws IOException {
        for (String menu : new String[]{
                "cellar/menu/BrewKettleMenu.java",
                "cellar/menu/CultureJarMenu.java",
                "cellar/menu/FermentationBarrelMenu.java",
                "cellar/menu/FruitPressMenu.java",
                "cellar/menu/RoasterMenu.java",
                "milk/menu/CheesePressMenu.java",
                "milk/menu/ChurnMenu.java",
                "milk/menu/MixingVatMenu.java"
        }) {
            String source = Files.readString(Path.of("src/main/java/growthcraft", menu));
            assertTrue(source.contains(".stillValid(player)"), menu);
        }

        String pancheon = Files.readString(Path.of(
                "src/main/java/growthcraft/milk/menu/PancheonMenu.java"));
        assertTrue(pancheon.contains("player.distanceToSqr"));
        assertTrue(pancheon.contains("getBlockEntity(pancheon.getBlockPos()) == pancheon"));
    }

    @Test
    void fruitPressPistonIsAxeMineableWithoutDuplicatingDrops() throws IOException {
        String tags = Files.readString(Path.of(
                "src/main/java/growthcraft/core/data/tags/GrowthcraftBlockTags.java"));
        String piston = Files.readString(Path.of(
                "src/main/java/growthcraft/cellar/block/FruitPressPistonBlock.java"));

        assertTrue(tags.contains("GrowthcraftCellarBlocks.FRUIT_PRESS_PISTON.get()"));
        assertTrue(piston.contains("level.destroyBlock(pos.below(), false)"));
        assertTrue(piston.contains("popResource(level, pos, new ItemStack(GrowthcraftCellarBlocks.FRUIT_PRESS.get().asItem()))"));
        assertTrue(piston.contains("return Collections.emptyList()"));
    }

    private static void assertPersistence(
            String module,
            String className,
            boolean inventory,
            boolean fluid,
            String... keys
    ) throws IOException {
        String source = Files.readString(Path.of(
                "src/main/java/growthcraft", module, "block/entity", className + ".java"));

        assertTrue(source.contains("loadAdditional(ValueInput"), className);
        assertTrue(source.contains("saveAdditional(ValueOutput"), className);
        if (inventory) {
            assertTrue(source.contains("ContainerHelper.loadAllItems"), className);
            assertTrue(source.contains("ContainerHelper.saveAllItems"), className);
        }
        if (fluid) {
            assertTrue(source.contains("FluidTankPersistence.load"), className);
            assertTrue(source.contains("FluidTankPersistence.save"), className);
        }
        for (String key : keys) {
            assertTrue(source.contains("\"" + key + "\""), className + ": " + key);
        }
    }
}
