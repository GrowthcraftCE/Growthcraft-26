package growthcraft.cellar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import growthcraft.cellar.block.CorkCoasterBlock;
import growthcraft.cellar.block.entity.CorkCoasterBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CorkCoasterBlockEntityRenderer implements BlockEntityRenderer<CorkCoasterBlockEntity, CorkCoasterBlockEntityRenderer.State> {
    private final ItemModelResolver itemModelResolver;

    public CorkCoasterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(CorkCoasterBlockEntity coaster, State state, float partialTicks, Vec3 cameraPosition,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(coaster, state, partialTicks, cameraPosition, breakProgress);
        state.facing = coaster.getBlockState().getValue(CorkCoasterBlock.FACING);
        state.item.clear();
        ItemStack item = coaster.getItem(0);
        if (!item.isEmpty()) {
            int seed = (int) coaster.getBlockPos().asLong();
            this.itemModelResolver.updateForTopItem(state.item, item, ItemDisplayContext.FIXED, coaster.getLevel(), null, seed);
        }
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.item.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.8F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(switch (state.facing) {
            case SOUTH -> 0.0F;
            case EAST -> 90.0F;
            case WEST -> 270.0F;
            default -> 180.0F;
        }));
        poseStack.translate(0.0F, -0.1F, 0.0F);
        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, 0, -1);
        poseStack.popPose();
    }

    public static class State extends BlockEntityRenderState {
        private final ItemStackRenderState item = new ItemStackRenderState();
        private Direction facing = Direction.NORTH;
    }
}
