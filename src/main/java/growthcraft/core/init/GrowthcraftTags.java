package growthcraft.core.init;

import growthcraft.core.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class GrowthcraftTags {
    private GrowthcraftTags() {}

    public static final class Blocks {
        public static final TagKey<Block> ROPE = TagKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Tag.ROPE)
        );

        private Blocks() {}
    }

    public static final class Items {
        public static final TagKey<Item> KNIVES = TagKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Tag.KNIVES)
        );

        private Items() {}
    }
}
