package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.BrewKettleMenu;
import growthcraft.cellar.config.Reference;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class BrewKettleScreen extends TexturedMachineScreen<BrewKettleMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/brew_kettle_screen.png");
    private static final int INPUT_TANK_X = 46;
    private static final int OUTPUT_TANK_X = 114;
    private static final int TANK_Y = 17;
    private static final int TANK_W = 16;
    private static final int TANK_H = 52;
    private static final int PROGRESS_X = 98;
    private static final int PROGRESS_Y = 30;
    private static final int PROGRESS_U = 176;
    private static final int PROGRESS_V = 0;
    private static final int PROGRESS_W = 9;
    private static final int PROGRESS_H = 28;
    private static final int HEAT_X = 68;
    private static final int HEAT_Y = 53;
    private static final int HEAT_U = 176;
    private static final int HEAT_V = 28;
    private static final int HEAT_W = 13;
    private static final int HEAT_H = 13;
    private static final int TEXTURE_SIZE = 256;

    private final FluidTankRenderer tankRenderer;

    public BrewKettleScreen(BrewKettleMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
        this.tankRenderer = new FluidTankRenderer(TANK_W, TANK_H, menu.getTankCapacity(), 0.85F);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        this.tankRenderer.render(graphics, this.leftPos + INPUT_TANK_X, this.topPos + TANK_Y, this.menu.getInputFluidStack());
        this.tankRenderer.render(graphics, this.leftPos + OUTPUT_TANK_X, this.topPos + TANK_Y, this.menu.getOutputFluidStack());
        int progress = this.menu.getProgressionScaled(PROGRESS_H);
        if (progress > 0) {
            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    TEXTURE,
                    this.leftPos + PROGRESS_X,
                    this.topPos + PROGRESS_Y,
                    PROGRESS_U,
                    PROGRESS_V,
                    PROGRESS_W,
                    progress,
                    TEXTURE_SIZE,
                    TEXTURE_SIZE
            );
        }
        if (this.menu.isHeated()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    this.leftPos + HEAT_X, this.topPos + HEAT_Y,
                    HEAT_U, HEAT_V, HEAT_W, HEAT_H, TEXTURE_SIZE, TEXTURE_SIZE);
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (extractTankTooltip(graphics, mouseX, mouseY, INPUT_TANK_X, TANK_Y, TANK_W, TANK_H,
                this.menu.getInputFluidStack(), this.menu.getTankCapacity())) {
            return;
        }
        if (extractTankTooltip(graphics, mouseX, mouseY, OUTPUT_TANK_X, TANK_Y, TANK_W, TANK_H,
                this.menu.getOutputFluidStack(), this.menu.getTankCapacity())) {
            return;
        }
        super.extractTooltip(graphics, mouseX, mouseY);
    }
}
