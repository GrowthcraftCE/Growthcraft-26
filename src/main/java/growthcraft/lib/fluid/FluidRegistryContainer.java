package growthcraft.lib.fluid;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidRegistryContainer {
    public final String name;
    public final net.neoforged.neoforge.registries.DeferredHolder<FluidType, FluidType> type;
    public final FluidType.Properties typeProperties;
    public final net.neoforged.neoforge.registries.DeferredHolder<Block, LiquidBlock> block;
    @Nullable
        public net.neoforged.neoforge.registries.DeferredHolder<Item, BucketItem> bucket;
    public final net.neoforged.neoforge.registries.DeferredHolder<Fluid, net.neoforged.neoforge.fluids.BaseFlowingFluid.Source> source;
    public final net.neoforged.neoforge.registries.DeferredHolder<Fluid, net.neoforged.neoforge.fluids.BaseFlowingFluid.Flowing> flowing;
    public final FluidClientProperties clientProperties;
    private net.neoforged.neoforge.fluids.BaseFlowingFluid.Properties properties;

    public DeferredRegister<Fluid> FLUID_REGISTRY;
    public DeferredRegister<FluidType> FLUID_TYPE_REGISTRY;

    public DeferredRegister<Block> BLOCK_REGISTRY;
    public DeferredRegister<Item> ITEM_REGISTRY;

    public FluidRegistryContainer(String name,
                                  FluidType.Properties typeProperties,
                                  FluidClientProperties clientProperties,
                                  @Nullable AdditionalProperties additionalProperties,
                                  BlockBehaviour.Properties blockProperties,
                                  Item.Properties itemProperties,
                                  DeferredRegister<Fluid>  FLUID_REGISTRY,
                                  DeferredRegister<FluidType> FLUID_TYPE_REGISTRY,
                                  DeferredRegister<Block> BLOCK_REGISTRY,
                                  DeferredRegister<Item> ITEM_REGISTRY) {
        this.name = name;
        this.clientProperties = clientProperties;

        this.FLUID_REGISTRY = FLUID_REGISTRY;
        this.FLUID_TYPE_REGISTRY = FLUID_TYPE_REGISTRY;
        this.BLOCK_REGISTRY = BLOCK_REGISTRY;
        this.ITEM_REGISTRY = ITEM_REGISTRY;

        this.typeProperties = typeProperties.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY);

        this.type = FLUID_TYPE_REGISTRY.register(name, () -> new FluidType(this.typeProperties));

        this.source = FLUID_REGISTRY.register(name + "_fluid_source", () -> new BaseFlowingFluid.Source(this.properties));
        this.flowing = FLUID_REGISTRY.register(name + "_fluid_flowing",
                () -> new BaseFlowingFluid.Flowing(this.properties));

        this.properties = new BaseFlowingFluid.Properties(this.type, this.source, this.flowing);
        if (additionalProperties != null) {
            this.properties.explosionResistance(additionalProperties.explosionResistance)
                    .levelDecreasePerBlock(additionalProperties.levelDecreasePerBlock)
                    .slopeFindDistance(additionalProperties.slopeFindDistance).tickRate(additionalProperties.tickRate);
        }

        this.block = BLOCK_REGISTRY.register(name + "_fluid", key -> {
            BaseFlowingFluid.Source src = this.source.get();
            BlockBehaviour.Properties properties = blockProperties.setId(ResourceKey.create(Registries.BLOCK, key));
            if (additionalProperties != null && additionalProperties.blockFactory != null) {
                return additionalProperties.blockFactory.apply(src, properties);
            }
            return new LiquidBlock(src, properties);
        });
        this.properties.block(() -> this.block.get());

        if (additionalProperties == null || additionalProperties.registerBucket) {
            this.bucket = ITEM_REGISTRY.register(name + "_fluid_bucket",
                    key -> new BucketItem(this.source.get(), itemProperties.setId(ResourceKey.create(Registries.ITEM, key))));
            this.properties.bucket(() -> this.bucket.get());
        } else {
            this.bucket = null;
        }
    }

    public FluidRegistryContainer(String name, FluidType.Properties typeProperties,
                                  FluidClientProperties clientProperties, BlockBehaviour.Properties blockProperties,
                                  Item.Properties itemProperties,
                                  DeferredRegister<Fluid>  FLUID_REGISTRY,
                                  DeferredRegister<FluidType> FLUID_TYPE_REGISTRY,
                                  DeferredRegister<Block> BLOCK_REGISTRY,
                                  DeferredRegister<Item> ITEM_REGISTRY) {
        this(name, typeProperties, clientProperties, null, blockProperties, itemProperties,
                FLUID_REGISTRY, FLUID_TYPE_REGISTRY, BLOCK_REGISTRY, ITEM_REGISTRY);
    }

    /**
     * Exposes the NeoForge BaseFlowingFluid.Properties for this fluid pair (source/flowing).
     * Mods integrating with Growthcraft can use this to tweak runtime attributes such as
     * levelDecreasePerBlock, slopeFindDistance, tickRate, or explosionResistance if needed.
     *
     * Note: Prefer configuring through AdditionalProperties at registration time when possible.
     */
    public BaseFlowingFluid.Properties getProperties() {
        return this.properties;
    }

    public static class AdditionalProperties {
        private int levelDecreasePerBlock = 1;
        private float explosionResistance = 1;
        private int slopeFindDistance = 4;
        private int tickRate = 5;
        private boolean registerBucket = true;
        // Optional factory allowing callers to provide a custom LiquidBlock implementation for the source fluid
        private java.util.function.BiFunction<BaseFlowingFluid.Source, BlockBehaviour.Properties, LiquidBlock> blockFactory;

        public AdditionalProperties explosionResistance(float resistance) {
            this.explosionResistance = resistance;
            return this;
        }

        public AdditionalProperties levelDecreasePerBlock(int decrease) {
            this.levelDecreasePerBlock = decrease;
            return this;
        }

        public AdditionalProperties slopeFindDistance(int distance) {
            this.slopeFindDistance = distance;
            return this;
        }

        public AdditionalProperties tickRate(int rate) {
            this.tickRate = rate;
            return this;
        }

        public AdditionalProperties customBlock(java.util.function.BiFunction<BaseFlowingFluid.Source, BlockBehaviour.Properties, LiquidBlock> factory) {
            this.blockFactory = factory;
            return this;
        }

        /**
         * Disable automatic bucket item registration for this fluid.
         * Use when you want to provide custom bucket items separately.
         */
        public AdditionalProperties noBucket() {
            this.registerBucket = false;
            return this;
        }
    }

}
