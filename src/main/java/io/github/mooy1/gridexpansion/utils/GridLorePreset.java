package io.github.mooy1.gridexpansion.utils;

import io.github.mooy1.infinitylib.presets.LorePreset;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class GridLorePreset {
    
    public static String consumesGridPower(int gp) {
        return "&8\u21E8 &e\u26A1 &7Consumes " + LorePreset.format(gp) + " GP ";
    }

    public static String generatesGridPower(int gp) {
        return "&8\u21E8 &e\u26A1 &7Generates " + LorePreset.format(gp) + " GP ";
    }
    
}
