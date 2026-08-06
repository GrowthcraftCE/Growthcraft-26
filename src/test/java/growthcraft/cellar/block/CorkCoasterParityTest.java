package growthcraft.cellar.block;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CorkCoasterParityTest {
    @Test
    void unsupportedCoasterBreaksAfterNeighborUpdates() throws IOException {
        String source = source("block/CorkCoasterBlock.java");

        assertTrue(source.contains("protected BlockState updateShape"));
        assertTrue(source.contains("!state.canSurvive(level, pos)"));
        assertTrue(source.contains("Blocks.AIR.defaultBlockState()"));
    }

    @Test
    void storedBottlePersistsAndSynchronizesToClients() throws IOException {
        String source = source("block/entity/CorkCoasterBlockEntity.java");

        assertTrue(source.contains("ContainerHelper.saveAllItems(output, this.items)"));
        assertTrue(source.contains("ContainerHelper.loadAllItems(input, this.items)"));
        assertTrue(source.contains("return saveWithoutMetadata(registries)"));
    }

    private static String source(String relativePath) throws IOException {
        return Files.readString(Path.of("src/main/java/growthcraft/cellar", relativePath));
    }
}
