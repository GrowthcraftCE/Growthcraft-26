package growthcraft.cellar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import growthcraft.cellar.block.entity.FruitPressBlockEntity;
import growthcraft.lib.client.renderer.MachineFluidRenderer;
import growthcraft.lib.client.renderer.MachineFluidRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class FruitPressBlockEntityRenderer implements BlockEntityRenderer<FruitPressBlockEntity, MachineFluidRenderState> {
    private static final MachineFluidRenderer.Bounds TRAY_BOUNDS = new MachineFluidRenderer.Bounds(
            2.25F / 16.0F, 4.05F / 16.0F, 2.25F / 16.0F,
            13.75F / 16.0F, 6.75F / 16.0F, 13.75F / 16.0F);

    public FruitPressBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public MachineFluidRenderState createRenderState() {
        return new MachineFluidRenderState();
    }

    @Override
    public void extractRenderState(FruitPressBlockEntity press, MachineFluidRenderState state, float partialTicks, Vec3 cameraPosition,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(press, state, partialTicks, cameraPosition, breakProgress);
        state.inputFluid = press.getTank().getFluid().copy();
        state.inputCapacity = press.getTank().getCapacity();
    }

    @Override
    public void submit(MachineFluidRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        MachineFluidRenderer.submitSurface(poseStack, submitNodeCollector, state.inputFluid, state.inputCapacity, TRAY_BOUNDS, state.lightCoords);
    }
}
