# 26.1 Porting Primer Notes

These notes summarize the NeoForge migration primers from Minecraft 1.21.1 through 26.1 and translate them into
Growthcraft-specific porting work.

Primary target: **Minecraft 26.1.2**.

Official primer chain:

- [1.21.11 -> 26.1](https://docs.neoforged.net/primer/docs/26.1/)
- [1.21.10 -> 1.21.11](https://docs.neoforged.net/primer/docs/1.21.11/)
- [1.21.9 -> 1.21.10](https://docs.neoforged.net/primer/docs/1.21.10/)
- [1.21.8 -> 1.21.9](https://docs.neoforged.net/primer/docs/1.21.9/)
- [1.21.7 -> 1.21.8](https://docs.neoforged.net/primer/docs/1.21.8/)
- [1.21.6 -> 1.21.7](https://docs.neoforged.net/primer/docs/1.21.7/)
- [1.21.5 -> 1.21.6](https://docs.neoforged.net/primer/docs/1.21.6/)
- [1.21.4 -> 1.21.5](https://docs.neoforged.net/primer/docs/1.21.5/)
- [1.21.2/3 -> 1.21.4](https://docs.neoforged.net/primer/docs/1.21.4/)
- [1.21.1 -> 1.21.2](https://docs.neoforged.net/primer/docs/1.21.2/)

Read these local notes first:

- `growthcraft-impact.md` - which primer topics affect our codebase.
- `checklist.md` - first compile/data/client verification checklist.
- `source-hotspots.md` - 1.21.1 source areas to expect churn in.
