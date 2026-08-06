package growthcraft.milk.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import growthcraft.lib.client.renderer.MachineFluidRenderer;
import growthcraft.lib.client.renderer.MachineFluidRenderState;
import growthcraft.milk.block.entity.PancheonBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PancheonBlockEntityRenderer implements BlockEntityRenderer<PancheonBlockEntity, MachineFluidRenderState> {
    private static final MachineFluidRenderer.Bounds FULL_BOUNDS = new MachineFluidRenderer.Bounds(
            1.0F / 16.0F, 1.0F / 16.0F, 1.0F / 16.0F,
            15.0F / 16.0F, 3.5F / 16.0F, 15.0F / 16.0F);
    private static final MachineFluidRenderer.Bounds LOWER_OUTPUT_BOUNDS = new MachineFluidRenderer.Bounds(
            1.0F / 16.0F, 1.0F / 16.0F, 1.0F / 16.0F,
            15.0F / 16.0F, 2.25F / 16.0F, 15.0F / 16.0F);
    private static final MachineFluidRenderer.Bounds UPPER_OUTPUT_BOUNDS = new MachineFluidRenderer.Bounds(
            1.0F / 16.0F, 2.25F / 16.0F, 1.0F / 16.0F,
            15.0F / 16.0F, 3.5F / 16.0F, 15.0F / 16.0F);

    public PancheonBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public MachineFluidRenderState createRenderState() {
        return new MachineFluidRenderState();
    }

    @Override
    public void extractRenderState(PancheonBlockEntity pancheon, MachineFluidRenderState state, float partialTicks, Vec3 cameraPosition,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(pancheon, state, partialTicks, cameraPosition, breakProgress);
        state.inputFluid = pancheon.getInputTank().getFluid().copy();
        state.inputCapacity = pancheon.getInputTank().getCapacity();
        state.outputFluid = pancheon.getOutputTank0().getFluid().copy();
        state.outputCapacity = pancheon.getOutputTank0().getCapacity();
        state.outputFluid1 = pancheon.getOutputTank1().getFluid().copy();
        state.outputCapacity1 = pancheon.getOutputTank1().getCapacity();
    }

    @Override
    public void submit(MachineFluidRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.inputFluid.isEmpty()) {
            MachineFluidRenderer.submitSurface(poseStack, submitNodeCollector, state.inputFluid, state.inputCapacity, FULL_BOUNDS, state.lightCoords);
            return;
        }

        MachineFluidRenderer.submitSurface(poseStack, submitNodeCollector, state.outputFluid, state.outputCapacity, UPPER_OUTPUT_BOUNDS, state.lightCoords);
        MachineFluidRenderer.submitSurface(poseStack, submitNodeCollector, state.outputFluid1, state.outputCapacity1, LOWER_OUTPUT_BOUNDS, state.lightCoords);
    }
}
