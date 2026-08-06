package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.FruitPressMenu;
import growthcraft.cellar.config.Reference;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class FruitPressScreen extends TexturedMachineScreen<FruitPressMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/fruit_press_screen.png");
    private static final int TANK_X = 72;
    private static final int TANK_Y = 17;
    private static final int TANK_W = 50;
    private static final int TANK_H = 52;
    private static final int PROGRESS_X = 51;
    private static final int PROGRESS_Y = 20;
    private static final int PROGRESS_U = 188;
    private static final int PROGRESS_V = 0;
    private static final int PROGRESS_W = 8;
    private static final int PROGRESS_H = 28;
    private static final int OUTPUT_SLOT_X = 141;
    private static final int OUTPUT_SLOT_Y = 53;

    private final FluidTankRenderer tankRenderer;

    public FruitPressScreen(FruitPressMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
        this.tankRenderer = new FluidTankRenderer(TANK_W, TANK_H, menu.getTankCapacity(), 0.85F);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + OUTPUT_SLOT_X - 1, this.topPos + OUTPUT_SLOT_Y - 1,
                7, 83, 18, 18, 256, 256);
        int progress = this.menu.getProgressionScaled(PROGRESS_H);
        if (progress > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y + PROGRESS_H - progress,
                    PROGRESS_U, PROGRESS_V + PROGRESS_H - progress, PROGRESS_W, progress, 256, 256);
        }
        this.tankRenderer.render(graphics, this.leftPos + TANK_X, this.topPos + TANK_Y, this.menu.getFluidStack());
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (extractTankTooltip(graphics, mouseX, mouseY, TANK_X, TANK_Y, TANK_W, TANK_H,
                this.menu.getFluidStack(), this.menu.getTankCapacity())) {
            return;
        }
        if (isMouseAbove(mouseX, mouseY, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y, PROGRESS_W, PROGRESS_H)) {
            graphics.setTooltipForNextFrame(this.font,
                    Component.translatable("growthcraft_cellar.tooltip.fruit_press.progress", this.menu.getPercentProgress()), mouseX, mouseY);
            return;
        }
        super.extractTooltip(graphics, mouseX, mouseY);
    }
}
