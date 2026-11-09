package dev.creoii.greatbigworld.limitedarrows.config;

import dev.creoii.greatbigworld.limitedarrows.LimitedArrows;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

@Config(name = "limited_arrows")
public class LimitedArrowsConfig implements ConfigData {
    public int minArrows = 10;
    public int maxArrows = 20;
    public boolean modifyArrowLoot = false;

    public static void init() {
        AutoConfig.register(LimitedArrowsConfig.class, (conf, clazz) -> new Toml4jConfigSerializer<>(conf, clazz) {
            public LimitedArrowsConfig deserialize() {
                try {
                    return super.deserialize();
                } catch (Exception e) {
                    return createDefault();
                }
            }
        });

        LimitedArrows.CONFIG = AutoConfig.getConfigHolder(LimitedArrowsConfig.class).getConfig();
    }
}
