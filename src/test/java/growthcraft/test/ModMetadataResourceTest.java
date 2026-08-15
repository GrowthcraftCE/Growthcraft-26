package growthcraft.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModMetadataResourceTest {
    private static final Path METADATA_TEMPLATE =
            Path.of("src/main/templates/META-INF/neoforge.mods.toml");
    @Test
    void launcherMetadataRetainsUpdateUrlAndPackagedLogo() throws IOException {
        String metadata = Files.readString(METADATA_TEMPLATE);

        assertTrue(metadata.contains(
                "updateJSONURL=\"https://neoforge.curseupdate.com/235092/growthcraft\""));
        for (String logo : new String[]{"growthcraft_logo.png", "growthcraft_cellar_logo.png",
                "growthcraft_milk_logo.png", "growthcraft_rice_logo.png", "growthcraft_bamboo_logo.png",
                "growthcraft_apples_logo.png", "growthcraft_bees_logo.png"}) {
            assertTrue(metadata.contains("logoFile=\"" + logo + "\""),
                    () -> logo + " is not assigned to a mod entry");
            Path logoPath = Path.of("src/main/resources", logo);
            assertTrue(Files.isRegularFile(logoPath), () -> logo + " is not packaged as a resource");
            assertTrue(Files.size(logoPath) > 0, () -> logo + " is empty");
        }
    }
}
