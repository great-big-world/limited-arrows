package dev.creoii.greatbigworld.limitedarrows.fabric;

import dev.creoii.greatbigworld.limitedarrows.config.LimitedArrowsConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class LimitedArrowsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            LimitedArrowsConfig.init();
        }
    }
}
