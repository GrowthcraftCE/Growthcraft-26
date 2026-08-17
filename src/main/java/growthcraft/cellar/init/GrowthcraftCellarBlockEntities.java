package growthcraft.cellar.init;

import growthcraft.cellar.block.CultureJarBlock;
import growthcraft.cellar.block.entity.BrewKettleBlockEntity;
import growthcraft.cellar.block.entity.CorkCoasterBlockEntity;
import growthcraft.cellar.block.entity.CultureJarBlockEntity;
import growthcraft.cellar.block.entity.FermentationBarrelBlockEntity;
import growthcraft.cellar.block.entity.FruitPressBlockEntity;
import growthcraft.cellar.block.entity.LargeFermentationBarrelBlockEntity;
import growthcraft.cellar.block.entity.RoasterBlockEntity;
import growthcraft.cellar.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftCellarBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Reference.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CultureJarBlockEntity>> CULTURE_JAR = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.Block.CULTURE_JAR,
            () -> new BlockEntityType<>(CultureJarBlockEntity::new, GrowthcraftCellarBlocks.CULTURE_JAR.get())
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BrewKettleBlockEntity>> BREW_KETTLE = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.Block.BREW_KETTLE,
            () -> new BlockEntityType<>(BrewKettleBlockEntity::new, GrowthcraftCellarBlocks.BREW_KETTLE.get())
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FermentationBarrelBlockEntity>> FERMENTATION_BARREL = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.Block.FERMENT_BARREL_OAK,
            () -> new BlockEntityType<>(FermentationBarrelBlockEntity::new,
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_ACACIA.get(),
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_APPLE.get(),
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_BAMBOO.get(),
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_BIRCH.get(),
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_CHERRY.get(),
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_CRIMSON.get(),
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_DARK_OAK.get(),
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_JUNGLE.get(),
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_MANGROVE.get(),
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK.get(),
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_PALE_OAK.get(),
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_SPRUCE.get(),
                    GrowthcraftCellarBlocks.FERMENTATION_BARREL_WARPED.get())
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LargeFermentationBarrelBlockEntity>> LARGE_FERMENTATION_BARREL = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.Block.LARGE_FERMENT_BARREL_OAK,
            () -> new BlockEntityType<>(LargeFermentationBarrelBlockEntity::new,
                    GrowthcraftCellarBlocks.LARGE_FERMENTATION_BARREL_OAK.get())
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FruitPressBlockEntity>> FRUIT_PRESS = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.Block.FRUIT_PRESS,
            () -> new BlockEntityType<>(FruitPressBlockEntity::new, GrowthcraftCellarBlocks.FRUIT_PRESS.get())
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RoasterBlockEntity>> ROASTER = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.Block.ROASTER,
            () -> new BlockEntityType<>(RoasterBlockEntity::new, GrowthcraftCellarBlocks.ROASTER.get())
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CorkCoasterBlockEntity>> CORK_COASTER = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.Item.CORK_COASTER,
            () -> new BlockEntityType<>(CorkCoasterBlockEntity::new, GrowthcraftCellarBlocks.CORK_COASTER.get())
    );

    private GrowthcraftCellarBlockEntities() {}
}
