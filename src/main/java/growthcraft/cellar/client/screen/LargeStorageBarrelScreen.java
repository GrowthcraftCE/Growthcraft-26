package growthcraft.cellar.client.screen;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.menu.LargeStorageBarrelMenu;
import growthcraft.lib.client.screen.FluidIngredientScreen;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import growthcraft.lib.utils.FluidVolumeFormat;
import growthcraft.lib.utils.SequentialFluidGauge;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class LargeStorageBarrelScreen extends TexturedMachineScreen<LargeStorageBarrelMenu>
        implements FluidIngredientScreen {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            Reference.MODID, "textures/gui/large_storage_barrel_screen.png");
    private static final int TANK_X = 54;
    private static final int TANK_Y = 17;
    private static final int TANK_W = 69;
    private static final int TANK_H = 53;
    private static final int GAUGES = 4;
    private static final int GAUGE_W = 15;
    private static final int GAUGE_GAP = 3;

    public LargeStorageBarrelScreen(LargeStorageBarrelMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, TEXTURE);
    }

    @Override
    public List<FluidIngredientArea> getFluidIngredientAreas() {
        return List.of(new FluidIngredientArea(menu.getFluidStack(),
                new Rect2i(leftPos + TANK_X, topPos + TANK_Y, TANK_W, TANK_H)));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int x = leftPos + TANK_X;
        int y = topPos + TANK_Y;
        // Cover the old single-tank frame, then draw four recessed gauges.
        graphics.fill(x - 1, y - 1, x + TANK_W + 1, y + TANK_H + 1, 0xFFC6C6C6);
        var fluid = menu.getFluidStack();
        for (int index = 0; index < GAUGES; index++) {
            int gaugeX = x + index * (GAUGE_W + GAUGE_GAP);
            graphics.fill(gaugeX, y, gaugeX + GAUGE_W, y + TANK_H, 0xFF373737);
            graphics.fill(gaugeX + 1, y + 1, gaugeX + GAUGE_W, y + TANK_H, 0xFFFFFFFF);
            graphics.fill(gaugeX + 1, y + 1, gaugeX + GAUGE_W - 1, y + TANK_H - 1, 0xFF8B8B8B);
            int amount = SequentialFluidGauge.amount(fluid.getAmount(), menu.getTankCapacity(), GAUGES, index);
            new FluidTankRenderer(GAUGE_W - 2, TANK_H - 2,
                    SequentialFluidGauge.capacity(menu.getTankCapacity(), GAUGES, index), 0.85F)
                    .render(graphics, gaugeX + 1, y + 1, fluid.copyWithAmount(amount));
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFF404040, false);
        // The total occupies the usual inventory-caption row, above its slots.
        String total = FluidVolumeFormat.tank(menu.getFluidStack().getAmount(), menu.getTankCapacity());
        graphics.text(this.font, total, (this.imageWidth - this.font.width(total)) / 2,
                TANK_Y + TANK_H + 3, 0xFF404040, false);
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (!extractTankTooltip(graphics, mouseX, mouseY, TANK_X, TANK_Y, TANK_W, TANK_H,
                menu.getFluidStack(), menu.getTankCapacity(), true)) {
            super.extractTooltip(graphics, mouseX, mouseY);
        }
    }
}
