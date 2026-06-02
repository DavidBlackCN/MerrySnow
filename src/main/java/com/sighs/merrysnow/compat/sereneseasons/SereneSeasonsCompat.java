package com.sighs.merrysnow.compat.sereneseasons;

import com.sighs.merrysnow.Config;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.Level;

import java.lang.reflect.Method;

/**
 * Optional integration with Serene Seasons / Serene Seasons Plus.
 * Uses reflection exclusively — no compile-time dependency on SereneSeasons.
 * All SS API references are resolved at runtime and fail gracefully when SS is absent.
 */
public final class SereneSeasonsCompat {
    private static final boolean LOADED = FabricLoader.getInstance().isModLoaded("sereneseasons");

    // Cached reflection handles, initialized lazily on first use
    private static Method getSeasonState;
    private static Method getSeason;
    private static Method getSubSeason;
    private static boolean reflectionInitialized;
    private static boolean reflectionAvailable;

    private SereneSeasonsCompat() {
    }

    /** True if the Serene Seasons mod JAR is on the classpath. */
    public static boolean isLoaded() {
        return LOADED;
    }

    /** True if Serene Seasons is loaded AND the user has enabled compatibility. */
    public static boolean isActive() {
        if (!LOADED) return false;
        try {
            return Config.SERENE_SEASONS_COMPATIBILITY.get();
        } catch (IllegalStateException e) {
            return true; // config not yet loaded, assume active
        }
    }

    /**
     * Returns true if the current season in the given level is WINTER (any sub-season).
     */
    public static boolean isWinterSeason(Level level) {
        if (!isActive() || level == null) return false;
        if (!initReflection()) return false;
        try {
            Object state = getSeasonState.invoke(null, level);
            Object season = getSeason.invoke(state);
            return "WINTER".equals(season.toString());
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Returns true if the current sub-season is cold enough that biomes
     * which normally rain should be treated as snowy for spawn-blocking purposes.
     * Covers: late autumn, all winter sub-seasons, and early spring.
     */
    public static boolean isColdSeason(Level level) {
        if (!isActive() || level == null) return false;
        if (!initReflection()) return false;
        try {
            Object state = getSeasonState.invoke(null, level);
            Object sub = getSubSeason.invoke(state);
            String name = sub.toString();
            return "LATE_AUTUMN".equals(name)
                    || "EARLY_WINTER".equals(name)
                    || "MID_WINTER".equals(name)
                    || "LATE_WINTER".equals(name)
                    || "EARLY_SPRING".equals(name);
        } catch (Exception ignored) {
            return false;
        }
    }

    private static boolean initReflection() {
        if (reflectionInitialized) return reflectionAvailable;
        reflectionInitialized = true;
        try {
            Class<?> seasonHelperClass = Class.forName("sereneseasons.api.season.SeasonHelper");
            getSeasonState = seasonHelperClass.getMethod("getSeasonState", Level.class);

            Class<?> seasonStateClass = Class.forName("sereneseasons.api.season.ISeasonState");
            getSeason = seasonStateClass.getMethod("getSeason");
            getSubSeason = seasonStateClass.getMethod("getSubSeason");

            reflectionAvailable = true;
        } catch (Exception e) {
            reflectionAvailable = false;
        }
        return reflectionAvailable;
    }
}
