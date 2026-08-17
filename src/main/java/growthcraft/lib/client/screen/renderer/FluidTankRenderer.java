package growthcraft.lib.client.screen.renderer;

import growthcraft.lib.client.ClientFluidTypeExtensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Simple reusable renderer for drawing a fluid stack inside a rectangular GUI region.
 * <p>
 * Features:
     * - Applies the fluid's tint color
     * - Supports configurable alpha scaling for additional transparency control
     * - Optionally draws a colored overlay rectangle on top (e.g., to simulate a glass tint)
 * <p>
 * This class is UI-agnostic and can be reused by any screen.
 *
 * @param alphaScale 1.0 = original alpha; <1 for more transparency
 */
public record FluidTankRenderer(int width, int height, int capacityMb, float alphaScale) {
    private static final int TILE_SIZE = 16;
    private static final int WATER_TINT = 0xFF3F76E4;
    private static final int LAVA_TINT = 0xFFFFFFFF;
    private static final Identifier WATER_STILL = Identifier.withDefaultNamespace("block/water_still");
    private static final Identifier LAVA_STILL = Identifier.withDefaultNamespace("block/lava_still");

    /**
     * @param width      tank draw width in pixels
     * @param height     tank draw height in pixels
     * @param capacityMb tank capacity in millibuckets
     * @param alphaScale scales the fluid's tint alpha (1.0 keeps original, 0.1 makes it very transparent)
     */
    public FluidTankRenderer(int width, int height, int capacityMb, float alphaScale) {
        this.width = width;
        this.height = height;
        this.capacityMb = Math.max(1, capacityMb);
        this.alphaScale = Math.max(0f, Math.min(1f, alphaScale));
    }

    /**
     * Render the given fluid into the specified screen-space rectangle.
     *
     * @param graphics gui graphics
     * @param x        left x in screen coords
     * @param y        top y in screen coords
     * @param stack    fluid stack to render (amount determines fill height)
     */
    public void render(GuiGraphicsExtractor graphics, int x, int y, FluidStack stack) {
        if (stack == null || stack.isEmpty()) return;
        int amount = stack.getAmount();
        if (amount <= 0) return;

        int filled = Math.min(height, Math.max(1, (int) Math.floor((amount / (double) capacityMb) * height)));
        int yTop = y + (height - filled);

        renderSprite(graphics, x, yTop, filled, stack);
    }

    /**
     * Optionally draw a simple colored overlay rectangle (e.g., glass tint).
     * Provide ARGB packed color.
     */
    public void renderOverlayTint(GuiGraphicsExtractor graphics, int x, int y, int argbColor) {
        graphics.fill(x, y, x + this.width, y + this.height, argbColor);
    }

    /**
     * Render fluid with a subtle animated shimmer overlay. Call this instead of render() for the effect.
     */
    public void renderWithShimmer(GuiGraphicsExtractor graphics, int x, int y, FluidStack stack) {
        if (stack == null || stack.isEmpty()) return;
        int amount = stack.getAmount();
        if (amount <= 0) return;

        int filled = Math.min(height, Math.max(1, (int) Math.floor((amount / (double) capacityMb) * height)));
        int yTop = y + (height - filled);

        renderSprite(graphics, x, yTop, filled, stack);

        int pulse = 0x10000000 + (int) ((System.currentTimeMillis() % 1800L) / 1800.0F * 0x10000000);
        graphics.fill(x, yTop, x + this.width, yTop + Math.max(1, filled / 4), pulse | 0x00FFFFFF);
    }

    private void renderSprite(GuiGraphicsExtractor graphics, int x, int yTop, int filled, FluidStack stack) {
        TextureAtlasSprite sprite = sprite(graphics, stack);
        int color = tint(stack);
        for (int remainingY = filled; remainingY > 0; remainingY -= TILE_SIZE) {
            int tileHeight = Math.min(TILE_SIZE, remainingY);
            int tileY = yTop + remainingY - tileHeight;
            for (int drawX = 0; drawX < this.width; drawX += TILE_SIZE) {
                int tileWidth = Math.min(TILE_SIZE, this.width - drawX);
                int tileX = x + drawX;
                graphics.enableScissor(tileX, tileY, tileX + tileWidth, tileY + tileHeight);
                graphics.blitSprite(
                        RenderPipelines.GUI_TEXTURED,
                        sprite,
                        tileX,
                        tileY - (TILE_SIZE - tileHeight),
                        TILE_SIZE,
                        TILE_SIZE,
                        color
                );
                graphics.disableScissor();
            }
        }
    }

    private static TextureAtlasSprite sprite(GuiGraphicsExtractor graphics, FluidStack stack) {
        Identifier texture = texture(stack);
        return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(texture);
    }

    private static Identifier texture(FluidStack stack) {
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(stack.getFluid());
        if (extensions instanceof ClientFluidTypeExtensions growthcraftExtensions) {
            if (growthcraftExtensions.getStillTexture() != null) return growthcraftExtensions.getStillTexture();
            if (growthcraftExtensions.getFlowingTexture() != null) return growthcraftExtensions.getFlowingTexture();
        }

        Identifier reflected = extensionTexture(extensions, stack);
        if (reflected != null) return reflected;

        if (stack.getFluid().getFluidType() == Fluids.WATER.getFluidType()) return WATER_STILL;
        if (stack.getFluid().getFluidType() == Fluids.LAVA.getFluidType()) return LAVA_STILL;

        Identifier id = BuiltInRegistries.FLUID.getKey(stack.getFluid());
        return Identifier.fromNamespaceAndPath(id.getNamespace(), "block/fluid/" + id.getPath().replace("_fluid_source", "").replace("_fluid_flowing", "") + "_fluid_still");
    }

    private static Identifier extensionTexture(IClientFluidTypeExtensions extensions, FluidStack stack) {
        try {
            var method = extensions.getClass().getDeclaredMethod("getStillTexture", FluidStack.class);
            method.setAccessible(true);
            Object texture = method.invoke(extensions, stack);
            return texture instanceof Identifier id ? id : null;
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    private int tint(FluidStack stack) {
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(stack.getFluid());
        int tint = vanillaTint(stack);
        if (tint == 0) {
            tint = extensions instanceof ClientFluidTypeExtensions growthcraftExtensions
                ? growthcraftExtensions.getTintColor()
                : extensionTint(extensions, stack);
        }
        int alpha = (int) (((tint >>> 24) & 0xFF) * this.alphaScale);
        if (alpha == 0) {
            alpha = (int) (0x7F * this.alphaScale);
        }
        return (alpha << 24) | (tint & 0x00FFFFFF);
    }

    private static int vanillaTint(FluidStack stack) {
        if (stack.getFluid().getFluidType() == Fluids.WATER.getFluidType()) return WATER_TINT;
        if (stack.getFluid().getFluidType() == Fluids.LAVA.getFluidType()) return LAVA_TINT;
        return 0;
    }

    private static int extensionTint(IClientFluidTypeExtensions extensions, FluidStack stack) {
        try {
            var method = extensions.getClass().getDeclaredMethod("getTintColor", FluidStack.class);
            method.setAccessible(true);
            Object tint = method.invoke(extensions, stack);
            return tint instanceof Integer color ? color : 0xFFFFFFFF;
        } catch (ReflectiveOperationException ignored) {
            return 0xFFFFFFFF;
        }
    }
}
