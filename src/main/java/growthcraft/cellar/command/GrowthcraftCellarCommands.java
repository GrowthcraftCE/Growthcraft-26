package growthcraft.cellar.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.config.GrowthcraftCellarConfig;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = GrowthcraftCellar.MODID)
public final class GrowthcraftCellarCommands {
    private GrowthcraftCellarCommands() {
    }

    @SubscribeEvent
    static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("growthcraft")
                .then(Commands.literal("cellar")
                        .then(Commands.literal("largeBarrelManualUnlock")
                                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                                .executes(context -> reportValue(context.getSource()))
                                .then(Commands.argument("enabled", BoolArgumentType.bool())
                                        .executes(context -> setValue(
                                                context.getSource(),
                                                BoolArgumentType.getBool(context, "enabled")))))));
    }

    private static int reportValue(net.minecraft.commands.CommandSourceStack source) {
        boolean allowed = GrowthcraftCellarConfig.isLargeFermentationBarrelManualUnlockAllowed();
        source.sendSuccess(() -> Component.translatable(
                "growthcraft_cellar.command.large_barrel_manual_unlock.status", allowed), false);
        return allowed ? 1 : 0;
    }

    private static int setValue(net.minecraft.commands.CommandSourceStack source, boolean allowed) {
        GrowthcraftCellarConfig.setLargeFermentationBarrelManualUnlockAllowed(allowed);
        source.sendSuccess(() -> Component.translatable(
                "growthcraft_cellar.command.large_barrel_manual_unlock.changed", allowed), true);
        return allowed ? 1 : 0;
    }
}
