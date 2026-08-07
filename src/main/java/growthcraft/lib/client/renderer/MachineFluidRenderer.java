package growthcraft.lib.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import growthcraft.lib.client.ClientFluidTypeExtensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public final class MachineFluidRenderer {
    private static final float DEFAULT_ALPHA = 0.85F;
    private static final Identifier WATER_STILL = Identifier.withDefaultNamespace("block/water_still");
    private static final Identifier LAVA_STILL = Identifier.withDefaultNamespace("block/lava_still");
    private static final int WATER_TINT = 0xFF3F76E4;
    private static final int LAVA_TINT = 0xFFFFFFFF;

    private MachineFluidRenderer() {
    }

    public static void submitSurface(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, FluidStack fluidStack, int capacity, Bounds bounds, int light) {
        if (fluidStack.isEmpty() || fluidStack.getAmount() <= 0 || capacity <= 0) {
            return;
        }

        RenderContext context = context(fluidStack);
        if (context == null) {
            return;
        }

        double fill = Math.clamp(fluidStack.getAmount() / (double) capacity, 0.0D, 1.0D);
        float y = (float) (bounds.minY() + (bounds.maxY() - bounds.minY()) * fill);
        submitNodeCollector.submitCustomGeometry(
                poseStack,
                RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS),
                (pose, consumer) -> putTopQuad(pose, consumer, context.sprite(), bounds.minX(), y, bounds.minZ(), bounds.maxX(), bounds.maxZ(),
                        context.red(), context.green(), context.blue(), context.alpha(), light));
    }

    public static void submitCuboid(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, FluidStack fluidStack, int capacity, Bounds bounds, int light) {
        if (fluidStack.isEmpty() || fluidStack.getAmount() <= 0 || capacity <= 0) {
            return;
        }

        RenderContext context = context(fluidStack);
        if (context == null) {
            return;
        }

        double fill = Math.clamp(fluidStack.getAmount() / (double) capacity, 0.0D, 1.0D);
        float maxY = (float) (bounds.minY() + (bounds.maxY() - bounds.minY()) * fill);
        submitNodeCollector.submitCustomGeometry(
                poseStack,
                RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS),
                (pose, consumer) -> {
                    putTopQuad(pose, consumer, context.sprite(), bounds.minX(), maxY, bounds.minZ(), bounds.maxX(), bounds.maxZ(),
                            context.red(), context.green(), context.blue(), context.alpha(), light);
                    putBottomQuad(pose, consumer, context.sprite(), bounds.minX(), bounds.minY(), bounds.minZ(), bounds.maxX(), bounds.maxZ(),
                            context.red(), context.green(), context.blue(), context.alpha(), light);
                    putNorthQuad(pose, consumer, context.sprite(), bounds.minX(), bounds.minY(), maxY, bounds.minZ(), bounds.maxX(),
                            context.red(), context.green(), context.blue(), context.alpha(), light);
                    putSouthQuad(pose, consumer, context.sprite(), bounds.minX(), bounds.minY(), maxY, bounds.maxZ(), bounds.maxX(),
                            context.red(), context.green(), context.blue(), context.alpha(), light);
                    putWestQuad(pose, consumer, context.sprite(), bounds.minX(), bounds.minY(), maxY, bounds.minZ(), bounds.maxZ(),
                            context.red(), context.green(), context.blue(), context.alpha(), light);
                    putEastQuad(pose, consumer, context.sprite(), bounds.maxX(), bounds.minY(), maxY, bounds.minZ(), bounds.maxZ(),
                            context.red(), context.green(), context.blue(), context.alpha(), light);
                });
    }

    private static RenderContext context(FluidStack fluidStack) {
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        Identifier texture;
        int tint;
        if (extensions instanceof ClientFluidTypeExtensions growthcraftExtensions) {
            texture = growthcraftExtensions.getStillTexture();
            if (texture == null) {
                texture = growthcraftExtensions.getFlowingTexture();
            }
            tint = growthcraftExtensions.getTintColor();
        } else if (fluidStack.getFluid().getFluidType() == Fluids.WATER.getFluidType()) {
            texture = WATER_STILL;
            tint = WATER_TINT;
        } else if (fluidStack.getFluid().getFluidType() == Fluids.LAVA.getFluidType()) {
            texture = LAVA_STILL;
            tint = LAVA_TINT;
        } else {
            return null;
        }
        if (texture == null) return null;

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getAtlasManager()
                .getAtlasOrThrow(AtlasIds.BLOCKS)
                .getSprite(texture);
        float alpha = alpha(tint);
        float red = ((tint >> 16) & 0xFF) / 255.0F;
        float green = ((tint >> 8) & 0xFF) / 255.0F;
        float blue = (tint & 0xFF) / 255.0F;
        return new RenderContext(sprite, red, green, blue, alpha);
    }

    private static float alpha(int tint) {
        float alpha = ((tint >>> 24) & 0xFF) / 255.0F;
        return alpha == 0.0F ? DEFAULT_ALPHA : alpha;
    }

    private static void putTopQuad(PoseStack.Pose pose, VertexConsumer consumer, TextureAtlasSprite sprite,
                                   float minX, float y, float minZ, float maxX, float maxZ,
                                   float red, float green, float blue, float alpha, int light) {
        vertex(pose, consumer, minX, y, minZ, red, green, blue, alpha, sprite.getU0(), sprite.getV0(), light, Direction.UP);
        vertex(pose, consumer, maxX, y, minZ, red, green, blue, alpha, sprite.getU1(), sprite.getV0(), light, Direction.UP);
        vertex(pose, consumer, maxX, y, maxZ, red, green, blue, alpha, sprite.getU1(), sprite.getV1(), light, Direction.UP);
        vertex(pose, consumer, minX, y, maxZ, red, green, blue, alpha, sprite.getU0(), sprite.getV1(), light, Direction.UP);
    }

    private static void putBottomQuad(PoseStack.Pose pose, VertexConsumer consumer, TextureAtlasSprite sprite,
                                      float minX, float y, float minZ, float maxX, float maxZ,
                                      float red, float green, float blue, float alpha, int light) {
        vertex(pose, consumer, minX, y, maxZ, red, green, blue, alpha, sprite.getU0(), sprite.getV1(), light, Direction.DOWN);
        vertex(pose, consumer, maxX, y, maxZ, red, green, blue, alpha, sprite.getU1(), sprite.getV1(), light, Direction.DOWN);
        vertex(pose, consumer, maxX, y, minZ, red, green, blue, alpha, sprite.getU1(), sprite.getV0(), light, Direction.DOWN);
        vertex(pose, consumer, minX, y, minZ, red, green, blue, alpha, sprite.getU0(), sprite.getV0(), light, Direction.DOWN);
    }

    private static void putNorthQuad(PoseStack.Pose pose, VertexConsumer consumer, TextureAtlasSprite sprite,
                                     float minX, float minY, float maxY, float z, float maxX,
                                     float red, float green, float blue, float alpha, int light) {
        vertex(pose, consumer, minX, minY, z, red, green, blue, alpha, sprite.getU0(), sprite.getV1(), light, Direction.NORTH);
        vertex(pose, consumer, minX, maxY, z, red, green, blue, alpha, sprite.getU0(), sprite.getV0(), light, Direction.NORTH);
        vertex(pose, consumer, maxX, maxY, z, red, green, blue, alpha, sprite.getU1(), sprite.getV0(), light, Direction.NORTH);
        vertex(pose, consumer, maxX, minY, z, red, green, blue, alpha, sprite.getU1(), sprite.getV1(), light, Direction.NORTH);
    }

    private static void putSouthQuad(PoseStack.Pose pose, VertexConsumer consumer, TextureAtlasSprite sprite,
                                     float minX, float minY, float maxY, float z, float maxX,
                                     float red, float green, float blue, float alpha, int light) {
        vertex(pose, consumer, maxX, minY, z, red, green, blue, alpha, sprite.getU1(), sprite.getV1(), light, Direction.SOUTH);
        vertex(pose, consumer, maxX, maxY, z, red, green, blue, alpha, sprite.getU1(), sprite.getV0(), light, Direction.SOUTH);
        vertex(pose, consumer, minX, maxY, z, red, green, blue, alpha, sprite.getU0(), sprite.getV0(), light, Direction.SOUTH);
        vertex(pose, consumer, minX, minY, z, red, green, blue, alpha, sprite.getU0(), sprite.getV1(), light, Direction.SOUTH);
    }

    private static void putWestQuad(PoseStack.Pose pose, VertexConsumer consumer, TextureAtlasSprite sprite,
                                    float x, float minY, float maxY, float minZ, float maxZ,
                                    float red, float green, float blue, float alpha, int light) {
        vertex(pose, consumer, x, minY, maxZ, red, green, blue, alpha, sprite.getU1(), sprite.getV1(), light, Direction.WEST);
        vertex(pose, consumer, x, maxY, maxZ, red, green, blue, alpha, sprite.getU1(), sprite.getV0(), light, Direction.WEST);
        vertex(pose, consumer, x, maxY, minZ, red, green, blue, alpha, sprite.getU0(), sprite.getV0(), light, Direction.WEST);
        vertex(pose, consumer, x, minY, minZ, red, green, blue, alpha, sprite.getU0(), sprite.getV1(), light, Direction.WEST);
    }

    private static void putEastQuad(PoseStack.Pose pose, VertexConsumer consumer, TextureAtlasSprite sprite,
                                    float x, float minY, float maxY, float minZ, float maxZ,
                                    float red, float green, float blue, float alpha, int light) {
        vertex(pose, consumer, x, minY, minZ, red, green, blue, alpha, sprite.getU0(), sprite.getV1(), light, Direction.EAST);
        vertex(pose, consumer, x, maxY, minZ, red, green, blue, alpha, sprite.getU0(), sprite.getV0(), light, Direction.EAST);
        vertex(pose, consumer, x, maxY, maxZ, red, green, blue, alpha, sprite.getU1(), sprite.getV0(), light, Direction.EAST);
        vertex(pose, consumer, x, minY, maxZ, red, green, blue, alpha, sprite.getU1(), sprite.getV1(), light, Direction.EAST);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer consumer,
                               float x, float y, float z,
                               float red, float green, float blue, float alpha,
                               float u, float v, int light, Direction normal) {
        consumer.addVertex(pose.pose(), x, y, z)
                .setColor(red, green, blue, alpha)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(normal.getStepX(), normal.getStepY(), normal.getStepZ());
    }

    public record Bounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        public Bounds {
            if (maxX <= minX || maxY <= minY || maxZ <= minZ) {
                throw new IllegalArgumentException("Fluid render bounds must have positive volume");
            }
        }
    }

    private record RenderContext(TextureAtlasSprite sprite, float red, float green, float blue, float alpha) {
    }
}
