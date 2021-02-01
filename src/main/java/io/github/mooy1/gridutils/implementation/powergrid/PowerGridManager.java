package io.github.mooy1.gridutils.implementation.powergrid;

import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * manages all of the {@link PowerGrid}'s
 */
@UtilityClass
public final class PowerGridManager {
    
    private static final Map<String, PowerGrid> GRIDS = new HashMap<>();
    
    public static void tick() {
        for (PowerGrid grid : GRIDS.values()) {
            grid.tick();
        }
    }

    public static PowerGrid get(@Nonnull String uuid) {
        return GRIDS.computeIfAbsent(uuid, k -> new PowerGrid());
    }
    
}
