package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.CultureJarMenu;
import growthcraft.cellar.config.Reference;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.FluidIngredientScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class CultureJarScreen extends TexturedMachineScreen<CultureJarMenu> implements FluidIngredientScreen {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/culture_jar_screen.png");
    private static final int TANK_X = 80;
    private static final int TANK_Y = 18;
    private static final int TANK_W = 16;
    private static final int TANK_H = 52;
    private static final int PROGRESS_X = 106;
    private static final int PROGRESS_Y = 43;
    private static final int PROGRESS_W = 9;
    private static final int PROGRESS_H = 27;
    private static final int[][] BUBBLE_PIXELS = {
            {108, 43}, {107, 44}, {108, 44}, {113, 47}, {112, 48}, {113, 48},
            {109, 51}, {108, 52}, {109, 52}, {113, 55}, {114, 55}, {108, 56},
            {112, 56}, {113, 56}, {114, 56}, {107, 57}, {108, 57}, {112, 57},
            {113, 57}, {114, 57}, {107, 60}, {108, 60}, {106, 61}, {107, 61},
            {108, 61}, {114, 61}, {106, 62}, {107, 62}, {108, 62}, {113, 62},
            {114, 62}, {110, 65}, {111, 65}, {112, 65}, {109, 66}, {110, 66},
            {111, 66}, {112, 66}, {109, 67}, {110, 67}, {111, 67}, {112, 67},
            {109, 68}, {110, 68}, {111, 68}, {112, 68}, {110, 69}, {111, 69}, {112, 69}
    };

    private final FluidTankRenderer tankRenderer;

    public CultureJarScreen(CultureJarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
        this.tankRenderer = new FluidTankRenderer(TANK_W, TANK_H, menu.getTankCapacity(), 0.7F);
    }

    @Override
    public List<FluidIngredientArea> getFluidIngredientAreas() {
        return List.of(new FluidIngredientArea(this.menu.getClientFluidStack(),
                new Rect2i(this.leftPos + TANK_X, this.topPos + TANK_Y, TANK_W, TANK_H)));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        if (this.menu.isHeated()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + 59, this.topPos + 57,
                    176, 28, 13, 13, 256, 256);
        }
        this.tankRenderer.render(graphics, this.leftPos + TANK_X, this.topPos + TANK_Y, this.menu.getClientFluidStack());
        int progress = this.menu.getProgressionScaled(PROGRESS_H);
        if (progress > 0) {
            int minY = PROGRESS_Y + PROGRESS_H - progress;
            for (int[] pixel : BUBBLE_PIXELS) {
                if (pixel[1] >= minY) {
                    graphics.fill(this.leftPos + pixel[0], this.topPos + pixel[1],
                            this.leftPos + pixel[0] + 1, this.topPos + pixel[1] + 1, 0xFFEDEDED);
                }
            }
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (extractTankTooltip(graphics, mouseX, mouseY, TANK_X, TANK_Y, TANK_W, TANK_H,
                this.menu.getClientFluidStack(), this.menu.getTankCapacity())) {
            return;
        }
        if (this.menu.getProcessTotal() > 0 && isMouseAbove(mouseX, mouseY,
                this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y, PROGRESS_W, PROGRESS_H)) {
            graphics.setTooltipForNextFrame(this.font, Component.literal(this.menu.getPercentProgress() + "%"), mouseX, mouseY);
            return;
        }
        super.extractTooltip(graphics, mouseX, mouseY);
    }
}
