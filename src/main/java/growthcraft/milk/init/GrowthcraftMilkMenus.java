package growthcraft.milk.init;

import growthcraft.milk.config.Reference;
import growthcraft.milk.menu.CheesePressMenu;
import growthcraft.milk.menu.ChurnMenu;
import growthcraft.milk.menu.MixingVatMenu;
import growthcraft.milk.menu.PancheonMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftMilkMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Reference.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<CheesePressMenu>> CHEESE_PRESS = MENUS.register(
            Reference.UnlocalizedName.CHEESE_PRESS,
            () -> new MenuType<>(CheesePressMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<ChurnMenu>> CHURN = MENUS.register(
            Reference.UnlocalizedName.CHURN,
            () -> new MenuType<>(ChurnMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<MixingVatMenu>> MIXING_VAT = MENUS.register(
            Reference.UnlocalizedName.MIXING_VAT,
            () -> new MenuType<>(MixingVatMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );
    public static final DeferredHolder<MenuType<?>, MenuType<PancheonMenu>> PANCHEON = MENUS.register(
            Reference.UnlocalizedName.PANCHEON,
            () -> new MenuType<>(PancheonMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    private GrowthcraftMilkMenus() {
    }
}
