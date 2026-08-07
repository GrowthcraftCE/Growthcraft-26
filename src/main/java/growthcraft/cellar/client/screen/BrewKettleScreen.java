package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.BrewKettleMenu;
import growthcraft.cellar.config.Reference;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.Rect2i;
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
    private static final int LID_BUTTON_X = 10;
    private static final int LID_BUTTON_Y = 27;
    private static final int LID_BUTTON_W = 13;
    private static final int LID_BUTTON_H = 16;
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

    public Rect2i getInputTankArea() {
        return new Rect2i(this.leftPos + INPUT_TANK_X, this.topPos + TANK_Y, TANK_W, TANK_H);
    }

    public Rect2i getOutputTankArea() {
        return new Rect2i(this.leftPos + OUTPUT_TANK_X, this.topPos + TANK_Y, TANK_W, TANK_H);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int lidV = this.menu.hasLid() ? 57 : 112;
        int lidU = isMouseAbove(mouseX, mouseY,
                this.leftPos + LID_BUTTON_X, this.topPos + LID_BUTTON_Y, LID_BUTTON_W, LID_BUTTON_H) ? 221 : 186;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                this.leftPos + 7, this.topPos + 16,
                lidU, lidV, 34, 54, TEXTURE_SIZE, TEXTURE_SIZE);
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
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (this.minecraft != null && this.minecraft.gameMode != null
                && isMouseAbove((int) event.x(), (int) event.y(),
                this.leftPos + LID_BUTTON_X, this.topPos + LID_BUTTON_Y, LID_BUTTON_W, LID_BUTTON_H)) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
            return true;
        }
        return super.mouseClicked(event, doubleClick);
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
