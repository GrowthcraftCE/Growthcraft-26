# Growthcraft 26.1.2.0 UAT Candidate

Growthcraft 26.1.2.0 ports the stable Growthcraft 1.21.1.7 gameplay baseline to Minecraft 26.1.2 and
NeoForge 26.1.2.77. This is a UAT candidate, not an authorized production release.

## Highlights

- Restores the stable rope, grapevine, hops, apple, rice, Cellar, Milk, and cheese gameplay systems.
- Restores machine screens, block-entity rendering, fluid rendering, particles, JEI integration, and the Patchouli
  manual.
- Restores supported Mystical Agriculture and Packing Tape compatibility for the representative 26.1 runtime.
- Migrates custom recipes, conditions, tags, loot modifiers, models, and item stacks to the 26.1 data and networking
  lifecycle.
- Separates client-only rendering extensions from common registration so dedicated servers load safely.
- Adds focused parity tests, content baselines, resource validation, and real-server GameTests.

## Verification

- Clean Gradle build and 71 automated tests pass.
- Normal and representative-compatibility dedicated servers start cleanly.
- Normal and representative-compatibility GameTest runs pass all required tests.
- The representative-compatibility client enters a world, save/quits, and reconnects with a clean final log.

## Compatibility Scope

- Tested representative integrations: Patchouli, JEI, AppleSkin, Mystical Agriculture/Cucumber, and Packing Tape.
- Farmer's Delight, Immersive Engineering, Mekanism, and EMI are deferred because compatible 26.1 builds were not
  available when evaluated.

## Before Production Release

- Complete team UAT and triage findings through the Growthcraft 26 project.
- Restore informational SonarCloud scanning and CI coverage reporting.
- Do not begin the 26.2 migration until the 26.1 UAT and release decision are complete.
