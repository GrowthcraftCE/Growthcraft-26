package growthcraft.milk.client;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MilkMachineScreenParityTest {
    @Test
    void cheesePressAndChurnUseServerBoundMenus() throws IOException {
        String menus = source("init/GrowthcraftMilkMenus.java");
        String pressEntity = source("block/entity/CheesePressBlockEntity.java");
        String churnEntity = source("block/entity/ChurnBlockEntity.java");

        assertTrue(menus.contains("MenuType<CheesePressMenu>"));
        assertTrue(menus.contains("MenuType<ChurnMenu>"));
        assertTrue(pressEntity.contains("implements WorldlyContainer, Clearable, net.minecraft.world.MenuProvider"));
        assertTrue(churnEntity.contains("implements WorldlyContainer, Clearable, net.minecraft.world.MenuProvider"));
    }

    @Test
    void authoredScreensAreRegisteredAndExposeMachineState() throws IOException {
        String client = source("client/GrowthcraftMilkClient.java");
        String press = source("client/screen/CheesePressScreen.java");
        String churn = source("client/screen/ChurnScreen.java");

        assertTrue(client.contains("CheesePressScreen::new"));
        assertTrue(client.contains("ChurnScreen::new"));
        assertTrue(press.contains("cheese_press_screen.png"));
        assertTrue(press.contains("getPercentProgress()"));
        assertTrue(churn.contains("churn_screen.png"));
        assertTrue(churn.contains("FluidTankRenderer"));
        assertTrue(churn.contains("getPlungeCount()"));
    }

    @Test
    void crouchUseOpensMenusWithoutReplacingStableDirectControls() throws IOException {
        String press = source("block/CheesePressBlock.java");
        String churn = source("block/ChurnBlock.java");

        assertTrue(press.contains("player.isShiftKeyDown()"));
        assertTrue(press.contains("player.openMenu(press)"));
        assertTrue(churn.contains("player.isShiftKeyDown()"));
        assertTrue(churn.contains("player.openMenu(churn)"));
        assertTrue(churn.contains("togglePlunger(level, pos, state, churn)"));
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/milk", relativePath));
    }
}
