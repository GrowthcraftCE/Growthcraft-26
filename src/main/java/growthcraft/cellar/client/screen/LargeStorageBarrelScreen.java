package growthcraft.cellar.client.screen;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.menu.LargeStorageBarrelMenu;
import growthcraft.lib.client.screen.FluidIngredientScreen;
import growthcraft.lib.client.screen.TexturedMachineScreen;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
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
    private static final int TANK_W = 68;
    private static final int TANK_H = 52;

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
        new FluidTankRenderer(TANK_W, TANK_H, menu.getTankCapacity(), 0.85F)
                .render(graphics, leftPos + TANK_X, topPos + TANK_Y, menu.getFluidStack());
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (!extractTankTooltip(graphics, mouseX, mouseY, TANK_X, TANK_Y, TANK_W, TANK_H,
                menu.getFluidStack(), menu.getTankCapacity())) {
            super.extractTooltip(graphics, mouseX, mouseY);
        }
    }
}
