package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.CultureJarMenu;
import growthcraft.cellar.config.Reference;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CultureJarScreen extends TexturedMachineScreen<CultureJarMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/culture_jar_screen.png");
    private static final int TANK_X = 80;
    private static final int TANK_Y = 18;
    private static final int TANK_W = 16;
    private static final int TANK_H = 52;

    private final FluidTankRenderer tankRenderer;

    public CultureJarScreen(CultureJarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
        this.tankRenderer = new FluidTankRenderer(TANK_W, TANK_H, menu.getTankCapacity(), 0.7F);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        this.tankRenderer.render(graphics, this.leftPos + TANK_X, this.topPos + TANK_Y, this.menu.getClientFluidStack());
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (extractTankTooltip(graphics, mouseX, mouseY, TANK_X, TANK_Y, TANK_W, TANK_H,
                this.menu.getClientFluidStack(), this.menu.getTankCapacity())) {
            return;
        }
        super.extractTooltip(graphics, mouseX, mouseY);
    }
}
