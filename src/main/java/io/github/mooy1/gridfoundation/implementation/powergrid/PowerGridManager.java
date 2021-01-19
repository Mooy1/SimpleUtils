package io.github.mooy1.gridfoundation.implementation.powergrid;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * manages {@link PowerGrid}'s
 */
public final class PowerGridManager {

    private static final Map<UUID, PowerGrid> players = new HashMap<>();
    
    public static void tick() {
        for (PowerGrid grid : players.values()) {
            grid.tick();
        }
    }

    static PowerGrid get(@Nonnull UUID uuid) {
        return players.computeIfAbsent(uuid, k -> new PowerGrid());
    }
    
}
