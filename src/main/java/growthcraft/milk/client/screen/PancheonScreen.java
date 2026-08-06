package growthcraft.milk.client.screen;

import growthcraft.milk.menu.PancheonMenu;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import growthcraft.milk.config.Reference;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class PancheonScreen extends TexturedMachineScreen<PancheonMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/pancheon_screen.png");
    private static final int INPUT_TANK_X = 62;
    private static final int INPUT_TANK_Y = 18;
    private static final int INPUT_TANK_W = 16;
    private static final int INPUT_TANK_H = 52;
    private static final int OUTPUT_TANK_X = 98;
    private static final int OUTPUT_0_TANK_Y = 18;
    private static final int OUTPUT_1_TANK_Y = 47;
    private static final int OUTPUT_TANK_W = 16;
    private static final int OUTPUT_TANK_H = 23;
    private static final int PROGRESS_X = 82;
    private static final int PROGRESS_Y = 29;
    private static final int PROGRESS_U = 176;
    private static final int PROGRESS_V = 42;
    private static final int PROGRESS_W = 13;
    private static final int PROGRESS_H = 29;

    private final FluidTankRenderer inputTankRenderer;
    private final FluidTankRenderer outputTankRenderer;

    public PancheonScreen(PancheonMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
        this.inputTankRenderer = new FluidTankRenderer(INPUT_TANK_W, INPUT_TANK_H, menu.getInputTankCapacity(), 0.85F);
        this.outputTankRenderer = new FluidTankRenderer(OUTPUT_TANK_W, OUTPUT_TANK_H, menu.getOutputTankCapacity(), 0.85F);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int progress = this.menu.getProgressionScaled(PROGRESS_H);
        if (progress > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y + PROGRESS_H - progress,
                    PROGRESS_U, PROGRESS_V + PROGRESS_H - progress, PROGRESS_W, progress, 256, 256);
        }
        this.inputTankRenderer.render(graphics, this.leftPos + INPUT_TANK_X, this.topPos + INPUT_TANK_Y, this.menu.getInputFluidStack());
        this.outputTankRenderer.render(graphics, this.leftPos + OUTPUT_TANK_X, this.topPos + OUTPUT_0_TANK_Y, this.menu.getOutput0FluidStack());
        this.outputTankRenderer.render(graphics, this.leftPos + OUTPUT_TANK_X, this.topPos + OUTPUT_1_TANK_Y, this.menu.getOutput1FluidStack());
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (extractTankTooltip(graphics, mouseX, mouseY, INPUT_TANK_X, INPUT_TANK_Y, INPUT_TANK_W, INPUT_TANK_H,
                this.menu.getInputFluidStack(), this.menu.getInputTankCapacity())) {
            return;
        }
        if (extractTankTooltip(graphics, mouseX, mouseY, OUTPUT_TANK_X, OUTPUT_0_TANK_Y, OUTPUT_TANK_W, OUTPUT_TANK_H,
                this.menu.getOutput0FluidStack(), this.menu.getOutputTankCapacity())) {
            return;
        }
        if (extractTankTooltip(graphics, mouseX, mouseY, OUTPUT_TANK_X, OUTPUT_1_TANK_Y, OUTPUT_TANK_W, OUTPUT_TANK_H,
                this.menu.getOutput1FluidStack(), this.menu.getOutputTankCapacity())) {
            return;
        }
        if (isMouseAbove(mouseX, mouseY, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y, PROGRESS_W, PROGRESS_H)) {
            graphics.setTooltipForNextFrame(this.font,
                    Component.literal(this.menu.getPercentProgress() + "%"), mouseX, mouseY);
            return;
        }
        super.extractTooltip(graphics, mouseX, mouseY);
    }
}
