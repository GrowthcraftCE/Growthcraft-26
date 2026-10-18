package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.FermentationBarrelMenu;
import growthcraft.cellar.config.Reference;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.FluidIngredientScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
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

    private static final Component STOP_ICON = Component.literal("\u25a0").withStyle(Style.EMPTY.withColor(0xcccccc));
    private static final Component START_ICON = Component.literal("\u25b6").withStyle(Style.EMPTY.withColor(0x55ff55));
    private static final Component YEAST_PROCESSING = Component.translatable("growthcraft_cellar.tooltip.fermentation.processing");
    private static final Component YEAST_PAUSED = Component.translatable("growthcraft_cellar.tooltip.fermentation.paused");
    private Button fermentationControl;

    public FermentationBarrelScreen(FermentationBarrelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
    }

    @Override
    protected void init() {
        super.init();
        fermentationControl = addRenderableWidget(Button.builder(STOP_ICON, button -> {
                    if (this.minecraft != null && this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
                    }
                }).bounds(this.leftPos + STOP_X, this.topPos + STOP_Y, STOP_W, STOP_H)
                .createNarration(supplier -> Component.translatable(this.menu.isProcessing()
                        ? "growthcraft_cellar.control.fermentation.stop" : "growthcraft_cellar.control.fermentation.start"))
                .build());
        updateFermentationControl();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateFermentationControl();
    }

    private void updateFermentationControl() {
        fermentationControl.visible = this.menu.allowsManualUnlock()
                && (this.menu.isProcessing() || this.menu.isManuallyStopped());
        fermentationControl.active = fermentationControl.visible;
        fermentationControl.setMessage(this.menu.isProcessing() ? STOP_ICON : START_ICON);
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
        updateFermentationControl();
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
                                    Component.translatable("growthcraft_cellar.control.fermentation.stop"),
                                    Component.translatable("growthcraft_cellar.tooltip.fermentation.discards_progress"),
                                    Component.translatable("growthcraft_cellar.tooltip.fermentation.unlocks_contents"))
                            : List.of(
                                    Component.translatable("growthcraft_cellar.control.fermentation.start"),
                                    Component.translatable("growthcraft_cellar.tooltip.fermentation.locks_contents"))).stream()
                            .map(Component::getVisualOrderText)
                            .toList(),
                    mouseX, mouseY);
            return;
        }
        if (this.hoveredSlot != null && this.hoveredSlot.hasItem()
                && this.hoveredSlot.index == FermentationBarrelMenu.YEAST_SLOT
                && (this.menu.isProcessing() || this.menu.hasYeastWarning() || this.menu.hasYeastError())) {
            ItemStack stack = this.hoveredSlot.getItem();
            List<Component> tooltip = new ArrayList<>(this.getTooltipFromContainerItem(stack));
            tooltip.add(this.menu.isProcessing()
                    ? (this.menu.isRedstonePaused() ? YEAST_PAUSED : YEAST_PROCESSING)
                    : (this.menu.hasYeastWarning() ? YEAST_WARNING : YEAST_ERROR));
            graphics.setTooltipForNextFrame(this.font, tooltip, stack.getTooltipImage(), stack, mouseX, mouseY);
            return;
        }
        super.extractTooltip(graphics, mouseX, mouseY);
    }
}
