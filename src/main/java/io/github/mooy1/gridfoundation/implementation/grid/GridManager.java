package io.github.mooy1.gridfoundation.implementation.grid;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * manages {@link Grid}'s
 */
public final class GridManager {

    private static final Map<UUID, Grid> players = new HashMap<>();
    
    public static void tick() {
        for (Grid grid : players.values()) {
            grid.tick();
        }
    }

    public static Grid get(@Nonnull UUID uuid) {
        return players.computeIfAbsent(uuid, k -> new Grid());
    }
    
}
