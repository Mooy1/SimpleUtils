package io.github.mooy1.gridutils.implementation.powergrid;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a player's power grid
 */
public final class PowerGrid {
    
    private final Set<GridGenerator> generators = new HashSet<>();
    private final Set<GridConsumer> consumers = new HashSet<>();
    
    int usage;
    int max;
    boolean maxed;

    void tick() {
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
    
    public GridGenerator addGenerator(GridGenerator generator) {
        this.generators.add(generator);
        return generator;
    }

    public void removeGenerator(GridGenerator generator) {
        this.generators.remove(generator);
    }
    
    public GridConsumer addConsumer(GridConsumer consumer) {
        this.consumers.add(consumer);
        return consumer;
    }

    public void removeConsumer(GridConsumer consumer) {
        this.consumers.remove(consumer);
    }
    
    @Nonnull
    List<GridComponent> getComponents() {
        List<GridComponent> list = new ArrayList<>();
        list.addAll(this.generators);
        list.addAll(this.consumers);
        return list;
    }
    
    @Nonnull
    ItemStack getStatusItem(@Nonnull String status) {
        return new CustomItem(
                this.maxed ? Material.RED_STAINED_GLASS_PANE : this.usage == this.max ? Material.YELLOW_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE,
                status + " GP",
                "&7Grid: " + this.usage + " / " + this.max,
                "",
                this.maxed ? "&cGrid Overloaded!" : ""
        );
    }
    
    public void updateStatus(@Nonnull BlockMenu menu, int slot, @Nonnull String status) {
        menu.replaceExistingItem(slot, getStatusItem(status));
    }
    
    PowerGrid() {
        
    }
    
}
