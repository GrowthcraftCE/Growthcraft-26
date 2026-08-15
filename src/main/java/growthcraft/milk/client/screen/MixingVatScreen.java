package growthcraft.milk.client.screen;

import growthcraft.lib.client.screen.FluidIngredientScreen;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import growthcraft.milk.block.entity.MixingVatBlockEntity;
import growthcraft.milk.config.Reference;
import growthcraft.milk.menu.MixingVatMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MixingVatScreen extends TexturedMachineScreen<MixingVatMenu> implements FluidIngredientScreen {
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
    private static final int HEAT_X = 99;
    private static final int HEAT_Y = 57;
    private static final int HEAT_U = 176;
    private static final int HEAT_V = 28;
    private static final int HEAT_W = 13;
    private static final int HEAT_H = 13;
    private static final int TEXTURE_SIZE = 256;
    private static final Style ACTIVATION_STYLE = Style.EMPTY.withColor(0xDDBB44);
    private static final Style ACTIVATION_ITEM_STYLE = Style.EMPTY.withColor(0xFFFF88);
    private static final Component EMPTY_HAND = Component.translatable("message.growthcraft_milk.get_using_item_empty_hand")
            .withStyle(ACTIVATION_ITEM_STYLE);
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
    private ItemStack cachedTooltipResult = ItemStack.EMPTY;
    private ItemStack cachedTooltipTool = ItemStack.EMPTY;
    private List<Component> cachedResultTooltip = List.of();

    public MixingVatScreen(MixingVatMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
        this.mainTankRenderer = new FluidTankRenderer(MAIN_TANK_W, MAIN_TANK_H, menu.getMainTankCapacity(), 0.85F);
        this.sideTankRenderer = new FluidTankRenderer(SIDE_TANK_W, SIDE_TANK_H, menu.getSideTankCapacity(), 0.85F);
    }

    @Override
    public List<FluidIngredientArea> getFluidIngredientAreas() {
        return List.of(
                new FluidIngredientArea(this.menu.getMainFluidStack(),
                        new Rect2i(this.leftPos + MAIN_TANK_X, this.topPos + MAIN_TANK_Y, MAIN_TANK_W, MAIN_TANK_H)),
                new FluidIngredientArea(this.menu.getSideFluidStack(),
                        new Rect2i(this.leftPos + SIDE_TANK_X, this.topPos + SIDE_TANK_Y, SIDE_TANK_W, SIDE_TANK_H)));
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
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()
                && this.hoveredSlot.index == MixingVatBlockEntity.SLOT_RESULT) {
            ItemStack result = this.hoveredSlot.getItem();
            ItemStack tool = this.menu.getResultActivationTool();
            if (!ItemStack.matches(result, this.cachedTooltipResult) || !ItemStack.matches(tool, this.cachedTooltipTool)) {
                List<Component> tooltip = new ArrayList<>(this.getTooltipFromContainerItem(result));
                Component toolName = tool.isEmpty()
                        ? EMPTY_HAND
                        : tool.getHoverName().copy().withStyle(ACTIVATION_ITEM_STYLE);
                tooltip.add(Component.translatable("message.growthcraft_milk.get_using_item", toolName)
                        .withStyle(ACTIVATION_STYLE));
                this.cachedResultTooltip = List.copyOf(tooltip);
                this.cachedTooltipResult = result.copy();
                this.cachedTooltipTool = tool.copy();
            }
            graphics.setTooltipForNextFrame(this.font, this.cachedResultTooltip,
                    result.getTooltipImage(), result, mouseX, mouseY);
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
