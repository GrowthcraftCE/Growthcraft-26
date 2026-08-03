package growthcraft.milk.client.screen;

import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import growthcraft.milk.config.Reference;
import growthcraft.milk.menu.ChurnMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class ChurnScreen extends TexturedMachineScreen<ChurnMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/churn_screen.png");
    private static final int TANK_X = 64;
    private static final int TANK_Y = 18;
    private static final int TANK_WIDTH = 16;
    private static final int TANK_HEIGHT = 52;
    private final FluidTankRenderer tankRenderer;

    public ChurnScreen(ChurnMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, TEXTURE);
        this.tankRenderer = new FluidTankRenderer(TANK_WIDTH, TANK_HEIGHT, menu.getTankCapacity(), 0.85F);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        tankRenderer.render(graphics, leftPos + TANK_X, topPos + TANK_Y, menu.getFluidStack());
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (extractTankTooltip(graphics, mouseX, mouseY, TANK_X, TANK_Y, TANK_WIDTH, TANK_HEIGHT,
                menu.getFluidStack(), menu.getTankCapacity())) return;
        if (menu.getPlungesNeeded() > 0 && isMouseAbove(mouseX, mouseY, leftPos + 91, topPos + 17, 20, 14)) {
            graphics.setTooltipForNextFrame(font,
                    Component.literal(menu.getPlungeCount() + " / " + menu.getPlungesNeeded()), mouseX, mouseY);
        }
    }
}
