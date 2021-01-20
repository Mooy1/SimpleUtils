package io.github.mooy1.gridfoundation.implementation.powergrid;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * represents a consumer in a power grid
 */
public final class GridConsumer extends GridComponent {
    
    final int consumption;
    boolean consume;
    
    public boolean canConsume() {
        return this.consume;
    }
    
    GridConsumer(@Nonnull ItemStack item, int consumption) {
        super(item);
        this.consumption = consumption;
    }
    
    @Override
    protected void addViewerInfo(List<String> info) {
        info.add("");
        info.add("&aConsuming: " + this.consumption);
    }

}
