package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.FermentationBarrelMenu;
import growthcraft.cellar.config.Reference;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.FluidIngredientScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FermentationBarrelScreen extends TexturedMachineScreen<FermentationBarrelMenu> implements FluidIngredientScreen {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/fermentation_barrel_screen.png");
    private static final Identifier BUBBLE_OVERLAY = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/fermentation_bubbles_overlay.png");
    private static final Identifier LOCK_TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/fermentation_lock.png");
    private static final Identifier UNLOCK_TEXTURE = Identifier.fromNamespaceAndPath(Reference.MODID, "textures/gui/fermentation_unlock.png");
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
    private static final int BUBBLE_X = 59;
    private static final int BUBBLE_Y = 20;
    private static final int BUBBLE_W = 9;
    private static final int BUBBLE_H = 28;
    private static final int STOP_X = 154;
    private static final int STOP_Y = 53;
    private static final int STOP_W = 16;
    private static final int STOP_H = 16;
    private static final Component YEAST_WARNING = Component.translatable("growthcraft_cellar.tooltip.fermentation.yeast_warning")
            .withStyle(Style.EMPTY.withColor(0xd5bb88));
    private static final Component YEAST_ERROR = Component.translatable("growthcraft_cellar.tooltip.fermentation.yeast_error")
            .withStyle(Style.EMPTY.withColor(0xd68a71));

    public FermentationBarrelScreen(FermentationBarrelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
    }

    @Override
    public List<FluidIngredientArea> getFluidIngredientAreas() {
        return List.of(new FluidIngredientArea(this.menu.getFluidStack(),
                new Rect2i(this.leftPos + TANK_X, this.topPos + TANK_Y, TANK_W, TANK_H)));
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
        if (this.menu.isProcessing()) {
            int bubbleScreenX = this.leftPos + BUBBLE_X;
            int bubbleScreenY = this.topPos + BUBBLE_Y;
            int fillHeight = 1 + (this.menu.getProcessTicks() / 3) % BUBBLE_H;
            graphics.enableScissor(bubbleScreenX, bubbleScreenY + BUBBLE_H - fillHeight,
                    bubbleScreenX + BUBBLE_W, bubbleScreenY + BUBBLE_H);
            graphics.blit(RenderPipelines.GUI_TEXTURED, BUBBLE_OVERLAY,
                    bubbleScreenX, bubbleScreenY,
                    0, 0, BUBBLE_W, BUBBLE_H, BUBBLE_W, BUBBLE_H);
            graphics.disableScissor();
        }
        new FluidTankRenderer(TANK_W, TANK_H, this.menu.getTankCapacity(), 0.85F)
                .render(graphics, this.leftPos + TANK_X, this.topPos + TANK_Y, this.menu.getFluidStack());
        if (this.menu.allowsManualUnlock() && (this.menu.isProcessing() || this.menu.isManuallyStopped())) {
            graphics.blit(RenderPipelines.GUI_TEXTURED,
                    this.menu.isProcessing() ? LOCK_TEXTURE : UNLOCK_TEXTURE,
                    this.leftPos + STOP_X, this.topPos + STOP_Y,
                    0, 0, STOP_W, STOP_H, STOP_W, STOP_H);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (this.menu.allowsManualUnlock() && (this.menu.isProcessing() || this.menu.isManuallyStopped())
                && this.minecraft != null && this.minecraft.gameMode != null
                && isMouseAbove((int) event.x(), (int) event.y(),
                this.leftPos + STOP_X, this.topPos + STOP_Y, STOP_W, STOP_H)) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (extractTankTooltip(graphics, mouseX, mouseY, TANK_X, TANK_Y, TANK_W, TANK_H,
                this.menu.getFluidStack(), this.menu.getTankCapacity())) {
            return;
        }
        if (isMouseAbove(mouseX, mouseY, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y, PROGRESS_W, PROGRESS_H)) {
            graphics.setTooltipForNextFrame(this.font,
                    Component.literal((this.menu.isRedstonePaused() ? "Paused — " : "")
                            + this.menu.getPercentProgress() + "% — " + this.menu.getRemainingSeconds() + "s remaining"), mouseX, mouseY);
            return;
        }
        if (this.menu.allowsManualUnlock() && (this.menu.isProcessing() || this.menu.isManuallyStopped()) && isMouseAbove(mouseX, mouseY,
                this.leftPos + STOP_X, this.topPos + STOP_Y, STOP_W, STOP_H)) {
            graphics.setTooltipForNextFrame(this.font,
                    (this.menu.isProcessing()
                            ? List.of(
                                    Component.literal("Stop fermentation"),
                                    Component.literal("Discards progress"),
                                    Component.literal("Unlocks contents"))
                            : List.of(
                                    Component.literal("Resume fermentation"),
                                    Component.literal("Locks contents"))).stream()
                            .map(Component::getVisualOrderText)
                            .toList(),
                    mouseX, mouseY);
            return;
        }
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()
                && this.hoveredSlot.index == FermentationBarrelMenu.YEAST_SLOT
                && (this.menu.hasYeastWarning() || this.menu.hasYeastError())) {
            ItemStack stack = this.hoveredSlot.getItem();
            List<Component> tooltip = new ArrayList<>(this.getTooltipFromContainerItem(stack));
            tooltip.add(this.menu.hasYeastWarning() ? YEAST_WARNING : YEAST_ERROR);
            graphics.setTooltipForNextFrame(this.font, tooltip, stack.getTooltipImage(), stack, mouseX, mouseY);
            return;
        }
        super.extractTooltip(graphics, mouseX, mouseY);
    }
}
