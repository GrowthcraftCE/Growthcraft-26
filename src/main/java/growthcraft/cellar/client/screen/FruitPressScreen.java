package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.FruitPressMenu;
import growthcraft.cellar.config.Reference;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class FruitPressScreen extends TexturedMachineScreen<FruitPressMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/fruit_press_screen.png");
    private static final int TANK_X = 72;
    private static final int TANK_Y = 17;
    private static final int TANK_W = 50;
    private static final int TANK_H = 52;

    private final FluidTankRenderer tankRenderer;

    public FruitPressScreen(FruitPressMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
        this.tankRenderer = new FluidTankRenderer(TANK_W, TANK_H, menu.getTankCapacity(), 0.85F);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        this.tankRenderer.render(graphics, this.leftPos + TANK_X, this.topPos + TANK_Y, this.menu.getFluidStack());
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (extractTankTooltip(graphics, mouseX, mouseY, TANK_X, TANK_Y, TANK_W, TANK_H,
                this.menu.getFluidStack(), this.menu.getTankCapacity())) {
            return;
        }
        super.extractTooltip(graphics, mouseX, mouseY);
    }
}
