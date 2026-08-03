package growthcraft.milk.client.screen;

import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.milk.config.Reference;
import growthcraft.milk.menu.CheesePressMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CheesePressScreen extends TexturedMachineScreen<CheesePressMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/cheese_press_screen.png");

    public CheesePressScreen(CheesePressMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, TEXTURE);
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (menu.getProgressionScaled(100) > 0 && isMouseAbove(mouseX, mouseY, leftPos + 78, topPos + 34, 18, 18)) {
            graphics.setTooltipForNextFrame(font,
                    Component.literal(menu.getPercentProgress() + "%"), mouseX, mouseY);
        }
    }
}
