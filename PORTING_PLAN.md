# Growthcraft 26.x Porting Plan

## Target Decision

The Minecraft 26.x sub-version has not been chosen yet. Before copying source in bulk, decide:

- Minecraft target version.
- NeoForge target version.
- Java version required by that Minecraft release.
- Parchment or official mapping strategy.
- JEI, Patchouli, AppleSkin, and optional compatibility dependency versions.

## Bootstrap Checklist

- Create the primary GitHub epic for the 26.x port.
- Create child issues for Core, Apples, Apiary, Cellar, Milk, Rice, compatibility, and the in-game manual.
- Copy or import the `1.21.1.6` source baseline.
- Update `gradle.properties`, `build.gradle`, mod metadata, README, and version docs.
- Generate run configs with the current NeoForge ModDev workflow.
- Run compile and data/resource checks before opening the game.

## Early Technical Risks

- Registry and data schema changes between 1.21.1 and 26.x.
- NeoForge recipe condition syntax and generated data changes.
- Patchouli recipe/book APIs and item component syntax.
- Client rendering changes for block entities, fluid rendering, and particle behavior.
- GUI/menu synchronization for multi-tank machines.
- Bucket/fluid interaction behavior and vanilla fallback actions.

## Manual And Documentation

- Keep the Patchouli in-game manual as the gameplay source of truth.
- Keep manual pages short to avoid tiny/pixelated Patchouli text.
- Validate every category for missing screenshots, raw translation keys, and loader errors.
- Start the new changelog with the first 26.x production release rather than copying older line history into the book.

## Verification Loop

Use this loop for each porting slice:

```text
./gradlew.bat compileJava -x createMinecraftArtifacts
./gradlew.bat processResources -x createMinecraftArtifacts
./gradlew.bat runData
git diff --check
```

Then launch the dev client and inspect `run/logs/latest.log`, especially for:

- recipe parse errors
- missing registry ids
- Patchouli book compile errors
- missing model or texture warnings
