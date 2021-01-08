package io.github.mooy1.gridfoundation.implementation.grid;

import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a grid
 */
@NoArgsConstructor
public final class Grid {
    
    private final Map<Integer, Generator> generators = new HashMap<>();
    private final Map<Integer, Consumer> consumers = new HashMap<>();
    
    int usage;
    int max;
    boolean maxed;

    public void tick() {
        this.usage = 0;
        this.max = 0;
        this.maxed = false;

        for (Generator generator : this.generators.values()) {
            this.max += generator.generation;
        }
        
        for (Consumer consumer : this.consumers.values()) {
            this.usage += consumer.consumption;
            if (this.maxed) {
                consumer.consume = false;
                continue;
            }
            if (this.usage > this.max) {
                this.maxed = true;
                consumer.consume = false;
                continue;
            }
            consumer.consume = true;
        }
    }
    
    @Nullable
    public Generator getGenerator(int hash) {
        return this.generators.get(hash);
    }

    @Nonnull
    public Generator addGenerator(int hash, @Nonnull ItemStack item, @Nullable Location l) {
        Generator generator = new Generator(item, this, l);
        this.generators.put(hash, generator);
        return generator;
    }

    public void removeGenerator(int hash) {
        this.generators.remove(hash);
    }

    @Nullable
    public Consumer getConsumer(int hash) {
        return this.consumers.get(hash);
    }

    @Nonnull
    public Consumer addConsumer(int hash, @Nonnull ItemStack item, int consumption, @Nullable Location l) {
        Consumer consumer = new Consumer(item, consumption, this, l);
        this.consumers.put(hash, consumer);
        return consumer;
    }

    public void removeConsumer(int hash) {
        this.consumers.remove(hash);
    }
    
    public List<Component> getComponents() {
        List<Component> list = new ArrayList<>();
        list.addAll(this.generators.values());
        list.addAll(this.consumers.values());
        return list;
    }
    
}
