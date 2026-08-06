package growthcraft.milk.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import growthcraft.milk.block.entity.ShopSignBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.state.HangingSignRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ShopSignRenderer extends HangingSignRenderer {
    private final ItemModelResolver itemModelResolver;

    public ShopSignRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(SignBlockEntity sign, HangingSignRenderState renderState, float partialTicks,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(sign, renderState, partialTicks, cameraPosition, breakProgress);
        State state = (State) renderState;
        state.item.clear();
        state.yRotation = ((SignBlock) sign.getBlockState().getBlock()).getYRotationDegrees(sign.getBlockState());
        if (sign instanceof ShopSignBlockEntity shopSign && !shopSign.getItem().isEmpty()) {
            int seed = (int) sign.getBlockPos().asLong();
            itemModelResolver.updateForTopItem(state.item, shopSign.getItem(), ItemDisplayContext.FIXED,
                    sign.getLevel(), null, seed);
        }
    }

    @Override
    public void submit(HangingSignRenderState renderState, PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(renderState, poseStack, submitNodeCollector, camera);
        State state = (State) renderState;
        if (state.item.isEmpty()) return;

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.9375F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.yRotation));
        poseStack.translate(0.0F, -0.55F, -0.08F);
        poseStack.scale(0.45F, 0.45F, 0.45F);
        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, 0, -1);
        poseStack.popPose();
    }

    public static class State extends HangingSignRenderState {
        private final ItemStackRenderState item = new ItemStackRenderState();
        private float yRotation;
    }
}
