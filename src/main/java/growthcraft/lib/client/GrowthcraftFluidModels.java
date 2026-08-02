package growthcraft.lib.client;

import growthcraft.lib.fluid.FluidRegistryContainer;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.fluid.FluidTintSources;

import java.util.function.Supplier;

public final class GrowthcraftFluidModels {
    private GrowthcraftFluidModels() {
    }

    public static void registerContainers(RegisterFluidModelsEvent event, String namespace, FluidRegistryContainer[] containers) {
        for (FluidRegistryContainer container : containers) {
            IClientFluidTypeExtensions extensions = new ClientFluidTypeExtensions(container.clientProperties);
            event.register(model(namespace, container.name, tint(extensions), extensions), container.source, container.flowing);
        }
    }

    public static void registerClientExtensions(RegisterClientExtensionsEvent event, FluidRegistryContainer[] containers) {
        for (FluidRegistryContainer container : containers) {
            event.registerFluidType(new ClientFluidTypeExtensions(container.clientProperties), container.type.get());
        }
    }

    public static void register(RegisterFluidModelsEvent event, String namespace, String fluidName, Supplier<? extends Fluid> source, Supplier<? extends Fluid> flowing) {
        register(event, namespace, fluidName, fluidName, 0xFFFFFFFF, source, flowing);
    }

    public static void register(RegisterFluidModelsEvent event, String namespace, String fluidName, String textureName, Supplier<? extends Fluid> source, Supplier<? extends Fluid> flowing) {
        register(event, namespace, fluidName, textureName, 0xFFFFFFFF, source, flowing);
    }

    public static void register(RegisterFluidModelsEvent event, String namespace, String fluidName, String textureName, int tint, Supplier<? extends Fluid> source, Supplier<? extends Fluid> flowing) {
        event.register(model(namespace, textureName, tint), source, flowing);
    }

    public static void register(
            RegisterFluidModelsEvent event,
            Identifier still,
            Identifier flowingTexture,
            Identifier overlay,
            int tint,
            Supplier<? extends Fluid> source,
            Supplier<? extends Fluid> flowing
    ) {
        event.register(model(still, flowingTexture, overlay, tint), source, flowing);
    }

    private static FluidModel.Unbaked model(String namespace, String textureName, int tint) {
        return model(
                Identifier.fromNamespaceAndPath(namespace, "block/fluid/" + textureName + "_fluid_still"),
                Identifier.fromNamespaceAndPath(namespace, "block/fluid/" + textureName + "_fluid_flowing"),
                Identifier.fromNamespaceAndPath(namespace, "block/fluid/" + textureName + "_fluid_overlay"),
                tint
        );
    }

    private static FluidModel.Unbaked model(String namespace, String textureName, int tint, IClientFluidTypeExtensions extensions) {
        return model(
                texture(extensions, "getStillTexture", Identifier.fromNamespaceAndPath(namespace, "block/fluid/" + textureName + "_fluid_still")),
                texture(extensions, "getFlowingTexture", Identifier.fromNamespaceAndPath(namespace, "block/fluid/" + textureName + "_fluid_flowing")),
                texture(extensions, "getOverlayTexture", Identifier.fromNamespaceAndPath(namespace, "block/fluid/" + textureName + "_fluid_overlay")),
                tint
        );
    }

    private static FluidModel.Unbaked model(Identifier still, Identifier flowing, Identifier overlay, int tint) {
        return new FluidModel.Unbaked(
                new Material(still),
                new Material(flowing),
                new Material(overlay),
                FluidTintSources.constant(tint)
        );
    }

    private static Material material(String namespace, String texture) {
        return new Material(Identifier.fromNamespaceAndPath(namespace, "block/fluid/" + texture));
    }

    public static int tint(IClientFluidTypeExtensions extensions) {
        try {
            var method = extensions.getClass().getDeclaredMethod("getTintColor");
            method.setAccessible(true);
            Object tint = method.invoke(extensions);
            return tint instanceof Integer color ? color : 0xFFFFFFFF;
        } catch (ReflectiveOperationException ignored) {
            return 0xFFFFFFFF;
        }
    }

    private static Identifier texture(IClientFluidTypeExtensions extensions, String methodName, Identifier fallback) {
        try {
            var method = extensions.getClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            Object texture = method.invoke(extensions);
            return texture instanceof Identifier id ? id : fallback;
        } catch (ReflectiveOperationException ignored) {
            return fallback;
        }
    }
}
