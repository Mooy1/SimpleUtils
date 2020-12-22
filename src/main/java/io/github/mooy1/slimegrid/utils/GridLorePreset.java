package io.github.mooy1.slimegrid.utils;

import io.github.mooy1.infinitylib.presets.LorePreset;

/**
 * Collection of utils for building item lore with gp
 *
 * @author Mooy1
 */
public final class GridLorePreset {
    
    public static String consumesGridPower(int gp) {
        return "&8\u21E8 &e\u26A1 &7Consumes " + LorePreset.format(gp) + " GP ";
    }

    public static String generatesGridPower(int gp) {
        return "&8\u21E8 &e\u26A1 &7Generates " + LorePreset.format(gp) + " GP ";
    }
    
}
