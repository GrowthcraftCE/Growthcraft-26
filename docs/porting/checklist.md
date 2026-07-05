# 26.1 Porting Checklist

## Before Source Import

- Confirm target Minecraft version: `26.1.2`.
- Pick latest compatible NeoForge `26.1.x` build.
- Confirm required Java version and install JDK 25 locally.
- Confirm Gradle/ModDev plugin version requirements.
- Create GitHub epic and child issues before code migration begins.

## First Import Pass

- Copy the 1.21.1 source baseline from `D:\Projects\Forge\Growthcraft-1.21`.
- Update `.gitignore` only as needed; keep `docs/` tracked.
- Update Gradle files, mod metadata, README, and version docs.
- Remove or defer Parchment until the 26.1 mapping strategy is confirmed.
- Generate IDE run configs after Gradle sync.

## Compile Pass

- Fix Java 25/toolchain issues first.
- Fix package/class renames from deobfuscation and the `ResourceLocation`/`Identifier` shuffle.
- Fix `level.isClientSide` access.
- Fix `Level#random` direct field usage.
- Fix interaction-result signatures and return values.
- Fix block entity type construction and lifecycle errors.

## Data And Recipe Pass

- Fix custom recipe serializers/codecs.
- Fix `Recipe#assemble` and result handling.
- Fix ingredient codecs and custom recipe inputs.
- Fix generated recipes, tags, loot tables, and manual recipe JSON.
- Run:

```text
./gradlew.bat runData
./gradlew.bat processResources -x createMinecraftArtifacts
```

## Client And Rendering Pass

- Fix GUI screens and menu synchronization.
- Fix block entity renderers.
- Fix fluid rendering and client fluid extensions.
- Fix particles and custom drip/pooling visuals.
- Fix item tint/colorization paths.
- Fix manual screenshot and icon assets.

## Gameplay Verification

- Bucket fill/drain across all machines.
- Culture Jar duplicate-fill/vanilla fluid-place behavior.
- Brew Kettle, Fermentation Barrel, Fruit Press, Roaster.
- Mixing Vat, Pancheon, Churn, Cheese Press.
- Curds, drained curds, cheese wheels, cheese slices, aging, waxing, and slicing.
- Rope placement/shearing/fence conversion.
- Shop sign transformation and drops.
- Cork coaster item rendering and drops.
- Apple tree growth/fruit, rice/cultivated farmland, and crop loot.
- Patchouli manual categories, search, recipes, images, and text readability.

## Release Readiness

- README badge/version/history agree with `gradle.properties`.
- `VERSION.md` agrees with target version.
- In-game manual changelog starts at the first 26.x production release.
- Startup logs have no Patchouli compile errors.
- Startup logs have no recipe parse errors.
- `git diff --check` passes.
