# Growthcraft 1.21.1 Source Hotspots For 26.1

Reference source baseline:

```text
D:\Projects\Forge\Growthcraft-1.21
```

## Custom Recipe Implementations

Expect serializer, codec, ingredient, and assemble/result churn.

- `growthcraft.cellar.recipe.BrewKettleRecipe`
- `growthcraft.cellar.recipe.CultureJarRecipe`
- `growthcraft.cellar.recipe.FermentationBarrelRecipe`
- `growthcraft.cellar.recipe.FruitPressRecipe`
- `growthcraft.cellar.recipe.RoasterRecipe`
- `growthcraft.milk.recipe.MixingVatRecipe`
- `growthcraft.milk.recipe.PancheonRecipe`
- `growthcraft.milk.recipe.ChurnRecipe`
- `growthcraft.*.init.*Recipes`
- generated recipe providers under `growthcraft.*.data`

## Block And Item Interactions

Expect interaction-result and client/server checks to need attention.

- `growthcraft.core.item.RopeItem`
- `growthcraft.core.block.RopeBlock`
- `growthcraft.core.block.RopeFenceBlock`
- `growthcraft.rice.item.CultivatorItem`
- `growthcraft.rice.block.RiceCropBlock`
- `growthcraft.milk.block.*`
- `growthcraft.cellar.block.*`
- shop sign transform/behavior classes under `growthcraft.milk.block.signs`

## Block Entities And Menus

Expect lifecycle, save/load, menu validity, and synchronization changes.

- `growthcraft.cellar.block.entity.*`
- `growthcraft.milk.block.entity.*`
- `growthcraft.milk.menu.*`
- `growthcraft.cellar.menu.*`
- `growthcraft.milk.client.screen.*`
- `growthcraft.cellar.client.screen.*`

## Rendering And Client

Expect the largest API churn here.

- `growthcraft.lib.client.render.MachineFluidRenderer`
- all `client.renderer.*BlockEntityRenderer`
- `growthcraft.lib.particle.ColoredDripParticleOption`
- client registration classes in each module
- fluid client extension classes and fluid registration containers
- item property/color/tint registrations for cheese, curds, waxes, fluids, and signs

## Data Generation And Assets

Expect schema and tag changes.

- data providers under `src/main/java/growthcraft/**/data`
- `src/main/resources/data/**/recipe`
- `src/main/resources/data/**/loot_table`
- `src/main/resources/data/**/tags`
- `src/main/resources/assets/**/models`
- `src/main/resources/assets/**/patchouli_books`

## Known 1.21.1 Lessons To Preserve

- Keep Patchouli as source-of-truth documentation.
- Keep machine fluid rendering configurable by bounds, alpha/expiry, and gravity behavior.
- For multi-tank machines, render tanks in distinct visible regions.
- Bucket interactions must consume handled actions on client/server to avoid vanilla fallback flashes.
- Validate block drops for stacked/variant block states.
- Use screenshots early for visual bugs; many rendering issues are easier to diagnose from the dev client than from code.
