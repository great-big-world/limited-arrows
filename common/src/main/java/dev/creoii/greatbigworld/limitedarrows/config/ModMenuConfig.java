package dev.creoii.greatbigworld.limitedarrows.config;

import dev.creoii.greatbigworld.limitedarrows.LimitedArrows;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModMenuConfig {
    public static final int MAX_ARROWS_CONFIG = 200 + 1;

    public static Screen createConfigScreen(Screen parent) {
        return createConfigScreenBuilder(parent).build();
    }

    public static ConfigBuilder createConfigScreenBuilder(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(Component.translatable("title.limited_arrows.config"));
        builder.setSavingRunnable(() -> AutoConfig.getConfigHolder(LimitedArrowsConfig.class).save());

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory generalCategory = builder.getOrCreateCategory(Component.translatable("limited_arrows.config.category.general"));

        generalCategory.addEntry(entryBuilder.startIntSlider(Component.translatable("limited_arrows.config.option.minArrows"), LimitedArrows.getMinArrows(), 0, MAX_ARROWS_CONFIG)
                .setDefaultValue(10)
                .setTextGetter(integer -> integer <= 0 ? Component.translatable("limited_arrows.config.option.none") : integer == MAX_ARROWS_CONFIG ? Component.translatable("limited_arrows.config.option.infinity") : Component.translatable("limited_arrows.config.option.minArrows.value", integer))
                .setTooltip(Component.translatable("limited_arrows.config.option.minArrows.tooltip"))
                .setSaveConsumer(integer -> {
                    if (integer == MAX_ARROWS_CONFIG) {
                        LimitedArrows.setMinArrows(Integer.MAX_VALUE);
                    } else LimitedArrows.setMinArrows(integer);
                })
                .build());

        generalCategory.addEntry(entryBuilder.startIntSlider(Component.translatable("limited_arrows.config.option.maxArrows"), LimitedArrows.getMaxArrows(), 0, MAX_ARROWS_CONFIG)
                .setDefaultValue(20)
                .setTextGetter(integer -> integer <= 0 ? Component.translatable("limited_arrows.config.option.none") : integer == MAX_ARROWS_CONFIG ? Component.translatable("limited_arrows.config.option.infinity") : Component.translatable("limited_arrows.config.option.minArrows.value", integer))
                .setTooltip(Component.translatable("limited_arrows.config.option.maxArrows.tooltip"))
                .setSaveConsumer(integer -> {
                    if (integer == MAX_ARROWS_CONFIG) {
                        LimitedArrows.setMaxArrows(Integer.MAX_VALUE);
                    } else LimitedArrows.setMaxArrows(integer);
                })
                .build());

        return builder;
    }
}
