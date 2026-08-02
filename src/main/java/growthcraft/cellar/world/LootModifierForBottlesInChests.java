package growthcraft.cellar.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.List;
import java.util.Optional;

public class LootModifierForBottlesInChests extends LootModifier {
    public static final MapCodec<LootModifierForBottlesInChests> CODEC = RecordCodecBuilder.mapCodec(instance ->
            codecStart(instance)
                    .and(Chances.CODEC.forGetter(m -> m.chances))
                    .apply(instance, LootModifierForBottlesInChests::new));

    private final Chances chances;

    public LootModifierForBottlesInChests(LootItemCondition[] conditions, int priority, Chances chances) {
        super(conditions, priority);
        this.chances = chances;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot, LootContext context) {
        Identifier table = context.getQueriedLootTableId();
        int chance = chanceFor(table);
        if (chance == 0 || context.getRandom().nextInt(100) >= chance) {
            return loot;
        }

        BottleLoot bottle = bottleFor(table);
        if (bottle == null) {
            return loot;
        }

        ItemStack stack = bottle.item().getDefaultInstance();
        stack.setCount(context.getRandom().nextIntBetweenInclusive(bottle.min(), bottle.max()));
        stack.set(DataComponents.POTION_CONTENTS,
                new PotionContents(Optional.empty(), Optional.of(bottle.color()), bottle.effects(), Optional.empty()));
        stack.set(DataComponents.ITEM_NAME, Component.translatable(bottle.nameKey()));

        if (loot.size() < 27) {
            loot.add(stack);
        } else {
            for (int i = loot.size() - 1; i >= 0; i--) {
                if (loot.get(i).isEmpty()) {
                    loot.set(i, stack);
                    break;
                }
            }
        }
        return loot;
    }

    private int chanceFor(Identifier table) {
        if (is(table, BuiltInLootTables.PILLAGER_OUTPOST)) return chances.pillager();
        if (is(table, BuiltInLootTables.UNDERWATER_RUIN_SMALL) || is(table, BuiltInLootTables.UNDERWATER_RUIN_BIG)) return chances.oceanRuin();
        if (is(table, BuiltInLootTables.SHIPWRECK_SUPPLY) || is(table, BuiltInLootTables.SHIPWRECK_MAP)) return chances.shipwreck();
        if (table.getPath().startsWith("chests/village/")) return chances.village();
        if (is(table, BuiltInLootTables.BURIED_TREASURE)) return chances.buriedTreasure();
        if (is(table, BuiltInLootTables.WOODLAND_MANSION)) return chances.mansion();
        if (is(table, BuiltInLootTables.STRONGHOLD_CORRIDOR) || is(table, BuiltInLootTables.STRONGHOLD_CROSSING)) return chances.stronghold();
        return 0;
    }

    private BottleLoot bottleFor(Identifier table) {
        if (is(table, BuiltInLootTables.PILLAGER_OUTPOST)) {
            return bottle(GrowthcraftCellarItems.POTION_WINE.get(), 6, 14, 0xE5C7A2,
                    "fluid_type.growthcraft_apiary.honey_mead_fluid",
                    effect(MobEffects.HEALTH_BOOST, 3600, 1), effect(MobEffects.SLOW_FALLING, 3600, 0));
        }
        if (is(table, BuiltInLootTables.UNDERWATER_RUIN_SMALL) || is(table, BuiltInLootTables.UNDERWATER_RUIN_BIG)) {
            return bottle(GrowthcraftCellarItems.POTION_WINE.get(), 1, 4, 0xDFEBD5,
                    "fluid_type.growthcraft_cellar.white_grape_wine",
                    effect(MobEffects.SATURATION, 1200, 1), effect(MobEffects.DOLPHINS_GRACE, 1200, 0));
        }
        if (is(table, BuiltInLootTables.SHIPWRECK_SUPPLY) || is(table, BuiltInLootTables.SHIPWRECK_MAP)) {
            return bottle(GrowthcraftCellarItems.POTION_ALE.get(), 4, 8, 0x805C2F,
                    "fluid_type.growthcraft_cellar.old_port_ale", effect(MobEffects.LUCK, 6000, 1));
        }
        if (table.getPath().startsWith("chests/village/")) {
            return bottle(GrowthcraftCellarItems.POTION_WINE.get(), 1, 3, 0xDFEBD5,
                    "fluid_type.growthcraft_cellar.white_grape_wine", effect(MobEffects.SATURATION, 1200, 1));
        }
        if (is(table, BuiltInLootTables.BURIED_TREASURE)) {
            return bottle(GrowthcraftCellarItems.POTION_WINE.get(), 2, 4, 0x3C0357,
                    "fluid_type.growthcraft_cellar.purple_grape_wine", effect(MobEffects.ABSORPTION, 4800, 1));
        }
        if (is(table, BuiltInLootTables.WOODLAND_MANSION)) {
            return bottle(GrowthcraftCellarItems.POTION_LAGER.get(), 4, 8, 0x936B53,
                    "fluid_type.growthcraft_cellar.copper_lager",
                    effect(MobEffects.RESISTANCE, 3600, 2), effect(MobEffects.TRIAL_OMEN, 24000, 0));
        }
        if (is(table, BuiltInLootTables.STRONGHOLD_CORRIDOR) || is(table, BuiltInLootTables.STRONGHOLD_CROSSING)) {
            return bottle(GrowthcraftCellarItems.POTION_WINE.get(), 2, 10, 0x3C0357,
                    "fluid_type.growthcraft_cellar.purple_grape_wine", effect(MobEffects.ABSORPTION, 7200, 1));
        }
        return null;
    }

    private static boolean is(Identifier actual, ResourceKey<LootTable> expected) {
        return actual.equals(expected.identifier());
    }

    private static MobEffectInstance effect(net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect,
            int duration, int amplifier) {
        return new MobEffectInstance(effect, duration, amplifier, true, false);
    }

    private static BottleLoot bottle(Item item, int min, int max, int color, String nameKey,
            MobEffectInstance... effects) {
        return new BottleLoot(item, min, max, color, nameKey, List.of(effects));
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    private record BottleLoot(Item item, int min, int max, int color, String nameKey,
                              List<MobEffectInstance> effects) {}

    public record Chances(int pillager, int oceanRuin, int shipwreck, int village, int buriedTreasure,
                          int mansion, int stronghold) {
        private static final MapCodec<Chances> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_pillager_outpost").forGetter(Chances::pillager),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_ocean_ruin").forGetter(Chances::oceanRuin),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_shipwreck").forGetter(Chances::shipwreck),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_village").forGetter(Chances::village),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_beach_treasure").forGetter(Chances::buriedTreasure),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_dark_forest_mansion").forGetter(Chances::mansion),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_stronghold").forGetter(Chances::stronghold)
        ).apply(instance, Chances::new));
    }
}
