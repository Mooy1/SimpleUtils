package io.github.mooy1.gridexpansion.implementation.powergrid;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a player's power grid
 */
public final class PowerGrid {
    
    private static final Map<String, PowerGrid> GRIDS = new HashMap<>();

    public static void tickAll() {
        for (PowerGrid grid : GRIDS.values()) {
            grid.tick();
        }
    }

    public static PowerGrid get(@Nonnull String uuid) {
        return GRIDS.computeIfAbsent(uuid, k -> new PowerGrid());
    }

    public static PowerGrid get(@Nonnull Location l) {
        return get(BlockStorage.getLocationInfo(l, "owner"));
    }

    public static void addOwner(@Nonnull Block b, @Nonnull Player p) {
        BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
    }
    
    private final Set<GridGenerator> generators = new HashSet<>();
    private final Set<GridConsumer> consumers = new HashSet<>();
    
    int usage;
    int max;
    boolean maxed;

    private void tick() {
        this.usage = 0;
        this.max = 0;
        this.maxed = false;

        for (GridGenerator generator : this.generators) {
            this.max += generator.generation;
        }
        
        for (GridConsumer consumer : this.consumers) {
            this.usage += consumer.consumption;
            if (this.maxed) {
                consumer.consume = false;
                continue;
            }
            if (this.usage > this.max) {
                consumer.consume = false;
                this.maxed = true;
                continue;
            }
            consumer.consume = true;
        }
    }
    
    void addGenerator(GridGenerator generator) {
        this.generators.add(generator);
    }

    void removeGenerator(GridGenerator generator) {
        this.generators.remove(generator);
    }
    
    void addConsumer(GridConsumer consumer) {
        this.consumers.add(consumer);
    }

    void removeConsumer(GridConsumer consumer) {
        this.consumers.remove(consumer);
    }
    
    @Nonnull
    List<GridComponent> getComponents() {
        List<GridComponent> list = new ArrayList<>();
        list.addAll(this.generators);
        list.addAll(this.consumers);
        return list;
    }
    
    private PowerGrid() {
        
    }
    
}
