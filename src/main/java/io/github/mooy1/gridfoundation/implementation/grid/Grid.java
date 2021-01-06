package io.github.mooy1.gridfoundation.implementation.grid;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a grid
 */
public final class Grid {
    
    final UUID uuid;
    
    private final Map<Integer, Generator> generators = new HashMap<>();
    private final Map<Integer, Consumer> consumers = new HashMap<>();
    
    int usage;
    int max;
    boolean maxed;

    Grid(@Nonnull UUID uuid) {
        this.uuid = uuid;
    }

    public void tick() {
        this.usage = 0;
        this.max = 0;
        this.maxed = false;

        for (Generator generator : this.generators.values()) {
            this.max += generator.generation;
        }
        
        for (Consumer consumer : this.consumers.values()) {
            if (this.usage + consumer.consumption <= this.max) {
                consumer.consume = true;
                this.usage += consumer.consumption;
            } else {
                this.maxed = true;
                consumer.consume = false;
            }
        }
    }

    @Nullable
    public Generator getGenerator(@Nonnull Location l) {
        return this.generators.get(l.hashCode());
    }

    @Nonnull
    public Generator addGenerator(@Nonnull Location l, @Nonnull ItemStack item) {
        Generator generator = new Generator(item, this);
        this.generators.put(l.hashCode(), generator);
        return generator;
    }

    public void removeGenerator(@Nonnull Location l) {
        this.generators.remove(l.hashCode());
    }

    @Nullable
    public Consumer getConsumer(@Nonnull Location l) {
        return this.consumers.get(l.hashCode());
    }

    @Nonnull
    public Consumer addConsumer(@Nonnull Location l, @Nonnull ItemStack item, int consumption) {
        Consumer consumer = new Consumer(item, consumption, this);
        this.consumers.put(l.hashCode(), consumer);
        return consumer;
    }

    public void removeConsumer(@Nonnull Location l) {
        this.consumers.remove(l.hashCode());
    }
    
    public List<Component> getComponents() {
        List<Component> list = new ArrayList<>();
        list.addAll(this.generators.values());
        list.addAll(this.consumers.values());
        return list;
    }
    
}
