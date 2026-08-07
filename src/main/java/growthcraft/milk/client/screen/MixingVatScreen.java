package growthcraft.milk.client.screen;

import growthcraft.milk.menu.MixingVatMenu;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import growthcraft.milk.config.Reference;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class MixingVatScreen extends TexturedMachineScreen<MixingVatMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/mixing_vat_screen.png");
    private static final int MAIN_TANK_X = 49;
    private static final int MAIN_TANK_Y = 32;
    private static final int MAIN_TANK_W = 16;
    private static final int MAIN_TANK_H = 38;
    private static final int SIDE_TANK_X = 49;
    private static final int SIDE_TANK_Y = 18;
    private static final int SIDE_TANK_W = 16;
    private static final int SIDE_TANK_H = 11;
    private static final int PROGRESS_X = 100;
    private static final int PROGRESS_Y = 21;
    private static final int PROGRESS_W = 11;
    private static final int PROGRESS_H = 27;
    private static final int RESULT_SLOT_X = 124;
    private static final int RESULT_SLOT_Y = 18;
    private static final int HEAT_X = 99;
    private static final int HEAT_Y = 57;
    private static final int HEAT_U = 176;
    private static final int HEAT_V = 28;
    private static final int HEAT_W = 13;
    private static final int HEAT_H = 13;
    private static final int TEXTURE_SIZE = 256;
    private static final int[][] BUBBLE_PIXELS = new int[][] {
            { 102, 21 }, { 101, 22 }, { 102, 22 }, { 107, 25 }, { 106, 26 }, { 107, 26 },
            { 103, 29 }, { 102, 30 }, { 103, 30 }, { 107, 33 }, { 108, 33 }, { 102, 34 },
            { 106, 34 }, { 107, 34 }, { 108, 34 }, { 101, 35 }, { 102, 35 }, { 106, 35 },
            { 107, 35 }, { 108, 35 }, { 101, 38 }, { 102, 38 }, { 100, 39 }, { 101, 39 },
            { 102, 39 }, { 108, 39 }, { 100, 40 }, { 101, 40 }, { 102, 40 }, { 107, 40 },
            { 108, 40 }, { 104, 43 }, { 105, 43 }, { 106, 43 }, { 103, 44 }, { 104, 44 },
            { 105, 44 }, { 106, 44 }, { 103, 45 }, { 104, 45 }, { 105, 45 }, { 106, 45 },
            { 103, 46 }, { 104, 46 }, { 105, 46 }, { 106, 46 }, { 104, 47 }, { 105, 47 },
            { 106, 47 }
    };

    private final FluidTankRenderer mainTankRenderer;
    private final FluidTankRenderer sideTankRenderer;

    public MixingVatScreen(MixingVatMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
        this.mainTankRenderer = new FluidTankRenderer(MAIN_TANK_W, MAIN_TANK_H, menu.getMainTankCapacity(), 0.85F);
        this.sideTankRenderer = new FluidTankRenderer(SIDE_TANK_W, SIDE_TANK_H, menu.getSideTankCapacity(), 0.85F);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        this.mainTankRenderer.render(graphics, this.leftPos + MAIN_TANK_X, this.topPos + MAIN_TANK_Y, this.menu.getMainFluidStack());
        this.sideTankRenderer.render(graphics, this.leftPos + SIDE_TANK_X, this.topPos + SIDE_TANK_Y, this.menu.getSideFluidStack());
        drawProgress(graphics);
        if (this.menu.isHeated()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    this.leftPos + HEAT_X, this.topPos + HEAT_Y,
                    HEAT_U, HEAT_V, HEAT_W, HEAT_H, TEXTURE_SIZE, TEXTURE_SIZE);
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (extractTankTooltip(graphics, mouseX, mouseY, MAIN_TANK_X, MAIN_TANK_Y, MAIN_TANK_W, MAIN_TANK_H,
                this.menu.getMainFluidStack(), this.menu.getMainTankCapacity())) {
            return;
        }
        if (extractTankTooltip(graphics, mouseX, mouseY, SIDE_TANK_X, SIDE_TANK_Y, SIDE_TANK_W, SIDE_TANK_H,
                this.menu.getSideFluidStack(), this.menu.getSideTankCapacity())) {
            return;
        }
        if (isMouseAbove(mouseX, mouseY, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y, PROGRESS_W, PROGRESS_H)) {
            graphics.setTooltipForNextFrame(this.font,
                    Component.literal(this.menu.getPercentProgress() + "%"), mouseX, mouseY);
            return;
        }
        if (!this.menu.getResultActivationTool().isEmpty()
                && isMouseAbove(mouseX, mouseY, this.leftPos + RESULT_SLOT_X, this.topPos + RESULT_SLOT_Y, 16, 16)) {
            graphics.setTooltipForNextFrame(this.font,
                    Component.translatable("gui.growthcraft_milk.mixing_vat.result_tool",
                            this.menu.getResultActivationTool().getHoverName()), mouseX, mouseY);
            return;
        }
        super.extractTooltip(graphics, mouseX, mouseY);
    }

    private void drawProgress(GuiGraphicsExtractor graphics) {
        int progress = this.menu.getProgressionScaled(PROGRESS_H);
        if (progress <= 0) {
            return;
        }

        int minY = PROGRESS_Y + PROGRESS_H - progress;
        for (int[] pixel : BUBBLE_PIXELS) {
            if (pixel[1] >= minY) {
                graphics.fill(this.leftPos + pixel[0], this.topPos + pixel[1],
                        this.leftPos + pixel[0] + 1, this.topPos + pixel[1] + 1, 0xFFEDEDED);
            }
        }
    }
}
