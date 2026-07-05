# Growthcraft Impact From NeoForge Primers

This is a Growthcraft-focused interpretation of the 1.21.1 -> 26.1 primer chain. It is intentionally not a full copy
of the primers; use the official links in `README.md` for complete details.

## Build And Mapping Baseline

From the 26.1 primer:

- Java moves from 21 to **25**. Update toolchains, IDE, CI, Gradle, and local docs before chasing compile errors.
- Vanilla is deobfuscated again, so expect name churn around value types and Mojang official names. Avoid assuming
  Parchment names will carry forward unchanged.
- Start without `.decompile`. Bring it in only if direct source/log/docs comparison is not enough.

Growthcraft action:

- Update `gradle.properties`, `build.gradle`, generated metadata, README, and version docs first.
- Confirm the chosen NeoForge 26.1.x build supports Minecraft 26.1.2 before importing all source.

## Recipes, Ingredients, And Data Generation

Primer topics:

- 1.21.2 introduced major recipe registry changes, recipe displays, recipe placements, recipe book categories, and an
  ingredient shift.
- 26.1 changes recipe assembly/component assumptions and replaces serializer classes in vanilla with codec-bearing
  serializer records.
- Item stack templates are used where immutable item stacks are needed, especially in recipes, advancements, and
  predicates.
- Validation and context-key changes affect loot/advancement/data-generation code.

Growthcraft action:

- Audit every custom recipe implementation before touching gameplay:
  `BrewKettleRecipe`, `CultureJarRecipe`, `FermentationBarrelRecipe`, `FruitPressRecipe`, `RoasterRecipe`,
  `MixingVatRecipe`, `PancheonRecipe`, and churn/milk recipes.
- Expect `Recipe#assemble`, result handling, ingredient codecs, and serializer registration to change.
- Re-run and inspect generated recipes early. Do not trust copied JSON schemas from 1.21.1 until the 26.1 runtime loads
  them.
- Patchouli manual recipe should be revalidated. In 1.21.1, the working pattern was a vanilla shapeless recipe returning
  `patchouli:guide_book` with the `patchouli:book` component.

## Items, Components, Food, And Tools

Primer topics:

- Data components continue moving toward delayed/holder-based initialization.
- 1.21.2 introduced consumable components and item/model/property changes.
- 1.21.5 and 26.1 add more data-component getters and initializer behavior.
- Dye behavior is component-driven in later primers.
- Tool/weapon/armor properties were rewritten across the chain.

Growthcraft action:

- Audit food and drink items: cheese slices, yogurt, ice cream, beverages, and any item effects.
- Audit custom tools/items: crowbars, wrench, cultivator, rope, bottles/buckets, curds, and cheese wheels.
- Confirm AppleSkin still reads food properties in dev runtime.
- Re-check item translation keys for custom item/block splits, such as `item.growthcraft.rope_linen`.

## Interaction Results And Block Use

Primer topics:

- 1.21.2 changed interaction result handling.
- Later primers continue changing method signatures and access patterns, including `Level#isClientSide` becoming private
  by 1.21.9 and `Level#random` becoming protected by 26.1.

Growthcraft action:

- Search for `level.isClientSide` and migrate to the new accessor/pattern.
- Search for `level.random` and use public random accessors.
- Re-test all bucket/fluid interactions. In 1.21.1, returning the correct sided success/consume result was essential to
  avoid vanilla fluid-place flashes.
- Re-test sign transformation and empty-hand/sign-item interactions.

## Block Entities, Saved Data, And Menus

Primer topics:

- 1.21.5 calls out proper block entity removal handling.
- Saved data is typed and reorganized across the chain.
- 1.21.6 and 26.1 include GUI/container-screen changes.

Growthcraft action:

- Audit block entity lifecycle and drops for machines and inventory blocks:
  Culture Jar, Brew Kettle, Fermentation Barrel, Fruit Press, Roaster, Mixing Vat, Pancheon, Churn, Cheese Press,
  Cork Coaster, and shop signs.
- Confirm `setRemoved`, menu validity checks, and drop/save/load behavior after porting compile fixes.
- Expect menu/container data synchronization to need touch-ups for Mixing Vat and Pancheon tank state.
- Re-test GUI textures and progress bars after rendering/API migration.

## Rendering, Models, Particles, And Fluids

Primer topics:

- 1.21.2 starts shader/render-state churn.
- 1.21.4 introduces client item model changes and particles rendered through render types.
- 1.21.5 introduces render pipeline/model reworks.
- 1.21.6 changes GUI and Blaze3D rendering.
- 1.21.9 changes feature submissions, block entity renderers, particles, and client asset split.
- 1.21.11 and 26.1 include additional rendering rewrites, item atlases, fluid models, block tint sources, and removal of
  old block/item renderer paths.

Growthcraft action:

- Prioritize renderer compile fixes after recipes/data:
  `MachineFluidRenderer`, all block entity renderers, cheese/curd item rendering, shop sign renderer, fluid client
  extensions, and custom particles.
- Re-test fluid visuals in-world: Culture Jar sides/bottom, Brew Kettle, Mixing Vat, Pancheon, Fruit Press tank and drip
  behavior, Cheese Press drips, and hanging/drained curds.
- Re-test colorization/tint paths. Curds and drained curds exposed item tint/transparent-layer bugs in 1.21.1.
- Rebuild or revalidate manual images if the client asset split changes texture locations or loading.

## Tags, Loot, And Datapack Content

Primer topics:

- Tags change in almost every primer.
- 1.21.6 rewrites tag provider appenders.
- 26.1 unrolls loot types so registries directly use map codecs rather than wrapper type records.
- Villager trades become datapack registries in 26.1.

Growthcraft action:

- Audit generated tags and copied tags first. Rope, crops, grains, grapes, cheese/foods, buckets, fluids, and tool
  materials are likely places for renamed vanilla tags.
- Re-run loot generation and inspect custom block loot providers, especially rice crops, apple leaves/fruit, rope fence
  drops, and machine drops.
- Growthcraft does not currently depend heavily on villager trades, but any future village/market restoration should use
  datapack trade files rather than old map-based registrations.

## Patchouli Manual

Primer concern:

- Patchouli is external, but the vanilla item component, recipe, model, and resource changes may affect manual recipes,
  item strings, icons, and image assets.

Growthcraft action:

- Keep manual pages short. Patchouli image/spotlight/recipe pages can render tiny/pixelated text if captions are too
  long.
- Confirm all icons are registered items, not stale assets.
- Confirm manual recipe, changelog, and screenshot paths after the resource layout update.
- Re-open every manual category in-game before release.
