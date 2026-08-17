package growthcraft.cellar.client;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.block.LargeBarrelPart;
import growthcraft.cellar.block.LargeFermentationBarrelBlock;
import growthcraft.cellar.block.LargeStorageBarrelBlock;
import growthcraft.cellar.block.StorageBarrelPart;
import growthcraft.cellar.client.renderer.BrewKettleBlockEntityRenderer;
import growthcraft.cellar.client.renderer.CorkCoasterBlockEntityRenderer;
import growthcraft.cellar.client.renderer.CultureJarBlockEntityRenderer;
import growthcraft.cellar.client.renderer.FruitPressBlockEntityRenderer;
import growthcraft.cellar.client.screen.BrewKettleScreen;
import growthcraft.cellar.client.screen.CultureJarScreen;
import growthcraft.cellar.client.screen.FermentationBarrelScreen;
import growthcraft.cellar.client.screen.FruitPressScreen;
import growthcraft.cellar.client.screen.LargeStorageBarrelScreen;
import growthcraft.cellar.client.screen.RoasterScreen;
import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.init.GrowthcraftCellarFluids;
import growthcraft.cellar.init.GrowthcraftCellarMenus;
import growthcraft.lib.client.GrowthcraftFluidModels;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/**
 * Client-only registrations for Growthcraft Cellar.
 * Client-only registrations for Growthcraft Cellar.
 */
