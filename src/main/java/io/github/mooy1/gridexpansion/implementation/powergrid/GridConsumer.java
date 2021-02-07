package io.github.mooy1.gridexpansion.implementation.powergrid;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * represents a consumer in a power grid
 */
public final class GridConsumer extends GridComponent {
    
    private static final Map<Location, GridConsumer> CONSUMERS = new HashMap<>();
    
    public static GridConsumer get(@Nonnull Location l) {
        return CONSUMERS.get(l);
    }

    final int consumption;
    boolean consume;
    
    public boolean canConsume() {
        return this.consume;
    }
    
    public GridConsumer(@Nonnull ItemStack item, int consumption, @Nonnull Block b, @Nonnull PowerGrid grid) {
        super(item, b, grid);
        this.consumption = consumption;
    }


    @Override
    protected void add(@Nonnull Location l) {
        this.grid.addConsumer(this);
        CONSUMERS.put(l, this);
    }

    @Override
    public void remove(@Nonnull Location l) {
        this.grid.removeConsumer(this);
        CONSUMERS.remove(l);
    }

    @Nonnull
    @Override
    protected String getInfo() {
        return "&aConsuming: " + this.consumption + " GP";
    }

}
