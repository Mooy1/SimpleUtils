package io.github.mooy1.gridfoundation.implementation.powergrid;

import lombok.NoArgsConstructor;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a player's power grid
 */
@NoArgsConstructor
public final class PowerGrid {
    
    private final Map<Integer, GridGenerator> generators = new HashMap<>();
    private final Map<Integer, GridConsumer> consumers = new HashMap<>();
    
    int usage;
    int max;
    boolean maxed;

    public void tick() {
        this.usage = 0;
        this.max = 0;
        this.maxed = false;

        for (GridGenerator generator : this.generators.values()) {
            this.max += generator.generation;
        }
        
        for (GridConsumer consumer : this.consumers.values()) {
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
    
    @Nullable
    public GridGenerator getGenerator(int hash) {
        return this.generators.get(hash);
    }

    @Nonnull
    public GridGenerator addGenerator(int hash, @Nonnull GridGenerator generator) {
        this.generators.put(hash, generator);
        return generator;
    }

    public void removeGenerator(int hash) {
        this.generators.remove(hash);
    }

    @Nullable
    public GridConsumer getConsumer(int hash) {
        return this.consumers.get(hash);
    }

    @Nonnull
    public GridConsumer addConsumer(int hash, GridConsumer consumer) {
        this.consumers.put(hash, consumer);
        return consumer;
    }

    public void removeConsumer(int hash) {
        this.consumers.remove(hash);
    }
    
    List<GridComponent> getComponents() {
        List<GridComponent> list = new ArrayList<>();
        list.addAll(this.generators.values());
        list.addAll(this.consumers.values());
        return list;
    }
    
    @Nonnull
    public ItemStack getStatusItem(String status) {
        return new CustomItem(
                this.maxed ? Material.RED_STAINED_GLASS_PANE : this.usage == this.max ? Material.YELLOW_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE,
                status + " GP",
                "&7Grid: " + this.usage + " / " + this.max,
                "",
                this.maxed ? "&cGrid Overloaded!" : ""
        );
    }
    
    public void updateStatus(@Nonnull BlockMenu menu, int slot, String status) {
        menu.replaceExistingItem(slot, getStatusItem(status));
    }
    
}
