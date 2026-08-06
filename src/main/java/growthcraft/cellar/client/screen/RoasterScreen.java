package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.RoasterMenu;
import growthcraft.cellar.config.Reference;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class RoasterScreen extends TexturedMachineScreen<RoasterMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/roaster_screen.png");
    private static final int PROGRESS_X = 76;
    private static final int PROGRESS_Y = 44;
    private static final int PROGRESS_WIDTH = 28;
    private static final int PROGRESS_HEIGHT = 9;

    public RoasterScreen(RoasterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int progress = this.menu.getProgressionScaled(PROGRESS_WIDTH);
        if (progress > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y,
                    176, 0, progress, PROGRESS_HEIGHT, 256, 256);
        }
        if (this.menu.isHeated()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + 80, this.topPos + 56,
                    176, 28, 14, 14, 256, 256);
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (isMouseAbove(mouseX, mouseY, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y,
                PROGRESS_WIDTH, PROGRESS_HEIGHT)) {
            graphics.setTooltipForNextFrame(this.font,
                    Component.translatable("growthcraft_cellar.tooltip.roaster.progress",
                            this.menu.getRoastingLevel(), this.menu.getPercentProgress()), mouseX, mouseY);
            return;
        }
        super.extractTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        Component level = Component.translatable("label.growthcraft_cellar.roaster_level", this.menu.getRoastingLevel());
        graphics.centeredText(this.font, level, this.imageWidth / 2, 20, 4210752);
    }
}
