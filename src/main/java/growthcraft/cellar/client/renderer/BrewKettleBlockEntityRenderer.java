package growthcraft.cellar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import growthcraft.cellar.block.entity.BrewKettleBlockEntity;
import growthcraft.lib.client.renderer.MachineFluidRenderer;
import growthcraft.lib.client.renderer.MachineFluidRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class BrewKettleBlockEntityRenderer implements BlockEntityRenderer<BrewKettleBlockEntity, MachineFluidRenderState> {
    private static final MachineFluidRenderer.Bounds KETTLE_BOUNDS = new MachineFluidRenderer.Bounds(
            1.0F / 16.0F, 2.5F / 16.0F, 1.0F / 16.0F,
            15.0F / 16.0F, 13.5F / 16.0F, 15.0F / 16.0F);

    public BrewKettleBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public MachineFluidRenderState createRenderState() {
        return new MachineFluidRenderState();
    }

    @Override
    public void extractRenderState(BrewKettleBlockEntity kettle, MachineFluidRenderState state, float partialTicks, Vec3 cameraPosition,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(kettle, state, partialTicks, cameraPosition, breakProgress);
        state.inputFluid = kettle.getInputTank().getFluid().copy();
        state.inputCapacity = kettle.getInputTank().getCapacity();
        state.outputFluid = kettle.getOutputTank().getFluid().copy();
        state.outputCapacity = kettle.getOutputTank().getCapacity();
    }

    @Override
    public void submit(MachineFluidRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.inputFluid.isEmpty()) {
            MachineFluidRenderer.submitSurface(poseStack, submitNodeCollector, state.inputFluid, state.inputCapacity, KETTLE_BOUNDS, state.lightCoords);
            return;
        }

        MachineFluidRenderer.submitSurface(poseStack, submitNodeCollector, state.outputFluid, state.outputCapacity, KETTLE_BOUNDS, state.lightCoords);
    }
}
