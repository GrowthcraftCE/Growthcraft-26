package growthcraft.cellar.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Rename the legacy setting before NeoForge corrects unknown config keys. */
public final class CellarConfigMigration {
    private static final String LEGACY = "fermentation_barrel.allow_large_barrel_manual_unlock";
    private static final String CURRENT = "fermentation_barrel.allow_manual_unlock";
    private CellarConfigMigration() {}

    public static void migrate(Path path) throws IOException {
        if (!Files.isRegularFile(path)) return;
        try (var config = CommentedFileConfig.builder(path).sync().writingMode(WritingMode.REPLACE_ATOMIC).build()) {
            config.load();
            if (!config.contains(LEGACY)) return;
            if (!config.contains(CURRENT)) config.set(CURRENT, config.get(LEGACY));
            config.remove(LEGACY);
            Path backup = Files.createTempFile(path.getParent(), "growthcraft-cellar-config-", ".bak");
            Files.copy(path, backup, StandardCopyOption.REPLACE_EXISTING);
            config.save();
        }
    }
}
