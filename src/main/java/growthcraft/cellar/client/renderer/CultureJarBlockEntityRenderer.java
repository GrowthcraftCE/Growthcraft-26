package growthcraft.cellar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import growthcraft.cellar.block.entity.CultureJarBlockEntity;
import growthcraft.lib.client.renderer.MachineFluidRenderer;
import growthcraft.lib.client.renderer.MachineFluidRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CultureJarBlockEntityRenderer implements BlockEntityRenderer<CultureJarBlockEntity, MachineFluidRenderState> {
    private static final MachineFluidRenderer.Bounds JAR_BOUNDS = new MachineFluidRenderer.Bounds(
            6.125F / 16.0F, 0.1F / 16.0F, 6.125F / 16.0F,
            9.875F / 16.0F, 5.5F / 16.0F, 9.875F / 16.0F);

    public CultureJarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public MachineFluidRenderState createRenderState() {
        return new MachineFluidRenderState();
    }

    @Override
    public void extractRenderState(CultureJarBlockEntity jar, MachineFluidRenderState state, float partialTicks, Vec3 cameraPosition,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(jar, state, partialTicks, cameraPosition, breakProgress);
        state.inputFluid = jar.getTank().getFluid().copy();
        state.inputCapacity = jar.getTank().getCapacity();
    }

    @Override
    public void submit(MachineFluidRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        MachineFluidRenderer.submitCuboid(poseStack, submitNodeCollector, state.inputFluid, state.inputCapacity, JAR_BOUNDS, state.lightCoords);
    }
}
