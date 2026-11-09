package dev.creoii.greatbigworld.limitedarrows;

import dev.creoii.greatbigworld.limitedarrows.config.LimitedArrowsConfig;
import org.jspecify.annotations.Nullable;

public final class LimitedArrows {
    public static final String NAMESPACE = "limited_arrows";
    public static final String GBW = "great_big_world";

    @Nullable
    public static LimitedArrowsConfig CONFIG;

    public static int getMinArrows() {
        return CONFIG == null ? 10 : CONFIG.minArrows;
    }

    public static void setMinArrows(int minArrows) {
        if (CONFIG != null) CONFIG.minArrows = minArrows;
    }

    public static int getMaxArrows() {
        return CONFIG == null ? 20 : CONFIG.maxArrows;
    }

    public static void setMaxArrows(int maxArrows) {
        if (CONFIG != null) CONFIG.maxArrows = maxArrows;
    }

    public static boolean modifyArrowLoot() {
        return CONFIG != null && CONFIG.modifyArrowLoot;
    }

    public static void setModifyArrowLoot(boolean modifyArrowLoot) {
        if (CONFIG != null) CONFIG.modifyArrowLoot = modifyArrowLoot;
    }
}
