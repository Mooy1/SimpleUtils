package io.github.mooy1.gridfoundation.implementation.powergrid;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * manages all of the {@link PowerGrid}'s
 */
public final class PowerGridManager {

    private static final Map<UUID, PowerGrid> grids = new HashMap<>();
    
    public static void tick() {
        for (PowerGrid grid : grids.values()) {
            grid.tick();
        }
    }

    static PowerGrid get(@Nonnull UUID uuid) {
        return grids.computeIfAbsent(uuid, k -> new PowerGrid());
    }
    
}
