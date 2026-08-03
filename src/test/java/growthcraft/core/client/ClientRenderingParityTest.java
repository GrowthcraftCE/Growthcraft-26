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

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft", relativePath));
    }
}
