package growthcraft.cellar.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class CellarConfigMigrationTest {
    @TempDir Path directory;

    @Test
    void migratesExistingChoiceAndPreservesUnrelatedSettingsWithBackup() throws Exception {
        Path config = directory.resolve("cellar.toml");
        String original = "# Preserve this comment\n[fermentation_barrel]\nallow_large_barrel_manual_unlock = true\ndebugEnabled = true\n";
        Files.writeString(config, original);
        CellarConfigMigration.migrate(config);
        String updated = Files.readString(config);
        assertTrue(updated.matches("(?s).*allow_manual_unlock\\s*=\\s*true.*"), updated);
        assertFalse(updated.contains("allow_large_barrel_manual_unlock"));
        assertTrue(updated.matches("(?s).*debugEnabled\\s*=\\s*true.*"));
        assertTrue(updated.contains("Preserve this comment"));
        try (var files = Files.list(directory)) {
            var backup = files.filter(p -> p.toString().endsWith(".bak")).findFirst().orElseThrow();
            assertEquals(original, Files.readString(backup));
        }
        CellarConfigMigration.migrate(config);
        assertEquals(updated, Files.readString(config));
    }

    @Test
    void currentValueWinsAndDisabledLegacyValueIsPreserved() throws Exception {
        Path config = directory.resolve("cellar.toml");
        Files.writeString(config, "[fermentation_barrel]\nallow_large_barrel_manual_unlock = true\nallow_manual_unlock = false\n");
        CellarConfigMigration.migrate(config);
        assertTrue(Files.readString(config).matches("(?s).*allow_manual_unlock\\s*=\\s*false.*"), Files.readString(config));
        Files.writeString(config, "[fermentation_barrel]\nallow_large_barrel_manual_unlock = false\n");
        CellarConfigMigration.migrate(config);
        assertTrue(Files.readString(config).matches("(?s).*allow_manual_unlock\\s*=\\s*false.*"), Files.readString(config));
    }

    @Test
    void missingFileIsNotCreated() throws Exception {
        Path config = directory.resolve("missing.toml");
        CellarConfigMigration.migrate(config);
        assertFalse(Files.exists(config));
    }
}
