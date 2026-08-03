package growthcraft.core;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManualCompatibilityTest {
    @Test
    void patchouliIsAnOptionalDevelopmentDependency() throws IOException {
        String build = Files.readString(Path.of("build.gradle"));
        String properties = Files.readString(Path.of("gradle.properties"));

        assertTrue(build.contains("localRuntime \"maven.modrinth:patchouli:${patchouli_version}\""));
        assertFalse(build.contains("implementation \"maven.modrinth:patchouli"));
        assertTrue(properties.contains("patchouli_version=26.1-94-beta"));
    }

    @Test
    void apiaryManualDoesNotReferenceRemovedBeeContent() throws IOException {
        Path apiary = Path.of("src/main/resources/assets/growthcraft/patchouli_books/growthcraft/en_us");
        String category = Files.readString(apiary.resolve("categories/apiary.json"));
        String entry = Files.readString(apiary.resolve("entries/apiary/bee_keeping.json"));

        assertFalse(category.contains("growthcraft_apiary:bee\""));
        assertFalse(entry.contains("growthcraft_apiary:bee\""));
        assertTrue(category.contains("growthcraft_apiary:honey_comb_full"));
        assertTrue(entry.contains("minecraft:bee_nest"));
        assertTrue(entry.contains("patchouli:image"));
    }
}
