package growthcraft.lib.fluid;

import growthcraft.lib.utils.ColorUtils;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;

/**
 * Client presentation metadata for a fluid. This data object deliberately has no
 * references to Minecraft client classes so fluid registration remains safe on a
 * dedicated server.
 */
public class FluidClientProperties {
    public final String modid;
    public Identifier still;
    public Identifier flowing;
    public Identifier overlay;
    public Identifier renderOverlay;
    public Vector3f fogColor;
    public int tintColor = 0xFFFFFFFF;

    public FluidClientProperties(String modid, String fluidName) {
        this.modid = modid;
        still(fluidName);
        flowing(fluidName);
        overlay(fluidName);
    }

    public FluidClientProperties flowing(String name) { return flowing(name, "block/fluid"); }
    public FluidClientProperties flowing(String name, String folder) {
        this.flowing = Identifier.fromNamespaceAndPath(this.modid, folder + "/" + name + "_fluid_flowing");
        return this;
    }
    public FluidClientProperties fogColor(float red, float green, float blue) {
        this.fogColor = new Vector3f(red, green, blue);
        return this;
    }
    public FluidClientProperties overlay(String name) { return overlay(name, "block/fluid"); }
    public FluidClientProperties overlay(String name, String folder) {
        this.overlay = Identifier.fromNamespaceAndPath(this.modid, folder + "/" + name + "_fluid_overlay");
        return renderOverlay(Identifier.fromNamespaceAndPath(this.modid, "textures/" + folder + "/" + name + "_fluid_overlay.png"));
    }
    public FluidClientProperties renderOverlay(Identifier path) { this.renderOverlay = path; return this; }
    public FluidClientProperties sharedFluidTextures(String namespace) {
        this.still = Identifier.fromNamespaceAndPath(namespace, "block/fluid/fluid_still");
        this.flowing = Identifier.fromNamespaceAndPath(namespace, "block/fluid/fluid_flowing");
        this.overlay = Identifier.fromNamespaceAndPath(namespace, "block/fluid/fluid_overlay");
        this.renderOverlay = Identifier.fromNamespaceAndPath(namespace, "textures/block/fluid/fluid_overlay.png");
        return this;
    }
    public FluidClientProperties useTexturesFrom(String namespace, String name) {
        this.still = Identifier.fromNamespaceAndPath(namespace, "block/fluid/" + name + "_fluid_still");
        this.flowing = Identifier.fromNamespaceAndPath(namespace, "block/fluid/" + name + "_fluid_flowing");
        this.overlay = Identifier.fromNamespaceAndPath(namespace, "block/fluid/" + name + "_fluid_overlay");
        this.renderOverlay = Identifier.fromNamespaceAndPath(namespace, "textures/block/fluid/" + name + "_fluid_overlay.png");
        return this;
    }
    public FluidClientProperties still(String name) { return still(name, "block/fluid"); }
    public FluidClientProperties still(String name, String folder) {
        this.still = Identifier.fromNamespaceAndPath(this.modid, folder + "/" + name + "_fluid_still");
        return this;
    }
    public FluidClientProperties tint(int tint) {
        if (((tint >>> 24) & 0xFF) == 0) tint |= 0x7F000000;
        this.tintColor = tint;
        return this;
    }
    public FluidClientProperties tint(ColorUtils.GrowthcraftColor color) { return tint(color.toIntValue()); }
}
