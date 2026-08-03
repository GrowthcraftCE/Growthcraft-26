# Growthcraft 26.1.2.0 UAT Test Plan

This package is the team UAT candidate for the Growthcraft port from stable 1.21.1.7. It is not yet a production
release. Do not begin 26.2 migration work until the team accepts or rejects this candidate.

## Supported Test Stack

- Growthcraft artifact: `growthcraft-26.1.2.0.jar`
- Candidate size: 3,499,956 bytes
- Candidate SHA-256: `ad6f249c78ef80ab30bf14dd844892827e22696f2a79167b0396eb236e06e19c`
- Minecraft: **26.1.2 only**
- NeoForge: **26.1.2.77 or newer compatible 26.1.2 build**
- Java: **25**
- Optional integrations exercised by the porting team:
  - JEI 29.6.2.33
  - Patchouli CurseForge file 8404543
  - AppleSkin 3.0.10+mc26.1.2
  - Cucumber 9.0.5 with Mystical Agriculture 9.0.4
  - Packing Tape CurseForge file 8258694

Farmer's Delight, Immersive Engineering, Mekanism, and EMI are outside this UAT compatibility scope because usable
26.1 builds were unavailable during port verification.

## Installation

1. Create a fresh Minecraft 26.1.2 NeoForge 26.1.2.77 instance using Java 25.
2. Copy the Growthcraft candidate JAR into the instance `mods` directory.
3. For the baseline pass, install only Growthcraft. For the compatibility pass, add the optional integrations above.
4. Keep each pass in its own game directory and retain `logs/latest.log` after testing.
5. Do not reuse a world that has been opened by a different Growthcraft 26 candidate unless performing the explicit
   upgrade-world pass.

## Required Passes

Record pass, fail, or not tested for every item and add brief reproduction notes for failures.

### Startup and World Lifecycle

- [ ] A dedicated server reaches `Done` with Growthcraft alone.
- [ ] A client creates and enters a new survival world.
- [ ] Save and quit succeeds; reopening the same world succeeds.
- [ ] Repeat startup and world entry with the supported optional integrations installed.
- [ ] No Growthcraft-related `ERROR`, crash, missing registry, recipe, tag, model, or texture message appears in the
      final `latest.log`.

### Crops, Trees, and Harvesting

- [ ] Plant, grow, harvest, and replant grapes, hops, rice, and apples.
- [ ] Verify grapevine and rope placement, connections, climbing, breaking, and item drops.
- [ ] Verify representative apple wood blocks, leaves, saplings, and recipes.
- [ ] Verify bamboo and apiary content is present and representative recipes complete.

### Cellar and Fermentation

- [ ] Craft and place the fruit press, brew kettle, ferment barrel, culture jar, and representative storage blocks.
- [ ] Open each machine screen and verify slots, labels, progress, and fluid display alignment.
- [ ] Complete representative grape wine, ale, cider, mead, and sake production chains.
- [ ] Confirm recipe inputs are consumed, outputs are correct, names/tooltips are translated, and save/reload preserves
      machine inventory, tanks, and progress.
- [ ] Exercise representative fluid transfer and verify rendered fluid type and level.

### Milk and Cheese

- [ ] Complete representative milk processing and cheese production.
- [ ] Verify cheese blocks/items, aging or processing state, recipes, tooltips, and save/reload behavior.

### Client Presentation and Integrations

- [ ] Inspect representative blocks/items in inventory and in-world for missing or incorrect models/textures.
- [ ] Verify machine block-entity rendering and Growthcraft particles.
- [ ] Open the Patchouli manual and follow representative entries, links, recipes, and images.
- [ ] Verify Growthcraft JEI categories, catalysts, recipes, and click areas.
- [ ] With supported compatibility mods installed, verify Growthcraft loads and representative integration recipes/items
      remain usable.

### Upgrade-World Pass

- [ ] Back up a representative world before opening it.
- [ ] Open it with exactly this candidate and note the previous Growthcraft build/version.
- [ ] Inspect existing crops, ropes/vines, machines, inventories, tanks, and stored products.
- [ ] Save, quit, reopen, and confirm the inspected state remains stable.

## High-Risk and Deliberate Changes

- Custom recipes now delay item-stack construction until registry access is available. Recipe synchronization and world
  login are high-risk regression points.
- Rope/vine behavior, machine menus and renderers, fermentation state, and fluid persistence crossed substantial 26.1
  API changes and deserve focused hands-on testing.
- Client-only registration was separated from common/server loading. Any dedicated-server client-class error is a release
  blocker.
- The current automated suite validates source/resource contracts but executes no production lines under JaCoCo. Manual
  gameplay UAT and the existing GameTests therefore remain necessary.
- NeoForge fluid APIs and some JEI APIs produce known compile-time deprecation notices. These are follow-up work unless
  they cause observable runtime failures.

## Reporting Findings

Open one GitHub issue per distinct finding in `GrowthcraftCE/Growthcraft-26`. Apply `Bug` and `UAT` labels when
available, and use this template:

```text
Build: growthcraft-26.1.2.0.jar
Minecraft / NeoForge / Java:
Optional mods and exact versions:
New world or upgraded world (previous Growthcraft version if upgraded):
Area tested:
Expected result:
Actual result:
Reproduction steps:
Reproduces after restart: yes/no/not tested
Attachments: latest.log, crash report, screenshots, and affected world backup when safe
```

Treat crashes, startup failures, data loss, broken world reloads, required progression failures, dedicated-server
failures, and reproducible recipe synchronization failures as release blockers. Cosmetic defects, optional-integration
gaps outside the supported set, deprecation notices, and enhancements should normally become follow-up work unless they
prevent meaningful testing.

The release decision belongs on GitHub issue #24 after all submitted findings have been triaged.
