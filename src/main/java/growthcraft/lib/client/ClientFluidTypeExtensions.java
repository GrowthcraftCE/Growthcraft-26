package growthcraft.lib.client;

import growthcraft.lib.fluid.FluidClientProperties;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector4f;

public class ClientFluidTypeExtensions implements IClientFluidTypeExtensions {
    private static final Identifier UNDERWATER_LOCATION = Identifier.parse("textures/misc/underwater.png");
    private final FluidClientProperties properties;

    public ClientFluidTypeExtensions(FluidClientProperties properties) { this.properties = properties; }
    public Identifier getStillTexture() { return properties.still; }
    public Identifier getFlowingTexture() { return properties.flowing; }
    public Identifier getOverlayTexture() { return properties.overlay; }
    public int getTintColor() { return properties.tintColor; }
    public int getTintColor(FluidStack stack) { return getTintColor(); }
    public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) { return getTintColor(); }

    @Override
    public Identifier getRenderOverlayTexture(Minecraft minecraft) {
        return properties.renderOverlay != null ? properties.renderOverlay : UNDERWATER_LOCATION;
    }

    @Override
    public void modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                               int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor) {
        if (properties.fogColor != null) {
            fluidFogColor.set(properties.fogColor.x(), properties.fogColor.y(), properties.fogColor.z(), fluidFogColor.w());
        }
    }
}