@EventBusSubscriber(modid = GrowthcraftCellar.MODID, value = Dist.CLIENT)
public final class GrowthcraftCellarClient {
    private GrowthcraftCellarClient() {}

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(GrowthcraftCellarBlockEntities.BREW_KETTLE.get(), BrewKettleBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(GrowthcraftCellarBlockEntities.CULTURE_JAR.get(), CultureJarBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(GrowthcraftCellarBlockEntities.FRUIT_PRESS.get(), FruitPressBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(GrowthcraftCellarBlockEntities.CORK_COASTER.get(), CorkCoasterBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.ItemTintSources event) {
        if (event == null) {
            return;
        }
        // Growthcraft item tints are provided by 26.x item definition JSON.
    }

    @SubscribeEvent
    public static void onRegisterFluidModels(RegisterFluidModelsEvent event) {
        GrowthcraftFluidModels.registerContainers(event, Reference.MODID, GrowthcraftCellarFluids.ALL);
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        GrowthcraftFluidModels.registerClientExtensions(event, GrowthcraftCellarFluids.ALL);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(GrowthcraftCellarMenus.CULTURE_JAR.get(), CultureJarScreen::new);
        event.register(GrowthcraftCellarMenus.BREW_KETTLE.get(), BrewKettleScreen::new);
        event.register(GrowthcraftCellarMenus.FERMENTATION_BARREL.get(), FermentationBarrelScreen::new);
        event.register(GrowthcraftCellarMenus.LARGE_STORAGE_BARREL.get(), LargeStorageBarrelScreen::new);
        event.register(GrowthcraftCellarMenus.FRUIT_PRESS.get(), FruitPressScreen::new);
        event.register(GrowthcraftCellarMenus.ROASTER.get(), RoasterScreen::new);
    }

    @SubscribeEvent
    public static void onExtractBlockOutline(ExtractBlockOutlineRenderStateEvent event) {
        BlockState targetedState = event.getBlockState();
        if (targetedState.getBlock() instanceof LargeStorageBarrelBlock) {
            addStorageBarrelOutline(event, targetedState);
            return;
        }
        if (!(targetedState.getBlock() instanceof LargeFermentationBarrelBlock)) {
            return;
        }

        Direction facing = targetedState.getValue(LargeFermentationBarrelBlock.FACING);
        LargeBarrelPart targetedPart = targetedState.getValue(LargeFermentationBarrelBlock.PART);
        BlockPos controllerPos = targetedPart.controllerFrom(event.getBlockPos(), facing);
        VoxelShape combinedShape = Shapes.empty();

        for (LargeBarrelPart part : LargeBarrelPart.values()) {
            BlockPos partPos = part.fromController(controllerPos, facing);
            BlockState partState = event.getLevel().getBlockState(partPos);
            if (!(partState.getBlock() instanceof LargeFermentationBarrelBlock)
                    || partState.getValue(LargeFermentationBarrelBlock.FACING) != facing
                    || partState.getValue(LargeFermentationBarrelBlock.PART) != part) {
                continue;
            }

            VoxelShape partShape = partState.getShape(event.getLevel(), partPos, event.getCollisionContext())
                    .move(
                            partPos.getX() - controllerPos.getX(),
                            partPos.getY() - controllerPos.getY(),
                            partPos.getZ() - controllerPos.getZ());
            combinedShape = Shapes.or(combinedShape, partShape);
        }

        VoxelShape outlineShape = combinedShape.optimize();
        Vec3 cameraPos = event.getCamera().position();
        boolean translucentPass = event.isInTranslucentPass();
        boolean highContrast = event.isHighContrast();
        event.addCustomRenderer((renderState, buffer, poseStack, currentTranslucentPass, levelRenderState) -> {
            if (currentTranslucentPass == translucentPass) {
                double offsetX = controllerPos.getX() - cameraPos.x;
                double offsetY = controllerPos.getY() - cameraPos.y;
                double offsetZ = controllerPos.getZ() - cameraPos.z;
                if (highContrast) {
                    ShapeRenderer.renderShape(
                            poseStack,
                            buffer.getBuffer(RenderTypes.secondaryBlockOutline()),
                            outlineShape,
                            offsetX,
                            offsetY,
                            offsetZ,
                            0xFF000000,
                            7.0F);
                }
                ShapeRenderer.renderShape(
                        poseStack,
                        buffer.getBuffer(RenderTypes.lines()),
                        outlineShape,
                        offsetX,
                        offsetY,
                        offsetZ,
                        highContrast ? -11010079 : ARGB.black(102),
                        1.0F);
                buffer.endLastBatch();
            }
            return true;
        });
    }

    private static void addStorageBarrelOutline(ExtractBlockOutlineRenderStateEvent event, BlockState targetedState) {
        Direction facing = targetedState.getValue(LargeStorageBarrelBlock.FACING);
        StorageBarrelPart targetedPart = targetedState.getValue(LargeStorageBarrelBlock.PART);
        BlockPos controllerPos = targetedPart.controllerFrom(event.getBlockPos(), facing);
        VoxelShape combinedShape = Shapes.empty();
        for (StorageBarrelPart part : StorageBarrelPart.values()) {
            BlockPos partPos = part.fromController(controllerPos, facing);
            BlockState partState = event.getLevel().getBlockState(partPos);
            if (partState.getBlock() != targetedState.getBlock()
                    || partState.getValue(LargeStorageBarrelBlock.FACING) != facing
                    || partState.getValue(LargeStorageBarrelBlock.PART) != part) continue;
            VoxelShape partShape = partState.getShape(event.getLevel(), partPos, event.getCollisionContext())
                    .move(partPos.getX() - controllerPos.getX(), partPos.getY() - controllerPos.getY(),
                            partPos.getZ() - controllerPos.getZ());
            combinedShape = Shapes.or(combinedShape, partShape);
        }
        VoxelShape outlineShape = combinedShape.optimize();
        Vec3 cameraPos = event.getCamera().position();
        boolean translucentPass = event.isInTranslucentPass();
        boolean highContrast = event.isHighContrast();
        event.addCustomRenderer((renderState, buffer, poseStack, currentTranslucentPass, levelRenderState) -> {
            if (currentTranslucentPass == translucentPass) {
                double x = controllerPos.getX() - cameraPos.x;
                double y = controllerPos.getY() - cameraPos.y;
                double z = controllerPos.getZ() - cameraPos.z;
                if (highContrast) {
                    ShapeRenderer.renderShape(poseStack, buffer.getBuffer(RenderTypes.secondaryBlockOutline()),
                            outlineShape, x, y, z, 0xFF000000, 7.0F);
                }
                ShapeRenderer.renderShape(poseStack, buffer.getBuffer(RenderTypes.lines()), outlineShape,
                        x, y, z, highContrast ? -11010079 : ARGB.black(102), 1.0F);
                buffer.endLastBatch();
            }
            return true;
        });
    }
}
