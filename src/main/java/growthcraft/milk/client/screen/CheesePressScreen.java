package growthcraft.milk.client.screen;

import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.milk.config.Reference;
import growthcraft.milk.menu.CheesePressMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CheesePressScreen extends TexturedMachineScreen<CheesePressMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/cheese_press_screen.png");
    private static final int PROGRESS_X = 83;
    private static final int PROGRESS_Y = 28;
    private static final int PROGRESS_U = 176;
    private static final int PROGRESS_V = 0;
    private static final int PROGRESS_W = 9;
    private static final int PROGRESS_H = 28;
    private static final int TEXTURE_SIZE = 256;

    public CheesePressScreen(CheesePressMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, TEXTURE);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int progress = this.menu.getProgressionScaled(PROGRESS_H);
        if (progress > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y,
                    PROGRESS_U, PROGRESS_V, PROGRESS_W, progress, TEXTURE_SIZE, TEXTURE_SIZE);
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.menu.getProgressionScaled(PROGRESS_H) > 0
                && isMouseAbove(mouseX, mouseY, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y,
                PROGRESS_W, PROGRESS_H)) {
            graphics.setTooltipForNextFrame(this.font,
                    Component.literal(this.menu.getPercentProgress() + "%"), mouseX, mouseY);
            return;
        }
        super.extractTooltip(graphics, mouseX, mouseY);
    }
}
