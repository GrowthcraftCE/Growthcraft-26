package growthcraft.rice.init;

import growthcraft.rice.block.CultivatedFarmlandBlock;
import growthcraft.rice.block.RiceCropBlock;
import growthcraft.rice.config.Reference;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftRiceBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    public static final DeferredBlock<CultivatedFarmlandBlock> CULTIVATED_FARMLAND = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.CULTIVATED_FARMLAND,
            CultivatedFarmlandBlock::new,
            GrowthcraftRiceBlocks::cultivatedFarmlandProperties
    );
    public static final DeferredBlock<RiceCropBlock> RICE_CROP = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.RICE_CROP,
            RiceCropBlock::new,
            GrowthcraftRiceBlocks::riceCropProperties
    );

    private static BlockBehaviour.Properties cultivatedFarmlandProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)
                .noOcclusion()
                .randomTicks();
    }

    private static BlockBehaviour.Properties riceCropProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)
                .noOcclusion()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP);
    }

    private GrowthcraftRiceBlocks() {}
}
