package growthcraft.milk.client.screen;

import growthcraft.milk.menu.MixingVatMenu;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import growthcraft.milk.config.Reference;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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
        super.extractTooltip(graphics, mouseX, mouseY);
    }
}
