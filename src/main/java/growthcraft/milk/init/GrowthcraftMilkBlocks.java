package growthcraft.milk.init;

import growthcraft.apiary.init.GrowthcraftApiaryItems;
import growthcraft.milk.block.CheesePressBlock;
import growthcraft.milk.block.CheeseCurdBlock;
import growthcraft.milk.block.CheeseWheelBlock;
import growthcraft.milk.block.ChurnBlock;
import growthcraft.milk.block.MixingVatBlock;
import growthcraft.milk.block.PancheonBlock;
import growthcraft.milk.block.ThistleCropBlock;
import growthcraft.milk.block.signs.ShopCeilingHangingSignBlock;
import growthcraft.milk.block.signs.ShopWallHangingSignBlock;
import growthcraft.milk.config.Reference;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftMilkBlocks {
    private GrowthcraftMilkBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    public static final DeferredBlock<CheesePressBlock> CHEESE_PRESS = BLOCKS.registerBlock(Reference.UnlocalizedName.CHEESE_PRESS, CheesePressBlock::new, GrowthcraftMilkBlocks::woodMachineProperties);
    public static final DeferredBlock<ChurnBlock> CHURN = BLOCKS.registerBlock(Reference.UnlocalizedName.CHURN, ChurnBlock::new, GrowthcraftMilkBlocks::woodMachineProperties);
    public static final DeferredBlock<MixingVatBlock> MIXING_VAT = BLOCKS.registerBlock(Reference.UnlocalizedName.MIXING_VAT, MixingVatBlock::new, GrowthcraftMilkBlocks::metalMachineProperties);
    public static final DeferredBlock<PancheonBlock> PANCHEON = BLOCKS.registerBlock(Reference.UnlocalizedName.PANCHEON, PancheonBlock::new, GrowthcraftMilkBlocks::stoneMachineProperties);

    public static final DeferredBlock<CheeseWheelBlock> APPENZELLER_CHEESE = registerCheese(Reference.UnlocalizedName.APPENZELLER);
    public static final DeferredBlock<CheeseWheelBlock> ASIAGO_CHEESE = registerCheese(Reference.UnlocalizedName.ASIAGO);
    public static final DeferredBlock<CheeseWheelBlock> CASU_MARZU_CHEESE = registerCheese(Reference.UnlocalizedName.CASU_MARZU);
    public static final DeferredBlock<CheeseWheelBlock> CHEDDAR_CHEESE = registerCheese(Reference.UnlocalizedName.CHEDDAR);
    public static final DeferredBlock<CheeseWheelBlock> EMMENTALER_CHEESE = registerCheese(Reference.UnlocalizedName.EMMENTALER);
    public static final DeferredBlock<CheeseWheelBlock> GORGONZOLA_CHEESE = registerCheese(Reference.UnlocalizedName.GORGONZOLA);
    public static final DeferredBlock<CheeseWheelBlock> GOUDA_CHEESE = registerCheese(Reference.UnlocalizedName.GOUDA);
    public static final DeferredBlock<CheeseWheelBlock> MONTEREY_CHEESE = registerCheese(Reference.UnlocalizedName.MONTEREY);
    public static final DeferredBlock<CheeseWheelBlock> PARMESAN_CHEESE = registerCheese(Reference.UnlocalizedName.PARMESAN);
    public static final DeferredBlock<CheeseWheelBlock> PROVOLONE_CHEESE = registerCheese(Reference.UnlocalizedName.PROVOLONE);
    public static final DeferredBlock<CheeseWheelBlock> APPENZELLER_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.APPENZELLER);
    public static final DeferredBlock<CheeseWheelBlock> ASIAGO_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.ASIAGO);
    public static final DeferredBlock<CheeseWheelBlock> CASU_MARZU_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.CASU_MARZU);
    public static final DeferredBlock<CheeseWheelBlock> CHEDDAR_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.CHEDDAR);
    public static final DeferredBlock<CheeseWheelBlock> EMMENTALER_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.EMMENTALER);
    public static final DeferredBlock<CheeseWheelBlock> GORGONZOLA_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.GORGONZOLA);
    public static final DeferredBlock<CheeseWheelBlock> GOUDA_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.GOUDA);
    public static final DeferredBlock<CheeseWheelBlock> MONTEREY_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.MONTEREY);
    public static final DeferredBlock<CheeseWheelBlock> PARMESAN_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.PARMESAN);
    public static final DeferredBlock<CheeseWheelBlock> PROVOLONE_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.PROVOLONE);
    public static final DeferredBlock<CheeseWheelBlock> CHEDDAR_CHEESE_WAXED = registerWaxedCheese(Reference.UnlocalizedName.CHEDDAR);
    public static final DeferredBlock<CheeseWheelBlock> GOUDA_CHEESE_WAXED = registerWaxedCheese(Reference.UnlocalizedName.GOUDA);
    public static final DeferredBlock<CheeseWheelBlock> MONTEREY_CHEESE_WAXED = registerWaxedCheese(Reference.UnlocalizedName.MONTEREY);
    public static final DeferredBlock<CheeseWheelBlock> PROVOLONE_CHEESE_WAXED = registerWaxedCheese(Reference.UnlocalizedName.PROVOLONE);

    public static final DeferredBlock<CheeseCurdBlock> APPENZELLER_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.APPENZELLER);
    public static final DeferredBlock<CheeseCurdBlock> ASIAGO_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.ASIAGO);
    public static final DeferredBlock<CheeseCurdBlock> CASU_MARZU_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.CASU_MARZU);
    public static final DeferredBlock<CheeseCurdBlock> CHEDDAR_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.CHEDDAR);
    public static final DeferredBlock<CheeseCurdBlock> EMMENTALER_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.EMMENTALER);
    public static final DeferredBlock<CheeseCurdBlock> GORGONZOLA_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.GORGONZOLA);
    public static final DeferredBlock<CheeseCurdBlock> GOUDA_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.GOUDA);
    public static final DeferredBlock<CheeseCurdBlock> MONTEREY_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.MONTEREY);
    public static final DeferredBlock<CheeseCurdBlock> PARMESAN_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.PARMESAN);
    public static final DeferredBlock<CheeseCurdBlock> PROVOLONE_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.PROVOLONE);
    public static final DeferredBlock<CheeseCurdBlock> RICOTTA_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.RICOTTA);

    public static final DeferredBlock<ThistleCropBlock> THISTLE_CROP = BLOCKS.registerBlock(
            Reference.UnlocalizedName.THISTLE_CROP,
            ThistleCropBlock::new,
            GrowthcraftMilkBlocks::thistleProperties
    );
    public static final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN_1_OAK = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_1_OAK,
            properties -> new ShopCeilingHangingSignBlock(WoodType.OAK, properties, (SignBlock) Blocks.OAK_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN_1_SPRUCE = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_1_SPRUCE,
            properties -> new ShopCeilingHangingSignBlock(WoodType.SPRUCE, properties, (SignBlock) Blocks.SPRUCE_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN_1_BIRCH = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_1_BIRCH,
            properties -> new ShopCeilingHangingSignBlock(WoodType.BIRCH, properties, (SignBlock) Blocks.BIRCH_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN_1_JUNGLE = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_1_JUNGLE,
            properties -> new ShopCeilingHangingSignBlock(WoodType.JUNGLE, properties, (SignBlock) Blocks.JUNGLE_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN_1_ACACIA = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_1_ACACIA,
            properties -> new ShopCeilingHangingSignBlock(WoodType.ACACIA, properties, (SignBlock) Blocks.ACACIA_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN_1_DARK_OAK = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_1_DARK_OAK,
            properties -> new ShopCeilingHangingSignBlock(WoodType.DARK_OAK, properties, (SignBlock) Blocks.DARK_OAK_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN_1_MANGROVE = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_1_MANGROVE,
            properties -> new ShopCeilingHangingSignBlock(WoodType.MANGROVE, properties, (SignBlock) Blocks.MANGROVE_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN_1_CHERRY = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_1_CHERRY,
            properties -> new ShopCeilingHangingSignBlock(WoodType.CHERRY, properties, (SignBlock) Blocks.CHERRY_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN_1_BAMBOO = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_1_BAMBOO,
            properties -> new ShopCeilingHangingSignBlock(WoodType.BAMBOO, properties, (SignBlock) Blocks.BAMBOO_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN_1_CRIMSON = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_1_CRIMSON,
            properties -> new ShopCeilingHangingSignBlock(WoodType.CRIMSON, properties, (SignBlock) Blocks.CRIMSON_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN_1_WARPED = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_1_WARPED,
            properties -> new ShopCeilingHangingSignBlock(WoodType.WARPED, properties, (SignBlock) Blocks.WARPED_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_2_OAK = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_2_OAK,
            properties -> new ShopWallHangingSignBlock(WoodType.OAK, properties, (SignBlock) Blocks.OAK_WALL_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_2_SPRUCE = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_2_SPRUCE,
            properties -> new ShopWallHangingSignBlock(WoodType.SPRUCE, properties, (SignBlock) Blocks.SPRUCE_WALL_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_2_BIRCH = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_2_BIRCH,
            properties -> new ShopWallHangingSignBlock(WoodType.BIRCH, properties, (SignBlock) Blocks.BIRCH_WALL_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_2_JUNGLE = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_2_JUNGLE,
            properties -> new ShopWallHangingSignBlock(WoodType.JUNGLE, properties, (SignBlock) Blocks.JUNGLE_WALL_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_2_ACACIA = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_2_ACACIA,
            properties -> new ShopWallHangingSignBlock(WoodType.ACACIA, properties, (SignBlock) Blocks.ACACIA_WALL_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_2_DARK_OAK = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_2_DARK_OAK,
            properties -> new ShopWallHangingSignBlock(WoodType.DARK_OAK, properties, (SignBlock) Blocks.DARK_OAK_WALL_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_2_MANGROVE = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_2_MANGROVE,
            properties -> new ShopWallHangingSignBlock(WoodType.MANGROVE, properties, (SignBlock) Blocks.MANGROVE_WALL_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_2_CHERRY = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_2_CHERRY,
            properties -> new ShopWallHangingSignBlock(WoodType.CHERRY, properties, (SignBlock) Blocks.CHERRY_WALL_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_2_BAMBOO = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_2_BAMBOO,
            properties -> new ShopWallHangingSignBlock(WoodType.BAMBOO, properties, (SignBlock) Blocks.BAMBOO_WALL_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_2_CRIMSON = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_2_CRIMSON,
            properties -> new ShopWallHangingSignBlock(WoodType.CRIMSON, properties, (SignBlock) Blocks.CRIMSON_WALL_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);
    public static final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_2_WARPED = BLOCKS.registerBlock(
            Reference.UnlocalizedName.HANGING_SIGN_2_WARPED,
            properties -> new ShopWallHangingSignBlock(WoodType.WARPED, properties, (SignBlock) Blocks.WARPED_WALL_HANGING_SIGN),
            GrowthcraftMilkBlocks::hangingSignProperties);

    private static DeferredBlock<CheeseWheelBlock> registerCheese(String cheeseName) {
        return BLOCKS.registerBlock(cheeseName + "_cheese", properties -> new CheeseWheelBlock(
                getSliceItem(cheeseName),
                false,
                getFreshAgedBlock(cheeseName),
                getWaxItem(cheeseName),
                getWaxedBlock(cheeseName),
                properties), GrowthcraftMilkBlocks::cheeseProperties);
    }

    private static DeferredBlock<CheeseWheelBlock> registerAgedCheese(String cheeseName) {
        return BLOCKS.registerBlock(cheeseName + "_cheese_aged", properties -> new CheeseWheelBlock(
                getSliceItem(cheeseName),
                true,
                () -> null,
                () -> null,
                () -> null,
                properties), GrowthcraftMilkBlocks::cheeseProperties);
    }

    private static DeferredBlock<CheeseWheelBlock> registerWaxedCheese(String cheeseName) {
        return BLOCKS.registerBlock(cheeseName + "_cheese_waxed", properties -> new CheeseWheelBlock(
                getSliceItem(cheeseName),
                true,
                getAgedBlock(cheeseName),
                () -> null,
                () -> null,
                properties), GrowthcraftMilkBlocks::cheeseProperties);
    }

    private static java.util.function.Supplier<? extends net.minecraft.world.level.block.Block> getFreshAgedBlock(String cheeseName) {
        return switch (cheeseName) {
            case Reference.UnlocalizedName.CHEDDAR,
                 Reference.UnlocalizedName.GOUDA,
                 Reference.UnlocalizedName.MONTEREY,
                 Reference.UnlocalizedName.PROVOLONE -> () -> null;
            default -> getAgedBlock(cheeseName);
        };
    }

    private static java.util.function.Supplier<? extends net.minecraft.world.level.block.Block> getAgedBlock(String cheeseName) {
        return () -> switch (cheeseName) {
            case Reference.UnlocalizedName.APPENZELLER -> GrowthcraftMilkBlocks.APPENZELLER_CHEESE_AGED.get();
            case Reference.UnlocalizedName.ASIAGO -> GrowthcraftMilkBlocks.ASIAGO_CHEESE_AGED.get();
            case Reference.UnlocalizedName.CASU_MARZU -> GrowthcraftMilkBlocks.CASU_MARZU_CHEESE_AGED.get();
            case Reference.UnlocalizedName.CHEDDAR -> GrowthcraftMilkBlocks.CHEDDAR_CHEESE_AGED.get();
            case Reference.UnlocalizedName.EMMENTALER -> GrowthcraftMilkBlocks.EMMENTALER_CHEESE_AGED.get();
            case Reference.UnlocalizedName.GORGONZOLA -> GrowthcraftMilkBlocks.GORGONZOLA_CHEESE_AGED.get();
            case Reference.UnlocalizedName.GOUDA -> GrowthcraftMilkBlocks.GOUDA_CHEESE_AGED.get();
            case Reference.UnlocalizedName.MONTEREY -> GrowthcraftMilkBlocks.MONTEREY_CHEESE_AGED.get();
            case Reference.UnlocalizedName.PARMESAN -> GrowthcraftMilkBlocks.PARMESAN_CHEESE_AGED.get();
            case Reference.UnlocalizedName.PROVOLONE -> GrowthcraftMilkBlocks.PROVOLONE_CHEESE_AGED.get();
            default -> null;
        };
    }

    private static java.util.function.Supplier<? extends net.minecraft.world.level.block.Block> getWaxedBlock(String cheeseName) {
        return () -> switch (cheeseName) {
            case Reference.UnlocalizedName.CHEDDAR -> GrowthcraftMilkBlocks.CHEDDAR_CHEESE_WAXED.get();
            case Reference.UnlocalizedName.GOUDA -> GrowthcraftMilkBlocks.GOUDA_CHEESE_WAXED.get();
            case Reference.UnlocalizedName.MONTEREY -> GrowthcraftMilkBlocks.MONTEREY_CHEESE_WAXED.get();
            case Reference.UnlocalizedName.PROVOLONE -> GrowthcraftMilkBlocks.PROVOLONE_CHEESE_WAXED.get();
            default -> null;
        };
    }

    private static java.util.function.Supplier<? extends net.minecraft.world.item.Item> getWaxItem(String cheeseName) {
        return () -> switch (cheeseName) {
            case Reference.UnlocalizedName.CHEDDAR -> GrowthcraftApiaryItems.BEES_WAX_RED.get();
            case Reference.UnlocalizedName.GOUDA -> GrowthcraftApiaryItems.BEES_WAX.get();
            case Reference.UnlocalizedName.MONTEREY -> GrowthcraftApiaryItems.BEES_WAX_BLACK.get();
            case Reference.UnlocalizedName.PROVOLONE -> GrowthcraftApiaryItems.BEES_WAX_WHITE.get();
            default -> null;
        };
    }

    private static java.util.function.Supplier<? extends net.minecraft.world.item.Item> getSliceItem(String cheeseName) {
        return () -> switch (cheeseName) {
            case Reference.UnlocalizedName.APPENZELLER -> GrowthcraftMilkItems.APPENZELLER_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.ASIAGO -> GrowthcraftMilkItems.ASIAGO_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.CASU_MARZU -> GrowthcraftMilkItems.CASU_MARZU_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.CHEDDAR -> GrowthcraftMilkItems.CHEDDAR_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.EMMENTALER -> GrowthcraftMilkItems.EMMENTALER_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.GORGONZOLA -> GrowthcraftMilkItems.GORGONZOLA_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.GOUDA -> GrowthcraftMilkItems.GOUDA_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.MONTEREY -> GrowthcraftMilkItems.MONTEREY_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.PARMESAN -> GrowthcraftMilkItems.PARMESAN_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.PROVOLONE -> GrowthcraftMilkItems.PROVOLONE_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.RICOTTA -> GrowthcraftMilkItems.RICOTTA_CHEESE_SLICE.get();
            default -> null;
        };
    }

    private static DeferredBlock<CheeseCurdBlock> registerCheeseCurds(String cheeseName) {
        return BLOCKS.registerBlock(cheeseName + "_cheese_curds", CheeseCurdBlock::new, GrowthcraftMilkBlocks::cheeseCurdProperties);
    }

    private static BlockBehaviour.Properties woodMachineProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(net.minecraft.world.level.material.MapColor.WOOD)
                .strength(2.0F)
                .sound(net.minecraft.world.level.block.SoundType.WOOD)
                .noOcclusion();
    }

    private static BlockBehaviour.Properties metalMachineProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(net.minecraft.world.level.material.MapColor.STONE)
                .strength(1.5F)
                .sound(net.minecraft.world.level.block.SoundType.METAL)
                .noOcclusion();
    }

    private static BlockBehaviour.Properties stoneMachineProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(net.minecraft.world.level.material.MapColor.STONE)
                .strength(1.5F)
                .sound(net.minecraft.world.level.block.SoundType.STONE)
                .noOcclusion();
    }

    private static BlockBehaviour.Properties cheeseProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)
                .noOcclusion()
                .randomTicks();
    }

    private static BlockBehaviour.Properties cheeseCurdProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                .noOcclusion()
                .randomTicks()
                .isValidSpawn((state, getter, pos, type) -> false)
                .isRedstoneConductor((state, getter, pos) -> false)
                .isViewBlocking((state, getter, pos) -> false);
    }

    private static BlockBehaviour.Properties thistleProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)
                .noOcclusion()
                .randomTicks()
                .instabreak()
                .sound(net.minecraft.world.level.block.SoundType.CROP);
    }

    private static BlockBehaviour.Properties hangingSignProperties() {
        return BlockBehaviour.Properties.of()
                .forceSolidOn()
                .noCollision()
                .strength(1.0F)
                .ignitedByLava();
    }

    public static Block getShopSignFromOriginal(SignBlock original) {
        if (original == Blocks.OAK_HANGING_SIGN) return HANGING_SIGN_1_OAK.get();
        if (original == Blocks.SPRUCE_HANGING_SIGN) return HANGING_SIGN_1_SPRUCE.get();
        if (original == Blocks.BIRCH_HANGING_SIGN) return HANGING_SIGN_1_BIRCH.get();
        if (original == Blocks.JUNGLE_HANGING_SIGN) return HANGING_SIGN_1_JUNGLE.get();
        if (original == Blocks.ACACIA_HANGING_SIGN) return HANGING_SIGN_1_ACACIA.get();
        if (original == Blocks.DARK_OAK_HANGING_SIGN) return HANGING_SIGN_1_DARK_OAK.get();
        if (original == Blocks.MANGROVE_HANGING_SIGN) return HANGING_SIGN_1_MANGROVE.get();
        if (original == Blocks.CHERRY_HANGING_SIGN) return HANGING_SIGN_1_CHERRY.get();
        if (original == Blocks.BAMBOO_HANGING_SIGN) return HANGING_SIGN_1_BAMBOO.get();
        if (original == Blocks.CRIMSON_HANGING_SIGN) return HANGING_SIGN_1_CRIMSON.get();
        if (original == Blocks.WARPED_HANGING_SIGN) return HANGING_SIGN_1_WARPED.get();
        if (original == Blocks.OAK_WALL_HANGING_SIGN) return HANGING_SIGN_2_OAK.get();
        if (original == Blocks.SPRUCE_WALL_HANGING_SIGN) return HANGING_SIGN_2_SPRUCE.get();
        if (original == Blocks.BIRCH_WALL_HANGING_SIGN) return HANGING_SIGN_2_BIRCH.get();
        if (original == Blocks.JUNGLE_WALL_HANGING_SIGN) return HANGING_SIGN_2_JUNGLE.get();
        if (original == Blocks.ACACIA_WALL_HANGING_SIGN) return HANGING_SIGN_2_ACACIA.get();
        if (original == Blocks.DARK_OAK_WALL_HANGING_SIGN) return HANGING_SIGN_2_DARK_OAK.get();
        if (original == Blocks.MANGROVE_WALL_HANGING_SIGN) return HANGING_SIGN_2_MANGROVE.get();
        if (original == Blocks.CHERRY_WALL_HANGING_SIGN) return HANGING_SIGN_2_CHERRY.get();
        if (original == Blocks.BAMBOO_WALL_HANGING_SIGN) return HANGING_SIGN_2_BAMBOO.get();
        if (original == Blocks.CRIMSON_WALL_HANGING_SIGN) return HANGING_SIGN_2_CRIMSON.get();
        if (original == Blocks.WARPED_WALL_HANGING_SIGN) return HANGING_SIGN_2_WARPED.get();
        return null;
    }
}
