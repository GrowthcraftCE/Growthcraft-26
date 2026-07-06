package growthcraft.bamboo.init;

import growthcraft.bamboo.block.BambooPostBlock;
import growthcraft.bamboo.config.Reference;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftBambooBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    public static final DeferredBlock<BambooPostBlock> BAMBOO_POST_VERTICAL = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.BAMBOO_POST_VERTICAL,
            BambooPostBlock::new,
            GrowthcraftBambooBlocks::bambooPostProperties
    );
    public static final DeferredBlock<BambooPostBlock> BAMBOO_POST_HORIZONTAL = BLOCKS.registerBlock(
            Reference.UnlocalizedName.Block.BAMBOO_POST_HORIZONTAL,
            BambooPostBlock::new,
            GrowthcraftBambooBlocks::bambooPostProperties
    );

    private static BlockBehaviour.Properties bambooPostProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.SAND)
                .instrument(NoteBlockInstrument.CHIME)
                .strength(1.3F)
                .sound(SoundType.BAMBOO_WOOD)
                .ignitedByLava();
    }

    private GrowthcraftBambooBlocks() {}
}
