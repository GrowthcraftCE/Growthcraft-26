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
        String pressMenu = source("menu/CheesePressMenu.java");
        String churn = source("client/screen/ChurnScreen.java");

        assertTrue(client.contains("CheesePressScreen::new"));
        assertTrue(client.contains("ChurnScreen::new"));
        assertTrue(press.contains("cheese_press_screen.png"));
        assertTrue(press.contains("getPercentProgress()"));
        assertTrue(press.contains("this.menu.getProgressionScaled(PROGRESS_H)"));
        assertTrue(press.contains("PROGRESS_U, PROGRESS_V, PROGRESS_W, progress"));
        assertTrue(press.contains("PROGRESS_W, PROGRESS_H"));
        assertTrue(press.contains("super.extractTooltip(graphics, mouseX, mouseY)"));
        assertTrue(pressMenu.contains("SLOT_INPUT, 53, 35"));
        assertTrue(pressMenu.contains("SLOT_OUTPUT, 106, 35"));
        assertTrue(pressMenu.contains("8 + col * 18, 84 + row * 18"));
        assertTrue(pressMenu.contains("8 + col * 18, 142"));
        assertTrue(churn.contains("churn_screen.png"));
        assertTrue(churn.contains("FluidTankRenderer"));
        assertTrue(churn.contains("getPlungeCount()"));
    }

    @Test
    void mixingVatExplainsItsResultActivationTool() throws IOException {
        String screen = source("client/screen/MixingVatScreen.java");

        assertTrue(screen.contains("getResultActivationTool()"));
        assertTrue(screen.contains("new ArrayList<>(this.getTooltipFromContainerItem(result))"));
        assertTrue(screen.contains("message.growthcraft_milk.get_using_item_empty_hand"));
        assertTrue(screen.contains("message.growthcraft_milk.get_using_item"));
        assertTrue(screen.contains("Style.EMPTY.withColor(0xDDBB44)"));
        assertTrue(screen.contains("ItemStack.matches(result, this.cachedTooltipResult)"));
        assertTrue(screen.contains("ItemStack.matches(tool, this.cachedTooltipTool)"));
        assertTrue(screen.contains("result.getTooltipImage(), result"));
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

    @Test
    void shopSignsRegisterTheirItemRenderer() throws IOException {
        String client = source("client/GrowthcraftMilkClient.java");
        String renderer = source("client/renderer/ShopSignRenderer.java");

        assertTrue(client.contains("GrowthcraftMilkBlockEntities.SHOP_SIGN.get(), ShopSignRenderer::new"));
        assertTrue(renderer.contains("extends HangingSignRenderer"));
        assertTrue(renderer.contains("extractRenderState"));
        assertTrue(renderer.contains("state.item.submit"));

        String entity = source("block/entity/ShopSignBlockEntity.java");
        assertTrue(entity.contains("extends SignBlockEntity"));
        assertTrue(entity.contains("ContainerHelper.loadAllItems"));
        assertTrue(entity.contains("ContainerHelper.saveAllItems"));
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/milk", relativePath));
    }
}
