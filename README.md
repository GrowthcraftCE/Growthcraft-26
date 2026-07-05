# Growthcraft 26.x Port

This repository is the workspace for porting Growthcraft from the Minecraft 1.21.1 / NeoForge line to a Minecraft 26.x
target.

The exact Minecraft 26.x sub-version is not selected yet. Until that target is chosen, keep this repository focused on
porting context, migration notes, and source baseline tracking.

## Source Baseline

- Previous port source: `D:\Projects\Forge\Growthcraft-1.21`
- Baseline commit: `5ff77f18b5893458f20e70eb0815c1bc9df7ff56`
- Previous release version: `1.21.1.6`

## First Steps

1. Choose the Minecraft 26.x sub-version and matching NeoForge version.
2. Create the initial GitHub epic/issues for the 26.x port.
3. Bring over the 1.21.1 source baseline.
4. Update Gradle, mappings, dependency versions, and mod metadata.
5. Get compile, generated resources, and data loading clean before gameplay polish.

See `PORTING_NOTES.md` and `PORTING_PLAN.md` for handoff context.
