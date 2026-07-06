package growthcraft.milk.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import growthcraft.lib.client.renderer.MachineFluidRenderer;
import growthcraft.lib.client.renderer.MachineFluidRenderState;
import growthcraft.milk.block.entity.MixingVatBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class MixingVatBlockEntityRenderer implements BlockEntityRenderer<MixingVatBlockEntity, MachineFluidRenderState> {
    private static final MachineFluidRenderer.Bounds MAIN_BOUNDS = new MachineFluidRenderer.Bounds(
            1.0F / 16.0F, 4.0F / 16.0F, 1.0F / 16.0F,
            15.0F / 16.0F, 12.0F / 16.0F, 15.0F / 16.0F);
    private static final MachineFluidRenderer.Bounds SIDE_BOUNDS = new MachineFluidRenderer.Bounds(
            1.0F / 16.0F, 12.0F / 16.0F, 1.0F / 16.0F,
            15.0F / 16.0F, 14.0F / 16.0F, 15.0F / 16.0F);

    public MixingVatBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public MachineFluidRenderState createRenderState() {
        return new MachineFluidRenderState();
    }

    @Override
    public void extractRenderState(MixingVatBlockEntity vat, MachineFluidRenderState state, float partialTicks, Vec3 cameraPosition,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(vat, state, partialTicks, cameraPosition, breakProgress);
        state.inputFluid = vat.getMainTank().getFluid().copy();
        state.inputCapacity = vat.getMainTank().getCapacity();
        state.outputFluid = vat.getSideTank().getFluid().copy();
        state.outputCapacity = vat.getSideTank().getCapacity();
    }

    @Override
    public void submit(MachineFluidRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        MachineFluidRenderer.submitSurface(poseStack, submitNodeCollector, state.inputFluid, state.inputCapacity, MAIN_BOUNDS, state.lightCoords);
        MachineFluidRenderer.submitSurface(poseStack, submitNodeCollector, state.outputFluid, state.outputCapacity, SIDE_BOUNDS, state.lightCoords);
    }
}
