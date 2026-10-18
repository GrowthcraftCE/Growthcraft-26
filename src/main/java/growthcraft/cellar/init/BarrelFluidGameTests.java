package growthcraft.cellar.init;

import growthcraft.cellar.block.entity.FermentationBarrelBlockEntity;
import growthcraft.cellar.block.entity.LargeStorageBarrelBlockEntity;
import growthcraft.cellar.config.GrowthcraftCellarConfig;
import growthcraft.cellar.fluid.BarrelFluidPolicy;
import growthcraft.core.init.GrowthcraftGameRules;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import growthcraft.lib.util.FluidTankPersistence;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Runtime insertion limits must never trap or delete pre-existing contents. */
public final class BarrelFluidGameTests {
    private BarrelFluidGameTests() {}

    public static void insertionRestrictionsAndExistingContents(GameTestHelper helper) {
        ModConfigSpec.IntValue limit = GrowthcraftCellarConfig.SPEC.getValues().get("barrels.fluid_temperature_limit");
        int previousLimit = limit.get();
        var rules = helper.getLevel().getGameRules();
        var rule = GrowthcraftGameRules.BARREL_TEMPERATURE_LIMIT.get();
        int previousRule = rules.get(rule);
        Map<TagKey<Fluid>, List<Holder<Fluid>>> originalTags = new HashMap<>();
        BuiltInRegistries.FLUID.getTags().forEach(tag -> originalTags.put(tag.key(), tag.stream().toList()));
        try {
            helper.assertTrue(limit.getDefault() == Integer.MAX_VALUE,
                    "Default config must leave temperature control to the world gamerule");
            verifySavedRuleAndCommand(helper);
            rules.set(rule, Integer.MAX_VALUE, helper.getLevel().getServer());
            helper.assertTrue(originalTags.containsKey(BarrelFluidPolicy.BLOCKED_FLUIDS), "Blocked-fluid tag must be loaded");
            limit.set(573);
            var player = helper.makeMockPlayer(GameType.SURVIVAL);
            var blocks = List.of(GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK.get(),
                    GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_OAK.get(),
                    GrowthcraftCellarBlocks.LARGE_STORAGE_BARREL_OAK.get());
            int x = 1;
            for (var block : blocks) {
                BlockPos pos = new BlockPos(x++, 1, 1);
                helper.setBlock(pos, block);
                var entity = helper.getLevel().getBlockEntity(helper.absolutePos(pos));
                var tank = entity instanceof FermentationBarrelBlockEntity barrel ? barrel.getTank()
                        : ((LargeStorageBarrelBlockEntity) entity).getTank();
                verifyTemperatureAndBuckets(helper, pos, tank, player, limit);
                verifyExistingContents(helper, pos, tank, player);
                verifyWorldAndConfigLimits(helper, tank, limit);
            }
            // Use the real tag reload path, preserving every unrelated tag.
            var blockedTags = new HashMap<>(originalTags);
            blockedTags.put(BarrelFluidPolicy.BLOCKED_FLUIDS, List.of(Fluids.WATER.builtInRegistryHolder()));
            BuiltInRegistries.FLUID.prepareTagReload(new TagLoader.LoadResult<>(Registries.FLUID, blockedTags)).apply();
            limit.set(Integer.MAX_VALUE);
            x = 1;
            for (var block : blocks) {
                BlockPos pos = new BlockPos(x++, 1, 1);
                var entity = helper.getLevel().getBlockEntity(helper.absolutePos(pos));
                var tank = entity instanceof FermentationBarrelBlockEntity barrel ? barrel.getTank()
                        : ((LargeStorageBarrelBlockEntity) entity).getTank();
                helper.assertTrue(tank.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE) == 0,
                        "Tag must override even the highest temperature limit for " + block);
                player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WATER_BUCKET));
                helper.assertTrue(!interact(helper, pos, player) && player.getMainHandItem().is(Items.WATER_BUCKET),
                        "Blocked bucket must stay full for " + block);
                var port = helper.getLevel().getCapability(Capabilities.Fluid.BLOCK, helper.absolutePos(pos), Direction.UP);
                helper.assertTrue(port.insert(0, FluidResource.of(new FluidStack(Fluids.WATER, 1)), 1000, null) == 0,
                        "Tag must block automation for " + block);
                tank.setFluid(new FluidStack(Fluids.WATER, 1000));
                player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
                helper.assertTrue(interact(helper, pos, player) && player.getMainHandItem().is(Items.WATER_BUCKET) && tank.isEmpty(),
                        "Tagged pre-existing contents must remain bucket-drainable for " + block);
            }
            BuiltInRegistries.FLUID.prepareTagReload(new TagLoader.LoadResult<>(Registries.FLUID, originalTags)).apply();
            helper.assertTrue(BarrelFluidPolicy.allowsInsertion(new FluidStack(Fluids.WATER, 1000), helper.getLevel()),
                    "Reloading the empty tag must permit water again");
        } finally {
            limit.set(previousLimit);
            rules.set(rule, previousRule, helper.getLevel().getServer());
            BuiltInRegistries.FLUID.prepareTagReload(new TagLoader.LoadResult<>(Registries.FLUID, originalTags)).apply();
        }
        helper.succeed();
    }

    private static void verifySavedRuleAndCommand(GameTestHelper helper) {
        var level = helper.getLevel();
        var rule = GrowthcraftGameRules.BARREL_TEMPERATURE_LIMIT.get();
        var features = level.enabledFeatures();
        var defaults = new GameRules(features);
        helper.assertTrue(defaults.get(rule) == 573 && rule.category() == GameRuleCategory.UPDATES
                        && defaults.availableRules().anyMatch(available -> available == rule),
                "World creation must expose a default 573 K rule under World Updates");
        var codec = GameRules.codec(features);
        helper.assertTrue(codec.parse(NbtOps.INSTANCE, new CompoundTag()).getOrThrow().get(rule) == 573,
                "Existing worlds without the new rule must default to 573 K");
        defaults.set(rule, 301, null);
        var encoded = codec.encodeStart(NbtOps.INSTANCE, defaults).getOrThrow();
        var loaded = codec.parse(NbtOps.INSTANCE, encoded).getOrThrow();
        helper.assertTrue(encoded instanceof CompoundTag tag
                        && tag.getInt("growthcraft:barrel_temperature_limit").orElseThrow() == 301
                        && loaded.get(rule) == 301 && loaded.copy(features).get(rule) == 301,
                "World temperature must survive save/load and world-creation copies");
        var dispatcher = level.getServer().getCommands().getDispatcher();
        var source = level.getServer().createCommandSourceStack().withLevel(level);
        try {
            dispatcher.execute("gamerule growthcraft:barrel_temperature_limit 300", source);
        } catch (CommandSyntaxException error) {
            throw new IllegalStateException("Barrel temperature gamerule command must work", error);
        }
        helper.assertTrue(level.getGameRules().get(rule) == 300, "Operator command must change the world limit");
        for (int invalid : List.of(0, -1)) {
            var parsed = dispatcher.parse("gamerule growthcraft:barrel_temperature_limit " + invalid, source);
            helper.assertTrue(parsed.getReader().canRead() || !parsed.getExceptions().isEmpty(),
                    "Temperature rule must reject values below 1 K");
        }
    }

    private static void verifyWorldAndConfigLimits(GameTestHelper helper, FluidTank tank, ModConfigSpec.IntValue config) {
        var rules = helper.getLevel().getGameRules();
        var rule = GrowthcraftGameRules.BARREL_TEMPERATURE_LIMIT.get();
        var server = helper.getLevel().getServer();
        var water = new FluidStack(Fluids.WATER, 1000);
        int temperature = water.getFluidType().getTemperature(water);
        config.set(config.getDefault());
        rules.set(rule, 573, server);
        var lava = new FluidStack(Fluids.LAVA, 1000);
        helper.assertTrue(!tank.isFluidValid(lava), "Default world rule must block lava with the default config");
        rules.set(rule, 1301, server);
        helper.assertTrue(tank.fill(lava, IFluidHandler.FluidAction.SIMULATE) == 1000 && tank.isEmpty(),
                "Raising only the gamerule must allow lava with the default config");
        config.set(573);
        rules.set(rule, temperature, server);
        helper.assertTrue(tank.fill(water, IFluidHandler.FluidAction.EXECUTE) == 0 && tank.isEmpty(),
                "Lower world limit must reject fluid exactly at the boundary");
        rules.set(rule, temperature + 1, server);
        helper.assertTrue(tank.fill(water, IFluidHandler.FluidAction.SIMULATE) == 1000,
                "Changing the gamerule must immediately affect existing tanks");
        config.set(temperature);
        rules.set(rule, Integer.MAX_VALUE, server);
        helper.assertTrue(tank.fill(water, IFluidHandler.FluidAction.EXECUTE) == 0,
                "High world limit must never bypass a lower server config ceiling");
        config.set(573);
    }

    private static void verifyTemperatureAndBuckets(GameTestHelper helper, BlockPos pos, FluidTank tank, Player player,
                                                    ModConfigSpec.IntValue limit) {
        var lava = new FluidStack(Fluids.LAVA, 1000);
        helper.assertTrue(!tank.isFluidValid(lava) && tank.fill(lava, IFluidHandler.FluidAction.SIMULATE) == 0
                        && tank.fill(lava, IFluidHandler.FluidAction.EXECUTE) == 0 && tank.isEmpty(),
                "Default limit must reject lava without changing contents");
        int temperature = lava.getFluidType().getTemperature(lava);
        limit.set(temperature);
        helper.assertTrue(!tank.isFluidValid(lava), "Fluid at the limit must be rejected");
        limit.set(temperature - 1);
        helper.assertTrue(!tank.isFluidValid(lava), "Fluid above the limit must be rejected");
        limit.set(temperature + 1);
        helper.assertTrue(tank.fill(lava, IFluidHandler.FluidAction.SIMULATE) == 1000 && tank.isEmpty(),
                "Changing the limit must permit cooler fluid without recreating the tank");
        limit.set(573);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.LAVA_BUCKET));
        helper.assertTrue(!interact(helper, pos, player) && player.getMainHandItem().is(Items.LAVA_BUCKET) && tank.isEmpty(),
                "Rejected lava bucket must not be consumed");
        var port = helper.getLevel().getCapability(Capabilities.Fluid.BLOCK, helper.absolutePos(pos), Direction.UP);
        helper.assertTrue(port.insert(0, FluidResource.of(lava), 1000, null) == 0 && tank.isEmpty(),
                "Default limit must reject automated lava insertion");
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WATER_BUCKET));
        helper.assertTrue(interact(helper, pos, player) && player.getMainHandItem().is(Items.BUCKET)
                        && tank.getFluidAmount() == 1000, "Water bucket must remain usable");
        tank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
        helper.assertTrue(tank.isFluidValid(new FluidStack(GrowthcraftCellarFluids.BROWN_WORT.source.get(), 1000)),
                "Growthcraft drinks must remain accepted");
    }

    private static void verifyExistingContents(GameTestHelper helper, BlockPos pos, FluidTank tank, Player player) {
        tank.setFluid(new FluidStack(Fluids.LAVA, 2000));
        var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
        FluidTankPersistence.save(output, "Tank", tank);
        tank.setFluid(FluidStack.EMPTY);
        FluidTankPersistence.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), output.buildResult()), "Tank", tank);
        helper.assertTrue(tank.getFluid().getFluid() == Fluids.LAVA && tank.getFluidAmount() == 2000,
                "NBT round trip must preserve old lava despite the insertion restriction");
        var port = helper.getLevel().getCapability(Capabilities.Fluid.BLOCK, helper.absolutePos(pos), Direction.DOWN);
        var lava = FluidResource.of(tank.getFluid());
        try (var transaction = Transaction.openRoot()) {
            helper.assertTrue(port.extract(0, lava, 1000, transaction) == 1000, "Existing hot fluid must support extraction");
            // No commit: restoring the snapshot must bypass the insertion validator.
        }
        helper.assertTrue(tank.getFluidAmount() == 2000, "Aborted extraction must preserve blocked contents");
        try (var transaction = Transaction.openRoot()) {
            helper.assertTrue(port.extract(0, lava, 1000, transaction) == 1000, "Automation must drain existing hot fluid");
            transaction.commit();
        }
        helper.assertTrue(tank.getFluidAmount() == 1000, "Committed extraction must remove exactly one bucket");
        helper.assertTrue(tank.fill(new FluidStack(Fluids.LAVA, 1000), IFluidHandler.FluidAction.EXECUTE) == 0,
                "Existing lava must not allow topping up");
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
        helper.assertTrue(interact(helper, pos, player) && player.getMainHandItem().is(Items.LAVA_BUCKET) && tank.isEmpty(),
                "Empty buckets must still retrieve existing hot fluid");
    }

    private static boolean interact(GameTestHelper helper, BlockPos pos, Player player) {
        return FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, helper.getLevel(),
                helper.absolutePos(pos), Direction.UP);
    }
}
