package dev.creoii.greatbigworld.limitedarrows.neoforge;

import dev.creoii.greatbigworld.limitedarrows.config.LimitedArrowsConfig;
import dev.creoii.greatbigworld.limitedarrows.config.ModMenuConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod("limited_arrows")
public final class LimitedArrowsNeoForge {
    public LimitedArrowsNeoForge(ModContainer container, IEventBus bus) {
        if (ModList.get().isLoaded("cloth_config")) {
            LimitedArrowsConfig.init();
            container.registerExtensionPoint(IConfigScreenFactory.class, (_, screen) ->  ModMenuConfig.createConfigScreen(screen));
            bus.addListener(this::onClientSetup);
        }
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ModMenuConfig.createConfigScreenBuilder(null);
    }
}
