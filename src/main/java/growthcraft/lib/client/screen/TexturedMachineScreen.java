package growthcraft.lib.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.fluids.FluidStack;

public class TexturedMachineScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 256;

    private final Identifier texture;

    public TexturedMachineScreen(T menu, Inventory playerInventory, Component title, Identifier texture) {
        super(menu, playerInventory, title, 176, 166);
        this.texture = texture;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                this.texture,
                this.leftPos,
                this.topPos,
                0.0F,
                0.0F,
                this.imageWidth,
                this.imageHeight,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );
    }

    protected boolean extractTankTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int x, int y, int width, int height, FluidStack stack, int capacity) {
        if (!isMouseAbove(mouseX, mouseY, this.leftPos + x, this.topPos + y, width, height)) {
            return false;
        }
        Component name = stack.isEmpty() ? Component.literal("Empty") : stack.getHoverName();
        Component tooltip = Component.empty()
                .append(name)
                .append(Component.literal(" "))
                .append(Component.literal(stack.getAmount() + " mB / " + capacity + " mB"));
        graphics.setTooltipForNextFrame(this.font, tooltip, mouseX, mouseY);
        return true;
    }

    protected static boolean isMouseAbove(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
