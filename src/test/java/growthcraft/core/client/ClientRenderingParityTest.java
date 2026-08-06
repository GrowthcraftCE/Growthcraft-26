package growthcraft.core.client;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientRenderingParityTest {
    @Test
    void machineFluidsUseTheRenderStateSubmissionPipeline() throws IOException {
        String renderer = source("lib/client/renderer/MachineFluidRenderer.java");

        assertTrue(renderer.contains("submitCustomGeometry"));
        assertTrue(renderer.contains("RenderTypes.entityTranslucent"));
        assertTrue(renderer.contains("Math.clamp"));
        assertTrue(renderer.contains("DEFAULT_ALPHA"));
        assertTrue(renderer.contains("putBottomQuad"));
        assertTrue(renderer.contains("putNorthQuad"));
        assertTrue(renderer.contains("putSouthQuad"));
        assertTrue(renderer.contains("putWestQuad"));
        assertTrue(renderer.contains("putEastQuad"));
    }

    @Test
    void cuboidFacesDeclareTheirActualNormals() throws IOException {
        String renderer = source("lib/client/renderer/MachineFluidRenderer.java");

        for (String direction : new String[]{"UP", "DOWN", "NORTH", "SOUTH", "WEST", "EAST"}) {
            assertTrue(renderer.contains("Direction." + direction), "Missing fluid face normal " + direction);
        }
        assertTrue(renderer.contains("setNormal(normal.getStepX(), normal.getStepY(), normal.getStepZ())"));
    }

    @Test
    void coloredDripsRegisterBothFallingAndLandingParticles() throws IOException {
        String client = source("core/GrowthcraftClient.java");
        String particle = source("lib/client/particle/ColoredDripParticle.java");

        assertTrue(client.contains("GrowthcraftParticles.COLORED_DRIP.get()"));
        assertTrue(client.contains("GrowthcraftParticles.COLORED_DRIP_LAND.get()"));
        assertTrue(particle.contains("this.gravity = 0.06F"));
        assertTrue(particle.contains("ColoredDripLandParticleOption"));
        assertTrue(particle.contains("landingLingerTicks"));
        assertTrue(particle.contains("this.remove()"));
    }

    @Test
    void cultureJarUsesTheStableInventoryAndShelfTransforms() throws IOException {
        String model = resource("assets/growthcraft_cellar/models/item/culture_jar.json");

        assertTrue(model.contains("\"gui\""));
        assertTrue(model.contains("1.5"));
        assertTrue(model.contains("\"on_shelf\""));
        assertTrue(model.contains("1.7"));
    }

    @Test
    void pancheonShowsSynchronizedProgressBubblesAndTooltip() throws IOException {
        String screen = source("milk/client/screen/PancheonScreen.java");

        assertTrue(screen.contains("this.menu.getProgressionScaled(PROGRESS_H)"));
        assertTrue(screen.contains("PROGRESS_V + PROGRESS_H - progress"));
        assertTrue(screen.contains("this.menu.getPercentProgress() + \"%\""));
    }

    @Test
    void mixingVatShowsSynchronizedProgressBubblesAndTooltip() throws IOException {
        String screen = source("milk/client/screen/MixingVatScreen.java");

        assertTrue(screen.contains("this.menu.getProgressionScaled(PROGRESS_H)"));
        assertTrue(screen.contains("BUBBLE_PIXELS"));
        assertTrue(screen.contains("graphics.fill"));
        assertTrue(screen.contains("this.menu.getPercentProgress() + \"%\""));
    }

    @Test
    void heatedMachinesShowTheirFlameIndicators() throws IOException {
        String kettle = source("cellar/client/screen/BrewKettleScreen.java");
        String vat = source("milk/client/screen/MixingVatScreen.java");

        assertTrue(kettle.contains("this.menu.isHeated()"));
        assertTrue(kettle.contains("HEAT_U, HEAT_V, HEAT_W, HEAT_H"));
        assertTrue(vat.contains("this.menu.isHeated()"));
        assertTrue(vat.contains("HEAT_U, HEAT_V, HEAT_W, HEAT_H"));
    }

    @Test
    void fruitPressParticlesUseTheOutputFluidTint() throws IOException {
        String press = source("cellar/block/FruitPressBlock.java");
        String machine = source("cellar/block/entity/FruitPressBlockEntity.java");

        assertTrue(press.contains("IClientFluidTypeExtensions.of(fluidStack.getFluid())"));
        assertTrue(press.contains("growthcraftExtensions.getTintColor()"));
        assertTrue(press.contains("ColoredDripParticleOption.fromTintColor(getDripColor(output)"));
        assertTrue(press.contains("DRIP_LANDING_SCALE = 0.55F"));
        assertTrue(press.contains("DRIP_LANDING_LINGER_TICKS = 4"));
        assertTrue(machine.contains("activeOutputFluidId = BuiltInRegistries.FLUID.getId(output.getFluid())"));
        assertTrue(machine.contains("ActiveOutputFluidId"));
        assertTrue(machine.contains("BuiltInRegistries.FLUID.byId(this.activeOutputFluidId)"));
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft", relativePath));
    }

    private static String resource(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/resources", relativePath));
    }
}
